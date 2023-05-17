package KirayoApp.Kirayo.service;

import KirayoApp.Kirayo.dto.*;
import KirayoApp.Kirayo.model.SavedProduct;
import KirayoApp.Kirayo.returnStatus.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ProductService {

    ResponseStatus productUpload(ProductUploadDto productUploadDto, MultipartFile[] images) throws IOException;

    ResponseStatus savedProduct(SavedProductDto savedProductDto);

    ProductStatus getAllProducts(String email);

    ProductStatus getUserProducts(String email);

    SavedProductStatus getUserSavedProducts(String email);

    ProductsResponse getProductByProductId(Map<Long, SavedProduct> savedProductIDs, Long productId);

    ProductStatus searchProductByTitle(String email, String title);

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

    ResponseStatus productAcceptance(Long requestId, ProductAcceptanceDao productAcceptanceDao);

    ResponseStatus productCancellationByRenter(Long requestId);

    ProductRequestStatus getProductRequestsByRenter(String email);

    ProductRequestStatus getProductRequestsByRentee(String email);

    ResponseStatus reserveProductPaymentIntent(String email, Long amount) throws IOException;

    ResponseStatus getCustomerBalance(String email);
    // PAYMENT

//    ResponseStatus getCustomerBalance(String email);

//    ResponseStatus reserveProductPaymentIntent(String paymentData) throws IOException;
}
