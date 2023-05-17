package KirayoApp.Kirayo.implementation;

import KirayoApp.Kirayo.beans.ImageIdGenerator;
import KirayoApp.Kirayo.dto.*;
import KirayoApp.Kirayo.filter.ProductSorter;
import KirayoApp.Kirayo.model.*;
import KirayoApp.Kirayo.repository.*;
import KirayoApp.Kirayo.returnStatus.*;
import KirayoApp.Kirayo.service.ProductService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerListParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private ProductReviewRepository productReviewRepository;
    @Autowired
    private ProductSorter productSorter;
    @Autowired
    private ProductRequestRepository productRequestRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Value("${stripe.api.secretKey}")
    String stripeKey;

    @Transactional
    @Override
    public ResponseStatus productUpload(ProductUploadDto productUploadDto, MultipartFile[] images)  {

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
            product.setPrice(productUploadDto.getPrice()+productUploadDto.getPrice()*0.10);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public ProductStatus getAllProducts(String email) {
        ProductStatus productStatus = new ProductStatus();

        List<Product> products;
        List<Product> productsByUserName;
        List<SavedProduct> savedProducts;
        savedProducts = savedProductRepository.findAllSavedProductsByUserName(email).orElseThrow();
        products = productRepository.findAll();
        productsByUserName = productRepository.findAllProductsByUserName(email).orElseThrow();


        Map<Long, Product> productIDs = productsByUserName.stream()
                .collect(Collectors.toMap(sp -> sp.getProductId(), sp -> sp));
        List<ProductsResponse> productsResponses = new ArrayList<>();
        Map<Long, SavedProduct> savedProductIDs = savedProducts.stream()
                .collect(Collectors.toMap(sp -> sp.getProduct().getProductId(), sp -> sp));

        for (Product product : products) {
            if (product.getProductStatus() && !productIDs.containsKey(product.getProductId())) {

                ProductsResponse productsResponse = new ProductsResponse();
                List<ReservationResponse> reservationResponses=new ArrayList<>();
                List<ProductReview> productReviews=new ArrayList<>();
                productReviews=productReviewRepository.findAllByProductProductId(product.getProductId());
                List<Reservation> reservations=  reservationRepository.findAllByRequestIdProductProductId(product.getProductId());
                if(reservations.isEmpty()){
                    reservationResponses = null;
                }
                else{
                    for(Reservation reservation:reservations){
                        ReservationResponse reservationResponse=new ReservationResponse();
                        reservationResponse.setStartedAt(reservation.getStartedAt());
                        reservationResponse.setEndedAt(reservation.getEndedAt());
                        reservationResponses.add(reservationResponse);
                    }

                }



                double averageRating = productReviews.stream()
                        .mapToDouble(ProductReview::getRating)
                        .average()
                        .orElse(0.0);
                averageRating= Double.parseDouble(String.format("%.2f",averageRating));
                if (savedProductIDs.containsKey(product.getProductId())) {
                    productsResponse.setIs_Saved(true);

                } else {
                    productsResponse.setIs_Saved(false);
                }
                UserCredentials userCredentials = userCredentialsRepository.findById(product.getUser().getUserid())
                        .orElseThrow();
                productsResponse.setEmail(userCredentials.getEmail());
                productsResponse.setProductID(product.getProductId());
                productsResponse.setTitle(product.getTitle());
                productsResponse.setDescription(product.getDescription());
                productsResponse.setCategory(product.getCategory());
                productsResponse.setPrice(product.getPrice());
                productsResponse.setTimeStamp(product.getTimestamp());
                productsResponse.setRating(averageRating);
                ProductLocation productLocation = productLocationRepository
                        .findProductLocationByProductId(product.getProductId());
                productsResponse.setLatitude(productLocation.getLatitude());
                productsResponse.setLongitude(productLocation.getLongitude());
                productsResponse.setTotalreviews(productReviews.size());

                Set<ProductImage> productImages = product.getProductImages();
                List<String> imageIds = new ArrayList<>();
                for (ProductImage productImage : productImages) {
                    imageIds.add(productImage.getImageId());
                }
                productsResponse.setImageids(imageIds);

                productsResponse.setProductReservations(reservationResponses);
                productsResponses.add(productsResponse);


            }
        }


        productStatus.setProductsResponse(productSorter.sortProductsByTimeStampDescending(productsResponses));
        productStatus.setStatus(true);
        productStatus.setMessage("Product Found");

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
                    List<ProductReview> productReviews=productReviewRepository.findAllByProductProductId(product.getProductId());

                    double averageRating = productReviews.stream()
                            .mapToDouble(ProductReview::getRating)
                            .average()
                            .orElse(0.0);
                    averageRating= Double.parseDouble(String.format("%.2f",averageRating));
                    productsResponse.setEmail(userCredentials.getEmail());
                    productsResponse.setProductID(product.getProductId());
                    productsResponse.setTitle(product.getTitle());
                    productsResponse.setDescription(product.getDescription());
                    productsResponse.setCategory(product.getCategory());
                    productsResponse.setPrice(product.getPrice()-product.getPrice()*0.10);
                    productsResponse.setTimeStamp(product.getTimestamp());
                    productsResponse.setRating(averageRating);
                    ProductLocation productLocation = productLocationRepository
                            .findProductLocationByProductId(product.getProductId());
                    productsResponse.setLatitude(productLocation.getLatitude());
                    productsResponse.setLongitude(productLocation.getLongitude());

                    Set<ProductImage> productImages = product.getProductImages();
                    List<String> imageIds = new ArrayList<>();
                    for (ProductImage productImage : productImages) {
                        imageIds.add(productImage.getImageId());
                    }
                    productsResponse.setImageids(imageIds);
                    productsResponses.add(productsResponse);
                    productStatus.setProductsResponse(productSorter.sortProductsByTimeStampDescending(productsResponses));
                    productStatus.setStatus(true);
                    productStatus.setMessage("Product Found");


                }
            }
        } catch (NoSuchElementException e) {

            System.out.println("No product Found");
            productStatus.setStatus(false);
            productStatus.setMessage(e.getMessage());

        }
