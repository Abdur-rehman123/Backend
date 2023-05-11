package KirayoApp.Kirayo.controller;


import KirayoApp.Kirayo.dto.ProductUploadDto;
import KirayoApp.Kirayo.dto.SavedProductDto;
import KirayoApp.Kirayo.model.ProductImage;
import KirayoApp.Kirayo.repository.ProductImagesRepository;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;
import KirayoApp.Kirayo.service.ProductService;
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

    @RequestMapping(value="/product/productupload", method= RequestMethod.POST)
    ResponseEntity<?> productUpload(@RequestParam("productUploadDto") String productUploadDto,@RequestParam("images") MultipartFile[] images) throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        ProductUploadDto productUploadDto1= null;
        try {
            productUploadDto1 = objectMapper.readValue(productUploadDto, ProductUploadDto.class);

        } catch (IOException e) {
            ResponseStatus responseStatus=new ResponseStatus();
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(productService.productUpload(productUploadDto1, images));
    }

    @RequestMapping(value="/product/savedproduct", method= RequestMethod.POST)
    ResponseEntity<?> savedproduct(@RequestBody SavedProductDto savedProductDto){

        return ResponseEntity.ok(productService.savedProduct(savedProductDto));
    }
    @RequestMapping(value="/product/image",method= RequestMethod.GET)
    ResponseEntity<?> productimage(@RequestParam String id){

        ProductImage productImage;

        productImage=productImagesRepository.findByImageId(id);

        ByteArrayResource resource = new ByteArrayResource(productImage.getImage());
        return ResponseEntity.ok().contentLength(productImage.getImage().length)
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);

    }
    @RequestMapping(value="/product/getallproducts", method= RequestMethod.GET)
    ResponseEntity<?> getAllProducts(@RequestParam("email")String email){

        return ResponseEntity.ok(productService.getAllProducts(email));
    }

    @RequestMapping(value="/product/getuserproducts", method= RequestMethod.GET)
    ResponseEntity<?> getUserProducts(@RequestParam("email") String email){

        return ResponseEntity.ok(productService.getUserProducts(email));
    }
    @RequestMapping(value="/product/edituserproducts", method= RequestMethod.PUT)
    ResponseEntity<?> editUserProducts(@RequestParam("id") Long id,@RequestParam("productUploadDto") String productUploadDto,@RequestParam("images") MultipartFile[] images){
        ObjectMapper objectMapper=new ObjectMapper();
        ProductUploadDto productUploadDto1= null;
        try {
            productUploadDto1 = objectMapper.readValue(productUploadDto, ProductUploadDto.class);

        } catch (IOException e) {
            ResponseStatus responseStatus=new ResponseStatus();
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(productService.editUserProducts(id,productUploadDto1,images));
    }
    @RequestMapping(value="/product/deleteuserproducts", method= RequestMethod.DELETE)
    ResponseEntity<?> deleteUserProducts(@RequestParam("id") Long id){

        return ResponseEntity.ok(productService.deleteUserProducts(id));
    }
    @RequestMapping(value="/product/getusersavedproducts", method= RequestMethod.GET)
    ResponseEntity<?> getUserSavedProducts(@RequestParam("email") String email){

        return ResponseEntity.ok(productService.getUserSavedProducts(email));
    }


    @RequestMapping(value="/product/deleteusersavedproducts", method= RequestMethod.DELETE)
    ResponseEntity<?> deleteUserSavedProducts(@RequestParam("id") Long id){

        return ResponseEntity.ok(productService.deleteUserSavedProducts(id));
    }





}
