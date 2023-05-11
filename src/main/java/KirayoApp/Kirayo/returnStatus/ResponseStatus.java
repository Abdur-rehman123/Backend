package KirayoApp.Kirayo.returnStatus;

import java.util.List;

public class ResponseStatus {

    private Boolean status;
    private String message;

    public ResponseStatus() {

    }

    public Boolean getStatus() {
        return status;
    }

    /*
     * public void setStatus(String status) {
     * this.status = status;
     * }
     */

    public String getMessage() {
        return message;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}