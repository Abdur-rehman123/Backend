package KirayoApp.Kirayo.implementation;

import KirayoApp.Kirayo.beans.ImageIdGenerator;
import KirayoApp.Kirayo.dto.ProductUploadDto;
import KirayoApp.Kirayo.dto.SavedProductDto;
import KirayoApp.Kirayo.model.*;
import KirayoApp.Kirayo.repository.*;
import KirayoApp.Kirayo.returnStatus.ProductsResponse;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;
import KirayoApp.Kirayo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private ImageIdGenerator imageIdGenerator;
    @Autowired
    private ProductImagesRepository productImagesRepository;
    @Autowired
    private SavedProductRepository savedProductRepository;
    @Autowired
    private ProductLocationRepository productLocationRepository;


    @Transactional
    @Override
    public ResponseStatus productUpload(ProductUploadDto productUploadDto, MultipartFile[] images) throws IOException {

        ResponseStatus responseStatus=new ResponseStatus();
        try{
            Product product = new Product();
            ProductLocation productLocation= new ProductLocation();
            UserCredentials userCredentials = userCredentialsRepository.findByEmail(productUploadDto.getEmail()).orElseThrow(() -> new NoSuchElementException("User not found"));
            UserDetails userDetails=userDetailsRepository.findById(userCredentials.getUserId()).orElseThrow();
            product.setUser(userDetails);
            product.setCategory(productUploadDto.getCategory());
            product.setTitle(productUploadDto.getTitle());
            product.setDescription(productUploadDto.getDescription());
            product.setPrice(productUploadDto.getPrice());
            product.setTimestamp(productUploadDto.getTimeStamp());
            product.setProductStatus(true);



            // Save the ProductImages
            Set<ProductImage> productImages = new HashSet<>();
            for (MultipartFile image : images) {
                ProductImage productImage = new ProductImage();
                productImage.setImageId(imageIdGenerator.generateImageId());
                productImage.setImage(image.getBytes());
                productImage.setProduct(product);
                productImages.add(productImage);
            }
            //Save the ProductImages entity
            productImagesRepository.saveAll(productImages);

            product.setProductImages(productImages);
            // Save the Product entity
            productRepository.save(product);

            productLocation.setProduct(product);
            productLocation.setLatitude(productUploadDto.getLatitude());
            productLocation.setLongitude(productUploadDto.getLongitude());
            //Save the ProductLocation entity
            productLocationRepository.save(productLocation);

            responseStatus.setStatus(true);
            responseStatus.setMessage("Product Uploaded Successfully");
        }
        catch (NoSuchElementException e){
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());
        }

        return responseStatus;
    }

    @Override
    public ResponseStatus savedProduct(SavedProductDto savedProductDto) {
        ResponseStatus responseStatus=new ResponseStatus();
        SavedProduct savedProduct=new SavedProduct();
        try {
            UserCredentials userCredentials = userCredentialsRepository.findByEmail(savedProductDto.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("User not found"));

            UserDetails userDetails = userDetailsRepository.findById(userCredentials.getUserId())
                    .orElseThrow(() -> new NoSuchElementException("User details not found"));

            Product product = productRepository.findById(savedProductDto.getProductId())
                    .orElseThrow(() -> new NoSuchElementException("Product not found"));

            savedProduct.setUser(userDetails);
            savedProduct.setSavedAt(savedProductDto.getLocalDateTime());
            savedProduct.setProduct(product);
            savedProductRepository.save(savedProduct);

            responseStatus.setStatus(true);
            responseStatus.setMessage("Product saved successfully");
        } catch (NoSuchElementException e) {
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());
        }
        return responseStatus;
    }

    @Override
    public List<ProductsResponse> getAllProducts() {

       List<Product> products;
       products=productRepository.findAll();
       List<ProductsResponse> productsResponses=new ArrayList<>();
       for(Product product : products){
           if(product.getProductStatus()){
               ProductsResponse productsResponse = new ProductsResponse();
               productsResponse.setTitle(product.getTitle());
               productsResponse.setDescription(product.getDescription());
               productsResponse.setCategory(product.getCategory());
               productsResponse.setPrice(product.getPrice());
               productsResponse.setTimeStamp(product.getTimestamp());
               ProductLocation productLocation= productLocationRepository.findByProductProductId(product.getProductId());
               productsResponse.setLatitude(productLocation.getLatitude());
               productsResponse.setLongitude(productLocation.getLongitude());

               Set<ProductImage> productImages = product.getProductImages();
               List<String> imageIds = new ArrayList<>();
               for (ProductImage productImage : productImages) {
                   imageIds.add(productImage.getImageId());
               }
               productsResponse.setImageids(imageIds);
               productsResponse.setStatus(true);
               productsResponse.setMessage("Product Found");
               productsResponses.add(productsResponse);
           }
       }
//        ArrayList<Set<ProductImages>> productImages=new ArrayList<>;
//       for(Product  product:products){
//
//           productImages.add(product.getProductImages());
//
//       }
//       for()



        return productsResponses;
    }

    @Override
    public List<ProductsResponse> getUserProducts(String email) {
        List<ProductsResponse> productsResponses=new ArrayList<>();
        try{
            List<Product> products;
            products=productRepository.findAllProductsByUserName(email).orElseThrow(() -> new NoSuchElementException("No Product Found"));
            if(products.isEmpty()){
                ProductsResponse productsResponse=new ProductsResponse();
                System.out.println("No product Found");
                productsResponse.setStatus(false);
                productsResponse.setMessage("No product Found");
                productsResponses.add(productsResponse);
            }
            for(Product product : products){
                if(product.getProductStatus()){
                    ProductsResponse productsResponse = new ProductsResponse();
                    productsResponse.setTitle(product.getTitle());
                    productsResponse.setDescription(product.getDescription());
                    productsResponse.setCategory(product.getCategory());
                    productsResponse.setPrice(product.getPrice());
                    productsResponse.setTimeStamp(product.getTimestamp());
                    ProductLocation productLocation= productLocationRepository.findByProductProductId(product.getProductId());
                    productsResponse.setLatitude(productLocation.getLatitude());
                    productsResponse.setLongitude(productLocation.getLongitude());

                    Set<ProductImage> productImages = product.getProductImages();
                    List<String> imageIds = new ArrayList<>();
                    for (ProductImage productImage : productImages) {
                        imageIds.add(productImage.getImageId());
                    }
                    productsResponse.setImageids(imageIds);
                    productsResponse.setStatus(true);
                    productsResponse.setMessage("Product Found");
                    productsResponses.add(productsResponse);

                }
            }
        }
        catch (NoSuchElementException e) {
            ProductsResponse productsResponse=new ProductsResponse();
            System.out.println("No product Found");
            productsResponse.setStatus(false);
            productsResponse.setMessage(e.getMessage());
            productsResponses.add(productsResponse);
        }

        return productsResponses;
    }

    @Override
    public List<ProductsResponse>  getUserSavedProducts(String email) {
        List<ProductsResponse> productsResponses=new ArrayList<>();
        try{
            List<Product> products;
            products=savedProductRepository.findAllSavedProductsByUserName(email).orElseThrow(() -> new NoSuchElementException("No Product Found"));

            for(Product product : products){
                if(product.getProductStatus()){
                    ProductsResponse productsResponse = new ProductsResponse();
                    productsResponse.setTitle(product.getTitle());
                    productsResponse.setDescription(product.getDescription());
                    productsResponse.setCategory(product.getCategory());
                    productsResponse.setPrice(product.getPrice());
                    productsResponse.setTimeStamp(product.getTimestamp());
                    ProductLocation productLocation= productLocationRepository.findByProductProductId(product.getProductId());
                    productsResponse.setLatitude(productLocation.getLatitude());
                    productsResponse.setLongitude(productLocation.getLongitude());

                    Set<ProductImage> productImages = product.getProductImages();
                    List<String> imageIds = new ArrayList<>();
                    for (ProductImage productImage : productImages) {
                        imageIds.add(productImage.getImageId());
                    }
                    productsResponse.setImageids(imageIds);
                    productsResponse.setStatus(true);
                    productsResponse.setMessage("Product Found");
                    productsResponses.add(productsResponse);

                }
            }
        }
        catch (NoSuchElementException e) {
            ProductsResponse productsResponse=new ProductsResponse();
            productsResponse.setStatus(false);
            productsResponse.setMessage(e.getMessage());
            productsResponses.add(productsResponse);
        }

        return productsResponses;
    }
}
