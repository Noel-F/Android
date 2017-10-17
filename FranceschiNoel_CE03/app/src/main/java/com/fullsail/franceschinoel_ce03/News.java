package com.fullsail.franceschinoel_ce03;

import java.io.Serializable;

// Noel Franceschi
// MDF3 - 1610
// News.java

@SuppressWarnings("WeakerAccess")
public class News implements Serializable {

    private String section = "";
    private String subSection = "";
    private String title = "";
    private String description = "";
    private String url = "";

    public String getSection() {
        return section;
    }

    void setSection(String section) {
        this.section = section;
    }

    public String getSubSection() {
        return subSection;
    }

    public void setSubSection(String subSection) {
        this.subSection = subSection;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

