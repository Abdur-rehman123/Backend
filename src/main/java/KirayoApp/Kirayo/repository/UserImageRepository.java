package KirayoApp.Kirayo.repository;

import KirayoApp.Kirayo.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface UserImageRepository extends JpaRepository<UserImage,Long> {
    @Transactional
    UserImage findByImageId(String imageId);
}
