package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDetailsRepository extends JpaRepository<UserDetails,Long> {
    @Query(value="SELECT ud.*  FROM  user_details ud WHERE ud.user_id = (SELECT uc.user_id FROM user_credentials uc WHERE uc.email=:email)", nativeQuery = true)
    UserDetails findByEmail(String email);

    UserDetails findByImage(String image);

}
