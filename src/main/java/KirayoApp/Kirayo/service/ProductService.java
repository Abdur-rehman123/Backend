package KirayoApp.Kirayo.service;

import KirayoApp.Kirayo.dto.ProductUploadDto;
import KirayoApp.Kirayo.dto.SavedProductDto;
import KirayoApp.Kirayo.returnStatus.ProductsResponse;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ResponseStatus productUpload(ProductUploadDto productUploadDto, MultipartFile[] images) throws IOException;
    ResponseStatus savedProduct(SavedProductDto savedProductDto);
    List<ProductsResponse> getAllProducts();
    List<ProductsResponse> getUserProducts(String email);

    List<ProductsResponse>  getUserSavedProducts(String email);
}
