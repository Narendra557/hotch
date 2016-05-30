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
        String firstName = args[0];
        String lastName = args[1];
        String userName = args[2];
        String password = args[3];
        String message = args[4];
        String dob = args[5];
        String email = args[6];
        String gender = args[7];
        String imageName = args[8];
        String catIds = args[9];
        String sportingIds = args[10];
        String gymIds = args[11];
        String artsIds = args[12];
        String relaxationIds = args[13];
        String childrenIds = args[14];
        String concernsIds = args[15];
        String languagesIds = args[16];



//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date convertedDate = new Date();
//            try {
//                convertedDate = dateFormat.parse(dob);
//            } catch (ParseException e) {
//        }

        String link;
        String data;
        BufferedReader bufferedReader;
        String result=null;

        try {
            data = "?first_name=" + URLEncoder.encode(firstName, "UTF-8");
            data += "&last_name=" + URLEncoder.encode(lastName, "UTF-8");
            data += "&user_name=" + URLEncoder.encode(userName, "UTF-8");
            data += "&password=" + URLEncoder.encode(password, "UTF-8");
            data += "&message=" + URLEncoder.encode(message, "UTF-8");
            data += "&dob=" + URLEncoder.encode(dob, "UTF-8");
            data += "&email=" + URLEncoder.encode(email, "UTF-8");
            data += "&gender=" + URLEncoder.encode(gender, "UTF-8");
            data += "&profile_photo=" + URLEncoder.encode(imageName, "UTF-8");
            data += "&category_ids=" + URLEncoder.encode(catIds, "UTF-8");
            data += "&sprting_ids=" + URLEncoder.encode(sportingIds, "UTF-8");
            data += "&gym_activities_ids=" + URLEncoder.encode(gymIds, "UTF-8");
            data += "&arts_culture_ids=" + URLEncoder.encode(artsIds, "UTF-8");
            data += "&relaxation_ids=" + URLEncoder.encode(relaxationIds, "UTF-8");
            data += "&children_ids=" + URLEncoder.encode(childrenIds, "UTF-8");
            data += "&concerns_ids=" + URLEncoder.encode(concernsIds, "UTF-8");
            data += "&languages_ids=" + URLEncoder.encode(languagesIds, "UTF-8");


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