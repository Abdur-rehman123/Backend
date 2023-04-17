package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductUploadRepository extends JpaRepository<Product, Long> {
}
