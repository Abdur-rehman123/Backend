package KirayoApp.Kirayo.returnStatus;



import java.util.Date;
import java.util.List;

public class SavedProductResponse {
    private Long savedProductId;
    private Long productId;
    private String email;
    private String title;
    private String description;
    private String category;
    private Double price;
    private Date timeStamp;
    private String latitude;
    private String longitude;
    private List<String> imageids;
    private Double rating;


    public Long getSavedProductId() {
        return savedProductId;
    }

    public void setSavedProductId(Long savedProductId) {
        this.savedProductId = savedProductId;
    }

    public Long getProductID() {
        return productId;
    }

    public void setProductID(Long productId) {
        this.productId = productId;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}

