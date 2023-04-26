package KirayoApp.Kirayo.model;


import javax.persistence.*;

@Entity
@Table(name="ProductImages")

public class ProductImages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="imageId")
    private String imageId;

    @Column(name="image")
    private byte[] image;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="productId", nullable = false)
    private Product product;

}
