package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails,Long> {

}
