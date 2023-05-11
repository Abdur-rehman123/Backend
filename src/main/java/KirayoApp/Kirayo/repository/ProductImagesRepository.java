package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductImagesRepository extends JpaRepository<ProductImage,Long> {
    @Transactional
    ProductImage findByImageId(String imageId);

    @Query(value="SELECT pi.*  FROM product_images pi where pi.product_id=:id", nativeQuery = true)
    List<ProductImage> findProductByProductId(@Param("id") Long id);
}
