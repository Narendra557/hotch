package au.com.hellopeople.hotch.register;

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
 * Created by Dell on 4/4/2016.
 */
public class RegisterPassword extends AsyncTask<String, Void, String> {

    private Context context;
    private RegisterCompleted mCallback;

    public RegisterPassword(Context context) {
        this.context = context;
        this.mCallback = (RegisterCompleted) context;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... args) {
        String personId = args[0];
        String userName = args[1];
        String password = args[2];

        String link;
        String data;
        BufferedReader bufferedReader;
        String result;

        try {
            data = "?personid=" + URLEncoder.encode(personId, "UTF-8");
            data += "&username=" + URLEncoder.encode(userName, "UTF-8");
            data += "&password=" + URLEncoder.encode(password, "UTF-8");

            link = "http://www.myhotch.com/app_control/register_password.php" + data;
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
    protected void onPostExecute(String result) {
        String jsonStr = result;
        if (jsonStr != null) {
            try {
//                boolean isNumeric = result.matches("[0-9]*");
//                if (isNumeric) {
//                    RegisterActivity.lastPersonId = Integer.parseInt(result);
//                    Toast.makeText(context, "Last inserted person id: " + RegisterActivity.lastPersonId, Toast.LENGTH_SHORT).show();
//                } else {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String query_result = jsonObj.getString("query_result");

                    if (query_result.equals("SUCCESS")) {
                        Toast.makeText(context, "Registration success", Toast.LENGTH_SHORT).show();
                        mCallback.onTaskComplete2(query_result);
                    }else if (query_result.equals("FAILURE")) {
                        Toast.makeText(context, "Data could not be inserted. Registration failed.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                    }
//                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
        }
    }
}