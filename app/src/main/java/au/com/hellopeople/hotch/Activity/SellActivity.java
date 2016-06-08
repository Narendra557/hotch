package au.com.hellopeople.hotch.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import au.com.hellopeople.hotch.Activity.PhotoSelectionActivity;
import au.com.hellopeople.hotch.Activity.PostingOptionCompleted;
import au.com.hellopeople.hotch.Activity.PostingOptionJSON;
import au.com.hellopeople.hotch.Activity.SellOrShareActivity;
import au.com.hellopeople.hotch.ExploreActivity;
import au.com.hellopeople.hotch.FriendsActivity;
import au.com.hellopeople.hotch.ProfileActivity;
import au.com.hellopeople.hotch.R;
import au.com.hellopeople.hotch.UpcomingActivitiesActivity;
import au.com.hellopeople.hotch.choose_category.CategoryCompleted;
import au.com.hellopeople.hotch.choose_category.ChooseCategoryJSON;
import au.com.hellopeople.hotch.util.StaticData;

public class SellActivity extends AppCompatActivity implements CategoryCompleted, PostingOptionCompleted {

    Button btChooseCategory, btDurationType, btTimeSelection, btPickupOption, btEventType, btPictureActivity, btVideoActivity, btCategories, btPostingOptions, btPickMyLocation;
    EditText etActivityNameText, etKeywordText, etDuration, etPrice, etShortDesc, etAddress;

