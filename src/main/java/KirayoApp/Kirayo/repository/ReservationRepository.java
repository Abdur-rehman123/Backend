package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findAllByRequestIdProductProductId(Long productId);

}
