package au.com.hellopeople.hotch.concern;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dell on 5/18/2016.
 */
public class ConcernJSON  extends AsyncTask<String,Void,String> {

    private Context context;
    private ConcernCompleted mCallBack;

    public ConcernJSON(Context context) {
        this.context = context;
        this.mCallBack = (ConcernCompleted) context;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... args) {

        String link;
        String data;
        BufferedReader bufferedReader;
        String result;

        try {
            link = "http://www.myhotch.com/app_control/get_all_concerns.php";
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
                JSONObject obj = new JSONObject(jsonStr);
                if(obj != null) {

                    JSONArray concernArr = obj.optJSONArray("concern");
                    String[] allConcerns = new String[concernArr.length()];
                    int[] allConcernsIds = new int[concernArr.length()];
                    if (concernArr != null) {
//                        onMainArrComplete(jsonMainArr.length());
                        for (int i = 0; i < concernArr.length(); i++) {
                            // Get the JSONObject ...........................
                            JSONObject concernObj = concernArr.getJSONObject(i);
                            if (concernObj != null) {
                                int concernId = concernObj.getInt("c_id");
                                String concern = concernObj.getString("name").toString();
                                allConcerns[i] = concern;
                                allConcernsIds[i] = concernId;
                            }
                        }
                        mCallBack.onConcernCompleted(allConcerns, allConcernsIds);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
        }
    }
}

