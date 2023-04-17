package KirayoApp.Kirayo.service;

import KirayoApp.Kirayo.dto.ProductUploadDto;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;

public interface ProductUploadService {

    ResponseStatus productUpload(ProductUploadDto productUploadDto);
}
