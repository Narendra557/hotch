package au.com.hellopeople.hotch.language;

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
public class LanguageJSON extends AsyncTask<String,Void,String> {

    private Context context;
    private LanguageCompleted mCallBack;

    public LanguageJSON(Context context) {
        this.context = context;
        this.mCallBack = (LanguageCompleted) context;
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
            link = "http://www.myhotch.com/app_control/get_all_languages.php"; // + data;
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
                    JSONArray languageArr = obj.optJSONArray("language");
                    String[] allLanguages = new String[languageArr.length()];
                    int[] allLanguagesIds = new int[languageArr.length()];
                    if (languageArr != null) {
                        for (int i = 0; i < languageArr.length(); i++) {
                            JSONObject languageObj = languageArr.getJSONObject(i);
                            if (languageObj != null) {
                                int languageId = languageObj.getInt("language_id");
                                String language = languageObj.getString("language_name").toString();
                                allLanguages[i] = language;
                                allLanguagesIds[i] = languageId;
                            }
                        }
                        mCallBack.onLanguageListCompleted(allLanguages, allLanguagesIds);
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

