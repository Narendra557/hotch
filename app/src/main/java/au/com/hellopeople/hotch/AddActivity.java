package au.com.hellopeople.hotch;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import au.com.hellopeople.hotch.entity.AgeGroup;
import au.com.hellopeople.hotch.entity.Country;
import au.com.hellopeople.hotch.entity.Currency;
import au.com.hellopeople.hotch.entity.Language;
import au.com.hellopeople.hotch.register.RegisterCompleted;
import au.com.hellopeople.hotch.register.interests.ExpandableListDataPump;
import au.com.hellopeople.hotch.register.interests.HTTPDataHandler;
import au.com.hellopeople.hotch.register.interests.Sports;
import au.com.hellopeople.hotch.register.interests.SportsAdapterExp;
import au.com.hellopeople.hotch.register_offer_services.DatePickerFragment;
import au.com.hellopeople.hotch.util.HttpConnectionManager;
import au.com.hellopeople.hotch.util.StaticData;

public class AddActivity extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    RelativeLayout activityInfoLayout, otherInfoLayout, contactInfoLayout, activityImagesLayout, activityInformationContent,
            otherInformationContent, contactInformationContent, activityImagesContent;

    boolean activityInfo = false, contactInfo = false, otherInfo = false, activityImages = false;

    String activityName = "", activityDescription = "", activityStartDate = "Activity Start Date", activityEndDate = "Activity End Date", activityStartTime = "Activity Start Time",
            activityEndTime = "Activity End Time", activityType = "Activity Type",
            activityInterests = "Interests", isActivityEvent = "", languageConducted = "Languages", suitableAgeGroup = "Suitable Age Groups", amountPerParticipant = "", activityCurrency = "Currency",
            activityStreetAddress = "", activityApartment = "", activitySuburb = "", activityState = "", activityCountry = "", activityLatitude = "", activityLongitude = "", activityStatus = "";

    EditText activityET, activityDescET, amountET, streetAddressET, apartmentET, suburbET, stateET;
    ImageView ivProfilePic;
    Button activityStartDateB, activityEndDateB, activityStartTimeB, activityEndTimeB, activityTypeB, interestsB, isEventB,
            languagesB, currencyB, ageGroupB, countryB, pickLocationB, activeB;

    public int personTypeId;
    public static int lastPersonId = 0;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;

    private String upLoadServerUri = null;
    private String imagePath = null, imageName = null, fileName = null;
    public static ExpandableListView gvSports;
    public static Dialog dialogInterest;
    public int m, s;
    public int subWiseArr[];
    public int subIdArr[];
    public String subNameArr[];
    public String strSelectedSubId = "";
    public String strSelectedSubName = "";
    List<String> selectedSubId = new ArrayList<String>();
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    private boolean startDate, endDate, startTime, endTime, isEvent, myLocation, active;
    private int hour;
    private int minute;
    static final int TIME_DIALOG_ID = 999;

    private ArrayList<Currency> currenciesArray;
    private ArrayList<AgeGroup> ageGroupsArray;
    private ArrayList<Language> languagesArray;
    private ArrayList<Country> countriesArray;

    private ArrayList<String> currenciesArr;
    private ArrayList<String> ageGroupsArr;
    private ArrayList<String> languagesArr;
    private ArrayList<String> countriesArr;



    public GoogleMap map;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    String provider;

    Spinner country;

    AutoCompleteTextView local_county;
    double lat, lng;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        activityInfoLayout = (RelativeLayout) findViewById(R.id.activity_info_layout);
        contactInfoLayout = (RelativeLayout) findViewById(R.id.contact_info_layout);
        otherInfoLayout = (RelativeLayout) findViewById(R.id.other_info_layout);
        activityImagesLayout = (RelativeLayout) findViewById(R.id.activity_images_layout);
        activityInformationContent = (RelativeLayout) findViewById(R.id.activity_information_content);
        contactInformationContent = (RelativeLayout) findViewById(R.id.contact_information_content);
        otherInformationContent = (RelativeLayout) findViewById(R.id.other_information_content);
        activityImagesContent = (RelativeLayout) findViewById(R.id.activity_images_content);

        int[] onclickButtonIds = {R.id.top_button_1, R.id.top_button_2, R.id.top_button_3, R.id.top_button_4, R.id.cancel_button, R.id.save_button};
        for (int i = 0; i < onclickButtonIds.length; i++) {
            Button button = (Button) findViewById(onclickButtonIds[i]);
            button.setOnClickListener(this);
        }

        activityET = (EditText) findViewById(R.id.activity_name_text);
        activityDescET = (EditText) findViewById(R.id.activity_description_text);
        amountET = (EditText) findViewById(R.id.amount_text);
        streetAddressET = (EditText) findViewById(R.id.street_address_text);
        apartmentET = (EditText) findViewById(R.id.apartment_text);
        suburbET = (EditText) findViewById(R.id.suburb_text);
        stateET = (EditText) findViewById(R.id.state_text);

        ivProfilePic = (ImageView) findViewById(R.id.activity_pic_image);
        upLoadServerUri = "http://www.myhotch.com/app_control/profile_pic_upload.php";

        activityStartDateB = (Button) findViewById(R.id.activity_start_date_button);
        activityEndDateB = (Button) findViewById(R.id.activity_end_date_button);
        activityStartTimeB = (Button) findViewById(R.id.activity_start_time_button);
        activityEndTimeB = (Button) findViewById(R.id.activity_end_time_button);
        activityTypeB = (Button) findViewById(R.id.activity_type_button);
        interestsB = (Button) findViewById(R.id.interests_button);
        isEventB = (Button) findViewById(R.id.is_event_button);
        languagesB = (Button) findViewById(R.id.languages_button);
        currencyB = (Button) findViewById(R.id.currency_button);
        ageGroupB = (Button) findViewById(R.id.age_group_button);
        countryB = (Button) findViewById(R.id.country_button);
        pickLocationB = (Button) findViewById(R.id.pick_my_location_button);
        activeB = (Button) findViewById(R.id.active_button);

        createDialogInterests();
        isEvent = true;
        isActivityEvent = "Yes";
        myLocation = true;
        active = true;
        activityStatus = "Active";

        if (checkInternetConnection()) {
            ActivityDataLoadingAsyncTask activityDataLoadingAsyncTask = new ActivityDataLoadingAsyncTask();
            activityDataLoadingAsyncTask.execute((Void) null);
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private boolean checkInternetConnection() {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null

                && conMgr.getActiveNetworkInfo().isAvailable()

                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;

        } else {
            return false;
        }
    }

    public void setCurrentTimeOnView() {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public void activityButton1(View view) {

    }

    public void activityButton2(View view) {

        activityName = activityET.getText().toString().trim();
        activityDescription = activityDescET.getText().toString().trim();

        if (activityName.equalsIgnoreCase("")) {
            activityET.setError("Please enter the activity name.");
            activityET.setFocusable(true);
            if (activityDescription.equalsIgnoreCase("")) {
                activityDescET.setError("Please enter the activity description.");
            }
        } else if (activityDescription.equalsIgnoreCase("")) {
            if (activityDescription.equalsIgnoreCase("")) {
                activityDescET.setError("Please enter the activity description.");
                activityDescET.setFocusable(true);
            }
        } else if (activityStartDate.equalsIgnoreCase("Activity Start Date")) {
            Toast.makeText(this, "Please select the activity start date.", Toast.LENGTH_LONG).show();
        } else if (activityEndDate.equalsIgnoreCase("Activity End Date")) {
            Toast.makeText(this, "Please select the activity end date.", Toast.LENGTH_LONG).show();
        } else if (activityStartTime.equalsIgnoreCase("Activity Start Time")) {
            Toast.makeText(this, "Please select the activity start time.", Toast.LENGTH_LONG).show();
        } else if (activityEndTime.equalsIgnoreCase("Activity End Time")) {
            Toast.makeText(this, "Please select the activity end time.", Toast.LENGTH_LONG).show();
        } else {
            if (imagePath != null) {
                fileName = getImageName(imagePath);
                profilePicUploader();
            } else {
                fileName = "default";
            }

            activityInfoLayout.setVisibility(View.GONE);
            activityInformationContent.setVisibility(View.GONE);
            contactInfoLayout.setVisibility(View.VISIBLE);
            contactInformationContent.setVisibility(View.VISIBLE);
            otherInfoLayout.setVisibility(View.GONE);
            otherInformationContent.setVisibility(View.GONE);
            activityImagesLayout.setVisibility(View.GONE);
            activityImagesContent.setVisibility(View.GONE);

            Button button = (Button) findViewById(R.id.top_button_2);
            button.setBackgroundResource(R.drawable.dark_blue_circle);
        }

    }

    public void activityButton3(View view) {

        amountPerParticipant = amountET.getText().toString().trim();

        if (activityType.equalsIgnoreCase("Activity Type")) {
            Toast.makeText(this, "Please select the activity type.", Toast.LENGTH_LONG).show();
        } else if (activityInterests.equalsIgnoreCase("Interests")) {
            Toast.makeText(this, "Please select the interests.", Toast.LENGTH_LONG).show();
        } else if (languageConducted.equalsIgnoreCase("Languages")) {
            Toast.makeText(this, "Please select the languages.", Toast.LENGTH_LONG).show();
        } else if (activityCurrency.equalsIgnoreCase("Currency")) {
            Toast.makeText(this, "Please select the currency.", Toast.LENGTH_LONG).show();
        } else if (amountPerParticipant.equalsIgnoreCase("")) {
//            Toast.makeText(this, "Please enter the amount.", Toast.LENGTH_LONG).show();
            amountET.setError("Please enter the amount.");
            amountET.setFocusable(true);
        } else if (suitableAgeGroup.equalsIgnoreCase("Suitable Age Groups")) {
            Toast.makeText(this, "Please select the age groups.", Toast.LENGTH_LONG).show();
        } else {
            activityInfoLayout.setVisibility(View.GONE);
            activityInformationContent.setVisibility(View.GONE);
            contactInfoLayout.setVisibility(View.GONE);
            contactInformationContent.setVisibility(View.GONE);
            otherInfoLayout.setVisibility(View.VISIBLE);
            otherInformationContent.setVisibility(View.VISIBLE);
            activityImagesLayout.setVisibility(View.GONE);
            activityImagesContent.setVisibility(View.GONE);

            Button button = (Button) findViewById(R.id.top_button_3);
            button.setBackgroundResource(R.drawable.dark_blue_circle);
        }
    }

    public void activityButton4(View view) {

        activityStreetAddress = streetAddressET.getText().toString().trim();
        activityApartment = apartmentET.getText().toString().trim();
        activitySuburb = suburbET.getText().toString().trim();
        activityState = stateET.getText().toString().trim();

        if (activityStreetAddress.equalsIgnoreCase("")) {
            streetAddressET.setError("Please enter the street address.");
            streetAddressET.setFocusable(true);
            if (activitySuburb.equalsIgnoreCase(""))
                suburbET.setError("Please enter the suburb.");
            if (activityState.equalsIgnoreCase(""))
                stateET.setError("Please enter the state.");
        } else if (activitySuburb.equalsIgnoreCase("")) {
            suburbET.setError("Please enter the suburb.");
            suburbET.setFocusable(true);
            if (activityState.equalsIgnoreCase(""))
                stateET.setError("Please enter the state.");
        } else if (activityState.equalsIgnoreCase("")) {
            stateET.setError("Please enter the state.");
            stateET.setFocusable(true);
        } else {

        }


    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);

        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if (startDate) {
                activityStartDateB.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year));
                activityStartDate = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year);
                startDate = false;
            } else if (endDate) {
                activityEndDateB.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year));
                activityStartDate = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year);
                endDate = false;
            }
