package KirayoApp.Kirayo.controller;

import KirayoApp.Kirayo.dto.SignupDto;
import KirayoApp.Kirayo.dto.UserCredentialsDto;
import KirayoApp.Kirayo.model.UserImage;
import KirayoApp.Kirayo.repository.UserDetailsRepository;
import KirayoApp.Kirayo.repository.UserImageRepository;
import KirayoApp.Kirayo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserDetailsRepository userDetailsRepository;
    @Autowired
    UserImageRepository userImageRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserDetailsService userDetailsService;



    @RequestMapping(value="/verification", method= RequestMethod.GET)
    ResponseEntity<?> verification(@RequestBody UserCredentialsDto userCredentialsDto){

        return ResponseEntity.ok(userService.verify(userCredentialsDto));

    }
    @RequestMapping(value="/image",method= RequestMethod.GET)
    ResponseEntity<?> register(@RequestParam String id){
        System.out.println(id);
        UserImage userImage;
//        System.out.println(id);
        userImage=userImageRepository.findByImageId(id);

        ByteArrayResource resource = new ByteArrayResource(userImage.getImage());
        return ResponseEntity.ok().contentLength(userImage.getImage().length)
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);

    }
    private String generateImageId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    //SIGNUP consumes = "multipart/form-data"
    @RequestMapping(value="/signup",method= RequestMethod.POST)
    ResponseEntity<?> saveUser(@RequestParam("signupDto") String signupDto, @RequestParam("image")MultipartFile image) throws IOException {

        ObjectMapper objectMapper=new ObjectMapper();
        String imageId = generateImageId();
        UserImage userImage = new UserImage();
        userImage.setImageId(imageId);
        userImageRepository.save(userImage);


//        try {
//            signupDto.getUserDetailsDto().setImage(signupDto.getImage().getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        SignupDto signupDto1= null;
        try {
            signupDto1 = objectMapper.readValue(signupDto, SignupDto.class);
            if(!image.isEmpty()){
                signupDto1.getUserDetailsDto().setImage(userImage.getImageId());
                userImage.setImage(image.getBytes());
            }
            else{
                userImage.setImage(null);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(userService.signup(signupDto1.getUserDetailsDto(), signupDto1.getUserCredentialsDto()));

    }

    @RequestMapping(value="/login",method= RequestMethod.POST)
    ResponseEntity<?> loginUser(@RequestBody UserCredentialsDto userCredentialsDto) throws Exception     {
        System.out.println(userCredentialsDto.getEmail());
        try {

            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCredentialsDto.getEmail(), userCredentialsDto.getPassword())));


        }
        catch(BadCredentialsException e){

            throw new Exception("Invalid Credentials", e);
        }


        return ResponseEntity.ok(userService.login(userCredentialsDto));
    }

    @RequestMapping(value="/forgetpassword",method= RequestMethod.POST)
    ResponseEntity<?> forgetPassword(@RequestBody UserCredentialsDto userCredentialsDto){

        return ResponseEntity.ok(userService.forgetPassword(userCredentialsDto));
    }

    @RequestMapping(value="/editprofile",method= RequestMethod.POST)
    ResponseEntity<?> editProfile(@RequestBody SignupDto signupDto){
        return ResponseEntity.ok(userService.editProfile(signupDto.getUserCredentialsDto(),signupDto.getUserDetailsDto()));
    }


}
