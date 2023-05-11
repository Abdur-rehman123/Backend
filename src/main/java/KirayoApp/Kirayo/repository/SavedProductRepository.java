package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.SavedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedProductRepository extends JpaRepository<SavedProduct,Long> {

     @Query(value="SELECT sp.*  FROM saved_product sp INNER JOIN user_details ud ON sp.user_id = ud.user_id WHERE ud.user_id = (SELECT uc.user_id FROM user_credentials uc WHERE uc.email=:email)", nativeQuery = true)
     Optional<List<SavedProduct>> findAllSavedProductsByUserName(@Param("email") String email);

     @Query(value="SELECT sp.*  FROM saved_product sp where sp.product_id=:id", nativeQuery = true)
     Optional<List<SavedProduct>> findAllSavedProductsByProductId(@Param("id") Long id);
}
