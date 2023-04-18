package KirayoApp.Kirayo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "user_details")
public class UserDetails {


    @Id
    @Column(name = "user_id")
    private Long userid;
    @Column(name = "full_name")
    private String fullname;
    @Column(name="city")
    private String city;

    @Column(name="dob")
    private Date dob;



    @Column(name="user_image")
    private String image;


}
