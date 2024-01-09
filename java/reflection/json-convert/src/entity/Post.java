package entity;

public class Post {
    private final long number;
    private final String title;
    private final String content;
    private final boolean isDeleted;
    private final String[] comment;

    public Post(long number, String title, String content, boolean isDeleted, String... comment) {
        this.number = number;
        this.title = title;
        this.content = content;
        this.isDeleted = isDeleted;
        this.comment = comment;
    }
}
