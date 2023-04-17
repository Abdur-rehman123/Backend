package KirayoApp.Kirayo.model;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "payment_datetime", nullable = false)
    private LocalDateTime paymentDatetime;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    // Constructors, getters and setters
}