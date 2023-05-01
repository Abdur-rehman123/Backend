package KirayoApp.Kirayo.returnStatus;

import java.util.Date;

public class LoginStatus {
    private String jwt;

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    private Date  dob;
    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }



}
