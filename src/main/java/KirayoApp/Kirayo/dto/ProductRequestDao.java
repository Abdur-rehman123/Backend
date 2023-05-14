package KirayoApp.Kirayo.dto;

import java.util.Date;

public class ProductRequestDao {
    private Long productId;
    private Date timeStamp;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
