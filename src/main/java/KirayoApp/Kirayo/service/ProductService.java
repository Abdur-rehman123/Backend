package KirayoApp.Kirayo.service;

import KirayoApp.Kirayo.dto.*;
import KirayoApp.Kirayo.returnStatus.ProductStatus;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;
import KirayoApp.Kirayo.returnStatus.ReviewStatus;
import KirayoApp.Kirayo.returnStatus.SavedProductStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    ResponseStatus productUpload(ProductUploadDto productUploadDto, MultipartFile[] images) throws IOException;

    ResponseStatus savedProduct(SavedProductDto savedProductDto);

    ProductStatus getAllProducts(String email);

    ProductStatus getUserProducts(String email);

    SavedProductStatus getUserSavedProducts(String email);

    ResponseStatus deleteUserSavedProducts(Long email);

    ResponseStatus deleteUserProducts(Long email);

    ResponseStatus editUserProducts(Long id, ProductUploadDto productUploadDto);

    ResponseStatus deleteProductImage(String id);
     ResponseStatus editProductImage(Long productId, ImageIdsDao imageIds, MultipartFile[] images) throws IOException;

     ResponseStatus productReview(ProductReviewDao productReviewDao);

    ReviewStatus getProductReviews(Long productId);

    ReviewStatus getProductReviewByUser(String email);

    ResponseStatus editProductReview(Long productReviewId, ProductReviewDao productReviewDao);

    ResponseStatus productRequest(String email, ProductRequestDao productRequestDao);
    // PAYMENT

//    ResponseStatus getCustomerBalance(String email);

//    ResponseStatus reserveProductPaymentIntent(String paymentData) throws IOException;
}
