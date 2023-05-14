package KirayoApp.Kirayo.returnStatus;

import java.util.Date;

public class ReviewResponse {
    Long reviewId;
    String renterName;
    String comment;
    Float rating;
    Date timeStamp;
    String renterCity;

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterName(String renterName) {
        this.renterName = renterName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRenterCity() {
        return renterCity;
    }

    public void setRenterCity(String renterCity) {
        this.renterCity = renterCity;
    }
}
