package au.com.hellopeople.hotch.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import au.com.hellopeople.hotch.ProfileActivity;
import au.com.hellopeople.hotch.register.RegisterCompleted;

/**
 * Created by Dell on 4/4/2016.
 */
public class LoginValidation extends AsyncTask<String,Void,String> {

    private LoginCompleted mCallBack;
    private Context context;

    public LoginValidation(Context context) {
        this.context = context;
        this.mCallBack = (LoginCompleted) context;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... args) {
        String email = args[0];
        String password = args[1];

        String link;
        String data;
        BufferedReader bufferedReader;
        String result;

        try {
            data = "?email=" + URLEncoder.encode(email, "UTF-8");
            data += "&password=" + URLEncoder.encode(password, "UTF-8");

            link = "http://www.myhotch.com/app_control/login_validation.php" + data;
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            result = bufferedReader.readLine();
            return result;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result){
        String jsonStr = result;
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                JSONArray profileArr = jsonObj.optJSONArray("profile");
                String[] profile = new String[profileArr.length()];

//                int[] allConcernsIds = new int[concernArr.length()];
                if (profileArr.length() > 0) {
//                        onMainArrComplete(jsonMainArr.length());
//                    for (int i = 0; i < profileArr.length(); i++) {
                    // Get the JSONObject ...........................
                    JSONObject profileObj = profileArr.getJSONObject(0);
                    if (profileObj != null) {
                        int personId = profileObj.getInt("person_id");
                        String firstName = profileObj.getString("first_name").toString();
                        String lastName = profileObj.getString("last_name").toString();
                        String userName = profileObj.getString("username").toString();
                        String message = profileObj.getString("message").toString();
                        String profilePhoto = profileObj.getString("profile_photo").toString();

//                            profile[i] = firstName;
//                            allConcernsIds[i] = concernId;
                        mCallBack.onLoginCompleted(personId, firstName, lastName, userName, message, profilePhoto);
                    }
//                    }
//                    mCallBack.onConcernCompleted(allConcerns, allConcernsIds);
                } else {
                    Toast.makeText(context, "Login Fail", Toast.LENGTH_SHORT).show();
                }




//                String login_result = jsonObj.getString("first_name");
//
//                if (login_result.equals("SUCCESS")) {
//                    mCallBack.onLoginCompleted(login_result);
////                    Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show();
//                } else if (login_result.equals("FAILURE")) {
//                    Toast.makeText(context, "Login Fail", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
//                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
        }
    }
}
