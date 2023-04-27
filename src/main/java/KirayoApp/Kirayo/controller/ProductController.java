package KirayoApp.Kirayo.controller;


import KirayoApp.Kirayo.dto.ProductUploadDto;
import KirayoApp.Kirayo.dto.SavedProductDto;
import KirayoApp.Kirayo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;

import java.io.IOException;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

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
    @RequestMapping(value="/product/getallproducts", method= RequestMethod.GET)
    ResponseEntity<?> getAllProducts(){

        return ResponseEntity.ok(productService.getAllProducts());
    }

    @RequestMapping(value="/product/getuserproducts", method= RequestMethod.GET)
    ResponseEntity<?> getUserProducts(){

        return ResponseEntity.ok(productService.getUserProducts());
    }
    @RequestMapping(value="/product/getusersavedproducts", method= RequestMethod.GET)
    ResponseEntity<?> getUserSavedProducts(){

        return ResponseEntity.ok(productService.getUserSavedProducts());
    }


}
