package KirayoApp.Kirayo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "user_details")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class UserDetails {


    @Id
    @Column(name = "user_id")
    private Long userid;
    @Column(name = "full_name")
    private String fullname;
    @Column(name="city")
    private String city;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="dob")
    private Date dob;



    @Column(name="user_image")
    private String image;


}
