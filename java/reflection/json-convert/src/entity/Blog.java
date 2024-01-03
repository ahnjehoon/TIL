package entity;

import java.net.URI;

public class Blog {
    private final String name;
    private final String siteUri;

    public Blog(String name, String siteUri) {
        this.name = name;
        this.siteUri = siteUri;
    }
}