    List<String> categoryList;
    String[] allCategories, allPostingOption, allPostingOptionPrices, arrActivityPicsNames, arrActivityPicsPaths, arrActivityVideoNames, arrActivityVideoPaths;
    int[] allCategoriesIds, allPostingOptionIds;
    String strSelectedCatIds, activityName, keywords, durationType, timeSelection, eventType, price, shortDesc, activityPictures, activityVideos, postingOption, poPrice, activityAddress;
    int personId, categoryId, duration=0, pickupOption, poId=0, ccId=0, pId=0, pickedCurrent =0;
    private ProgressDialog dialog = null;
    private int serverResponseCode = 0;
    private String upLoadServerUri = null;
    int i = 0;
    Date activityStartDate;
    String imgPath1 = "NONE";
    String imgPath2 = "NONE";
    String imgPath3 = "NONE";
    String imgPath4 = "NONE";
    String imgPath5 = "NONE";
    HttpEntity resEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_activity);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        personId = pref.getInt("personIdKey", 0);

        btChooseCategory = (Button)findViewById(R.id.chooseCategoryButton);
        btDurationType = (Button)findViewById(R.id.durationTypeButton);
        btTimeSelection = (Button)findViewById(R.id.timeSelectionButton);
        btPickupOption = (Button)findViewById(R.id.pickUpOptionButton);
        btEventType = (Button)findViewById(R.id.eventTypeButton);
        btPictureActivity = (Button)findViewById(R.id.picturesButton);
        btVideoActivity = (Button)findViewById(R.id.videosButton);
        btPostingOptions = (Button)findViewById(R.id.postingOptionsButton);
        btPickMyLocation = (Button)findViewById(R.id.pickCurrentLocationButton);

        etActivityNameText = (EditText)findViewById(R.id.activityNameText);
        etKeywordText = (EditText)findViewById(R.id.keywordsText);
        etDuration = (EditText)findViewById(R.id.durationText);
        etPrice = (EditText)findViewById(R.id.priceText);
        etShortDesc = (EditText)findViewById(R.id.shortDescText);
        etAddress = (EditText)findViewById(R.id.activityAddressText);

        btCategories = (Button)findViewById(R.id.chooseCategoryButton);

        checkingPickMyLocation();
        new ChooseCategoryJSON(this).execute();
        new PostingOptionJSON(this).execute();
        upLoadServerUri = "http://www.myhotch.com/app_control/activity_photos_uploader.php";
    }

    public void myProfile(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAllCategoryListCompleted(String[] mAllCategories, int[] mAllCategoriesIds) {
        allCategories = mAllCategories;
        allCategoriesIds = mAllCategoriesIds;
    }

    @Override
    public void onAllPOCompleted(String[] mAllPostingOptions, int[] mAllPostingOptionIds, String[] mAllPostingOptionPrices) {
        allPostingOption = mAllPostingOptions;
        allPostingOptionIds = mAllPostingOptionIds;
        allPostingOptionPrices = mAllPostingOptionPrices;
    }

    public void categoryListClicked(View v){
        try {
            if (allCategories.length > 0) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Make your selection");
                builder.setItems(allCategories, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        btCategories.setText(allCategories[item]);
                        categoryId = allCategoriesIds[item];
                    }
                });

                android.app.AlertDialog alert = builder.create();
                alert.show();
            }else {
                Toast.makeText(this,"No data connection found, try again", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this,"No data connection found, try again",Toast.LENGTH_SHORT).show();
        }
    }

    public void durationClick(View view){
        final CharSequence[] items = {"Hours", "Days"};
        int selectedItem = 0;
        if (btDurationType.getText().equals("Hours"))
            selectedItem = 0;
        else
            selectedItem = 1;

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select the duration");
        builder.setSingleChoiceItems(items,selectedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        btDurationType.setText("Hours");
                        break;
                    case 1:
                        btDurationType.setText("Days");
                        break;
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

//    @Override
//    public void onAllCategoryListCompleted(String[] allCategories, int[] allCategoriesIds) {
//        this.allCategories = allCategories;
//        this.allCategoriesIds = allCategoriesIds;
//    }

//    @Override
//    public void onAllCategoryListCompleted_Ids(int[] allCategoriesIds) {
////        this.allCategoriesIds = allCategoriesIds;
//    }

    public void eventTypeClick(View v){
        final CharSequence[] items = {"Individual", "Group"};
        int selectedItem = 0;
        if (btEventType.getText().equals("Individual"))
            selectedItem = 0;
        else
            selectedItem = 1;

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select the Event Type");
        builder.setSingleChoiceItems(items,selectedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        btEventType.setText("Individual");
                        break;
                    case 1:
                        btEventType.setText("Group");
                        break;
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Select event type");
//        builder.setItems(allCategories, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//                btEventType.setText(allCategories[item]);
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
    }

    public void pickupOptionClick(View view){
        final CharSequence[] items = {"Yes", "No"};
        int selectedItem = 0;
        String x = btPickupOption.getText().toString();
        if (btPickupOption.getText().toString().equals("Yes"))
            selectedItem = 0;
        else
            selectedItem = 1;

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select the Pickup Option");
        builder.setSingleChoiceItems(items,selectedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        btPickupOption.setText("Yes");
                        break;
                    case 1:
                        btPickupOption.setText("No");
                        break;
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public void postingOptionClick(View v){
        try {
            if (allPostingOption.length > 0) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Make your selection");
                builder.setItems(allPostingOption, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        btPostingOptions.setText(allPostingOption[item]);
                        poId = allPostingOptionIds[item];
                        poPrice = allPostingOptionPrices[item];
                    }
                });

                android.app.AlertDialog alert = builder.create();
                alert.show();
            }else {
                Toast.makeText(this,"No data connection found, try again", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this,"No data connection found, try again",Toast.LENGTH_SHORT).show();
        }
    }

    public void exploreActivitiesActivity(View view) {
        Intent intent = new Intent(this, ExploreActivity.class);
        startActivity(intent);
        finish();
    }

    public void upcomingActivitiesActivity(View view) {
        Intent intent = new Intent(this, UpcomingActivitiesActivity.class);
        startActivity(intent);
        finish();
    }

    public void friendsActivity(View view) {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
        finish();
    }
    public void myProfileActivity(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    public void pickMyLocationClick(View view){
        checkingPickMyLocation();
    }

    private void checkingPickMyLocation(){
        if (btPickMyLocation.getText().toString().equals("Pick my current location")){
            btPickMyLocation.setText("Do not pick my location");
            btPickMyLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_deselect_grey, 0, 0, 0);
            etAddress.setEnabled(true);
            pickedCurrent = 0;
        } else {
            btPickMyLocation.setText("Pick my current location");
            btPickMyLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_select_grey, 0, 0, 0);
            etAddress.setEnabled(false);
            etAddress.setText("");
            pickedCurrent = 1;
        }
    }

    public void picturesButtonClick(View v){
        Intent intent = new Intent(this, PhotoSelectionActivity.class);
        startActivity(intent);
    }

    public void videoButtonClick(View v){
        Intent intent = new Intent(this, VideoSelectionActivity.class);
        startActivity(intent);
    }

    public void postingButtonClick(View v){
        Intent intent = new Intent(this, VideoSelectionActivity.class);
        startActivity(intent);
    }

    public void postActivity(View v){
        if (btCategories.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(),"Please select category",Toast.LENGTH_SHORT).show();
        else if (etActivityNameText.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(),"Please enter activity name",Toast.LENGTH_SHORT).show();
        else if (etAddress.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(),"Please enter location",Toast.LENGTH_SHORT).show();
        else if (etKeywordText.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(),"Please enter keywords",Toast.LENGTH_SHORT).show();
        else if (etDuration.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(),"Please enter duration",Toast.LENGTH_SHORT).show();
//        else if (btTimeSelection.getText().toString().equals(""))
//            Toast.makeText(getApplicationContext(),"Please enter time",Toast.LENGTH_SHORT).show();
        else if (etPrice.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(),"Please enter price",Toast.LENGTH_SHORT).show();
        else if (etShortDesc.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(),"Please enter short description",Toast.LENGTH_SHORT).show();
        else {
            activityName = etActivityNameText.getText().toString();
            activityAddress = etAddress.getText().toString();
            keywords = etKeywordText.getText().toString();
            durationType = btDurationType.getText().toString();
            duration = Integer.parseInt(etDuration.getText().toString());
//
            if (btPickupOption.getText().toString().equals("Yes")) pickupOption = 1;
            else pickupOption = 0;
            eventType = btEventType.getText().toString();
            price = etPrice.getText().toString();
            shortDesc = etShortDesc.getText().toString();

            String picNames = "", videoNames = "";
            if (StaticData.arrActivityPicsNames != null) {
                arrActivityPicsNames = getImageNames(StaticData.arrActivityPicsNames);
                arrActivityPicsPaths = StaticData.arrActivityPicsPaths;

                for (int i = 0; i < arrActivityPicsNames.length; i++) {
                    if (arrActivityPicsNames[i] != null)
                        picNames += arrActivityPicsNames[i].toString() + ", ";
                }
                picNames = picNames.substring(0, picNames.length() - 2);
                picNames = "{" + picNames + "}";
            } else picNames = "{}";

            if (StaticData.arrActivityVideoNames != null) {
                arrActivityVideoNames = getVideoNames(StaticData.arrActivityVideoNames);
                arrActivityVideoPaths = StaticData.arrActivityVideoPaths;

                for (int i = 0; i < arrActivityVideoNames.length; i++) {
                    if (arrActivityVideoNames[i] != null)
                        videoNames += arrActivityVideoNames[i].toString() + ", ";
                }
                videoNames = videoNames.substring(0, videoNames.length() - 2);
                videoNames = "{" + videoNames + "}";
            } else videoNames = "{}";

//                    arrActivityVideoNames = getVideoNames(StaticData.arrActivityVideoNames);
//                    arrActivityVideoPaths = StaticData.arrActivityVideoPaths;
                    imagesUploader();
//            videoUploader();
            new SellOrShareActivity(this).execute(String.valueOf(personId), String.valueOf(categoryId), activityName, keywords, durationType, String.valueOf(duration), String.valueOf(activityStartDate), String.valueOf(pickupOption), eventType, price, shortDesc, activityAddress, String.valueOf(pickedCurrent), "lat", "long", picNames, videoNames, String.valueOf(poId), String.valueOf(ccId), String.valueOf(pId));
        }
    }

    public String[] getImageNames(String[] imageNames){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String fDate = "";

        final Random r = new Random();
        String rNum = "";
        for(int i=0; i<imageNames.length; i++) {
            fDate = df.format(c.getTime());
            rNum = Integer.toString(r.nextInt(999)+1);
            if (imageNames[i] != null) {
                String ext = imageNames[i].substring(imageNames[i].length() - 4);
                imageNames[i] = "IMG_" + fDate + "_" + rNum + ext;
            }
        }
        return imageNames;
    }

    public String[] getVideoNames(String[] videoNames){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String fDate = df.format(c.getTime());

        final Random r = new Random();
        String rNum = Integer.toString(r.nextInt(999)+1);
        for(int i=0; i<videoNames.length; i++) {
            if (videoNames[i] != null) {
                String ext = videoNames[i].substring(videoNames[i].length() - 4);
                videoNames[i] = "VID_" + fDate + "_" + rNum + ext;
            }
        }
        return videoNames;
    }

    private void videoUploader(){
        upLoadServerUri = "http://www.myhotch.com/app_control/activity_videos_uploader.php";
        dialog = ProgressDialog.show(SellActivity.this, "", "Uploading file...", true);
        new Thread(new Runnable() {
            public void run() {
                uploadFile(upLoadServerUri, arrActivityVideoPaths[0], arrActivityVideoNames[0]);
            }
        }).start();
    }

    public void imagesUploader(){
        if(!(arrActivityPicsPaths[0].trim().equalsIgnoreCase("NONE"))){
            dialog = ProgressDialog.show(SellActivity.this, "", "Uploading file...", true);
            Thread thread=new Thread(new Runnable(){
                public void run(){
                    doImagesUpload();
                    runOnUiThread(new Runnable(){
                        public void run() {
                            if(dialog.isShowing())
                                dialog.dismiss();
                        }
                    });
                }
            });
            thread.start();
        }else{
            Toast.makeText(getApplicationContext(),"Please select two files to upload.", Toast.LENGTH_SHORT).show();
        }
    }

    public int uploadFile(String serverUri, String sourceFileUri, String sourceFileName) {
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
//            Log.e("uploadFile", "Source File not exist :" + imagePath);
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
                conn.setRequestProperty("uploaded_file", sourceFileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + sourceFileName + "\"" + lineEnd);

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
                        Toast.makeText(SellActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SellActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "Exception : "  + e.getMessage(), e);
            }
            dialog.dismiss(); i++;
            return serverResponseCode;
        } // End else block
    }













    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void doImagesUpload(){
        File file1 = new File(arrActivityPicsPaths[0]);
        File file2 = file1;
        File file3 = file1;
        File file4 = file1;
        File file5 = file1;
        String urlString = "http://www.myhotch.com/app_control/activity_photos_uploader.php";
        try
        {
            if (arrActivityPicsPaths[1] != null) file2 = new File(arrActivityPicsNames[1]);
            if (arrActivityPicsPaths[2] != null) file3 = new File(arrActivityPicsNames[2]);
            if (arrActivityPicsPaths[3] != null) file4 = new File(arrActivityPicsNames[3]);
            if (arrActivityPicsPaths[4] != null) file5 = new File(arrActivityPicsNames[4]);


            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            FileBody bin1 = new FileBody(file1), bin2 = new FileBody(file2), bin3 = new FileBody(file3), bin4 = new FileBody(file4), bin5 = new FileBody(file5);
            if (arrActivityPicsPaths[1] != null) bin2 = new FileBody(file2);
            if (arrActivityPicsPaths[2] != null) bin3 = new FileBody(file3);
            if (arrActivityPicsPaths[3] != null) bin4 = new FileBody(file4);
            if (arrActivityPicsPaths[4] != null) bin5 = new FileBody(file5);
//              FileBody bin3 = new FileBody(file3);
//            FileBody bin4 = new FileBody(file4);
//            FileBody bin5 = new FileBody(file5);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("uploadedfile1", bin1);
            if (arrActivityPicsPaths[1] != null)reqEntity.addPart("uploadedfile2", bin2);
            if (arrActivityPicsPaths[2] != null)reqEntity.addPart("uploadedfile3", bin3);
            if (arrActivityPicsPaths[3] != null)reqEntity.addPart("uploadedfile4", bin4);
            if (arrActivityPicsPaths[4] != null)reqEntity.addPart("uploadedfile5", bin5);
            reqEntity.addPart("user", new StringBody("User"));
            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();
            final String response_str = EntityUtils.toString(resEntity);
            if (resEntity != null) {
                Log.i("RESPONSE",response_str);
                runOnUiThread(new Runnable(){
                    public void run() {
                        try {
                            Toast.makeText(getApplicationContext(),"Upload Complete. Check the server uploads directory.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        catch (Exception ex){
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
    }
}
