package au.com.hellopeople.hotch.register;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import au.com.hellopeople.hotch.R;
import au.com.hellopeople.hotch.login.LoginActivity;
import au.com.hellopeople.hotch.register.interests.ExpandableListDataPump;
import au.com.hellopeople.hotch.register.interests.HTTPDataHandler;
import au.com.hellopeople.hotch.register.interests.Sports;
import au.com.hellopeople.hotch.register.interests.SportsAdapterExp;
import au.com.hellopeople.hotch.util.StaticData;

public class RegisterActivity extends AppCompatActivity implements RegisterCompleted {

    String firstName, lastName, personType, email, interest, password, rePassword, emailPattern;
    EditText etFirstName, etLastName, etEmail, etPassword, etRePassword;
    Button btInterest, btRegister, btProfilePicUploader;
    ImageView ivProfilePic;
    public int personTypeId;
    public static int lastPersonId =0;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;

    private String upLoadServerUri = null;
    private String imagePath=null, imageName =null, fileName = null;
    public static ExpandableListView gvSports;
    public static Dialog dialogInterest;
    public int m, s;
    public int subWiseArr[];
    public int subIdArr[];
    public String subNameArr[];
    public String strSelectedSubId="";
    public String strSelectedSubName="";
    List<String> selectedSubId = new ArrayList<String>();
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    RelativeLayout personInfo, aboutYourself, linkYourAccounts, sellingOrSharing;

    ScrollView scrollView;
//    expandableListDetail = ExpandableListDataPump.getData();
//    expandableListTitle = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bundle extras = getIntent().getExtras();
        personTypeId = extras.getInt("person_type");

        etFirstName = (EditText)this.findViewById(R.id.first_name_text);
        etLastName = (EditText)this.findViewById(R.id.last_name_text);
        etEmail = (EditText)this.findViewById(R.id.email_text);
        btInterest = (Button)this.findViewById(R.id.interests_button);
        etPassword = (EditText)this.findViewById(R.id.password_text);
        etRePassword = (EditText)this.findViewById(R.id.retype_password_text);

        btRegister = (Button)findViewById(R.id.register_button);
        ivProfilePic = (ImageView) findViewById(R.id.profile_pic_image);
        btProfilePicUploader = (Button) findViewById(R.id.upload_profile_pic_button);
        upLoadServerUri = "http://www.myhotch.com/app_control/profile_pic_upload.php";

