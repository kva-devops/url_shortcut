package ru.job4j.shortcut.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.shortcut.model.*;
import ru.job4j.shortcut.repository.LinkRepository;
import ru.job4j.shortcut.repository.SiteRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/link")
public class LinkController {

    private final LinkRepository linkRepository;

    private final SiteRepository siteRepository;

    public LinkController(LinkRepository linkRepository,
                          SiteRepository siteRepository) {
        this.linkRepository = linkRepository;
        this.siteRepository = siteRepository;
    }

    @GetMapping("/all")
    public List<Link> findAll() {
        return StreamSupport.stream(
                this.linkRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Link> findById(@PathVariable int id) {
        var link = this.linkRepository.findById(id);
        if (link.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Link not found");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(link.get());
    }

    @PostMapping("/shortcut")
    public ResponseEntity<UrlShortcutDTO> create(@RequestBody LinkDTO linkDTO) {
        if (linkDTO.getUrl() == null) {
            throw new NullPointerException("Link is empty or site not registered");
        }
        Optional<Site> buffSite = siteRepository.findById(linkDTO.getSiteId());
        String rootUrlFromLinkDTO;
        String linkForSave;
        if (linkDTO.getUrl().startsWith("http://")) {
            linkForSave = linkDTO.getUrl().split("//")[1];
            rootUrlFromLinkDTO = linkForSave.split("/")[0];
        } else {
            linkForSave = linkDTO.getUrl();
            rootUrlFromLinkDTO = linkDTO.getUrl().split("/")[0];
        }
        if (buffSite.isPresent() && buffSite.get().getNameOfSite().equals(rootUrlFromLinkDTO)) {
            Link buff = this.linkRepository.save(Link.of(linkForSave, buffSite.get()));
            UrlShortcutDTO urlShortcutDTO = new UrlShortcutDTO("http://localhost:8080/link/redirect/" + buff.getShortcut());
            return new ResponseEntity<>(
                    urlShortcutDTO,
                    HttpStatus.CREATED);
        } else {
            throw new NullPointerException("Site not registered, please register");
        }
    }

    @PatchMapping("/")
    public ResponseEntity<Void> update(@RequestBody Link link) throws InvocationTargetException, IllegalAccessException {
        var current = linkRepository.findById(link.getId());
        if (current.isEmpty()) {
            throw new NullPointerException("Link not found");
        }
        var buffLink = current.get();
        var methods = buffLink.getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method: methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new NullPointerException("Invalid properties");
                }
                var newValue = getMethod.invoke(link);
                if (newValue != null) {
                    setMethod.invoke(buffLink, newValue);
                }
            }
        }
        linkRepository.save(buffLink);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (this.linkRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Link not found");
        }
        Link link = new Link();
        link.setId(id);
        this.linkRepository.delete(link);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/redirect/{shortcut}")
    public ResponseEntity<UrlShortcutDTO> redirect(@PathVariable("shortcut") String shortUrl) {
        var link = this.linkRepository.findByShortcut(shortUrl);
        if (link == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shortcut not found");
        }
        this.linkRepository.incrementTotal();
        UrlShortcutDTO urlShortcutDTO = new UrlShortcutDTO("http://" + link.getUrl());
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Content-Type", "application/json")
                .body(urlShortcutDTO);
    }

    @GetMapping("/statistic/{siteId}")
    public ResponseEntity<StatLinkListDTO> stat(@PathVariable("siteId") int siteId) {
        List<StatLinkDTO> buffListDTO = new ArrayList<>();
        Collection<Link> buffLinkList = StreamSupport.stream(
                this.linkRepository.findBySiteId(siteId).spliterator(), false
        ).collect(Collectors.toList());
        for (Link elem : buffLinkList) {
            buffListDTO.add(new StatLinkDTO(elem.getUrl(), elem.getTotal()));
        }
        StatLinkListDTO statLinkListDTO = new StatLinkListDTO(buffListDTO);
        return new ResponseEntity<>(
                statLinkListDTO,
                HttpStatus.OK);
    }
}
