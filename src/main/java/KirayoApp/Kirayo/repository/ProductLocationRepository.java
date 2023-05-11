package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.ProductLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductLocationRepository extends JpaRepository<ProductLocation,Long> {
    @Query(value="SELECT pl.*  FROM product_location pl where pl.product_id=:id", nativeQuery = true)
    ProductLocation findProductLocationByProductId(@Param("id") Long id);

}
