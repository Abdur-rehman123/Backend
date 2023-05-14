package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview,Long> {
    List<ProductReview> findAllByProductProductId(Long productId);
    List<ProductReview> findAllByRenteeUserid(Long renteeId);
}
