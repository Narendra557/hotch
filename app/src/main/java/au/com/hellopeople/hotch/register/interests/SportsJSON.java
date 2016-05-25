package au.com.hellopeople.hotch.register.interests;

/**
 * Created by DELL on 3/11/2016.
 */
//public class SportsJSON extends AsyncTask <String, Void, String> {
//
//    Context context;
//    SportsAdapter sportsAdapter;
//    //    ListView listView;
//    GridView gvSports;
//    ArrayList<ClipData.Item> gridArray = new ArrayList<ClipData.Item>();
//    ArrayList<String> list = new ArrayList<>();
//
//    public SportsJSON(Context context){
//        this.context = context;
//    }
//
//    protected String doInBackground (String... strings){
//        String stream = null;
//        String urlString = strings[0];
//
//        HTTPDataHandler hh = new HTTPDataHandler();
//        stream = hh.GetHTTPData(urlString);
//
//        // Return the data from specified url
//        return stream;
//    }
//
//    protected void onPostExecute(String stream){
////        MainActivity mainActivity = new MainActivity();
////        TextView tv = (TextView) ((Activity)context).findViewById(R.id.textView);
////        String data = "";
//
//        sportsAdapter = new SportsAdapter(context, R.layout.list_main_interest);
////        listView = (ListView) ((Activity) context).findViewById(R.id.listView);
////        gvSports = (GridView) ((Dialog) context).findViewById(R.id.gvSports);
//        gvSports = (GridView) ((Activity) context).findViewById(R.id.gvSports);
//
//
//
//        //..........Process JSON DATA................
//        if(stream !=null){
//            Log.d("", "Inside onPost");
//
////            productAdapter = new ProductAdapter(context,(R.layout.gridview_item)); // (context, R.layout.gridview_item);
////            gridView = (GridView) ((Activity) context).findViewById(R.id.gridview);
//
//            try{
//                // Get the full HTTP Data as JSONObject
//                JSONObject obj = new JSONObject(stream);
//                if(obj != null) {
//                    JSONArray jsonArray = obj.optJSONArray("result");
//                    if (jsonArray != null) {
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            // Get the JSONObject ...........................
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            if (jsonObject != null) {
//
//                                String main = jsonObject.getString("main_interest").toString();
//
//                                    Sports sports = new Sports(main);
//                                    sportsAdapter.add(sports);
//                            }
//                        }
//                    }
//                }
//
//                    gvSports.setAdapter(sportsAdapter);
//
////                tv.setText(data);
//
//            }catch(JSONException e){
//                e.printStackTrace();
//            }
//
//        } // if statement end
//    } // onPostExecute() end
//
//}
