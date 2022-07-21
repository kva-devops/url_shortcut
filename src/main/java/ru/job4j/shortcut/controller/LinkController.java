package ru.job4j.shortcut.controller;

import org.springframework.web.bind.annotation.*;
import ru.job4j.shortcut.model.*;
import ru.job4j.shortcut.service.MainService;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Controller for wokring "Link" models
 */
@RestController
@RequestMapping("/link")
public class LinkController {
    /**
     * Main object of business logic
     */
    private final MainService mainService;

    public LinkController(MainService mainService) {
        this.mainService = mainService;
    }

    /**
     * GET Method for getting list of all links from database
     * @return List of links
     */
    @GetMapping("/all")
    public List<Link> findAll() {
        return mainService.findAllLinks();
    }

    /**
     * GET Method for getting link from database from ID
     * @param id - database link ID
     * @return Link object
     */
    @GetMapping("/{id}")
    public Link findById(@PathVariable int id) {
        return mainService.findLinkById(id);
    }

    /**
     * POST Method for link shortening
     * @param linkDTO - DTO model containing: site ID and initial URL
     * @return UrlShortcutDTO object
     */
    @PostMapping("/shortcut")
    public UrlShortcutDTO create(@Valid @RequestBody LinkDTO linkDTO) {
        return mainService.saveLink(linkDTO);
    }

    /**
     * PUT Method for updating link
     * @param link - Link object
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PatchMapping("/")
    public void update(@Valid @RequestBody Link link) throws InvocationTargetException, IllegalAccessException {
        mainService.updateLink(link);
    }

    /**
     * DELETE Method for deleting link by link ID
     * @param id - link ID
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        mainService.deleteLink(id);
    }

    /**
     * GET Method for getting original link by shorted identifier
     * @param shortUrl - shorted identifier
     * @return OriginalUrlDTO object
     */
    @GetMapping("/redirect/{shortcut}")
    public OriginalUrlDTO redirect(@PathVariable("shortcut") String shortUrl) {
        return mainService.redirectLink(shortUrl);
    }

    /**
     * GET Method for getting shortened link call statistics for the site by ID
     * @param siteId - cite ID
     * @return StatLinkListDTO object
     */
    @GetMapping("/statistic/{siteId}")
    public StatLinkListDTO stat(@PathVariable("siteId") int siteId) {
        return mainService.statLinkForSite(siteId);
    }
}
