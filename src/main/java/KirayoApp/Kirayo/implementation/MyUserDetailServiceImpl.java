package KirayoApp.Kirayo.implementation;

import KirayoApp.Kirayo.dto.MyUserDetails;
import KirayoApp.Kirayo.model.UserCredentials;
import KirayoApp.Kirayo.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailServiceImpl implements UserDetailsService {
    @Autowired
   UserCredentialsRepository userCredentialsRepository;
//    @Autowired
//    public MyUserDetailServiceImpl(UserCredentialsRepository userCredentialsRepository) {
//        this.userCredentialsRepository = userCredentialsRepository;
//    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        System.out.println("IN USER DETAIL SERVICE of its function 'loadUserByUsername'");
        System.out.println(id);

        Optional<UserCredentials> user=userCredentialsRepository.findByEmail(id);
        if(user.isEmpty()){
            user=userCredentialsRepository.findByPhoneNumber(id);
        }
        user.orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials"));
//        System.out.println("I crossed one hindrance");
        return user.map(MyUserDetails::new).get();
    }
}