        personInfo = (RelativeLayout) findViewById(R.id.personal_info_layout);
        aboutYourself = (RelativeLayout) findViewById(R.id.about_yourself_layout);
        linkYourAccounts = (RelativeLayout) findViewById(R.id.link_your_accounts_layout);
        sellingOrSharing = (RelativeLayout) findViewById(R.id.selling_or_sharing_dis_layout);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        createDialogInterests();
    }

    public void nextPage(View view) {
        personInfo.setVisibility(View.GONE);
        aboutYourself.setVisibility(View.VISIBLE);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }
    public void nextPage1(View view) {
        aboutYourself.setVisibility(View.GONE);
        linkYourAccounts.setVisibility(View.VISIBLE);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }
    public void nextPage2(View view) {
        linkYourAccounts.setVisibility(View.GONE);
        sellingOrSharing.setVisibility(View.VISIBLE);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }
    public void backPage(View view) {
        personInfo.setVisibility(View.VISIBLE);
        aboutYourself.setVisibility(View.GONE);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }
    public void backPage1(View view) {
        aboutYourself.setVisibility(View.VISIBLE);
        linkYourAccounts.setVisibility(View.GONE);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }
    public void backPage2(View view) {
        linkYourAccounts.setVisibility(View.VISIBLE);
        sellingOrSharing.setVisibility(View.GONE);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }
    public void signUpButton(View view) {

    }
    @Override
    public void onTaskComplete(String result) {
//        Toast.makeText(this, "The result is " + result, Toast.LENGTH_SHORT).show();
        boolean isNumeric = result.matches("[0-9]*");
        if (isNumeric) {
            lastPersonId = Integer.parseInt(result);
            //Insert data to password table...........
            new RegisterPassword(this).execute(String.valueOf(lastPersonId), email, password);
        }
    }

    @Override
    public void onTaskComplete2(String result) {
//        Toast.makeText(this, result,Toast.LENGTH_SHORT).show();
        if (result.equals("SUCCESS")){
            Intent intent = new Intent(this,LoginActivity.class);
            this.finish();
            startActivity(intent);
        }
    }

    @Override
    public void onMainArrComplete(int result) {
        m = result;
    }

    @Override
    public void onSubArrComplete(int result) {
        s = result;
    }

    @Override
    public void onSubArrCompleteAll(String[] result) {
        subNameArr = result;
    }

    @Override
    public void onSubWiseArrComplete(int[] result) {
        subWiseArr = result;
    }

    @Override
    public void onSubIdArrComplete(int[] result) {
        subIdArr = result;
    }

    public void registerNewParticipator(View view) {
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        email = etEmail.getText().toString();
//        interest = btInterest.getText().toString();
        password = etPassword.getText().toString();
        rePassword = etRePassword.getText().toString();

        if (firstName.equals(null) || firstName.equals("")) {
            Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show();
            etFirstName.requestFocus();
        } else if (lastName.equals(null) || lastName.equals("")) {
            Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show();
            etLastName.requestFocus();
        } else if (email.equals(null) || email.equals("")) {
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
        } else if (!email.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
        }
        else if (password.equals(null) || password.equals("")) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
        }  else if (rePassword.equals(null) || rePassword.equals("")) {
            Toast.makeText(this, "Re-type password", Toast.LENGTH_SHORT).show();
            etRePassword.requestFocus();
        } else if (!password.trim().equals(rePassword.trim())) {
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            etPassword.setText(null);
            etRePassword.setText(null);
            etPassword.requestFocus();
        }
        else if (password.length() < 4 || rePassword.length() < 4) {
            Toast.makeText(this, "Password should have atleast 4 characters", Toast.LENGTH_SHORT).show();
            etPassword.setText(null);
            etRePassword.setText(null);
            etPassword.requestFocus();
        }
        else {
//            Toast.makeText(this, "Signing up...", Toast.LENGTH_SHORT).show();
            //Insert data to person table...........
            if (imagePath != null) {
                fileName = getImageName(imagePath);
                profilePicUploader();
            }else
                fileName = "default";
            new RegisterPerson(this).execute(String.valueOf(personTypeId), strSelectedSubId, firstName, lastName, email, fileName);
        }
    }

    public void profilePicUploader(){
        dialog = ProgressDialog.show(RegisterActivity.this, "", "Uploading file...", true);
        new Thread(new Runnable() {
            public void run() {
                uploadFile(imagePath);
            }
        }).start();
    }

    public int uploadFile(String sourceFileUri) {
//        fileName = getImageName(sourceFileUri); // sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            dialog.dismiss();
            Log.e("uploadFile", "Source File not exist :" + imagePath);
            runOnUiThread(new Runnable() {
                public void run() {
//                    messageText.setText("Source File not exist :"+ imagepath);
                }
            });
            return 0;
        }
        else
        {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {
//                            Toast.makeText(RegisterActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
//                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(RegisterActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "Exception : "  + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;
        } // End else block
    }

    public String getImageName(String imageName){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String fDate = df.format(c.getTime());

        final Random r = new Random();
        String rNum = Integer.toString(r.nextInt(999)+1);
        String ext = imageName.substring(imageName.length() - 4);

        imageName = "IMG_" + fDate + "_"+ rNum + ext;
        return imageName;
    }

    public void cancelRegistration(View view){
        this.finish();
    }

    public void profilePicChooser(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri filePath = data.getData();
//            try {
//                if (requestCode == PICK_IMAGE_REQUEST) {
//                    String selectedPath1 = getPath(filePath);
//                    System.out.println("selectedPath1 : " + selectedPath1);
//                    Toast.makeText(this, selectedPath1, Toast.LENGTH_LONG).show();
//                }
//                //Getting the Bitmap from Gallery
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                //Setting the Bitmap to ImageView
//                ivProfilePic.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getData().getPath();

            Uri selectedImageUri = data.getData();
            imagePath = getPath(selectedImageUri);
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            ivProfilePic.setImageBitmap(bitmap);
//            messageText.setText("Uploading file path:" +imagepath);

            String Fpath = getPath(selectedImageUri) ;
            File file = new File(Fpath);
            imageName = file.getName();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public void showInterests(View v){
        if (StaticData.arrInterests == null) {
            Toast.makeText(this, "Please try in 2 sec", Toast.LENGTH_LONG).show();
        } else {
            dialogInterest.show();
//            Toast.makeText(this, String.valueOf(m) +"   "+ String.valueOf(s), Toast.LENGTH_SHORT).show();
        }
    }

    public void createDialogInterests(){
        new SportsJSON(this).execute("http://www.myhotch.com/app_control/interests_main_all.php");
        dialogInterest = new Dialog(this);
        dialogInterest.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInterest.setContentView(R.layout.dialog_interests);

        gvSports = (ExpandableListView) dialogInterest.findViewById(R.id.gvSports);
        Button dlgBtCancel = (Button) dialogInterest.findViewById(R.id.dlgBtCancel);
        Button dlgBtOk = (Button) dialogInterest.findViewById(R.id.dlgBtOk);

        gvSports.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Toast.makeText(getApplicationContext(), expandableListTitle.get(groupPosition) + " -> " + expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();

                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);

                int pos = 0, myIndex =0;
                while (pos < groupPosition) {
                    myIndex += subWiseArr[pos];
                    pos++;
                }

//                Toast.makeText(getApplicationContext(), String.valueOf(groupPosition) +" - "+ String.valueOf(childPosition), Toast.LENGTH_SHORT).show();
                if (StaticData.arrInterests[groupPosition][childPosition] == 0) {
                    StaticData.arrInterests[groupPosition][childPosition] = 1;
//                    Toast.makeText(getApplicationContext(), String.valueOf(groupPosition)+"  -  "+ String.valueOf(groupPosition),Toast.LENGTH_SHORT).show();
                    v.setBackgroundColor(Color.YELLOW);
                    selectedSubId.add(String.valueOf(subIdArr[(myIndex + childPosition)]));
                }
                else {
                    StaticData.arrInterests[groupPosition][childPosition] = 0;
                    v.setBackgroundColor(Color.WHITE);
                    selectedSubId.remove(String.valueOf(subIdArr[(myIndex + childPosition)]));
                }
                strSelectedSubId = ""; strSelectedSubName = "";
                for (String s : selectedSubId)
                {
                    strSelectedSubId += s + ", ";
                    strSelectedSubName += subNameArr[Integer.parseInt(s) - 1] + ", ";
                }
                strSelectedSubId = strSelectedSubId.substring(0, strSelectedSubId.length()-2);
                strSelectedSubName = strSelectedSubName.substring(0, strSelectedSubName.length()-2);
//                Toast.makeText(getApplicationContext(), strSelectedSubName,Toast.LENGTH_LONG).show();
                return false;
            }
        });

        dlgBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < StaticData.arrInterests.length; i++) {
                    for (int j = 0; j < StaticData.arrInterests[0].length; j++) {
                        StaticData.arrInterests[i][j] = 0;
                    }
                }
                for (int i = 0; i < selectedSubId.size(); i++) {
                    selectedSubId.remove(i);
                }
                strSelectedSubId = "";
                strSelectedSubName = "";
                btInterest.setText("Interests");
                dialogInterest.dismiss();
            }
        });

        dlgBtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInterest.dismiss();
                btInterest.setText(strSelectedSubName);
            }
        });

    }

    public class SportsJSON extends AsyncTask<String, Void, String> {
        Context context;
        SportsAdapterExp sportsAdapter;
        //    ListView listView;
//        GridView gvSports;
        ArrayList<ClipData.Item> gridArray = new ArrayList<ClipData.Item>();
        ArrayList<String> list = new ArrayList<>();
        private RegisterCompleted mCallback;
        public SportsJSON(Context context){
            this.context = context;
            this.mCallback = (RegisterCompleted) context;
        }

        protected String doInBackground (String... strings){
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
//        MainActivity mainActivity = new MainActivity();
//        TextView tv = (TextView) ((Activity)context).findViewById(R.id.textView);
//        String data = "";

//            ExpandableListAdapter expandableListAdapter;
//            List<String> expandableListTitle;
//            HashMap<String, List<String>> expandableListDetail;

            expandableListDetail = ExpandableListDataPump.getData();
            expandableListTitle = new ArrayList<String>();


//            expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());


//            expandableListDetail = new ArrayList<String>();
//            expandableListTitle = new ArrayList<String>();

//            sportsAdapter = new SportsAdapterExp(context, R.layout.list_main_interest);
//        listView = (ListView) ((Activity) context).findViewById(R.id.listView);
//        gvSports = (GridView) ((Dialog) context).findViewById(R.id.gvSports);
//            gvSports = (GridView) ((Activity) context).findViewById(R.id.gvSports);

            //..........Process JSON DATA................
            if(stream !=null){
                Log.d("", "Inside onPost");

//            productAdapter = new ProductAdapter(context,(R.layout.gridview_item)); // (context, R.layout.gridview_item);
//            gridView = (GridView) ((Activity) context).findViewById(R.id.gridview);

                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject obj = new JSONObject(stream);
                    if(obj != null) {
                        JSONArray jsonMainArr = obj.optJSONArray("main_result");
                        JSONArray jsonSubArr = obj.optJSONArray("sub_result");
                        int subArrId[] = new int[jsonSubArr.length()];
                        String subArrName[] = new String[jsonSubArr.length()];
                        if (jsonMainArr != null) {
                            onMainArrComplete(jsonMainArr.length());
                            int mSubWiseArr[] = new int[jsonMainArr.length()];
                            int indx = 0;

                            for (int i = 0; i < jsonMainArr.length(); i++) {
                                // Get the JSONObject ...........................
                                JSONObject jsonMainObj = jsonMainArr.getJSONObject(i);
                                if (jsonMainObj != null) {
                                    String mId = jsonMainObj.getString("main_int_id").toString();
                                    String main = jsonMainObj.getString("main_interest").toString();

                                    List<String> subList = new ArrayList<String>();
//                                    JSONArray jsonSubArr = obj.optJSONArray("sub_result");
                                    if (jsonSubArr != null) {
                                        onSubArrComplete(jsonSubArr.length());

//
                                        for (int j = 0; j < jsonSubArr.length(); j++) {
                                            // Get the JSONObject ...........................
                                            JSONObject jsonSubObj = jsonSubArr.getJSONObject(j);
                                            if (jsonSubObj != null) {
                                                String mId2 = jsonSubObj.getString("main_int_id").toString();
                                                if (mId.equals(mId2)) {
                                                    String sId = jsonSubObj.getString("sub_int_id").toString();
                                                    String sub = jsonSubObj.getString("sub_interest").toString();
                                                    mSubWiseArr[i] = mSubWiseArr[i] + 1;
                                                    subList.add(sub);

                                                    subArrId[indx] = Integer.parseInt(sId);
                                                    subArrName[Integer.parseInt(sId)-1] = sub; indx++;
                                                }
                                            }
                                        }

                                    }

//                                                List<String> cricket = new ArrayList<String>();
//                                    cricket.add("India");
//                                    cricket.add("Pakistan");
//                                    cricket.add("Australia");
//                                    cricket.add("England");
//                                    cricket.add("South Africa");

                                    Sports sports = new Sports(main);
                                    expandableListTitle.add(main);
                                    expandableListDetail.put(main,subList);
                                }
                            }

                            onSubArrCompleteAll(subArrName);
                            onSubWiseArrComplete(mSubWiseArr);
                            onSubIdArrComplete(subArrId);
                        }
                    }
                    StaticData.arrInterests = new int [m][s];

                    sportsAdapter = new SportsAdapterExp(context, expandableListTitle, expandableListDetail);
                    gvSports.setAdapter(sportsAdapter);
//                tv.setText(data);

                }catch(JSONException e){
                    e.printStackTrace();
                }

            } // if statement end
        } // onPostExecute() end

    }

}
