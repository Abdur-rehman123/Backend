package KirayoApp.Kirayo.implementation;

import KirayoApp.Kirayo.dto.UserCredentialsDto;
import KirayoApp.Kirayo.dto.UserDetailsDto;
import KirayoApp.Kirayo.filter.JwtUtill;
import KirayoApp.Kirayo.model.UserCredentials;
import KirayoApp.Kirayo.model.UserDetails;
import KirayoApp.Kirayo.repository.UserCredentialsRepository;
import KirayoApp.Kirayo.repository.UserDetailsRepository;
import KirayoApp.Kirayo.returnStatus.LoginStatus;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;
import KirayoApp.Kirayo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    JwtUtill jwtUtill;
//    @Autowired
//    private PasswordEncoder passwordEncoder;



    @Override
    public ResponseStatus verify(UserCredentialsDto userCredentialsDto) {
        Optional<UserCredentials> userCredentials;
        ResponseStatus responseStatus = new ResponseStatus();
        if(userCredentialsDto.getEmail()!=null){
            userCredentials=userCredentialsRepository.findByEmail(userCredentialsDto.getEmail());


        } else if (userCredentialsDto.getPhoneNumber()!=null) {
            userCredentials=userCredentialsRepository.findByPhoneNumber(userCredentialsDto.getPassword());
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
    public ResponseStatus signup(UserDetailsDto userDetailsDto, UserCredentialsDto userCredentialsDto) {
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

        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setStatus(true);
        responseStatus.setMessage("SignUp Successful");
        return responseStatus;

    }

    @Override
    public LoginStatus login(UserCredentialsDto userCredentialsDto) {
        System.out.println(userCredentialsDto.getEmail());
        UserCredentials userCredentials=new UserCredentials();
        UserDetails userDetails3=new UserDetails();
        final org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserByUsername(userCredentialsDto.getEmail());

        Optional<UserCredentials> userCredentials2=userCredentialsRepository.findByEmail(userCredentialsDto.getEmail());
        if(userCredentials2.isPresent()){
            userCredentials=userCredentials2.get();
        }
        Optional<UserDetails>userDetails2=userDetailsRepository.findById(userCredentials.getUserId());
        if(userDetails2.isPresent()){
            userDetails3=userDetails2.get();
        }
       ByteArrayResource resource = new ByteArrayResource(userDetails3.getImage());
        String userImageBase64 = Base64.getEncoder().encodeToString(resource.getByteArray());
        userDetails3.setImage(null);
        Map<String,Object> claims = new HashMap<>();
        claims.put("UserDetails",userDetails3);
        claims.put("UserImage",userImageBase64);
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
        UserCredentials userCredentials=userCredentialsRepository.findByEmail(userCredentialsDto.getEmail()).orElseThrow();
        userCredentials.setPassword(new BCryptPasswordEncoder().encode(userCredentialsDto.getPassword()));
        userCredentialsRepository.save(userCredentials);
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setStatus(true);
        responseStatus.setMessage("Password Successfully Updated");
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
