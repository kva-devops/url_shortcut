package ru.job4j.shortcut.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.model.*;
import ru.job4j.shortcut.repository.LinkRepository;
import ru.job4j.shortcut.repository.SiteRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This class contains main business logic of application
 */
@Service
@AllArgsConstructor
@Slf4j
public class MainService {

    /**
     * DAO for site
     */
    private final SiteRepository siteRepository;

    /**
     * DAO for link
     */
    private final LinkRepository linkRepository;

    /**
     * Object for encoding user's password
     */
    private BCryptPasswordEncoder encoder;

    /**
     * Method for getting list of all sites from database
     * @return List of sites
     */
    public List<Site> findAllSites() {
        String anchor = UUID.randomUUID().toString();
        Iterable<Site> siteList = siteRepository.findAll();
        if (siteList == null) {
            throw new NullPointerException("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
        }
        return StreamSupport.stream(
                siteList.spliterator(), false
        ).collect(Collectors.toList());
    }

    /**
     * Method for getting list of all links from database
     * @return List of links
     */
    public List<Link> findAllLinks() {
        String anchor = UUID.randomUUID().toString();
        Iterable<Link> linkList = linkRepository.findAll();
        if (linkList == null) {
            throw new NullPointerException("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
        }
        return StreamSupport.stream(
               linkList.spliterator(), false
        ).collect(Collectors.toList());
    }

    /**
     * Method for getting site from database by ID
     * @param id database site ID
     * @return Object of site
     */
    public Site findSiteById(int id) {
        String anchor = UUID.randomUUID().toString();
        var site = this.siteRepository.findById(id);
        if (site.isEmpty()) {
            throw new IllegalArgumentException("Site not found. Actual parameters: site ID - " + id + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
        }
        return site.get();
    }

    /**
     * Method for getting link from database by link ID
     * @param id - database link ID
     * @return Link object
     */
    public Link findLinkById(int id) {
        String anchor = UUID.randomUUID().toString();
        var link = this.linkRepository.findById(id);
        if (link.isEmpty()) {
            throw new IllegalArgumentException("Link not found. Actual parameters: link ID - " + id + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
        }
        return link.get();
    }

    /**
     * Method for saving new site in database
     * @param siteOnlyNameDTO - DTO model containing: name of site
     * @return SiteDTO model that contained: registered status, login and password
     */
    public SiteDTO saveSite(SiteOnlyNameDTO siteOnlyNameDTO) {
        Site check = siteRepository.findByNameOfSite(siteOnlyNameDTO.getNameOfSite());
        if (check != null) {
            return new SiteDTO(true, check.getUsername(), "hidden password");
        } else {
            Site buff = Site.of(siteOnlyNameDTO.getNameOfSite());
            String passBefore = buff.getPassword();
            buff.setPassword(encoder.encode(passBefore));
            Site siteAfterSave = this.siteRepository.save(buff);
            return new SiteDTO(false, siteAfterSave.getUsername(), passBefore);
        }
    }

    /**
     * Method for saving short link to database
     * @param linkDTO - DTO model containing: site ID and initial URL
     * @return UrlShortcutDTO model contaning: string with shortened link
     */
    public UrlShortcutDTO saveLink(LinkDTO linkDTO) {
        String anchor = UUID.randomUUID().toString();
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
            return new UrlShortcutDTO("http://localhost:8080/link/redirect/" + buff.getShortcut());
        } else {
            throw new IllegalArgumentException("Site not found. Actual parameters: site ID - " + linkDTO.getSiteId() + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
        }
    }

    /**
     * Method for updating information about of site (ID, name of site, username, password)
     * @param site - object with information about of site
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void updateSite(Site site) throws InvocationTargetException, IllegalAccessException {
        String anchor = UUID.randomUUID().toString();
        var current = siteRepository.findById(site.getId());
        if (current.isEmpty()) {
            throw new IllegalArgumentException("Site not found. Actual parameters: site ID - " + site.getId() + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
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
                    throw new NullPointerException("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
                }
                var newValue = getMethod.invoke(site);
                if (newValue != null) {
                    setMethod.invoke(buffSite, newValue);
                }
            }
        }
        buffSite.setPassword(encoder.encode(buffSite.getPassword()));
        siteRepository.save(buffSite);
    }

    /**
     * Method for updating link object
     * @param link - Link object
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void updateLink(Link link) throws InvocationTargetException, IllegalAccessException {
        String anchor = UUID.randomUUID().toString();
        var current = linkRepository.findById(link.getId());
        if (current.isEmpty()) {
            throw new IllegalArgumentException("Link not found. Actual parameters: link ID - " + link.getId() + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
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
                    throw new NullPointerException("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
                }
                var newValue = getMethod.invoke(link);
                if (newValue != null) {
                    setMethod.invoke(buffLink, newValue);
                }
            }
        }
        linkRepository.save(buffLink);
    }

    /**
     * Method for deleting site from database by site ID
     * @param id - database site ID
     */
    public void deleteSite(int id) {
        String anchor = UUID.randomUUID().toString();
        if (this.siteRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Site not found. Actual parameters: site ID - " + id + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
        }
        if (!linkRepository.findBySiteId(id).isEmpty()) {
            throw new IllegalArgumentException("Site cannot be deleted because it contains active links. Actual parameters: site ID - " + id + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
        } else {
            Site site = new Site();
            site.setId(id);
            this.siteRepository.delete(site);
        }
    }

    /**
     * Method for deleting link by link ID
     * @param id - link ID
     */
    public void deleteLink(int id) {
        String anchor = UUID.randomUUID().toString();
        if (this.linkRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Link not found. Actual parameters: link ID - " + id + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
        }
        Link link = new Link();
        link.setId(id);
        this.linkRepository.delete(link);
    }

    /**
     * Method for getting original link by shorted identifier
     * @param shortUrl - shorted identifier
     * @return OriginalUrlDTO object
     */
    public OriginalUrlDTO redirectLink(String shortUrl) {
        String anchor = UUID.randomUUID().toString();
        var link = this.linkRepository.findByShortcut(shortUrl);
        if (link == null) {
            throw new IllegalArgumentException("Short url not found. Actual parameters: shortUrl - " + shortUrl + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
        }
        this.linkRepository.incrementTotal();
        return new OriginalUrlDTO(link.getUrl());
    }

    /**
     * Method for getting shortened link call statistics for the site by site ID
     * @param siteId - site ID
     * @return StatLinkListDTO object
     */
    public StatLinkListDTO statLinkForSite(int siteId) {
        String anchor = UUID.randomUUID().toString();
        List<StatLinkDTO> buffListDTO = new ArrayList<>();
        Collection<Link> linkCollection = linkRepository.findBySiteId(siteId);
        if (linkCollection == null) {
            throw new NullPointerException("An internal error has occurred. Please try again later or contact technical support with the 'anchor'. anchor: " + anchor);
        } else if (linkCollection.isEmpty()) {
            throw new IllegalArgumentException("Site not found. Actual parameters: site ID - " + siteId + ". Please contact technical support with the 'anchor'. anchor: " + anchor);
        }
        for (Link elem : linkCollection) {
            buffListDTO.add(new StatLinkDTO(elem.getUrl(), elem.getTotal()));
        }
        return new StatLinkListDTO(buffListDTO);
    }
}
