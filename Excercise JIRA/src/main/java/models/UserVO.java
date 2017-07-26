package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.bson.Document;

/**
 * Created by tmtai on 7/11/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVO{
    private String id;
    private String username;
    private String password;

    public UserVO(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserVO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmpty(){
        boolean check = true;
        if (this.username != null && this.username != "" && this.password != null && this.password != ""){
            return false;
        }
        return check;
    }
}
