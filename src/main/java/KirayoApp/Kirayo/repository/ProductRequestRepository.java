package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.ProductRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRequestRepository extends JpaRepository<ProductRequest,Long> {

    List<ProductRequest> findAllByRenterUserid(Long renterId);
    List<ProductRequest> findAllByProductUserUserid(Long renteeId);


}