//        productStatus.getProductsResponse().sort();
        return productStatus;
    }

    @Override
    public SavedProductStatus getUserSavedProducts(String email) {
        SavedProductStatus savedProductStatus = new SavedProductStatus();

        try {
            List<SavedProductResponse> savedProductResponses = new ArrayList<>();
            List<SavedProduct> savedProducts;
            savedProducts = savedProductRepository.findAllSavedProductsByUserName(email).orElseThrow();
            if (savedProducts.isEmpty()) {
                throw new NoSuchElementException("You haven't added any products to your favorites yet.");
            }

            for (SavedProduct savedProduct : savedProducts) {
                SavedProductResponse savedProductResponse = new SavedProductResponse();
                List<ProductReview> productReviews=productReviewRepository.findAllByRenteeUserid(savedProduct.getProduct().getProductId());

                double averageRating = productReviews.stream()
                        .mapToDouble(ProductReview::getRating)
                        .average()
                        .orElse(0.0);
                averageRating= Double.parseDouble(String.format("%.2f",averageRating));
                savedProductResponse.setSavedProductId(savedProduct.getId());
                savedProductResponse.setProductID(savedProduct.getProduct().getProductId());
                savedProductResponse.setEmail(email);

                savedProductResponse.setTitle(savedProduct.getProduct().getTitle());
                savedProductResponse.setDescription(savedProduct.getProduct().getDescription());
                savedProductResponse.setCategory(savedProduct.getProduct().getCategory());
                savedProductResponse.setPrice(savedProduct.getProduct().getPrice());
                savedProductResponse.setTimeStamp(savedProduct.getProduct().getTimestamp());
                ProductLocation productLocation = productLocationRepository
                        .findProductLocationByProductId(savedProduct.getProduct().getProductId());
                savedProductResponse.setLatitude(productLocation.getLatitude());
                savedProductResponse.setLongitude(productLocation.getLongitude());
                savedProductResponse.setRating(averageRating);

                Set<ProductImage> productImages = savedProduct.getProduct().getProductImages();
                List<String> imageIds = new ArrayList<>();
                for (ProductImage productImage : productImages) {
                    imageIds.add(productImage.getImageId());
                }
                savedProductResponse.setImageids(imageIds);
                savedProductResponses.add(savedProductResponse);


            }
            savedProductStatus.setSavedProductResponses(productSorter.sortSavedProductsByTimeStampDescending(savedProductResponses));
            savedProductStatus.setStatus(true);
            savedProductStatus.setMessage("Product Found");
        }

        catch (NoSuchElementException e) {

            System.out.println("No product Found");
            savedProductStatus.setStatus(false);
            savedProductStatus.setMessage(e.getMessage());

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

        return savedProductStatus;
    }
    @Override
    public ProductsResponse getProductByProductId(Map<Long, SavedProduct> savedProductIDs,Long productId){
        ProductsResponse productsResponse=new ProductsResponse();
        Product product;
        product = productRepository.findById(productId).orElseThrow();
        UserCredentials userCredentials = userCredentialsRepository.findById(product.getUser().getUserid())
                .orElseThrow();
        List<ProductReview> productReviews=productReviewRepository.findAllByProductProductId(product.getProductId());

        double averageRating = productReviews.stream()
                .mapToDouble(ProductReview::getRating)
                .average()
                .orElse(0.0);
        averageRating= Double.parseDouble(String.format("%.2f",averageRating));
        productsResponse.setIs_Saved(savedProductIDs.containsKey(product.getProductId()));
        productsResponse.setEmail(userCredentials.getEmail());
        productsResponse.setProductID(product.getProductId());
        productsResponse.setTitle(product.getTitle());
        productsResponse.setDescription(product.getDescription());
        productsResponse.setCategory(product.getCategory());
        productsResponse.setPrice(product.getPrice());
        productsResponse.setTimeStamp(product.getTimestamp());
        productsResponse.setRating(averageRating);
        ProductLocation productLocation = productLocationRepository
                .findProductLocationByProductId(product.getProductId());
        productsResponse.setLatitude(productLocation.getLatitude());
        productsResponse.setLongitude(productLocation.getLongitude());

        Set<ProductImage> productImages = product.getProductImages();
        List<String> imageIds = new ArrayList<>();
        for (ProductImage productImage : productImages) {
            imageIds.add(productImage.getImageId());
        }
        productsResponse.setImageids(imageIds);


        return productsResponse;
    }
    @Override
    public ProductStatus searchProductByTitle(String email, String title){
        ProductStatus productStatus=new ProductStatus();
        List<Product> products=productRepository.findByTitleContainingIgnoreCase(title);
        if(products.isEmpty()){
            productStatus.setStatus(false);
            productStatus.setMessage("No Product Found");
        }
        else{
            List<SavedProduct> savedProducts=new ArrayList<>();
            savedProducts = savedProductRepository.findAllSavedProductsByUserName(email).orElseThrow();
            Map<Long, SavedProduct> savedProductIDs = savedProducts.stream()
                    .collect(Collectors.toMap(sp -> sp.getProduct().getProductId(), sp -> sp));
            List<ProductsResponse> productsResponses=new ArrayList<>();

            for(Product product:products){
                productsResponses.add(getProductByProductId(savedProductIDs,product.getProductId()));
            }
            productStatus.setProductsResponse(productsResponses);
            productStatus.setStatus(true);
            productStatus.setMessage("Product Found");

        }


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

    public ResponseStatus deleteUserProducts(Long id) {
        ResponseStatus responseStatus = new ResponseStatus();
        try {

            Product product;
            ProductLocation productLocation;
            List<SavedProduct> savedProducts = savedProductRepository.findAllSavedProductsByProductId(id).orElseThrow();
            for (SavedProduct savedProduct : savedProducts) {
                savedProductRepository.delete(savedProduct);
            }
            // List<ProductImage>
            // productImages=productImagesRepository.findProductByProductId(id);
            // for(ProductImage productImage:productImages){
            // System.out.println(productImage.getProduct().getProductId());
            // productImagesRepository.delete(productImage);
            //
            // }

            productLocation = productLocationRepository.findProductLocationByProductId(id);

            productLocationRepository.delete(productLocation);

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
    public ResponseStatus editUserProducts(Long id, ProductUploadDto productUploadDto) {
        ResponseStatus responseStatus = new ResponseStatus();
        try {
            Product product;
            ProductLocation productLocation;

            product=productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No Product Found"));
            productLocation=productLocationRepository.findProductLocationByProductId(id);

            System.out.println(productLocation);
            if(productUploadDto.getCategory()!=null){
                product.setCategory(productUploadDto.getCategory());
            }
            if(productUploadDto.getTitle()!=null){
                product.setTitle(productUploadDto.getTitle());
            }
            if(productUploadDto.getDescription()!=null){
                product.setDescription(productUploadDto.getDescription());
            }
            if(productUploadDto.getPrice()!=0){
                product.setPrice(productUploadDto.getPrice());
            }
            if(productUploadDto.getTimeStamp()!=null){
                product.setTimestamp(productUploadDto.getTimeStamp());
            }




            // Save the Product entity
            productRepository.save(product);
            if (productUploadDto.getLatitude() != null) {
                productLocation.setLatitude(productUploadDto.getLatitude());
            }
            if (productUploadDto.getLongitude() != null) {
                productLocation.setLongitude(productUploadDto.getLongitude());
            }



            // Save the ProductLocation entity
            productLocationRepository.save(productLocation);

            responseStatus.setStatus(true);
            responseStatus.setMessage("Product Updated Successfully");
        }
            catch (NoSuchElementException  e) {
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());
        }

        return responseStatus;
    }

    @Override
    public ResponseStatus deleteProductImage(String id) {
        ResponseStatus responseStatus = new ResponseStatus();
        ProductImage productImage = productImagesRepository.findByImageId(id);
        if (productImage != null) {
            productImage.getProduct().removeImage(productImage);
            productImagesRepository.delete(productImage);
        }
        responseStatus.setStatus(true);
        responseStatus.setMessage("Product Image Deleted Successfully");
        return responseStatus;
    }
    @Override
    public ResponseStatus editProductImage(Long productId, ImageIdsDao imageIds, MultipartFile[] images) throws IOException {
        ResponseStatus responseStatus = new ResponseStatus();
        try {

            Product product = productRepository.findById(productId).orElseThrow();
            int numberOfImages = productImagesRepository.findImagesCountByProductId(productId);
            if (numberOfImages < 11) {
                if (imageIds != null) {
                     // Assuming you have already deserialized the JSON into the ImageIdsDao object

                    String[] imageIdsArray = imageIds.getImageIds();  // Retrieve the array of image IDs

                    for (int i = 0; i < imageIdsArray.length; i++) {
                        System.out.println(imageIdsArray[i]);
                        ProductImage productImage = productImagesRepository.findByImageId(imageIdsArray[i]);
                        System.out.println(productImage.getId());
                        productImage.setImage(images[i].getBytes());
                        productImagesRepository.save(productImage);
                    }
                } else if (images != null) {
                    Set<ProductImage> productImages = new HashSet<>();
                    productImages = product.getProductImages();
                    for (MultipartFile image : images) {
                        ProductImage productImage = new ProductImage();
                        productImage.setImageId(imageIdGenerator.generateImageId());
                        productImage.setImage(image.getBytes());
                        productImage.setProduct(product);
                        productImagesRepository.save(productImage);
                        productImages.add(productImage);
                    }
                    product.setProductImages(productImages);
                    productRepository.save(product);


                }
                responseStatus.setStatus(true);
                responseStatus.setMessage("Product Images Updated Successfully");

            } else {
                responseStatus.setStatus(false);
                responseStatus.setMessage("Product Images can't be updated");
            }
        }
        catch (IOException e) {

            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());
        }



        return responseStatus;
    }

    @Override
    public ResponseStatus productReview(ProductReviewDao productReviewDao) {
        ResponseStatus responseStatus=new ResponseStatus();
        try{
            UserCredentials userCredentials=userCredentialsRepository.findByEmail(productReviewDao.getEmail()).orElseThrow(() -> new NoSuchElementException("No User Found"));
            UserDetails userDetails=userDetailsRepository.findById(userCredentials.getUserId()).orElseThrow(() -> new NoSuchElementException("No User Found"));
            Product product=productRepository.findById(productReviewDao.getProductId()).orElseThrow(() -> new NoSuchElementException("No Product Found"));
            ProductReview productReview=new ProductReview();
            productReview.setComment(productReviewDao.getComment());
            productReview.setRating(productReviewDao.getRating());
            productReview.setRentee(userDetails);
            productReview.setProduct(product);
            productReviewRepository.save(productReview);
            responseStatus.setStatus(true);
            responseStatus.setMessage("Product Review Added Successfully");

        }
        catch(NoSuchElementException e){
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());

        }


        return responseStatus;
    }
    @Override
    public ReviewStatus getProductReviews(Long productId) {

        List<ProductReview>productReviews=productReviewRepository.findAllByProductProductId(productId);
        ReviewStatus reviewStatus=new ReviewStatus();
        if(productReviews.isEmpty())
        {
            reviewStatus.setStatus(false);
            reviewStatus.setMessage("Reviews Not found");
            reviewStatus.setProductsResponse(null);
        }
        else{
            List<ReviewResponse>reviewResponses=new ArrayList<>();
            for (ProductReview productReview:productReviews){
                ReviewResponse reviewResponse=new ReviewResponse();
                reviewResponse.setRenterName(productReview.getRentee().getFullname());
                reviewResponse.setRenterCity(productReview.getRentee().getCity());
                reviewResponse.setReviewId(productReview.getId());
                reviewResponse.setComment(productReview.getComment());
                reviewResponse.setRating(productReview.getRating());
                reviewResponse.setTimeStamp(productReview.getTimestamp());
                reviewResponses.add(reviewResponse);

            }
            reviewStatus.setStatus(true);
            reviewStatus.setMessage("Reviews FOUND");
            reviewStatus.setProductsResponse(reviewResponses);


        }

        return reviewStatus;
    }
    @Override
    public ReviewStatus getProductReviewByUser(String email) {
        ReviewStatus reviewStatus=new ReviewStatus();
        try{
            UserCredentials userCredentials=userCredentialsRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("No User Found"));
            List<ProductReview>productReviews=productReviewRepository.findAllByRenteeUserid(userCredentials.getUserId());
            if(productReviews.isEmpty())
            {
                reviewStatus.setStatus(false);
                reviewStatus.setMessage("Reviews Not found");
                reviewStatus.setProductsResponse(null);
            }
            else {
                List<ReviewResponse> reviewResponses = new ArrayList<>();
                for (ProductReview productReview : productReviews) {
                    ReviewResponse reviewResponse = new ReviewResponse();
                    reviewResponse.setRenterName(productReview.getRentee().getFullname());
                    reviewResponse.setRenterCity(productReview.getRentee().getCity());
                    reviewResponse.setReviewId(productReview.getId());
                    reviewResponse.setComment(productReview.getComment());
                    reviewResponse.setRating(productReview.getRating());
                    reviewResponse.setTimeStamp(productReview.getTimestamp());
                    reviewResponses.add(reviewResponse);

                }
                reviewStatus.setStatus(true);
                reviewStatus.setMessage("Reviews FOUND");
                reviewStatus.setProductsResponse(reviewResponses);
            }

        }
        catch(NoSuchElementException e){
            reviewStatus.setStatus(false);
            reviewStatus.setMessage("Reviews Not found");
            reviewStatus.setProductsResponse(null);
        }
        return reviewStatus;
    }
    @Override
    public ResponseStatus editProductReview(Long productReviewId,ProductReviewDao productReviewDao) {
        ResponseStatus responseStatus=new ResponseStatus();
        try{

            ProductReview productReview=new ProductReview();
            productReview=productReviewRepository.findById(productReviewId).orElseThrow(()-> new NoSuchElementException("No Review Found"));
            if(productReviewDao.getComment()!=null){
                productReview.setComment(productReviewDao.getComment());
            }
            else{
                productReview.setRating(productReviewDao.getRating());
            }



            productReviewRepository.save(productReview);
            responseStatus.setStatus(true);
            responseStatus.setMessage("Product Review Updated Successfully");

        }
        catch(NoSuchElementException e){
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());

        }


        return responseStatus;
    }

    @Override
    public ResponseStatus productRequest(String email, ProductRequestDao productRequestDao) {
        ProductRequest productRequest=new ProductRequest();
        UserCredentials userCredentials=userCredentialsRepository.findByEmail(email).orElseThrow();
        UserDetails userDetails=userDetailsRepository.findById(userCredentials.getUserId()).orElseThrow();
        Product product=productRepository.findById(productRequestDao.getProductId()).orElseThrow();
        productRequest.setProduct(product);
        productRequest.setRequestStatus("pending");
        productRequest.setTimestamp(productRequestDao.getTimeStamp());
        productRequest.setStartDate(productRequestDao.getStartDate());
        productRequest.setEndDate(productRequestDao.getEndDate());
        productRequest.setTotalDays(ChronoUnit.DAYS.between(productRequestDao.getStartDate(),productRequestDao.getEndDate()));
        productRequest.setTotalPrice(productRequestDao.getTotalPrice());
        productRequest.setRenter(userDetails);
        productRequestRepository.save(productRequest);
        ResponseStatus responseStatus=new ResponseStatus();
        responseStatus.setMessage("Request Submitted Succssfully");
        responseStatus.setStatus(true);

        return responseStatus;
    }

    @Override
    public ResponseStatus productAcceptance(Long requestId, ProductAcceptanceDao productAcceptanceDao) {
        ResponseStatus responseStatus=new ResponseStatus();
        ProductRequest productRequest=productRequestRepository.findById(requestId).orElseThrow();
        Reservation reservation= new Reservation();
        if(productAcceptanceDao.getIs_accept() & Objects.equals(productRequest.getRequestStatus(), "pending")){
            productRequest.setRequestStatus("accept");
            reservation.setStartedAt(productRequest.getStartDate());
            reservation.setEndedAt(productRequest.getEndDate());
            reservation.setTotalPrice(productRequest.getTotalPrice());
            reservation.setRequestId(productRequest);
            reservationRepository.save(reservation);
            responseStatus.setStatus(true);
            responseStatus.setMessage("Product Request Accepted");
        }
        else if(!productAcceptanceDao.getIs_accept() & Objects.equals(productRequest.getRequestStatus(), "pending")){

            productRequest.setRequestStatus("reject");
            responseStatus.setStatus(true);
            responseStatus.setMessage("Product Request Rejected");
        }
        else{
            responseStatus.setStatus(false);
            responseStatus.setMessage("Already "+productRequest.getRequestStatus()+"ed");
        }
        productRequestRepository.save(productRequest);
        return responseStatus;
    }
    @Override
    public ResponseStatus productCancellationByRenter(Long requestId){
        ResponseStatus responseStatus=new ResponseStatus();
        ProductRequest productRequest=productRequestRepository.findById(requestId).orElseThrow();

        if (!Objects.equals(productRequest.getRequestStatus(), "pending")){
            productRequestRepository.delete(productRequest);
            responseStatus.setStatus(true);
            responseStatus.setMessage("Cancelled Successfully");
        }
        else{
            responseStatus.setStatus(false);
            responseStatus.setMessage("Already "+productRequest.getRequestStatus()+"ed");
        }
        return responseStatus;
    }

    @Override
    public ProductRequestStatus getProductRequestsByRenter(String email) {
        UserDetails userDetails=userDetailsRepository.findByEmail(email);
        ProductRequestStatus productRequestStatus=new ProductRequestStatus();
        List<ProductRequest> productRequests=productRequestRepository.findAllByRenterUserid(userDetails.getUserid());
        if(productRequests.isEmpty()){
            productRequestStatus.setStatus(false);
            productRequestStatus.setMessage("No product Requested by you");
        }
        else{
            List<SavedProduct> savedProducts=new ArrayList<>();
            savedProducts = savedProductRepository.findAllSavedProductsByUserName(email).orElseThrow();
            Map<Long, SavedProduct> savedProductIDs = savedProducts.stream()
                    .collect(Collectors.toMap(sp -> sp.getProduct().getProductId(), sp -> sp));
            List<ProductRequestResponse> productRequestResponses=new ArrayList<>();
            for(ProductRequest productRequest:productRequests){
                ProductRequestResponse productRequestResponse=new ProductRequestResponse();
                productRequestResponse.setStartDate(productRequest.getStartDate());
                productRequestResponse.setEndDate(productRequest.getEndDate());
                productRequestResponse.setTotalPrice(productRequest.getTotalPrice());
                productRequestResponse.setRequestId(productRequest.getRequestId());
                productRequestResponse.setRequestStatus(productRequest.getRequestStatus());
                productRequestResponse.setTotalDays(productRequest.getTotalDays());
                productRequestResponse.setTimeStamp(productRequest.getTimestamp());
                productRequestResponse.setProduct(getProductByProductId(savedProductIDs,productRequest.getProduct().getProductId()));
                productRequestResponses.add(productRequestResponse);
            }
            productRequestStatus.setProductRequestResponses(productSorter.sortProductRequestResponsesByTimeStampDescending(productRequestResponses));
            productRequestStatus.setStatus(true);
            productRequestStatus.setMessage("Product Requests Found");
        }


        return productRequestStatus;
    }

    @Override
    public ProductRequestStatus getProductRequestsByRentee(String email) {
        UserDetails userDetails=userDetailsRepository.findByEmail(email);
        ProductRequestStatus productRequestStatus=new ProductRequestStatus();
        List<ProductRequest> productRequests=productRequestRepository.findAllByProductUserUserid(userDetails.getUserid());
        if(productRequests.isEmpty()){
            productRequestStatus.setStatus(false);
            productRequestStatus.setMessage("No product Requested by you");
        }
        else{
            List<SavedProduct> savedProducts=new ArrayList<>();
            savedProducts = savedProductRepository.findAllSavedProductsByUserName(email).orElseThrow();
            Map<Long, SavedProduct> savedProductIDs = savedProducts.stream()
                    .collect(Collectors.toMap(sp -> sp.getProduct().getProductId(), sp -> sp));
            List<ProductRequestResponse> productRequestResponses=new ArrayList<>();
            for(ProductRequest productRequest:productRequests){
                ProductRequestResponse productRequestResponse=new ProductRequestResponse();
                productRequestResponse.setRequestId(productRequest.getRequestId());
                productRequestResponse.setRequestStatus(productRequest.getRequestStatus());
                productRequestResponse.setTimeStamp(productRequest.getTimestamp());
                productRequestResponse.setStartDate(productRequest.getStartDate());
                productRequestResponse.setEndDate(productRequest.getEndDate());
                productRequestResponse.setTotalDays(productRequest.getTotalDays());
                productRequestResponse.setTotalPrice(productRequest.getTotalPrice());
                productRequestResponse.setProduct(getProductByProductId(savedProductIDs,productRequest.getProduct().getProductId()));
                productRequestResponses.add(productRequestResponse);
            }
            productRequestStatus.setProductRequestResponses(productSorter.sortProductRequestResponsesByTimeStampDescending(productRequestResponses));
            productRequestStatus.setStatus(true);
            productRequestStatus.setMessage("Product Requests Found");
        }

        return productRequestStatus;
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
    public ResponseStatus reserveProductPaymentIntent(String email, Long amount) throws IOException {
        ResponseStatus paymentResponse = new ResponseStatus();
        try {
            Stripe.apiKey = stripeKey;

            try {

                String customerId = getCustomerIdByEmail(email);
                System.out.println(customerId);
                Map<String, Object> params = new HashMap<>();
                params.put("amount", amount);
                params.put("currency", "pkr");
                params.put("payment_method_types", Arrays.asList("card"));
                params.put("return_url", "https://example.com/return");
                params.put("payment_method", "pm_card_visa");
               // params.put("customer", customerId);
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
