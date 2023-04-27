package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
