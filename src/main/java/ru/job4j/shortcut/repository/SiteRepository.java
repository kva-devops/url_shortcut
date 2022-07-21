package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.shortcut.model.Site;

/**
 * DAO interface for Site model
 */
public interface SiteRepository extends CrudRepository<Site, Integer> {

    /**
     * Method for finding site by username
     * @param username - username of site (generated automatically)
     * @return Site object
     */
    @Query("SELECT s FROM Site s WHERE s.username = :username")
    Site findByUsername(@Param("username") String username);

    /**
     * Method for finding site by name of site
     * @param name - name of site (entered at registration)
     * @return Site object
     */
    @Query("SELECT s FROM Site s WHERE s.nameOfSite = :name")
    Site findByNameOfSite(@Param("name") String name);
}
