package KirayoApp.Kirayo.controller;


import KirayoApp.Kirayo.dto.ProductUploadDto;
import KirayoApp.Kirayo.service.ProductUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductUploadController {
    @Autowired
    ProductUploadService productUploadService;

    @RequestMapping(value="/productupload", method= RequestMethod.POST)
    ResponseEntity productUpload(@RequestBody ProductUploadDto productUploadDto){

        return ResponseEntity.ok(productUploadService.productUpload(productUploadDto));
    }


}
