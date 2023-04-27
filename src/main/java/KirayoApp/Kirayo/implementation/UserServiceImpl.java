package KirayoApp.Kirayo.implementation;

import KirayoApp.Kirayo.beans.ImageIdGenerator;
import KirayoApp.Kirayo.dto.UserCredentialsDto;
import KirayoApp.Kirayo.dto.UserDetailsDto;
import KirayoApp.Kirayo.filter.JwtUtill;
import KirayoApp.Kirayo.model.UserCredentials;
import KirayoApp.Kirayo.model.UserDetails;
import KirayoApp.Kirayo.model.UserImage;
import KirayoApp.Kirayo.repository.UserCredentialsRepository;
import KirayoApp.Kirayo.repository.UserDetailsRepository;
import KirayoApp.Kirayo.repository.UserImageRepository;
import KirayoApp.Kirayo.returnStatus.LoginStatus;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;
import KirayoApp.Kirayo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private UserImageRepository userImageRepository;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private ImageIdGenerator imageIdGenerator;

    @Autowired
    private JwtUtill jwtUtill;
//    @Autowired
//    private PasswordEncoder passwordEncoder;



    @Override
    public ResponseStatus verify(UserCredentialsDto userCredentialsDto) {
        Optional<UserCredentials> userCredentials;
        System.out.println(userCredentialsDto.getEmail());
        System.out.println(userCredentialsDto.getPhoneNumber());
        ResponseStatus responseStatus = new ResponseStatus();
        if(!Objects.equals(userCredentialsDto.getEmail(), "null")){
            userCredentials=userCredentialsRepository.findByEmail(userCredentialsDto.getEmail());


        } else if (!Objects.equals(userCredentialsDto.getPhoneNumber(), "null")) {
            userCredentials=userCredentialsRepository.findByPhoneNumber(userCredentialsDto.getPhoneNumber());
        }
        else{
            userCredentials= Optional.empty();
        }
        if(userCredentials.isPresent()){
            responseStatus.setStatus(true);
            responseStatus.setMessage("Verification Successful");
        }
        else{
            responseStatus.setStatus(false);
            responseStatus.setMessage("Verification Unsuccessful");
        }




        return responseStatus;
    }


    @Override
    public ResponseStatus signup(UserDetailsDto userDetailsDto, UserCredentialsDto userCredentialsDto, MultipartFile image) throws IOException {
        ResponseStatus responseStatus=new ResponseStatus();
        Optional<UserCredentials> userCredentials2;
        userCredentials2=userCredentialsRepository.findByEmail(userCredentialsDto.getEmail());
        if(userCredentials2.isEmpty())
        {
            UserImage userImage = new UserImage();
            if(!image.isEmpty()){
                String imageId = imageIdGenerator.generateImageId();

                userImage.setImageId(imageId);
                userImage.setImage(image.getBytes());
                userImageRepository.save(userImage);

                userDetailsDto.setImage(userImage.getImageId());

            }
            else{
                userDetailsDto.setImage(null);
                userImage.setImage(null);
            }




            UserCredentials userCredentials=new UserCredentials();
            userCredentials.setEmail(userCredentialsDto.getEmail());
            userCredentials.setPhoneNumber(userCredentialsDto.getPhoneNumber());
            userCredentials.setPassword(new BCryptPasswordEncoder().encode(userCredentialsDto.getPassword()));
            userCredentials.setIsActive(true);
            userCredentialsRepository.save(userCredentials);

            UserDetails userDetails=new UserDetails();
            userDetails.setCity(userDetailsDto.getCity());
            userDetails.setFullname(userDetailsDto.getFullName());
            userDetails.setDob(userDetailsDto.getDob());
            userDetails.setImage(userDetailsDto.getImage());
            userDetails.setUserid(userCredentials.getUserId());
            userDetailsRepository.save(userDetails);


            responseStatus.setStatus(true);
            responseStatus.setMessage("SignUp Successful");
        }
        else{
            responseStatus.setStatus(false);
            responseStatus.setMessage("SignUp UnSuccessful");
        }
        return responseStatus;


    }

    @Override
    public LoginStatus login(UserCredentialsDto userCredentialsDto) throws IOException {



        System.out.println(userCredentialsDto.getEmail());
        UserCredentials userCredentials=null;
        UserDetails userDetails1=new UserDetails();
        if(userCredentialsDto.getEmail()!=null){
            userCredentials=userCredentialsRepository.findByEmail(userCredentialsDto.getEmail()).orElseThrow(() -> new NoSuchElementException("User not found"));
        }else {
            userCredentials=userCredentialsRepository.findByPhoneNumber(userCredentialsDto.getPhoneNumber()).orElseThrow(() -> new NoSuchElementException("User not found"));
        }
        System.out.println("before sending data" +userCredentials.getEmail());
        final org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserByUsername(userCredentials.getEmail());

        userDetails1=userDetailsRepository.findById(userCredentials.getUserId()).orElseThrow(() -> new NoSuchElementException("User not found"));

        Map<String,Object> claims = new HashMap<>();
        claims.put("UserDetails",userDetails1);

        final String jwt =jwtUtill.generateTokenforlogin(userDetails,claims);
        LoginStatus loginStatus=new LoginStatus();
        loginStatus.setJwt(jwt);

        return loginStatus;

 }
//    public String hashPassword(String password) {
//
//        return BCrypt.hashpw(password, BCrypt.gensalt());
//    }

    @Override
    public ResponseStatus forgetPassword(UserCredentialsDto userCredentialsDto) {
        ResponseStatus responseStatus = new ResponseStatus();
        try{
            UserCredentials userCredentials=userCredentialsRepository.findByEmail(userCredentialsDto.getEmail()).orElseThrow(() -> new NoSuchElementException("User not found"));
            userCredentials.setPassword(new BCryptPasswordEncoder().encode(userCredentialsDto.getPassword()));
            userCredentialsRepository.save(userCredentials);

            responseStatus.setStatus(true);
            responseStatus.setMessage("Password Successfully Updated");

        }

         catch (NoSuchElementException e){
            responseStatus.setStatus(false);
            responseStatus.setMessage(e.getMessage());
        }
        return responseStatus;
    }

    @Override
    public ResponseStatus editProfile(UserCredentialsDto userCredentialsDto,
                                      UserDetailsDto userDetailsDto) {
        UserCredentials userCredentials=userCredentialsRepository.findByEmail(userCredentialsDto.getEmail()).orElseThrow();
        UserDetails userDetails=userDetailsRepository.findById(userCredentials.getUserId()).orElseThrow();
        if(userCredentialsDto.getPhoneNumber()!=null){
            userCredentials.setPhoneNumber(userCredentialsDto.getPhoneNumber());
        }
        if(userCredentialsDto.getPassword()!=null){
            userCredentials.setPassword(new BCryptPasswordEncoder().encode(userCredentialsDto.getPassword()));
        }
        if(userDetailsDto.getCity()!=null){
            userDetails.setCity(userDetailsDto.getCity());
        }
        if(userDetailsDto.getImage()!=null){
            userDetails.setImage(userDetailsDto.getImage());
        }
        if(userDetailsDto.getDob()!=null){
            userDetails.setDob(userDetailsDto.getDob());
        }
        if(userDetailsDto.getFullName()!=null){
            userDetails.setFullname(userDetailsDto.getFullName());
        }
        userCredentialsRepository.save(userCredentials);
        userDetailsRepository.save(userDetails);
        ResponseStatus responseStatus= new ResponseStatus();
        responseStatus.setStatus(true);
        responseStatus.setMessage("Profile Updated Successfully");
        return responseStatus;
    }

}