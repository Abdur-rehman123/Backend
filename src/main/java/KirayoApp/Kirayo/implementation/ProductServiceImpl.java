package KirayoApp.Kirayo.implementation;

import KirayoApp.Kirayo.dto.ProductUploadDto;
import KirayoApp.Kirayo.model.Product;
import KirayoApp.Kirayo.model.UserCredentials;
import KirayoApp.Kirayo.model.UserDetails;
import KirayoApp.Kirayo.repository.ProductUploadRepository;
import KirayoApp.Kirayo.repository.UserCredentialsRepository;
import KirayoApp.Kirayo.repository.UserDetailsRepository;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;
import KirayoApp.Kirayo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
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

    @Override
    public ResponseStatus saveProduct(ProductUploadDto productUploadDto) {
        return null;
    }

    @Override
    public ResponseStatus getAllProducts() {
        return null;
    }

    @Override
    public ResponseStatus getUserProducts() {
        return null;
    }

    @Override
    public ResponseStatus getUserSavedProducts() {
        return null;
    }
}
