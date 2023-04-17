package KirayoApp.Kirayo.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
//@Table(name="user_credentials")
@Table(name = "user_credentials")
public class UserCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;


    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @Column(name="isActive")
    private Boolean isActive;

    // getters and setters
    // ...




    // getters and setters
    // ...

}
