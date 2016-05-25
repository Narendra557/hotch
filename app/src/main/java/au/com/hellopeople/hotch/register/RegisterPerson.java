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
public class RegisterPerson extends AsyncTask<String, Void, String> {

    private Context context;
    public static int lastPersonId;
    private RegisterCompleted mCallback;

    public RegisterPerson(Context context) {
        this.context = context;
        this.mCallback = (RegisterCompleted) context;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... args) {
        String personTypeId = args[0];
        String interestedIds = args[1];
        String firstName = args[2];
        String lastName = args[3];
        String email = args[4];
        String imageName = args[5];

        String link;
        String data;
        BufferedReader bufferedReader;
        String result=null;

        try {
            data = "?person_type_id=" + URLEncoder.encode(personTypeId, "UTF-8");
            data += "&interested_activity_ids=" + URLEncoder.encode(interestedIds, "UTF-8");
            data += "&firstname=" + URLEncoder.encode(firstName, "UTF-8");
            data += "&lastname=" + URLEncoder.encode(lastName, "UTF-8");
            data += "&email=" + URLEncoder.encode(email, "UTF-8");
            data += "&profilepic=" + URLEncoder.encode(imageName, "UTF-8");

            link = "http://www.myhotch.com/app_control/register_user.php" + data;
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
                boolean isNumeric = result.matches("[0-9]*");
                if (isNumeric) {
                    lastPersonId = Integer.parseInt(result);
//                    Toast.makeText(context, "Last inserted person id: " + lastPersonId, Toast.LENGTH_SHORT).show();
                    mCallback.onTaskComplete(result);
                } else {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String query_result = jsonObj.getString("query_result");

                    if (query_result.equals("FAILURE")) {
                        Toast.makeText(context, "Data could not be inserted. Registration failed.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Couldn't connect to remote database. Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Couldn't connect to remote database. Try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Couldn't connect to remote database. Try again", Toast.LENGTH_SHORT).show();
        }
    }
}