package KirayoApp.Kirayo.service;

import KirayoApp.Kirayo.dto.ProductUploadDto;
import KirayoApp.Kirayo.dto.SavedProductDto;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    ResponseStatus productUpload(ProductUploadDto productUploadDto, MultipartFile[] images) throws IOException;
    ResponseStatus savedProduct(SavedProductDto savedProductDto);
    ResponseStatus getAllProducts();
    ResponseStatus getUserProducts();

    ResponseStatus getUserSavedProducts();
}
