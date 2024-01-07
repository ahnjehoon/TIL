package entity;

import java.net.URL;

public class Blog {
    private final String name;
    private final URL url;
    private final Post post;

    public Blog(String name, URL url, Post post) {
        this.name = name;
        this.url = url;
        this.post = post;
    }
}
