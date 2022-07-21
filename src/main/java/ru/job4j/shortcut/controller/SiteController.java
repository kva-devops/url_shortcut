package ru.job4j.shortcut.controller;

import org.springframework.web.bind.annotation.*;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.SiteDTO;
import ru.job4j.shortcut.model.SiteOnlyNameDTO;
import ru.job4j.shortcut.service.MainService;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Controller for working with "Site" models
 */
@RestController
@RequestMapping("/site")
public class SiteController {

    /**
     * Main object of business logic
     */
    private final MainService mainService;

    public SiteController(MainService mainService) {
        this.mainService = mainService;
    }

    /**
     * POST method for registration new site in database
     * @param siteOnlyNameDTO - DTO model containing: name of site
     * @return SiteDTO model that contained: registered status, login and password
     */
    @PostMapping("/registration")
    public SiteDTO create(@Valid @RequestBody SiteOnlyNameDTO siteOnlyNameDTO) {
        return mainService.saveSite(siteOnlyNameDTO);
    }

    /**
     * GET method for getting list of registered sites from the database
     * @return List of sites
     */
    @GetMapping("/all")
    public List<Site> findAll() {
        return mainService.findAllSites();
    }

    /**
     * GET method for getting site by identifier
     * @param id - database site ID
     * @return Site object
     */
    @GetMapping("/{id}")
    public Site findById(@PathVariable int id) {
        return mainService.findSiteById(id);
    }

    /**
     * PUT method for updating to information about of site
     * @param site - object with full information about of site: id, nameOfSite, username and password
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PatchMapping("/")
    public void update(@Valid @RequestBody Site site) throws InvocationTargetException, IllegalAccessException {
        mainService.updateSite(site);
    }

    /**
     * DELETE method for deleting site from database by ID
     * @param id - database site ID
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        mainService.deleteSite(id);
    }
}
