package KirayoApp.Kirayo.dto;


import KirayoApp.Kirayo.model.UserCredentials;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {
    private  String userName;
    private  String password;
    private boolean isActive;
    private List<GrantedAuthority> authorities;


    public MyUserDetails(UserCredentials user){
        System.out.println("In MyUserDetails DAO class");
        this.userName=user.getEmail();
        this.password=user.getPassword();
        this.isActive=user.getIsActive();
        this.authorities= new ArrayList<>();
        System.out.println(this.userName+this.password+this.isActive);

    }
    MyUserDetails(){

    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}

