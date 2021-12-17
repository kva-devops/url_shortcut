package ru.job4j.shortcut.model;

import java.util.Objects;

public class LinkDTO {

    private int siteId;

    private String url;

    public LinkDTO(int siteId, String url) {
        this.siteId = siteId;
        this.url = url;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkDTO linkDTO = (LinkDTO) o;
        return siteId == linkDTO.siteId
                && Objects.equals(url, linkDTO.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(siteId, url);
    }
}
