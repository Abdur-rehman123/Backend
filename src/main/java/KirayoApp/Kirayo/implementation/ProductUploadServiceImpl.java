package KirayoApp.Kirayo.implementation;

import KirayoApp.Kirayo.dto.ProductUploadDto;
import KirayoApp.Kirayo.model.Product;
import KirayoApp.Kirayo.model.UserCredentials;
import KirayoApp.Kirayo.model.UserDetails;
import KirayoApp.Kirayo.repository.ProductUploadRepository;
import KirayoApp.Kirayo.repository.UserCredentialsRepository;
import KirayoApp.Kirayo.repository.UserDetailsRepository;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;
import KirayoApp.Kirayo.service.ProductUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductUploadServiceImpl implements ProductUploadService {
    @Autowired
    ProductUploadRepository productUploadRepository;
    @Autowired
    UserCredentialsRepository userCredentialsRepository;
    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Override
    public ResponseStatus productUpload(ProductUploadDto productUploadDto) {
        Product product = new Product();
        UserCredentials userCredentials = userCredentialsRepository.findByEmail(productUploadDto.getEmail()).orElseThrow();
        UserDetails userDetails=userDetailsRepository.findById(userCredentials.getUserId()).orElseThrow();
        product.setUser(userDetails);
        product.setCategory(productUploadDto.getCategory());
        product.setTitle(productUploadDto.getTitle());
        product.setDescription(productUploadDto.getDescription());
        product.setPrice(productUploadDto.getPrice());
        product.setProductStatus(true);
        productUploadRepository.save(product);
        ResponseStatus responseStatus=new ResponseStatus();
        responseStatus.setStatus(true);
        responseStatus.setMessage("Product Uploaded Successfully");
        return responseStatus;
    }
}
