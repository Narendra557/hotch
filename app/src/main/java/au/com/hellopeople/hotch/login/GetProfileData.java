package au.com.hellopeople.hotch.login;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Dell on 5/26/2016.
 */
public class GetProfileData extends AsyncTask<String,Void,String> {

        private LoginCompleted mCallBack;
        private Context context;

        public GetProfileData (Context context) {
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
                    String login_result = jsonObj.getString("login_result");

                    if (login_result.equals("SUCCESS")) {
//                        mCallBack.onLoginCompleted(login_result);
//                    Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show();
                    } else if (login_result.equals("FAILURE")) {
                        Toast.makeText(context, "Login Fail", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
            }
        }
    }
