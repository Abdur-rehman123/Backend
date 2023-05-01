package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface ProductImagesRepository extends JpaRepository<ProductImage,Long> {
    @Transactional
    ProductImage findByImageId(String imageId);
}
