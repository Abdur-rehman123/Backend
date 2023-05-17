package KirayoApp.Kirayo.controller;

import KirayoApp.Kirayo.dto.*;
import KirayoApp.Kirayo.model.ProductImage;
import KirayoApp.Kirayo.repository.ProductImagesRepository;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;
import KirayoApp.Kirayo.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductImagesRepository productImagesRepository;

    @RequestMapping(value = "/product/productupload", method = RequestMethod.POST)
    ResponseEntity<?> productUpload(@RequestParam("productUploadDto") String productUploadDto,
            @RequestParam("images") MultipartFile[] images) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductUploadDto productUploadDto1;

            productUploadDto1 = objectMapper.readValue(productUploadDto, ProductUploadDto.class);

        //return  ResponseEntity.ok(productUploadDto);
        return ResponseEntity.ok(productService.productUpload(productUploadDto1, images));
    }

    @RequestMapping(value = "/product/savedproduct", method = RequestMethod.POST)
    ResponseEntity<?> savedproduct(@RequestBody SavedProductDto savedProductDto) {

        return ResponseEntity.ok(productService.savedProduct(savedProductDto));
    }

    @RequestMapping(value = "/product/image", method = RequestMethod.GET)
    ResponseEntity<?> productimage(@RequestParam String id) {

        ProductImage productImage;

        productImage = productImagesRepository.findByImageId(id);

        ByteArrayResource resource = new ByteArrayResource(productImage.getImage());
        return ResponseEntity.ok().contentLength(productImage.getImage().length)
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);

    }

    @RequestMapping(value = "/product/getallproducts", method = RequestMethod.GET)
    ResponseEntity<?> getAllProducts(@RequestParam("email") String email) {

        return ResponseEntity.ok(productService.getAllProducts(email));
    }
    @RequestMapping(value = "/product/getaproductsbytitle", method = RequestMethod.GET)
    ResponseEntity<?> getProductsByTitle(@RequestParam("email") String email,@RequestParam("title") String title) {

        return ResponseEntity.ok(productService.searchProductByTitle(email,title));
    }

    @RequestMapping(value = "/product/getuserproducts", method = RequestMethod.GET)
    ResponseEntity<?> getUserProducts(@RequestParam("email") String email) {

        return ResponseEntity.ok(productService.getUserProducts(email));
    }

    @RequestMapping(value="/product/edituserproductdetails", method= RequestMethod.PATCH)
    ResponseEntity<?> editUserProducts(@RequestParam("id") Long id, @RequestBody ProductUploadDto productUploadDto) throws JsonProcessingException {


      return ResponseEntity.ok(productService.editUserProducts(id, productUploadDto));
    }
    @RequestMapping(value = "/product/image/editproductimages", method = RequestMethod.PUT)
    ResponseEntity<?> editProductImages(@RequestParam("id") Long id,@RequestParam("imageId") String imageIds,@RequestParam("images") MultipartFile[] images) throws JsonProcessingException {
        try{
            ImageIdsDao imageIdsDao = new ImageIdsDao();
            imageIdsDao.setImageIds(imageIds.split(","));

            return ResponseEntity.ok(productService.editProductImage(id, imageIdsDao, images));
        }
        catch (IOException e){
            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());
            return ResponseEntity.ok(responseStatus);
        }






    }
    @RequestMapping(value = "/product/image/deleteproductimage", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteProductImages(@RequestParam("id") String id) {

        return ResponseEntity.ok(productService.deleteProductImage(id));
    }

    @RequestMapping(value = "/product/deleteuserproducts", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteUserProducts(@RequestParam("id") Long id) {

        return ResponseEntity.ok(productService.deleteUserProducts(id));
    }

    @RequestMapping(value = "/product/savedproduct/getusersavedproducts", method = RequestMethod.GET)
    ResponseEntity<?> getUserSavedProducts(@RequestParam("email") String email) {


        return ResponseEntity.ok(productService.getUserSavedProducts(email));
    }

    @RequestMapping(value = "/product/savedproduct/deleteusersavedproducts", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteUserSavedProducts(@RequestParam("id") Long id) {

        return ResponseEntity.ok(productService.deleteUserSavedProducts(id));
    }
    @RequestMapping(value = "/product/reviews/productreviews", method = RequestMethod.POST)
    ResponseEntity<?> getProductReviews(@RequestBody ProductReviewDao productReviewDao) {

        return ResponseEntity.ok(productService.productReview(productReviewDao));
    }
    @RequestMapping(value = "/product/reviews/getproductreviews", method = RequestMethod.GET)
    ResponseEntity<?> getProductReviews(@RequestParam("productId") Long productId) {

        return ResponseEntity.ok(productService.getProductReviews(productId));
    }
    @RequestMapping(value = "/product/reviews/getproductreviewsbyuser", method = RequestMethod.GET)
    ResponseEntity<?> getProductReviewsByUser(@RequestParam("email") String  email) {

        return ResponseEntity.ok(productService.getProductReviewByUser(email));
    }
    @RequestMapping(value = "/product/reviews/editproductreviews", method = RequestMethod.PATCH)
    ResponseEntity<?> editProductReviews(@RequestParam("ReviewId") Long  reviewId, @RequestBody ProductReviewDao productReviewDao) {

        return ResponseEntity.ok(productService.editProductReview(reviewId,productReviewDao));
    }
    @RequestMapping(value = "/product/reservation/productrequest", method = RequestMethod.POST)
    ResponseEntity<?> productRequests(@RequestParam("email") String email, @RequestBody ProductRequestDao productRequestDao) {

        return ResponseEntity.ok(productService.productRequest(email,productRequestDao));
    }
    @RequestMapping(value = "/product/reservation/cancelproductrequest", method = RequestMethod.DELETE)
    ResponseEntity<?> cancelProductRequest(@RequestParam("requestId") Long requestId) {

        return ResponseEntity.ok(productService.productCancellationByRenter(requestId));
    }
    @RequestMapping(value = "/product/reservation/getproductrequestbyrenter", method = RequestMethod.GET)
    ResponseEntity<?> getProductRequestsByRenter(@RequestParam("email") String email) {

        return ResponseEntity.ok(productService.getProductRequestsByRenter(email));
    }
    @RequestMapping(value = "/product/reservation/getproductrequestbyrentee", method = RequestMethod.GET)
    ResponseEntity<?> getProductRequestsByRentee(@RequestParam("email") String email) {

        return ResponseEntity.ok(productService.getProductRequestsByRentee(email));
    }
    @RequestMapping(value = "/product/reservation/productacceptance", method = RequestMethod.POST)
    ResponseEntity<?> productRequests(@RequestParam("requestId") Long requestId,@RequestBody ProductAcceptanceDao productAcceptanceDao) {

        return ResponseEntity.ok(productService.productAcceptance(requestId,productAcceptanceDao));
    }





    // Create a REST endpoint to handle payment intent creation
    @RequestMapping(value = "/payment/create-payment-intent", method = RequestMethod.POST)
    ResponseEntity<?> reserveProductPaymentIntent(@RequestParam("email") String email,@RequestParam("amount") Long amount) throws IOException {

        return ResponseEntity.ok(productService.reserveProductPaymentIntent(email,amount));
    }

    @RequestMapping(value = "/payment/getCustomerBalance", method = RequestMethod.GET)
    ResponseEntity<?> getCustomerBalance(@RequestParam("email") String email) throws IOException {

        return ResponseEntity.ok(productService.getCustomerBalance(email));
    }


}
