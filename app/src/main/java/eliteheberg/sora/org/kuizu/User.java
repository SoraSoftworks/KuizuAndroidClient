package eliteheberg.sora.org.kuizu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by GaSs on 08/12/2015.
 */
public class User {

    private String userid,  email;
    int elo;

    public User(String userid, int elo, String email) {
        this.userid = userid;
        this.elo = elo;
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public int getElo() {
        return elo;
    }

    public String getEmail() {
        return email;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
