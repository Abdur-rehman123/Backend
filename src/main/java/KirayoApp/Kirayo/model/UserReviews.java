package KirayoApp.Kirayo.model;
import javax.persistence.*;
@Entity
@Table(name = "user_review")
public class UserReviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "renter_id", nullable = false)
    private UserDetails renter;

    @ManyToOne
    @JoinColumn(name = "rentee_id", nullable = false)
    private UserDetails rentee;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "rating", nullable = false)
    private Float rating;

    // Constructors, getters and setters
    // ...
}