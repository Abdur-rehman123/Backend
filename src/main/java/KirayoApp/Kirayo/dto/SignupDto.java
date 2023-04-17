package KirayoApp.Kirayo.dto;

import java.io.Serializable;

public class SignupDto implements Serializable {
//    private MultipartFile image;
    private UserCredentialsDto userCredentialsDto;
    private UserDetailsDto userDetailsDto;
    public UserCredentialsDto getUserCredentialsDto() {
        return userCredentialsDto;
    }

    public void setUserCredentialsDto(UserCredentialsDto userCredentialsDto) {
        this.userCredentialsDto = userCredentialsDto;
    }

    public UserDetailsDto getUserDetailsDto() {
        return userDetailsDto;
    }

    public void setUserDetailsDto(UserDetailsDto userDetailsDto) {
        this.userDetailsDto = userDetailsDto;
    }



//    public MultipartFile getImage() {
//        return image;
//    }
//
//    public void setImage(MultipartFile image) {
//        this.image = image;
//    }



}
