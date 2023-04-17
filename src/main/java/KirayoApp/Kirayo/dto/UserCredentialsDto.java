package KirayoApp.Kirayo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentialsDto {




//    @NotBlank(message = "Email address is required")
//    @Email(message = "Invalid email address")
    private String email;

//    @NotBlank(message = "Phone number is required")
//    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

//    @NotBlank(message = "Password is required")
//    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

//    public Boolean emailVerify(String email) {
//
//        UserCredentials userCredentials=new UserCredentials();
//        if(userCredentials.getEmail().equals(email)){
//            return true;
//        }
//        else{
//            return false;
//        }
//
//
//    }
    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public Boolean phoneNumberVerify(String phoneNumber) {
//        UserCredentials userCredentials=new UserCredentials();
//        return userCredentials.getPhoneNumber().equals(phoneNumber);
//    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // getters and setters
}