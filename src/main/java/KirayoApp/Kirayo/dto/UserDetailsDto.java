package KirayoApp.Kirayo.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailsDto {

//    @NotBlank(message = "Full name is required")
    private String fullName;

//    @NotBlank(message = "City is required")
    private String city;

//    @NotNull(message = "Date of birth is required")
    private Date dob;


    private String image;

    // getters and setters
}