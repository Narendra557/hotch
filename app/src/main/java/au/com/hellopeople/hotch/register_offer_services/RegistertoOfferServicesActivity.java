package au.com.hellopeople.hotch.register_offer_services;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import au.com.hellopeople.hotch.AddActivity;
import au.com.hellopeople.hotch.R;
import au.com.hellopeople.hotch.login.LoginActivity;
import au.com.hellopeople.hotch.register.RegisterCompleted;
import au.com.hellopeople.hotch.register.RegisterPassword;
import au.com.hellopeople.hotch.register.RegisterPerson;
import au.com.hellopeople.hotch.register.interests.ExpandableListDataPump;
import au.com.hellopeople.hotch.register.interests.HTTPDataHandler;
import au.com.hellopeople.hotch.register.interests.SportsAdapterExp;
import au.com.hellopeople.hotch.util.StaticData;

public class RegistertoOfferServicesActivity extends Activity implements View.OnClickListener, RegisterCompleted {

    Button btTop1, btTop2, btTop3, btTop4;
    String firstName, lastName, personType, email, interest, password, rePassword, emailPattern;
    String phoneNo, mobileNo, streetAddress, apartment, suburb, state, country;
    String bizName, phoneNoBiz, mobileNoBiz, streetAddressBiz, apartmentBiz, suburbBiz, stateBiz, countryBiz;
    EditText etFirstName, etLastName, etEmail, etPassword, etRePassword;
    EditText etPhoneNo, etMobileNo, etStreetAddress, etApartment, etSuburb, etState;
    EditText etBizName, etPhoneNoBiz, etMobileNoBiz, etStreetAddressBiz, etApartmentBiz, etSuburbBiz, etStateBiz;
    Button btInterest, btRegister, btProfilePicUploader, btDateOfBirth, btMale, btFemale;
    Button btCountry, btPickMyLocation, btCountryBiz, btPickMyLocationBiz;
    ImageView ivProfilePic;
    int personTypeId;
    public static int lastPersonId =0;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;

    private String upLoadServerUri = null;
    private String imagePath=null, imageName =null, fileName = null;
    public static ExpandableListView gvSports;
    public ListView lvCountry;
    public static Dialog dialogInterest, dialogCountry, dialogCountryBiz;
    public int m, s;
    public int subWiseArr[];
    public int subIdArr[];
    private boolean firstCompleted=false, secondCompleted=false, thirdCompleted=false, forthCompleted=false, fivthCompleted=false;
    private boolean pickMyLocation = true, pickMyLocationBIz = true;
    private DatePickerDialog bDayDialog;
    public static Calendar mCalendar;
    private static Button mDateButton;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    DialogFragment dateFragment;
    public String subNameArr[];
    public String strSelectedSubId="";
    public String strSelectedSubName="";
    List<String> selectedSubId = new ArrayList<String>();
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    RelativeLayout yourInfoLayout, contactInfoLayout, paymentInfoLayout, businessInfoLayout, yourInformationContent, contactInformationContent, paymentInformationContent, businessInformationContent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerto_offer_services);

        Bundle extras = getIntent().getExtras();
        personTypeId = extras.getInt("person_type");

        yourInfoLayout = (RelativeLayout) findViewById(R.id.your_info_layout);
        contactInfoLayout = (RelativeLayout) findViewById(R.id.contact_info_layout);
        paymentInfoLayout = (RelativeLayout) findViewById(R.id.payment_info_layout);
        businessInfoLayout = (RelativeLayout) findViewById(R.id.business_info_layout);
        yourInformationContent = (RelativeLayout) findViewById(R.id.your_information_content);
        contactInformationContent = (RelativeLayout) findViewById(R.id.contact_information_content);
        paymentInformationContent = (RelativeLayout) findViewById(R.id.payment_information_content);
        businessInformationContent = (RelativeLayout) findViewById(R.id.business_information_content);

        btTop1 = (Button)findViewById(R.id.top_button_1);
        btTop2 = (Button)findViewById(R.id.top_button_2);
        btTop3 = (Button)findViewById(R.id.top_button_3);
        btTop4 = (Button)findViewById(R.id.top_button_4);


        int []onclickButtonIds = {R.id.top_button_1, R.id.top_button_2, R.id.top_button_3, R.id.top_button_4, R.id.cancel_button, R.id.register_button};
        for (int i = 0; i < onclickButtonIds.length; i++) {
            Button button = (Button) findViewById(onclickButtonIds[i]);
            button.setOnClickListener(this);
        }
        btDateOfBirth = (Button)findViewById(R.id.btDateOfBirth);
        btMale = (Button) findViewById(R.id.btMale);
        btFemale = (Button) findViewById(R.id.btFemale);

        //Your Information......................
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
        createDialogInterests();

        //Contact Information......................................
        etPhoneNo = (EditText)this.findViewById(R.id.phone_no_text);
        etMobileNo = (EditText)this.findViewById(R.id.mobile_no_text);
        etStreetAddress = (EditText)this.findViewById(R.id.street_address_text);
        etApartment = (EditText)this.findViewById(R.id.apartment_text);
        etSuburb = (EditText)this.findViewById(R.id.suburb_text);
        etState = (EditText)this.findViewById(R.id.state_text);
        btCountry = (Button)this.findViewById(R.id.country_button);
        btPickMyLocation = (Button)this.findViewById(R.id.pick_my_location_button);
        createDialogCountry();
        mCalendar = Calendar.getInstance();

        //Business Information.....................................
        etPhoneNoBiz = (EditText)this.findViewById(R.id.b_phone_no_text);
        etMobileNoBiz = (EditText)this.findViewById(R.id.b_mobile_no_text);
        etStreetAddressBiz = (EditText)this.findViewById(R.id.b_street_address_text);
        etApartmentBiz = (EditText)this.findViewById(R.id.b_apartment_text);
        etSuburbBiz = (EditText)this.findViewById(R.id.b_suburb_text);
        etStateBiz = (EditText)this.findViewById(R.id.b_state_text);
        btCountryBiz = (Button)this.findViewById(R.id.b_country_button);
        btPickMyLocationBiz = (Button)this.findViewById(R.id.b_pick_my_location_button);
        createDialogCountryBiz();

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

    public void showDatePickerDialog(View v){
        showDatePicker();
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
            btDateOfBirth.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)+ "-" + String.valueOf(year));
