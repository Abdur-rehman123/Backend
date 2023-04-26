package KirayoApp.Kirayo.controller;


import KirayoApp.Kirayo.dto.ProductUploadDto;
import KirayoApp.Kirayo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Product {
    @Autowired
    ProductService productService;

    @RequestMapping(value="/product/productupload", method= RequestMethod.POST)
    ResponseEntity<?> productUpload(@RequestBody ProductUploadDto productUploadDto){

        return ResponseEntity.ok(productService.productUpload(productUploadDto));
    }

    @RequestMapping(value="/product/saveproduct", method= RequestMethod.POST)
    ResponseEntity<?> saveproduct(@RequestBody ProductUploadDto productUploadDto){

        return ResponseEntity.ok(productService.saveProduct(productUploadDto));
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
