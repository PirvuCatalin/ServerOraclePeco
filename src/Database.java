
/**
 * Created by Bogdan on 4/29/2018.
 */
public class Database {
    private Member[] members = new Member[20];
    private int count = 0;
    public int getCount(){
        return count;
    }
    public void setMember(String username, String password, String email, String licence, String token) {
            members[count] = new Member();
            members[count].setMember(username, password, email, licence, token);
            count++;
    }

    public String searchForLicence(String licence) {
        for (int i = 0; i < count; i++) {
            if (members[i].getLicence().equalsIgnoreCase(licence)) {
                return members[i].getToken();
            }
        }
        return "NULLTOKEN";
    }
    public boolean searchForUser(String email, String password){
        for (int i = 0; i < count; i++){
            if (members[i].getEmail().equals(email) && members[i].getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }


}
