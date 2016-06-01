package au.com.hellopeople.hotch.Activity;

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

import au.com.hellopeople.hotch.choose_category.CategoryCompleted;

/**
 * Created by Dell on 6/1/2016.
 */
public class PostingOptionJSON extends AsyncTask<String,Void,String> {

    private Context context;
    private PostingOptionCompleted mCallBack;

    public PostingOptionJSON(Context context) {
        this.context = context;
        this.mCallBack = (PostingOptionCompleted) context;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... args) {
//        String category = args[0];

        String link;
        String data;
        BufferedReader bufferedReader;
        String result;

        try {
//            data = "?category=" + URLEncoder.encode(category, "UTF-8");

            link = "http://www.myhotch.com/app_control/get_all_posting_options.php"; // + data;
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
//                    List<String> categoryList = new ArrayList<String>();

                    JSONArray postingOptionArr = obj.optJSONArray("posting_options");
                    String[] allPostingOption = new String[postingOptionArr.length()];
                    int[] allPostingOptionIds = new int[postingOptionArr.length()];
                    String[] allPostingOptionPrices = new String[postingOptionArr.length()];
                    if (postingOptionArr != null) {
//                        onMainArrComplete(jsonMainArr.length());
                        for (int i = 0; i < postingOptionArr.length(); i++) {
                            // Get the JSONObject ...........................
                            JSONObject postingOptionObj = postingOptionArr.getJSONObject(i);
                            if (postingOptionObj != null) {
                                int poId = postingOptionObj.getInt("po_id");
                                String poName = postingOptionObj.getString("po_name").toString();
                                String poPrice = postingOptionObj.getString("po_price").toString();
                                allPostingOption[i] = poName;
                                allPostingOptionIds[i] = poId;
                                allPostingOptionPrices[i] = poPrice;
                            }
                        }
                        mCallBack.onAllPOCompleted(allPostingOption, allPostingOptionIds, allPostingOptionPrices);
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
