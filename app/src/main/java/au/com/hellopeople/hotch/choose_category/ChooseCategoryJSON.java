package au.com.hellopeople.hotch.choose_category;

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
 * Created by Dell on 5/16/2016.
 */
public class ChooseCategoryJSON  extends AsyncTask<String,Void,String> {

    private Context context;
    private CategoryCompleted mCallBack;

    public ChooseCategoryJSON(Context context) {
        this.context = context;
        this.mCallBack = (CategoryCompleted) context;
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

            link = "http://www.myhotch.com/app_control/get_all_categories.php"; // + data;
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

                    JSONArray categoryArr = obj.optJSONArray("categories");
                    String[] allCategories = new String[categoryArr.length()];
                    int[] allCategoriesIds = new int[categoryArr.length()];
                    if (categoryArr != null) {
//                        onMainArrComplete(jsonMainArr.length());
                        for (int i = 0; i < categoryArr.length(); i++) {
                            // Get the JSONObject ...........................
                            JSONObject categoryObj = categoryArr.getJSONObject(i);
                            if (categoryObj != null) {
                                int categoryId = categoryObj.getInt("category_id");
                                String category = categoryObj.getString("name").toString();
                                allCategories[i] = category;
                                allCategoriesIds[i] = categoryId;
                            }
                        }
                        mCallBack.onAllCategoryListCompleted(allCategories, allCategoriesIds);
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
