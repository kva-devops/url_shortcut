package ru.job4j.shortcut.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.SiteDTO;
import ru.job4j.shortcut.repository.SiteRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/site")
public class SiteController {

    private final SiteRepository siteRepository;

    private BCryptPasswordEncoder encoder;

    public SiteController(SiteRepository siteRepository, BCryptPasswordEncoder encoder) {
        this.siteRepository = siteRepository;
        this.encoder = encoder;
    }

    @GetMapping("/all")
    public List<Site> findAll() {
        return StreamSupport.stream(
                this.siteRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Site> findById(@PathVariable int id) {
        var site = this.siteRepository.findById(id);
        if (site.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(site.get());
    }

    @PostMapping("/registration")
    public ResponseEntity<SiteDTO> create(@RequestBody Site site) {
        if (site.getNameOfSite() == null) {
            throw new NullPointerException("Name of site is empty");
        }
        Site check = siteRepository.findByNameOfSite(site.getNameOfSite());
        if (check != null) {
            return new ResponseEntity<>(
                  new SiteDTO(true, check.getUsername(), "hidden password"),
                  HttpStatus.OK
            );
        } else {
            Site buff = Site.of(site.getNameOfSite());
            String passBefore = buff.getPassword();
            buff.setPassword(encoder.encode(passBefore));
            Site siteAfterSave = this.siteRepository.save(buff);
            return new ResponseEntity<>(
                    new SiteDTO(false, siteAfterSave.getUsername(), passBefore),
                    HttpStatus.CREATED
            );
        }
    }

    @PatchMapping("/")
    public ResponseEntity<Void> update(@RequestBody Site site) throws InvocationTargetException, IllegalAccessException {
        var current = siteRepository.findById(site.getId());
        if (current.isEmpty()) {
            throw new NullPointerException("Role not found");
        }
        var buffSite = current.get();
        var methods = buffSite.getClass().getDeclaredMethods();
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
                var newValue = getMethod.invoke(site);
                if (newValue != null) {
                    setMethod.invoke(buffSite, newValue);
                }
            }
        }
        buffSite.setPassword(encoder.encode(buffSite.getPassword()));
        siteRepository.save(buffSite);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (this.siteRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found");
        }
        Site site = new Site();
        site.setId(id);
        this.siteRepository.delete(site);
        return ResponseEntity.ok().build();
    }
}
