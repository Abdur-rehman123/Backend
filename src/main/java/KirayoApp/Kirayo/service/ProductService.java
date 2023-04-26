package KirayoApp.Kirayo.service;

import KirayoApp.Kirayo.dto.ProductUploadDto;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;

public interface ProductService {

    ResponseStatus productUpload(ProductUploadDto productUploadDto);
    ResponseStatus saveProduct(ProductUploadDto productUploadDto);
    ResponseStatus getAllProducts();
    ResponseStatus getUserProducts();

    ResponseStatus getUserSavedProducts();
}
