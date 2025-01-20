public class Book {
    private String title;
    private int publishYear;
    private int authorId;

    public Book(String title, int publishYear) {
        this.title = title;
        this.publishYear = publishYear;
    }

    public Book(String title, int publishYear, int authorId) {
        this.title = title;
        this.publishYear = publishYear;
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public int getAuthorId() {
        return authorId;
    }
}
