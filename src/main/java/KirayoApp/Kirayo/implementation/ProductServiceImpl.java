package KirayoApp.Kirayo.implementation;

import KirayoApp.Kirayo.beans.ImageIdGenerator;
import KirayoApp.Kirayo.dto.ProductUploadDto;
import KirayoApp.Kirayo.dto.SavedProductDto;
import KirayoApp.Kirayo.model.*;
import KirayoApp.Kirayo.repository.*;
import KirayoApp.Kirayo.returnStatus.ProductStatus;
import KirayoApp.Kirayo.returnStatus.ProductsResponse;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;
import KirayoApp.Kirayo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.CashBalance;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentIntentCollection;
import com.stripe.param.CustomerListParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Value("${stripe.api.secretKey}")
    String stripeKey;

    @Transactional
    @Override
    public ResponseStatus productUpload(ProductUploadDto productUploadDto, MultipartFile[] images) throws IOException {

        ResponseStatus responseStatus = new ResponseStatus();
        try {
            Product product = new Product();
            ProductLocation productLocation = new ProductLocation();
            UserCredentials userCredentials = userCredentialsRepository.findByEmail(productUploadDto.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("User not found"));
            UserDetails userDetails = userDetailsRepository.findById(userCredentials.getUserId()).orElseThrow();
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
            // Save the ProductImages entity
            productImagesRepository.saveAll(productImages);

            product.setProductImages(productImages);
            // Save the Product entity
            productRepository.save(product);

            productLocation.setProduct(product);
            productLocation.setLatitude(productUploadDto.getLatitude());
            productLocation.setLongitude(productUploadDto.getLongitude());
            // Save the ProductLocation entity
            productLocationRepository.save(productLocation);

            responseStatus.setStatus(true);
            responseStatus.setMessage("Product Uploaded Successfully");
        } catch (NoSuchElementException e) {
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());
        }

        return responseStatus;
    }

    @Override
    public ResponseStatus savedProduct(SavedProductDto savedProductDto) {
        ResponseStatus responseStatus = new ResponseStatus();
        SavedProduct savedProduct = new SavedProduct();
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
    public ProductStatus getAllProducts() {
        ProductStatus productStatus = new ProductStatus();

        List<Product> products;
        products = productRepository.findAll();
        List<ProductsResponse> productsResponses = new ArrayList<>();
        for (Product product : products) {
            if (product.getProductStatus()) {
                ProductsResponse productsResponse = new ProductsResponse();
                UserCredentials userCredentials = userCredentialsRepository.findById(product.getUser().getUserid())
                        .orElseThrow();
                productsResponse.setEmail(userCredentials.getEmail());
                productsResponse.setProductID(product.getProductId());
                productsResponse.setTitle(product.getTitle());
                productsResponse.setDescription(product.getDescription());
                productsResponse.setCategory(product.getCategory());
                productsResponse.setPrice(product.getPrice());
                productsResponse.setTimeStamp(product.getTimestamp());
                ProductLocation productLocation = productLocationRepository
                        .findByProductProductId(product.getProductId());
                productsResponse.setLatitude(productLocation.getLatitude());
                productsResponse.setLongitude(productLocation.getLongitude());

                Set<ProductImage> productImages = product.getProductImages();
                List<String> imageIds = new ArrayList<>();
                for (ProductImage productImage : productImages) {
                    imageIds.add(productImage.getImageId());
                }
                productsResponse.setImageids(imageIds);
                productsResponses.add(productsResponse);
                productStatus.setProductsResponse(productsResponses);
                productStatus.setStatus(true);
                productStatus.setMessage("Product Found");
            }
        }
        // ArrayList<Set<ProductImages>> productImages=new ArrayList<>;
        // for(Product product:products){
        //
        // productImages.add(product.getProductImages());
        //
        // }
        // for()

        return productStatus;
    }

    @Override
    public ProductStatus getUserProducts(String email) {
        ProductStatus productStatus = new ProductStatus();

        try {
            List<ProductsResponse> productsResponses = new ArrayList<>();
            List<Product> products;
            products = productRepository.findAllProductsByUserName(email)
                    .orElseThrow(() -> new NoSuchElementException("No Product Found"));
            if (products.isEmpty()) {

                ProductsResponse productsResponse = new ProductsResponse();
                System.out.println("No product Found");
                productStatus.setStatus(false);
                productStatus.setMessage("No product Found");
                productsResponses.add(productsResponse);
            }
            for (Product product : products) {
                if (product.getProductStatus()) {
                    ProductsResponse productsResponse = new ProductsResponse();
                    UserCredentials userCredentials = userCredentialsRepository.findById(product.getUser().getUserid())
                            .orElseThrow();
                    productsResponse.setEmail(userCredentials.getEmail());
                    productsResponse.setProductID(product.getProductId());
                    productsResponse.setTitle(product.getTitle());
                    productsResponse.setDescription(product.getDescription());
                    productsResponse.setCategory(product.getCategory());
                    productsResponse.setPrice(product.getPrice());
                    productsResponse.setTimeStamp(product.getTimestamp());
                    ProductLocation productLocation = productLocationRepository
                            .findByProductProductId(product.getProductId());
                    productsResponse.setLatitude(productLocation.getLatitude());
                    productsResponse.setLongitude(productLocation.getLongitude());

                    Set<ProductImage> productImages = product.getProductImages();
                    List<String> imageIds = new ArrayList<>();
                    for (ProductImage productImage : productImages) {
                        imageIds.add(productImage.getImageId());
                    }
                    productsResponse.setImageids(imageIds);
                    productsResponses.add(productsResponse);
                    productStatus.setProductsResponse(productsResponses);
                    productStatus.setStatus(true);
                    productStatus.setMessage("Product Found");

                }
            }
        } catch (NoSuchElementException e) {

            System.out.println("No product Found");
            productStatus.setStatus(false);
            productStatus.setMessage(e.getMessage());

        }

        return productStatus;
    }

    @Override
    public ProductStatus getUserSavedProducts(String email) {
        ProductStatus productStatus = new ProductStatus();

        // try{
        List<ProductsResponse> productsResponses = new ArrayList<>();
        List<SavedProduct> products;
        products = savedProductRepository.findAllSavedProductsByUserName(email)
                .orElseThrow(() -> new NoSuchElementException("No Product Found"));

        for (SavedProduct product : products) {
            // if(product.getProductStatus()){
            ProductsResponse productsResponse = new ProductsResponse();
            // productsResponse.setTitle(product.getTitle());
            // productsResponse.setDescription(product.getDescription());
            // productsResponse.setCategory(product.getCategory());
            // productsResponse.setPrice(product.getPrice());
            // productsResponse.setTimeStamp(product.getTimestamp());
            // ProductLocation productLocation=
            // productLocationRepository.findByProductProductId(product.getProductId());
            // productsResponse.setLatitude(productLocation.getLatitude());
            // productsResponse.setLongitude(productLocation.getLongitude());

            // Set<ProductImage> productImages = product.getProductImages();
            // List<String> imageIds = new ArrayList<>();
            // for (ProductImage productImage : productImages) {
            // imageIds.add(productImage.getImageId());
            // }
            // productsResponse.setImageids(imageIds);
            productsResponses.add(productsResponse);
            productStatus.setProductsResponse(productsResponses);
            productStatus.setStatus(true);
            productStatus.setMessage("Product Found");

        }
        // }
        // }
        // catch (NoSuchElementException e) {
        //
        // System.out.println("No product Found");
        // productStatus.setStatus(false);
        // productStatus.setMessage(e.getMessage());
        //
        // }

        return productStatus;
    }

    @Override
    public ResponseStatus deleteUserSavedProducts(Long id) {
        ResponseStatus responseStatus = new ResponseStatus();
        try {
            SavedProduct savedProduct;
            savedProduct = savedProductRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("No Product Found"));
            savedProductRepository.delete(savedProduct);
            responseStatus.setStatus(true);
            responseStatus.setMessage("Product Deleted Successfully");

        } catch (NoSuchElementException e) {
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());
        }
        return responseStatus;

    }

    @Override
    public ResponseStatus deleteUserProducts(Long id) {
        ResponseStatus responseStatus = new ResponseStatus();
        try {
            Product product;
            product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No Product Found"));
            productRepository.delete(product);
            responseStatus.setStatus(true);
            responseStatus.setMessage("Product Deleted Successfully");
        } catch (NoSuchElementException e) {
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());
        }
        return responseStatus;
    }

    @Override
    public ResponseStatus editUserProducts(Long id, ProductUploadDto productUploadDto, MultipartFile[] images) {
        ResponseStatus responseStatus = new ResponseStatus();
        try {
            Product product;
            ProductLocation productLocation;
            product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No Product Found"));
            productLocation = productLocationRepository.findByProductProductId(id);
            if (productUploadDto.getCategory() != null) {
                product.setCategory(productUploadDto.getCategory());
            }
            if (productUploadDto.getCategory() != null) {
                product.setTitle(productUploadDto.getTitle());
            }
            if (productUploadDto.getCategory() != null) {
                product.setDescription(productUploadDto.getDescription());
            }
            if (productUploadDto.getCategory() != null) {
                product.setPrice(productUploadDto.getPrice());
            }
            if (productUploadDto.getCategory() != null) {
                product.setTimestamp(productUploadDto.getTimeStamp());
            }

            if (images != null) {
                Set<ProductImage> productImages = new HashSet<>();
                for (MultipartFile image : images) {
                    ProductImage productImage = new ProductImage();
                    productImage.setImageId(imageIdGenerator.generateImageId());
                    productImage.setImage(image.getBytes());
                    productImage.setProduct(product);
                    productImages.add(productImage);
                }
                // Save the ProductImages entity
                productImagesRepository.saveAll(productImages);
                product.setProductImages(productImages);
            }
            // Save the ProductImages

            // Save the Product entity
            productRepository.save(product);
            if (productUploadDto.getLatitude() != null) {
                productLocation.setLatitude(productUploadDto.getLatitude());
            }
            if (productUploadDto.getLongitude() != null) {
                productLocation.setLongitude(productUploadDto.getLongitude());
            }

            productLocation.setProduct(product);

            // Save the ProductLocation entity
            productLocationRepository.save(productLocation);

            responseStatus.setStatus(true);
            responseStatus.setMessage("Product Uploaded Successfully");
        } catch (NoSuchElementException | IOException e) {
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());
        }

        return responseStatus;
    }

    @Override
    public ResponseStatus deleteProductImage(String id) {
        ResponseStatus responseStatus = new ResponseStatus();
        productImagesRepository.delete(productImagesRepository.findByImageId(id));
        responseStatus.setStatus(true);
        responseStatus.setMessage("Product Deleted Successfully");
        return responseStatus;
    }

    // #################### PAYMENT ##########

    public String getCustomerIdByEmail(String email) throws StripeException {

        String customerId;
        email = email.replaceAll("[^a-zA-Z0-9@.]", "");
        CustomerListParams listParams = CustomerListParams.builder()
                .setEmail(email)
                .build();
        List<Customer> customers = Customer.list(listParams).getData();
        if (!customers.isEmpty()) {
            Customer customer = customers.get(0);
            System.out.println(customer.getId());
            customerId = customer.getId();
        } else {
            customerId = email;
        }

        return customerId;
    }

    @Override
    public ResponseStatus reserveProductPaymentIntent(String paymentData) throws IOException {
        ResponseStatus paymentResponse = new ResponseStatus();
        try {
            Stripe.apiKey = stripeKey;
            byte[] paymentDataBytes = paymentData.getBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode paymentDataJson = objectMapper.readTree(paymentDataBytes);

            Long amount = paymentDataJson.get("amount").asLong();
            String email = paymentDataJson.get("email").toString();
            try {

                String customerId = getCustomerIdByEmail(email);
                System.out.println(customerId);
                Map<String, Object> params = new HashMap<>();
                params.put("amount", amount);
                params.put("currency", "pkr");
                params.put("payment_method_types", Arrays.asList("card"));
                params.put("return_url", "https://example.com/return");
                params.put("payment_method", "pm_card_visa");
                params.put("customer", customerId);
                params.put("confirm", true);

                PaymentIntent paymentIntent = PaymentIntent.create(params);
                paymentResponse.setStatus(true);
                paymentResponse.setMessage(paymentIntent.getStatus());

            } catch (StripeException e) {
                // TODO Auto-generated catch block
                paymentResponse.setStatus(false);
                paymentResponse.setMessage("Failed");
                System.out.println("Stripe message: " + e.getMessage());
            }

        } catch (NoSuchElementException e) {
            paymentResponse.setStatus(false);
            paymentResponse.setMessage(e.getMessage());
        }
        return paymentResponse;

    }

    @Override
    public ResponseStatus getCustomerBalance(String email) {
        ResponseStatus responseStatus = new ResponseStatus();
        try {
            String customerId = getCustomerIdByEmail(email);

            // Get all charges for the customer
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("customer", customerId);
            ChargeCollection chargeCollection = Charge.list(chargeParams);

            // Calculate the total payment amount
            int totalAmount = 0;

            for (Charge charge : chargeCollection.getData()) {
                if (charge.getStatus().equals("succeeded")) {
                    totalAmount += charge.getAmount();
                }
            }

            responseStatus.setStatus(true);
            responseStatus.setMessage(String.valueOf(totalAmount));

        } catch (Exception e) {
            // TODO: handle exception
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());
        }
        return responseStatus;
    }
}
