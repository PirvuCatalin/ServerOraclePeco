/**
 * Created by Bogdan on 4/27/2018.
 */
public class Member {
    private String username;
    private String password;
    private String email;
    private String licence;
    private String token;

    public void setMember(String username, String password, String email, String licence, String token) {
        this.email = email;
        this.username = username;
        this.licence = licence;
        this.password = password;
        this.token = token;
    }

    public String getLicence() {
        return licence;
    }

    public String getToken() {
        return token;
    }
}
