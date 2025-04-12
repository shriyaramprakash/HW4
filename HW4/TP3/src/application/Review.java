package application;

public class Review {
    private static int nextId = 1;
    private final int id;
    private String content;
    private final String reviewerName;

    public Review(String content, String reviewerName) {
        this.id = nextId++;
        this.content = content;
        this.reviewerName = reviewerName;
    }

    public int getId() { return id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getReviewerName() { return reviewerName; }
}
