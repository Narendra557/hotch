package au.com.hellopeople.hotch.Activity;

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

import au.com.hellopeople.hotch.register.RegisterCompleted;

/**
 * Created by Dell on 5/31/2016.
 */
public class SellOrShareActivity extends AsyncTask<String, Void, String> {

    private Context context;
    public static int lastActivityId;
//    private RegisterCompleted mCallback;

    public SellOrShareActivity(Context context) {
        this.context = context;
//        this.mCallback = (RegisterCompleted) context;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... args) {
        String personId = args[0];
        String categoryId = args[1];
        String activityName = args[2];
        String keywords = args[3];
//        String duration = args[4];
//        String startDateTime = args[5];
        String pickUp = args[4];
        String eventType = args[5];
//        String interest = args[8];
//        String event = args[9];
        String amount = args[6];
//        String currencyId = args[11];
//        String address = args[12];
//        String latitude = args[13];
//        String longitude = args[14];
//        String activityImages = args[15];
//        String activityVideos = args[16];
//        String poId = args[17];
//        String ccId = args[18];
//        String pId = args[19];

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
            data = "?person_id=" + URLEncoder.encode(personId, "UTF-8");
            data += "&category_id=" + URLEncoder.encode(categoryId, "UTF-8");
            data += "&activity_name=" + URLEncoder.encode(activityName, "UTF-8");
            data += "&keywords=" + URLEncoder.encode(keywords, "UTF-8");
//            data += "&duration=" + URLEncoder.encode(duration, "UTF-8");
//            data += "&start_date_time=" + URLEncoder.encode(startDateTime, "UTF-8");
            data += "&pick_up=" + URLEncoder.encode(pickUp, "UTF-8");
            data += "&event_type=" + URLEncoder.encode(eventType, "UTF-8");
//            data += "&interest=" + URLEncoder.encode(interest, "UTF-8");
//            data += "&event=" + URLEncoder.encode(event, "UTF-8");
            data += "&amount=" + URLEncoder.encode(amount, "UTF-8");
//            data += "&currency_id=" + URLEncoder.encode(currencyId, "UTF-8");
//            data += "&address=" + URLEncoder.encode(address, "UTF-8");
//            data += "&latitude=" + URLEncoder.encode(latitude, "UTF-8");
//            data += "&longitude=" + URLEncoder.encode(longitude, "UTF-8");
//            data += "&activity_images=" + URLEncoder.encode(activityImages, "UTF-8");
//            data += "&activity_video=" + URLEncoder.encode(activityVideos, "UTF-8");
//            data += "&po_id=" + URLEncoder.encode(poId, "UTF-8");
//            data += "&cc_id=" + URLEncoder.encode(ccId, "UTF-8");
//            data += "&p_id=" + URLEncoder.encode(pId, "UTF-8");

            link = "http://www.myhotch.com/app_control/insert_activity.php" + data;
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
        Toast.makeText(context, "Data insertion completed.", Toast.LENGTH_SHORT).show();
//        String jsonStr = result;
//        if (jsonStr != null) {
//            try {
//                boolean isNumeric = result.matches("[0-9]*");
//                if (isNumeric) {
//                    lastActivityId = Integer.parseInt(result);
////                    Toast.makeText(context, "Last inserted person id: " + lastPersonId, Toast.LENGTH_SHORT).show();
////                    mCallback.onTaskComplete(result);
//                } else {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    String query_result = jsonObj.getString("query_result");
//
//                    if (query_result.equals("FAILURE")) {
//                        Toast.makeText(context, "Data could not be inserted. Registration failed.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(context, "Couldn't connect to remote database. Try again", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(context, "Couldn't connect to remote database. Try again", Toast.LENGTH_SHORT).show();
//        }
    }
}

