package KirayoApp.Kirayo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.transaction.Transactional;

@Getter
@Setter
@Entity
@Transactional
@Table(name="UserImage")
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="image_id")
    private String imageId;

    @Lob
    @Column(name="image")
    private byte[] image;

}
