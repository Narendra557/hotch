package au.com.hellopeople.hotch.register.interests;

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
public class ChooseSubInterestsJSON extends AsyncTask<String,Void,String> {

    private Context context;
    private SubInterestCompleted mCallBack;

    public ChooseSubInterestsJSON(Context context) {
        this.context = context;
        this.mCallBack = (SubInterestCompleted) context;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... args) {
//        String mainId = args[0];

        String link;
        String data;
        BufferedReader bufferedReader;
        String result;

        try {
//            data = "?main_id=" + URLEncoder.encode(mainId, "UTF-8");

            link = "http://www.myhotch.com/app_control/get_all_subinterests.php"; // + data;
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

                    JSONArray subInterestsArr = obj.optJSONArray("subInterests");
                    String[] allSubInterests = new String[subInterestsArr.length()];
                    int[] allSubInterestsIds = new int[subInterestsArr.length()];
                    String sporting="", gym="", arts="", relaxation="", children="";
                    String sportingId="", gymId="", artsId="", relaxationId="", childrenId="";
                    if (subInterestsArr != null) {
//                        onMainArrComplete(jsonMainArr.length());
                        for (int i = 0; i < subInterestsArr.length(); i++) {
                            // Get the JSONObject ...........................
                            JSONObject subInterestObj = subInterestsArr.getJSONObject(i);
                            if (subInterestObj != null) {
                                int subId = subInterestObj.getInt("sub_int_id");
                                String mainId = subInterestObj.getString("main_int_id").toString();
                                String subInterest = subInterestObj.getString("sub_interest").toString();
                                switch (mainId){
                                    case "1": sporting += subInterest + ","; sportingId += subId + ","; break;
                                    case "2": gym += subInterest + ","; gymId += subId + ","; break;
                                    case "3": arts += subInterest + ","; artsId += subId + ","; break;
                                    case "4": relaxation += subInterest + ","; relaxationId += subId + ","; break;
                                    default: children += subInterest + ","; childrenId += subId + ","; break;
                                }
                                allSubInterests[i] = subInterest;
                                allSubInterestsIds[i] = subId;
                            }
                        }
                        String[] sportingArr = sporting.split(",");
                        String[] sportingArrIds = sportingId.split(",");
                        mCallBack.sportingCompleted(sportingArr, sportingArrIds);

                        String[] gymArr = gym.split(",");
                        String[] gymArrIds = gymId.split(",");
                        mCallBack.gymCompleted(gymArr, gymArrIds);

                        String[] artsArr = arts.split(",");
                        String[] artsArrIds = artsId.split(",");
                        mCallBack.artsCompleted(artsArr, artsArrIds);

                        String[] relaxationArr = relaxation.split(",");
                        String[] relaxationArrIds = relaxationId.split(",");
                        mCallBack.relaxationCompleted(relaxationArr, relaxationArrIds);

                        String[] childrenArr = children.split(",");
                        String[] childrenArrIds = childrenId.split(",");
                        mCallBack.childrenCompleted(childrenArr, childrenArrIds);
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
