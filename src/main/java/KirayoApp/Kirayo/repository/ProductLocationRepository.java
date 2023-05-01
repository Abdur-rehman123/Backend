package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.ProductLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLocationRepository extends JpaRepository<ProductLocation,Long> {
    ProductLocation findByProductProductId(Long productid);
}
