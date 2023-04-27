package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.SavedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedProductRepository extends JpaRepository<SavedProduct,Long> {
}
