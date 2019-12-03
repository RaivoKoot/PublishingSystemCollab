package models;

public class Article {

    private int articleID;
    private String title;
    private String summary;
    private String content;
    private boolean isFinal;
    private boolean isAccepted;
    private String issn;

    private int reviewsReceived;
    private int reviewsContributed;
    private int responesToReviewsGiven;
    private int finalReviewsReceived;

    private int reviewID; // Only used in the case that the article of a review wants to be viewed and the user
    // must know later which reivew that article came from


    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public int getReviewsReceived() {
        return reviewsReceived;
    }

    public void setReviewsReceived(int reviewsReceived) {
        this.reviewsReceived = reviewsReceived;
    }

    public int getReviewsContributed() {
        return reviewsContributed;
    }

    public void setReviewsContributed(int reviewsContributed) {
        this.reviewsContributed = reviewsContributed;
    }

    public int getResponesToReviewsGiven() {
        return responesToReviewsGiven;
    }

    public void setResponesToReviewsGiven(int responesToReviewsGiven) {
        this.responesToReviewsGiven = responesToReviewsGiven;
    }

    public int getFinalReviewsReceived() {
        return finalReviewsReceived;
    }

    public void setFinalReviewsReceived(int finalReviewsReceived) {
        this.finalReviewsReceived = finalReviewsReceived;
    }
}