//            Toast.makeText(getApplicationContext(),String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)+ "-" + String.valueOf(year),Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.top_button_1) {
            btTop1.setBackgroundResource(R.drawable.dark_blue_circle);
            btTop2.setBackgroundResource(R.drawable.blue_circle);
            btTop3.setBackgroundResource(R.drawable.blue_circle);
            btTop4.setBackgroundResource(R.drawable.blue_circle);
            yourInfoLayout.setVisibility(View.VISIBLE);
            yourInformationContent.setVisibility(View.VISIBLE);
            contactInfoLayout.setVisibility(View.GONE);
            contactInformationContent.setVisibility(View.GONE);
            paymentInfoLayout.setVisibility(View.GONE);
            paymentInformationContent.setVisibility(View.GONE);
            businessInfoLayout.setVisibility(View.GONE);
            businessInformationContent.setVisibility(View.GONE);
        } else if (v.getId() == R.id.top_button_2) {
//            checkYourInformation();                     // should be remove comments.................
//            if (firstCompleted)
            {
                btTop1.setBackgroundResource(R.drawable.blue_circle);
                btTop2.setBackgroundResource(R.drawable.dark_blue_circle);
                btTop3.setBackgroundResource(R.drawable.blue_circle);
                btTop4.setBackgroundResource(R.drawable.blue_circle);
                yourInfoLayout.setVisibility(View.GONE);
                yourInformationContent.setVisibility(View.GONE);
                contactInfoLayout.setVisibility(View.VISIBLE);
                contactInformationContent.setVisibility(View.VISIBLE);
                paymentInfoLayout.setVisibility(View.GONE);
                paymentInformationContent.setVisibility(View.GONE);
                businessInfoLayout.setVisibility(View.GONE);
                businessInformationContent.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.top_button_3) {
//            if (firstCompleted && secondCompleted)            //should be remove comment here...............
            {
                btTop1.setBackgroundResource(R.drawable.blue_circle);
                btTop2.setBackgroundResource(R.drawable.blue_circle);
                btTop3.setBackgroundResource(R.drawable.dark_blue_circle);
                btTop4.setBackgroundResource(R.drawable.blue_circle);
                yourInfoLayout.setVisibility(View.GONE);
                yourInformationContent.setVisibility(View.GONE);
                contactInfoLayout.setVisibility(View.GONE);
                contactInformationContent.setVisibility(View.GONE);
                paymentInfoLayout.setVisibility(View.VISIBLE);
                paymentInformationContent.setVisibility(View.VISIBLE);
                businessInfoLayout.setVisibility(View.GONE);
                businessInformationContent.setVisibility(View.GONE);
//            } else if (!firstCompleted) {                                                                     //should be remove comment here too...................
//                Toast.makeText(this, "Please complete Your Information", Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(this, "Please complete Contact Information", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.top_button_4) {
//            if (firstCompleted && secondCompleted && thirdCompleted)               //should be remove comment here.........................................
            {
                btTop1.setBackgroundResource(R.drawable.blue_circle);
                btTop2.setBackgroundResource(R.drawable.blue_circle);
                btTop3.setBackgroundResource(R.drawable.blue_circle);
                btTop4.setBackgroundResource(R.drawable.dark_blue_circle);
                yourInfoLayout.setVisibility(View.GONE);
                yourInformationContent.setVisibility(View.GONE);
                contactInfoLayout.setVisibility(View.GONE);
                contactInformationContent.setVisibility(View.GONE);
                paymentInfoLayout.setVisibility(View.GONE);
                paymentInformationContent.setVisibility(View.GONE);
                businessInfoLayout.setVisibility(View.VISIBLE);
                businessInformationContent.setVisibility(View.VISIBLE);
//            } else if (!firstCompleted){
//                Toast.makeText(this, "Please complete Your Information", Toast.LENGTH_SHORT).show();               //should be remove comment here too............................
//            }else if (!secondCompleted) {
//                Toast.makeText(this, "Please complete Contact Information", Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(this, "Please complete Payment Information", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.register_button) {
//            if (firstCompleted && secondCompleted && thirdCompleted && forthCompleted)              //should be remove comment here.....................
            {
                Intent intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                finish();
//            } else if(!firstCompleted) {
//                Toast.makeText(this, "You have missed something in Your Information", Toast.LENGTH_SHORT).show();                //should be remove comment here too.............
//            }else if(!secondCompleted) {
//                Toast.makeText(this, "You have missed something in Contact Information", Toast.LENGTH_SHORT).show();
//            }else if(!thirdCompleted) {
//                Toast.makeText(this, "You have missed something in Payment Information", Toast.LENGTH_SHORT).show();
//            }else if(!forthCompleted) {
//                Toast.makeText(this, "You have missed something in Business Information", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.cancel_button) {
            finish();
        }
    }

    private void checkYourInformation(){
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
            firstCompleted = false;
        } else if (lastName.equals(null) || lastName.equals("")) {
            Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show();
            etLastName.requestFocus();
            firstCompleted = false;
        } else if (email.equals(null) || email.equals("")) {
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            firstCompleted = false;
        } else if (!email.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            firstCompleted = false;
        }
        else if (password.equals(null) || password.equals("")) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            firstCompleted = false;
        } else if (rePassword.equals(null) || rePassword.equals("")) {
            Toast.makeText(this, "Re-type password", Toast.LENGTH_SHORT).show();
            firstCompleted = false;
        } else if (!password.trim().equals(rePassword.trim())) {
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            etPassword.setText(null);
            etRePassword.setText(null);
            etPassword.requestFocus();
            firstCompleted = false;
        }
        else if (password.length() < 4 || rePassword.length() < 4) {
            Toast.makeText(this, "Password should have atleast 4 characters", Toast.LENGTH_SHORT).show();
            etPassword.setText(null);
            etRePassword.setText(null);
            etPassword.requestFocus();
            firstCompleted = false;
        }
        else {
//            Toast.makeText(this, "Signing up...", Toast.LENGTH_SHORT).show();
            //Insert data to person table...........
            if (imagePath != null) {
                fileName = getImageName(imagePath);
                profilePicUploader();
            }
            else fileName = "default";
            firstCompleted = true;
//            new RegisterPerson(this).execute(String.valueOf(personTypeId), strSelectedSubId, firstName, lastName, email, fileName);
        }

    }

    public void registerNewParticipator(View view){
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        email = etEmail.getText().toString();
//        interest = btInterest.getText().toString();
        password = etPassword.getText().toString();
        rePassword = etRePassword.getText().toString();

        if (firstName.equals(null) || firstName.equals(""))
            Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show();
        else if (lastName.equals(null) || lastName.equals(""))
            Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show();
        else if (email.equals(null) || email.equals(""))
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
        else if (!email.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
        }
        else if (password.equals(null) || password.equals(""))
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        else if (rePassword.equals(null) || rePassword.equals(""))
            Toast.makeText(this, "Re-type password", Toast.LENGTH_SHORT).show();
        else if (!password.trim().equals(rePassword.trim())) {
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
            }
            else fileName = "default";
            new RegisterPerson(this).execute(String.valueOf(personTypeId), strSelectedSubId, firstName, lastName, email, fileName);
        }
    }

    public void profilePicUploader(){
        dialog = ProgressDialog.show(getApplicationContext(), "", "Uploading file...", true);
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
                        Toast.makeText(getApplicationContext(), "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
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

    public void genderSelection(View v){
        if (v.getId() == R.id.btMale){
            btMale.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_select_grey, 0, 0, 0);
            btFemale.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_deselect_grey, 0, 0, 0);
        }
        else if (v.getId() == R.id.btFemale){
            btMale.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_deselect_grey,0,0,0);
            btFemale.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_select_grey,0,0,0);
        }

    }

    public void getCurrentLocation(View v){
        if (!pickMyLocation){
            btPickMyLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_select_grey ,0 ,0 ,0 );
            pickMyLocation = true;
        } else {
            btPickMyLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_deselect_grey ,0 , 0, 0 );
            pickMyLocation = false;
        }
    }

    public void getCurrentLocationBiz(View v){
        if (!pickMyLocationBIz){
            btPickMyLocationBiz.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_select_grey ,0 ,0 ,0 );
            pickMyLocationBIz = true;
        } else {
            btPickMyLocationBiz.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_deselect_grey ,0 , 0, 0 );
            pickMyLocationBIz = false;
        }
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
                for(int i=0; i<StaticData.arrInterests.length; i++){
                    for(int j=0; j<StaticData.arrInterests[0].length; j++){
                        StaticData.arrInterests[i][j] = 0;
                    }
                }
                for(int i=0; i<selectedSubId.size(); i++){
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

    public void showCountry(View v){
        if (StaticData.arrInterests == null) {
            Toast.makeText(this, "Please try in 2 sec", Toast.LENGTH_LONG).show();
        } else {
            dialogCountry.show();
//            Toast.makeText(this, String.valueOf(m) +"   "+ String.valueOf(s), Toast.LENGTH_SHORT).show();
        }
    }

    public void createDialogCountry(){
        new SportsJSON(this).execute("http://www.myhotch.com/app_control/interests_main_all.php");
        dialogCountry = new Dialog(this);
        dialogCountry.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCountry.setContentView(R.layout.dialog_country);

        lvCountry = (ListView) dialogCountry.findViewById(R.id.lvCountry);
        Button dlgBtCancel = (Button) dialogCountry.findViewById(R.id.dlgBtCancel);
        Button dlgBtOk = (Button) dialogCountry.findViewById(R.id.dlgBtOk);

//        lvCountry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        gvSports.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
////                Toast.makeText(getApplicationContext(), expandableListTitle.get(groupPosition) + " -> " + expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
//
//                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
//                parent.setItemChecked(index, true);
//
//                int pos = 0, myIndex =0;
//                while (pos < groupPosition) {
//                    myIndex += subWiseArr[pos];
//                    pos++;
//                }
//
////                Toast.makeText(getApplicationContext(), String.valueOf(groupPosition) +" - "+ String.valueOf(childPosition), Toast.LENGTH_SHORT).show();
//                if (StaticData.arrInterests[groupPosition][childPosition] == 0) {
//                    StaticData.arrInterests[groupPosition][childPosition] = 1;
////                    Toast.makeText(getApplicationContext(), String.valueOf(groupPosition)+"  -  "+ String.valueOf(groupPosition),Toast.LENGTH_SHORT).show();
//                    v.setBackgroundColor(Color.YELLOW);
//                    selectedSubId.add(String.valueOf(subIdArr[(myIndex + childPosition)]));
//                }
//                else {
//                    StaticData.arrInterests[groupPosition][childPosition] = 0;
//                    v.setBackgroundColor(Color.WHITE);
//                    selectedSubId.remove(String.valueOf(subIdArr[(myIndex + childPosition)]));
//                }
//                strSelectedSubId = ""; strSelectedSubName = "";
//                for (String s : selectedSubId)
//                {
//                    strSelectedSubId += s + ", ";
//                    strSelectedSubName += subNameArr[Integer.parseInt(s) - 1] + ", ";
//                }
//                strSelectedSubId = strSelectedSubId.substring(0, strSelectedSubId.length()-2);
//                strSelectedSubName = strSelectedSubName.substring(0, strSelectedSubName.length()-2);
////                Toast.makeText(getApplicationContext(), strSelectedSubName,Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });

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
                dialogCountry.dismiss();
            }
        });

        dlgBtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCountry.dismiss();
                btInterest.setText(strSelectedSubName);
            }
        });

    }

    public void showCountryBiz(View v){
        if (StaticData.arrInterests == null) {
            Toast.makeText(this, "Please try in 2 sec", Toast.LENGTH_LONG).show();
        } else {
            dialogCountryBiz.show();
//            Toast.makeText(this, String.valueOf(m) +"   "+ String.valueOf(s), Toast.LENGTH_SHORT).show();
        }
    }

    public void createDialogCountryBiz(){
        new SportsJSON(this).execute("http://www.myhotch.com/app_control/interests_main_all.php");
        dialogCountryBiz = new Dialog(this);
        dialogCountryBiz.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCountryBiz.setContentView(R.layout.dialog_country);

        lvCountry = (ListView) dialogCountry.findViewById(R.id.lvCountry);
        Button dlgBtCancel = (Button) dialogCountry.findViewById(R.id.dlgBtCancel);
        Button dlgBtOk = (Button) dialogCountry.findViewById(R.id.dlgBtOk);

//        lvCountry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        gvSports.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
////                Toast.makeText(getApplicationContext(), expandableListTitle.get(groupPosition) + " -> " + expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
//
//                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
//                parent.setItemChecked(index, true);
//
//                int pos = 0, myIndex =0;
//                while (pos < groupPosition) {
//                    myIndex += subWiseArr[pos];
//                    pos++;
//                }
//
////                Toast.makeText(getApplicationContext(), String.valueOf(groupPosition) +" - "+ String.valueOf(childPosition), Toast.LENGTH_SHORT).show();
//                if (StaticData.arrInterests[groupPosition][childPosition] == 0) {
//                    StaticData.arrInterests[groupPosition][childPosition] = 1;
////                    Toast.makeText(getApplicationContext(), String.valueOf(groupPosition)+"  -  "+ String.valueOf(groupPosition),Toast.LENGTH_SHORT).show();
//                    v.setBackgroundColor(Color.YELLOW);
//                    selectedSubId.add(String.valueOf(subIdArr[(myIndex + childPosition)]));
//                }
//                else {
//                    StaticData.arrInterests[groupPosition][childPosition] = 0;
//                    v.setBackgroundColor(Color.WHITE);
//                    selectedSubId.remove(String.valueOf(subIdArr[(myIndex + childPosition)]));
//                }
//                strSelectedSubId = ""; strSelectedSubName = "";
//                for (String s : selectedSubId)
//                {
//                    strSelectedSubId += s + ", ";
//                    strSelectedSubName += subNameArr[Integer.parseInt(s) - 1] + ", ";
//                }
//                strSelectedSubId = strSelectedSubId.substring(0, strSelectedSubId.length()-2);
//                strSelectedSubName = strSelectedSubName.substring(0, strSelectedSubName.length()-2);
////                Toast.makeText(getApplicationContext(), strSelectedSubName,Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });

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
                dialogCountryBiz.dismiss();
            }
        });

        dlgBtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCountryBiz.dismiss();
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
            expandableListDetail = ExpandableListDataPump.getData();
            expandableListTitle = new ArrayList<String>();

            //..........Process JSON DATA................
            if(stream !=null){
                Log.d("", "Inside onPost");

                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject obj = new JSONObject(stream);
                    if(obj != null) {
                        JSONArray jsonMainArr = obj.optJSONArray("main_result");
                        JSONArray jsonSubArr = obj.optJSONArray("sub_result");
                        int subListId[] = new int[jsonSubArr.length()];
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

                                                    subListId[indx] = Integer.parseInt(sId);
                                                    subArrName[Integer.parseInt(sId)-1] = sub; indx++;
                                                }
                                            }
                                        }
                                    }
                                    expandableListTitle.add(main);
                                    expandableListDetail.put(main,subList);
                                }
                            }
                            onSubArrCompleteAll(subArrName);
                            onSubWiseArrComplete(mSubWiseArr);
                            onSubIdArrComplete(subListId);
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
