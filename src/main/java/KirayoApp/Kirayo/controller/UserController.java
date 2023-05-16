package KirayoApp.Kirayo.controller;

import KirayoApp.Kirayo.dto.EditProfileDao;
import KirayoApp.Kirayo.dto.SignupDto;
import KirayoApp.Kirayo.dto.UserCredentialsDto;
import KirayoApp.Kirayo.model.UserImage;
import KirayoApp.Kirayo.repository.UserDetailsRepository;
import KirayoApp.Kirayo.repository.UserImageRepository;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;
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



    @RequestMapping(value="/user/verification", method= RequestMethod.GET)
    ResponseEntity<?> verification(@RequestParam String email, @RequestParam
                                    String phonenumber){
        UserCredentialsDto userCredentialsDto=new UserCredentialsDto();
        userCredentialsDto.setEmail(email);
        userCredentialsDto.setPhoneNumber(phonenumber);
        return ResponseEntity.ok(userService.verify(userCredentialsDto));

    }
    @RequestMapping(value="/user/image",method= RequestMethod.GET)
    ResponseEntity<?> userimage(@RequestParam String id){
        System.out.println(id);
        UserImage userImage;
//        System.out.println(id);
        userImage=userImageRepository.findByImageId(id);

        ByteArrayResource resource = new ByteArrayResource(userImage.getImage());
        return ResponseEntity.ok().contentLength(userImage.getImage().length)
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);

    }


    //SIGNUP consumes = "multipart/form-data"
    @RequestMapping(value="/user/signup",method= RequestMethod.POST)
    ResponseEntity<?> saveUser(@RequestParam("signupDto") String signupDto, @RequestParam("image") MultipartFile image) throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        SignupDto signupDto1= null;
        try {
            signupDto1 = objectMapper.readValue(signupDto, SignupDto.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(userService.signup(signupDto1.getUserDetailsDto(), signupDto1.getUserCredentialsDto(), image));

    }

    @RequestMapping(value="/user/login",method= RequestMethod.POST)
    ResponseEntity<?> loginUser(@RequestBody UserCredentialsDto userCredentialsDto) throws Exception     {
        String id;
        if(userCredentialsDto.getEmail()!=null){
            id=userCredentialsDto.getEmail();
        }
        else if (userCredentialsDto.getPhoneNumber()!=null){
            id=userCredentialsDto.getPhoneNumber();
        }
        else{
            id=null;
        }
        try {

            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(id, userCredentialsDto.getPassword())));


        }
        catch(BadCredentialsException e){

            ResponseStatus responseStatus = new ResponseStatus();
            responseStatus.setStatus(false);
            responseStatus.setMessage("InvalidCredentials");
            return ResponseEntity.ok(responseStatus);
        }


        return ResponseEntity.ok(userService.login(userCredentialsDto));
    }

    @RequestMapping(value="/user/forgetpassword",method= RequestMethod.POST)
    ResponseEntity<?> forgetPassword(@RequestBody UserCredentialsDto userCredentialsDto){

        return ResponseEntity.ok(userService.forgetPassword(userCredentialsDto));
    }

    @RequestMapping(value="/user/editprofile",method= RequestMethod.PATCH)
    ResponseEntity<?> editProfile(@RequestParam("email") String email,@RequestBody EditProfileDao editProfileDao){
        return ResponseEntity.ok(userService.editProfile(email,editProfileDao));
    }

    @RequestMapping(value="/user/getuserdetails",method= RequestMethod.GET)
    ResponseEntity<?> getUserDetails(@RequestParam("email") String email){
        return ResponseEntity.ok(userService.getUserDetails(email));

    }


}