//            Toast.makeText(getApplicationContext(),String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)+ "-" + String.valueOf(year),Toast.LENGTH_LONG).show();
        }
    };

    public void activityStartDateButton(View view) {
        showDatePicker();
        startDate = true;
    }

    public void activityEndDateButton(View view) {
        showDatePicker();
        endDate = true;
    }

    public void activityStartTimeButton(View view) {
        showDialog(TIME_DIALOG_ID);
        startTime = true;
    }

    public void activityEndTimeButton(View view) {
        showDialog(TIME_DIALOG_ID);
        endTime = true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            if (startTime) {
                activityStartTimeB.setText(new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)));
                activityStartTime = activityStartTimeB.getText().toString();
                startTime = false;
            } else if (endTime) {
                activityEndTimeB.setText(new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)));
                activityEndTime = activityEndTimeB.getText().toString();
                endTime = false;

                activityName = activityET.getText().toString().trim();
                activityDescription = activityDescET.getText().toString().trim();

                if (activityName.equalsIgnoreCase("")) {
                    activityET.setError("Please enter the activity name.");
                    activityET.setFocusable(true);
                    if (activityDescription.equalsIgnoreCase("")) {
                        activityDescET.setError("Please enter the activity description.");
                    }
                } else if (activityDescription.equalsIgnoreCase("")) {
                    if (activityDescription.equalsIgnoreCase("")) {
                        activityDescET.setError("Please enter the activity description.");
                        activityDescET.setFocusable(true);
                    }
                } else if (activityStartDate.equalsIgnoreCase("Activity Start Date")) {
                    Toast.makeText(AddActivity.this, "Please select the activity start date.", Toast.LENGTH_LONG).show();
                } else if (activityEndDate.equalsIgnoreCase("Activity End Date")) {
                    Toast.makeText(AddActivity.this, "Please select the activity end date.", Toast.LENGTH_LONG).show();
                } else if (activityStartTime.equalsIgnoreCase("Activity Start Time")) {
                    Toast.makeText(AddActivity.this, "Please select the activity start time.", Toast.LENGTH_LONG).show();
                } else if (activityEndTime.equalsIgnoreCase("Activity End Time")) {
                    Toast.makeText(AddActivity.this, "Please select the activity end time.", Toast.LENGTH_LONG).show();
                } else {
                    if (imagePath != null) {
                        fileName = getImageName(imagePath);
                        profilePicUploader();
                    } else {
                        fileName = "default";
                    }

                    activityInfoLayout.setVisibility(View.GONE);
                    activityInformationContent.setVisibility(View.GONE);
                    contactInfoLayout.setVisibility(View.VISIBLE);
                    contactInformationContent.setVisibility(View.VISIBLE);
                    otherInfoLayout.setVisibility(View.GONE);
                    otherInformationContent.setVisibility(View.GONE);
                    activityImagesLayout.setVisibility(View.GONE);
                    activityImagesContent.setVisibility(View.GONE);

                    Button button = (Button) findViewById(R.id.top_button_2);
                    button.setBackgroundResource(R.drawable.dark_blue_circle);
                }
            }
        }
    };

    public void profilePicUploader() {
        //dialog = ProgressDialog.show(AddActivity.this, "", "Uploading file...", true);
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
            //dialog.dismiss();
            Log.e("uploadFile", "Source File not exist :" + imagePath);
            runOnUiThread(new Runnable() {
                public void run() {
//                    messageText.setText("Source File not exist :"+ imagepath);
                }
            });
            return 0;
        } else {
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
                if (serverResponseCode == 200) {
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

                //dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
//                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(AddActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                //dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(AddActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "Exception : " + e.getMessage(), e);
            }
            //dialog.dismiss();
            return serverResponseCode;
        } // End else block
    }

    public String getImageName(String imageName) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String fDate = df.format(c.getTime());

        final Random r = new Random();
        String rNum = Integer.toString(r.nextInt(999) + 1);
        String ext = imageName.substring(imageName.length() - 4);

        imageName = "IMG_" + fDate + "_" + rNum + ext;
        return imageName;
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
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ivProfilePic.setImageBitmap(bitmap);
//            messageText.setText("Uploading file path:" +imagepath);

            String Fpath = getPath(selectedImageUri);
            File file = new File(Fpath);
            imageName = file.getName();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public void uploadActivityImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void activityTypeButton(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                AddActivity.this);
        builder.setTitle("Choose an option");
        String[] optionsList = {"Indoor", "Outdoor"};
        builder.setItems(optionsList,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (which == 0) {
                            activityTypeB.setText("Indoor");
                            activityType = "Indoor";
                        } else if (which == 1) {
                            activityTypeB.setText("Outdoor");
                            activityType = "Outdoor";
                        }
                        dialog.cancel();
                    }

                });
        builder.show();
    }

    public void createDialogInterests() {
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

                int pos = 0, myIndex = 0;
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
                } else {
                    StaticData.arrInterests[groupPosition][childPosition] = 0;
                    v.setBackgroundColor(Color.WHITE);
                    selectedSubId.remove(String.valueOf(subIdArr[(myIndex + childPosition)]));
                }
                strSelectedSubId = "";
                strSelectedSubName = "";
                for (String s : selectedSubId) {
                    strSelectedSubId += s + ", ";
                    strSelectedSubName += subNameArr[Integer.parseInt(s) - 1] + ", ";
                }
                strSelectedSubId = strSelectedSubId.substring(0, strSelectedSubId.length() - 2);
                strSelectedSubName = strSelectedSubName.substring(0, strSelectedSubName.length() - 2);
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
                interestsB.setText("Interests");
                activityInterests = "Interests";
                dialogInterest.dismiss();
            }
        });

        dlgBtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInterest.dismiss();
                interestsB.setText(strSelectedSubName);
                activityInterests = strSelectedSubName;
            }
        });

    }

    /**
     * Method to display the location on UI
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);


        if (mLastLocation != null) {

            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();

            StringBuilder builder = new StringBuilder();
            try {
                Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
                List<android.location.Address> address = geoCoder.getFromLocation(lat, lng, 1);

                LatLng mylatLng = new LatLng(lat, lng);

                if(address.size()>0){

                    int maxLines = address.get(0).getMaxAddressLineIndex();

                    String street_name = address.get(0).getAddressLine(0);

                    String curloc = address.get(0).getSubAdminArea();
                    String province = address.get(0).getAdminArea();
                    String coutry = address.get(0).getCountryName();
                    String locality_val = address.get(0).getLocality();
                    String Premises_val = address.get(0).getPremises();

                    String country_code = address.get(0).getCountryCode();
                    String postal_code = address.get(0).getPostalCode();
                    String state = address.get(0).getAdminArea();

                    String cordination = String.valueOf(lat) + "," + String.valueOf(lng);

//                    System.out.println("street" + street);
//
//
//
//                    //  Toast.makeText(getApplication(),locality_val+Premises_val,Toast.LENGTH_LONG).show();
//
//                    zone.setText(province);
//                    city.setText(locality_val);
//                    street.setText(street_name);

                    map = ((MapFragment) getFragmentManager()
                            .findFragmentById(R.id.map)).getMap();
                    Marker hotel = map.addMarker(new MarkerOptions()
                            .position(mylatLng)
                            .title("Your are here")
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.map_pin))
                            .snippet(locality_val));
                    hotel.showInfoWindow();
                    // Move the camera instantly to hamburg with a zoom of 15.
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(mylatLng, 15));
                    // Zoom in, animating the camera.
                    // map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


                    // fnialAddress = curloc + "," + province + "," + country; // This is
                    // the
                    // complete
                    // address.

                    // Toast.makeText(this, curloc + province + country,
                    // Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {

            }


        } else {


        }
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("add activity", "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
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
                            m = jsonMainArr.length();
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
                                        s = jsonSubArr.length();

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

                                    Sports sports = new Sports(main);
                                    expandableListTitle.add(main);
                                    expandableListDetail.put(main,subList);
                                }
                            }

                            subNameArr = subArrName;
                            subWiseArr = mSubWiseArr;
                            subIdArr = subArrId;
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

    public void interestsButton (View view) {
        if (StaticData.arrInterests == null) {
            Toast.makeText(this, "Please try in 2 sec", Toast.LENGTH_LONG).show();
        } else {
            dialogInterest.show();
//            Toast.makeText(this, String.valueOf(m) +"   "+ String.valueOf(s), Toast.LENGTH_SHORT).show();
        }
    }
    public void isEventButton (View view) {
        if (isEvent) {
            isEventB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_deselect_grey, 0, 0, 0);
            isEvent = false;
            isActivityEvent = "No";
        } else {
            isEventB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_select_grey, 0, 0, 0);
            isEvent = true;
            isActivityEvent = "Yes";
        }
    }

    private class ActivityDataLoadingAsyncTask extends
            AsyncTask<Void, Void, String> {
//        ProgressDialog dialog = ProgressDialog.show(AddActivity.this, "",
//                "", true);

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
//            dialog.cancel();
            if (result != null) {

                try {
                    JSONObject itemsResult = new JSONObject(result);
                    int value = itemsResult.getInt("value");

                    if (value == 0) {
                        Toast.makeText(AddActivity.this,
                                "No Data!", Toast.LENGTH_LONG).show();
                    } else {
                        JSONArray currencyArray = itemsResult
                                .getJSONArray("currency");
                        JSONArray ageGroupArray = itemsResult
                                .getJSONArray("age_group");
                        JSONArray languageArray = itemsResult
                                .getJSONArray("language");
                        JSONArray countryArray = itemsResult
                                .getJSONArray("country");

                        currenciesArray = new ArrayList<Currency>();
                        ageGroupsArray = new ArrayList<AgeGroup>();
                        languagesArray = new ArrayList<Language>();
                        countriesArray = new ArrayList<Country>();

                        currenciesArr = new ArrayList<String>();
                        ageGroupsArr = new ArrayList<String>();
                        languagesArr = new ArrayList<String>();
                        countriesArr = new ArrayList<String>();

                        for (int i = 0; i < currencyArray.length(); i++) {
                            JSONObject jsonObject = currencyArray
                                    .getJSONObject(i);
                            Currency currency = new Currency(jsonObject);
                            currenciesArray.add(currency);
                            currenciesArr.add(currency.getCurrency_code());
                        }
                        for (int i = 0; i < ageGroupArray.length(); i++) {
                            JSONObject jsonObject = ageGroupArray
                                    .getJSONObject(i);
                            AgeGroup ageGroup = new AgeGroup(jsonObject);
                            ageGroupsArray.add(ageGroup);
                            ageGroupsArr.add(ageGroup.getAge_group_value());
                        }
                        for (int i = 0; i < languageArray.length(); i++) {
                            JSONObject jsonObject = languageArray
                                    .getJSONObject(i);
                            Language language = new Language(jsonObject);
                            languagesArray.add(language);
                            languagesArr.add(language.getLanguage_name());
                        }
                        for (int i = 0; i < countryArray.length(); i++) {
                            JSONObject jsonObject = countryArray
                                    .getJSONObject(i);
                            Country country = new Country(jsonObject);
                            countriesArray.add(country);
                            countriesArr.add(country.getCountry_name());
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            StringBuilder responseStr = new StringBuilder();

            try {
                HttpGet httpGet = new HttpGet(
                        "http://www.myhotch.com/app_control/load_data_for_add_activity.php");
                HttpResponse response = HttpConnectionManager.getClient()
                        .execute(httpGet);

                InputStream responseInputStream = response.getEntity()
                        .getContent();
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(responseInputStream));

                String responseLineStr = null;
                while ((responseLineStr = bufferedReader.readLine()) != null) {
                    responseStr.append(responseLineStr);

                }

                bufferedReader.close();
                responseInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseStr.toString();
        }
    }

    public void languagesButton (View view) {

    }
    public void currencyButton (View view) {

    }
    public void ageGroupButton (View view) {

    }
    public void countryButton (View view) {

    }

    public void pickMyLocationButton (View view) {
        if (myLocation) {
            pickLocationB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_deselect_grey, 0, 0, 0);
            myLocation = false;
        } else {
            pickLocationB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_select_grey, 0, 0, 0);
            myLocation = true;
            displayLocation();
        }
    }
    public void activityStatusButton (View view) {
        if (active) {
            activeB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_deselect_grey, 0, 0, 0);
            active = false;
            activityStatus = "Inactive";
        } else {
            activeB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_select_grey, 0, 0, 0);
            active = true;
            activityStatus = "Active";
        }
    }
    public void uploadImagesButton (View view) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.top_button_1) {
            activityInfoLayout.setVisibility(View.VISIBLE);
            activityInformationContent.setVisibility(View.VISIBLE);
            contactInfoLayout.setVisibility(View.GONE);
            contactInformationContent.setVisibility(View.GONE);
            otherInfoLayout.setVisibility(View.GONE);
            otherInformationContent.setVisibility(View.GONE);
            activityImagesLayout.setVisibility(View.GONE);
            activityImagesContent.setVisibility(View.GONE);
        } else if (v.getId() == R.id.top_button_2) {
            if (activityInfo) {
                activityInfoLayout.setVisibility(View.GONE);
                activityInformationContent.setVisibility(View.GONE);
                contactInfoLayout.setVisibility(View.GONE);
                contactInformationContent.setVisibility(View.GONE);
                otherInfoLayout.setVisibility(View.VISIBLE);
                otherInformationContent.setVisibility(View.VISIBLE);
                activityImagesLayout.setVisibility(View.GONE);
                activityImagesContent.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Please fill the activity information", Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.top_button_3) {
            if (contactInfo) {
                activityInfoLayout.setVisibility(View.GONE);
                activityInformationContent.setVisibility(View.GONE);
                contactInfoLayout.setVisibility(View.VISIBLE);
                contactInformationContent.setVisibility(View.VISIBLE);
                otherInfoLayout.setVisibility(View.GONE);
                otherInformationContent.setVisibility(View.GONE);
                activityImagesLayout.setVisibility(View.GONE);
                activityImagesContent.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Please fill the contact information", Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.top_button_4) {
            if (otherInfo) {
                activityInfoLayout.setVisibility(View.GONE);
                activityInformationContent.setVisibility(View.GONE);
                contactInfoLayout.setVisibility(View.GONE);
                contactInformationContent.setVisibility(View.GONE);
                otherInfoLayout.setVisibility(View.GONE);
                otherInformationContent.setVisibility(View.GONE);
                activityImagesLayout.setVisibility(View.VISIBLE);
                activityImagesContent.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Please fill the other information", Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.save_button) {
            if (activityInfo && contactInfo && otherInfo && activityImages) {

            } else {

            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.cancel_button) {
            finish();
        }
    }
}
