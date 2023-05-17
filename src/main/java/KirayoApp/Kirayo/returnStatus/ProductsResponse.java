package KirayoApp.Kirayo.returnStatus;

import java.util.Date;
import java.util.List;

public class ProductsResponse {
    private Long productID;
    private String email;
    private String title;
    private String description;
    private String category;
    private Double price;
    private Date timeStamp;
    private String latitude;
    private String longitude;
    private List<String> imageids;

    private List<ReservationResponse> productReservations;
    private Boolean is_Saved;
    private Double rating;
    private int totalreviews;


    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<String> getImageids() {
        return imageids;
    }

    public void setImageids(List<String> imageids) {
        this.imageids = imageids;
    }

    public Boolean getIs_Saved() {
        return is_Saved;
    }

    public void setIs_Saved(Boolean is_Saved) {
        this.is_Saved = is_Saved;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<ReservationResponse> getProductReservations() {
        return productReservations;
    }

    public void setProductReservations(List<ReservationResponse> productReservations) {
        this.productReservations = productReservations;
    }

    public int getTotalreviews() {
        return totalreviews;
    }

    public void setTotalreviews(int totalreviews) {
        this.totalreviews = totalreviews;
    }
}
