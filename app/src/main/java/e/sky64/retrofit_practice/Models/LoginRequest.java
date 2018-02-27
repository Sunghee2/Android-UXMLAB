package e.sky64.retrofit_practice.Models;

/**
 * Created by sky64 on 2018-02-19.
 */

public class LoginRequest {
    int id;
    String password;

    public LoginRequest(int id, String password) {
        this.id = id;
        this.password = password;
    }
}
