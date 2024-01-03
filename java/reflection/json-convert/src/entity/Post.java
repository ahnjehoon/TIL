package entity;

public class Post {
    private final long number;
    private final String title;
    private final String content;
    private final boolean isDeleted;

    public Post(long number, String title, String content, boolean isDeleted) {
        this.number = number;
        this.title = title;
        this.content = content;
        this.isDeleted = isDeleted;
    }
}
