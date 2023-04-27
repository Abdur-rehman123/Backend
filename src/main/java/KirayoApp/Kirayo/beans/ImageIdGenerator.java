package KirayoApp.Kirayo.beans;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class ImageIdGenerator {

    public String generateImageId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}