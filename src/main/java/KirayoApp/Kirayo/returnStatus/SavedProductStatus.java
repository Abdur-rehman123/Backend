package KirayoApp.Kirayo.returnStatus;

import java.util.List;

public class SavedProductStatus {

    private Boolean status;
    private String message;


    private List<SavedProductResponse> savedProductResponses;
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SavedProductResponse> getSavedProductResponses() {
        return savedProductResponses;
    }

    public void setSavedProductResponses(List<SavedProductResponse> savedProductResponses) {
        this.savedProductResponses = savedProductResponses;
    }
}
