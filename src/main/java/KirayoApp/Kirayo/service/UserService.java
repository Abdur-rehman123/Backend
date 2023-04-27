package KirayoApp.Kirayo.service;

import KirayoApp.Kirayo.dto.UserCredentialsDto;
import KirayoApp.Kirayo.dto.UserDetailsDto;
import KirayoApp.Kirayo.returnStatus.ResponseStatus;
import KirayoApp.Kirayo.returnStatus.LoginStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    ResponseStatus verify(UserCredentialsDto userCredentialsDto);

    ResponseStatus signup(UserDetailsDto userDetailsDto, UserCredentialsDto userCredentialsDto, MultipartFile image) throws IOException;

    LoginStatus login(UserCredentialsDto userCredentialsDto) throws IOException;
    ResponseStatus forgetPassword(UserCredentialsDto userCredentialsDto);

    ResponseStatus editProfile(UserCredentialsDto userCredentialsDto,UserDetailsDto userDetailsDto);
}