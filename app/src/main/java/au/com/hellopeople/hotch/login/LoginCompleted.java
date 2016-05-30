package au.com.hellopeople.hotch.login;

/**
 * Created by Dell on 5/16/2016.
 */
public interface LoginCompleted {
    void onLoginCompleted(String firstName, String lastName, String userName, String message, String profilePhoto);

}
