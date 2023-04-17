package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials,Long> {
    Optional<UserCredentials> findByEmail(String username);
    Optional<UserCredentials> findByPhoneNumber(String phonenumber);


}
