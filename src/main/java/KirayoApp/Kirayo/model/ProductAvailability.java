package KirayoApp.Kirayo.model;

import javax.persistence.*;


@Entity
@Table(name = "product_availability")
public class ProductAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "is_reserved")
    private boolean isReserved = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    // constructors, getters and setters
    // ...

}

