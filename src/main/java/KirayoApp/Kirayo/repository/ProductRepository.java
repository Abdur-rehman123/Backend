package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value="SELECT p.*  FROM product p INNER JOIN user_details ud ON p.user_id = ud.user_id WHERE ud.user_id = (SELECT uc.user_id FROM user_credentials uc WHERE uc.email=:email)", nativeQuery = true)
    Optional<List<Product>> findAllProductsByUserName(@Param("email") String email);


    List<Product> findByTitleContainingIgnoreCase(String title);




}
