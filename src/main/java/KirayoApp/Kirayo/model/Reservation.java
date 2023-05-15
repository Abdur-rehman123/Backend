package KirayoApp.Kirayo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
@Getter
@Setter

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ProductRequest requestId;

    @Column(name = "started_at", nullable = false)
    private Date startedAt;

    @Column(name = "ended_at", nullable = false)
    private Date endedAt;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    // constructors, getters, and setters
    // ...
}
