package au.com.hellopeople.hotch.register;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
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
import au.com.hellopeople.hotch.choose_category.CategoryCompleted;
import au.com.hellopeople.hotch.choose_category.ChooseCategoryJSON;
import au.com.hellopeople.hotch.concern.ConcernCompleted;
import au.com.hellopeople.hotch.concern.ConcernJSON;
import au.com.hellopeople.hotch.language.LanguageCompleted;
import au.com.hellopeople.hotch.language.LanguageJSON;
import au.com.hellopeople.hotch.login.LoginActivity;
import au.com.hellopeople.hotch.register.interests.ChooseSubInterestsJSON;
import au.com.hellopeople.hotch.register.interests.ExpandableListDataPump;
import au.com.hellopeople.hotch.register.interests.HTTPDataHandler;
import au.com.hellopeople.hotch.register.interests.Sports;
import au.com.hellopeople.hotch.register.interests.SportsAdapterExp;
import au.com.hellopeople.hotch.register.interests.SubInterestCompleted;
import au.com.hellopeople.hotch.register_offer_services.DatePickerFragment;
import au.com.hellopeople.hotch.util.StaticData;

public class RegisterActivity extends AppCompatActivity implements RegisterCompleted, CategoryCompleted, SubInterestCompleted, ConcernCompleted, LanguageCompleted {

    String firstName, lastName, personType, email, userName, password, message, dob, gender, rePassword, emailPattern;
    EditText etFirstName, etLastName, etEmail, etPassword, etRePassword, etUserName, etMessage;
    Button  btRegister, btProfilePicUploader, btPostNow, btLocAllow, btDateOfBirth, btDob, btGender, btCategories, btSporting, btGym, btArts, btRelaxation, btChildren, btPossibleIssues, btLanguages;
    ImageView ivProfilePic;
    RelativeLayout personalInfoLayout, aboutYourSelfLayout, linkYourAccLayout, sellingOrSharingLayout;
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
    String[] allCategories, sportingInterests, gymInterests, artsInterests, relaxationInterests, childrenInterests, allConcerns, allLanguages = null;
    int[] allCategoriesIds, allConcernsIds, allLanguagesIds;
    String[] sportingInterestsIds, gymInterestsIds, artsInterestsIds, relaxationInterestsIds, childrenInterestsIds;
    String strSelectedCatIds="", strSelectedSportingIds="", strSelectedGymIds="", strSelectedArtsIds="", strSelectedRelaxationIds="", strSelectedChildrenIds="", strSelectedConcernsIds="", strSelectedLanguagesIds="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bundle extras = getIntent().getExtras();
        personTypeId = extras.getInt("person_type");

        etFirstName = (EditText)findViewById(R.id.firstNameText);
        etLastName = (EditText)this.findViewById(R.id.lastNameText);
        etEmail = (EditText)this.findViewById(R.id.emailText);
        etUserName = (EditText)findViewById(R.id.usernameText);
        etPassword = (EditText)this.findViewById(R.id.passwordText);
        etRePassword = (EditText)this.findViewById(R.id.retypePasswordText);
        etMessage = (EditText)this.findViewById(R.id.personMessageText);
        btDob = (Button)findViewById(R.id.dateOfBirthButton);
        btGender = (Button)findViewById(R.id.genderButton);

//        btRegister = (Button)findViewById(R.id.register_button);
        ivProfilePic = (ImageView) findViewById(R.id.imageView2);
//        btProfilePicUploader = (Button) findViewById(R.id.upload_profile_pic_button);
        upLoadServerUri = "http://www.myhotch.com/app_control/profile_pic_upload.php";

        btPostNow = (Button)findViewById(R.id.postNowButton);
        btLocAllow = (Button)findViewById(R.id.locationAllowButton);
        btDateOfBirth = (Button)findViewById(R.id.dateOfBirthButton);

        // Tell us about yourself page....................................................
        btCategories = (Button)findViewById(R.id.categoriesButton);
        btSporting = (Button)findViewById(R.id.sportingButton);
        btGym = (Button)findViewById(R.id.gymActivitiesButton);
        btArts = (Button)findViewById(R.id.artsCultureButton);
        btRelaxation = (Button)findViewById(R.id.relaxationButton);
        btChildren = (Button)findViewById(R.id.childrenButton);
        btPossibleIssues = (Button)findViewById(R.id.possibleIssuesButton);
        btLanguages = (Button)findViewById(R.id.languagesButton);

        personalInfoLayout = (RelativeLayout)findViewById(R.id.personal_info_layout);
        aboutYourSelfLayout = (RelativeLayout)findViewById(R.id.about_yourself_layout);
        linkYourAccLayout = (RelativeLayout)findViewById(R.id.link_your_accounts_layout);
        sellingOrSharingLayout = (RelativeLayout)findViewById(R.id.selling_or_sharing_dis_layout);
//        personalInfoLayout.setVisibility(View.VISIBLE);
//        aboutYourSelfLayout.setVisibility(View.GONE);
        createDialogInterests();

    }

    public void nextPage(View view) {
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        email = etEmail.getText().toString();
        userName = etUserName.getText().toString();
        password = etPassword.getText().toString();
        rePassword = etRePassword.getText().toString();
        message = etMessage.getText().toString();
        dob = btDob.getText().toString();
        gender = btGender.getText().toString();

        //uncomment the following codes............

        if (firstName.equals(null) || firstName.equals("")) {
            Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show();
            etFirstName.requestFocus();
        } else if (lastName.equals(null) || lastName.equals("")) {
            Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show();
            etLastName.requestFocus();
        } else if (userName.equals(null) || userName.equals("")) {
            Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show();
            etUserName.requestFocus();
        } else if (email.equals(null) || email.equals("")) {
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
        } else if (!email.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
        } else if (password.equals(null) || password.equals("")) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
        } else if (rePassword.equals(null) || rePassword.equals("")) {
            Toast.makeText(this, "Re-type password", Toast.LENGTH_SHORT).show();
            etRePassword.requestFocus();
        } else if (!password.trim().equals(rePassword.trim())) {
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            etPassword.setText(null);
            etRePassword.setText(null);
            etPassword.requestFocus();
        } else if (password.length() < 4 || rePassword.length() < 4) {
            Toast.makeText(this, "Password should have at least 4 characters", Toast.LENGTH_SHORT).show();
            etPassword.setText(null);
            etRePassword.setText(null);
            etPassword.requestFocus();
        } else if (gender.equals(null) || gender.equals("")) {
            Toast.makeText(this, "Please enter your sex", Toast.LENGTH_SHORT).show();
            btGender.requestFocus();
        } else {
//            Toast.makeText(this, "Signing up...", Toast.LENGTH_SHORT).show();
            //Insert data to person table...........
            if (imagePath != null) {
                fileName = getImageName(imagePath);
                profilePicUploader();
            }else
                fileName = "default";

            personalInfoLayout.setVisibility(View.GONE);
            aboutYourSelfLayout.setVisibility(View.VISIBLE);
            new ChooseCategoryJSON(this).execute();
            new ChooseSubInterestsJSON(this).execute();
            new ConcernJSON(this).execute();
            new LanguageJSON(this).execute();
//            new RegisterPassword(this).execute(String.valueOf(lastPersonId), email, password);
        }

    }

    public void nextPage2(View v){
        aboutYourSelfLayout.setVisibility(View.GONE);
        linkYourAccLayout.setVisibility(View.VISIBLE);
    }

    public void backPage2(View v){
        aboutYourSelfLayout.setVisibility(View.GONE);
        personalInfoLayout.setVisibility(View.VISIBLE);
    }

    public void nextPage3(View v){
        linkYourAccLayout.setVisibility(View.GONE);
        sellingOrSharingLayout.setVisibility(View.VISIBLE);
    }

    public void backPage3(View v){
        linkYourAccLayout.setVisibility(View.GONE);
        aboutYourSelfLayout.setVisibility(View.VISIBLE);
    }

    //saving all the registration details...................
    public void signUpButton(View v){
        new RegisterPerson(this).execute(firstName, lastName, userName, password, message, dob, email, gender, fileName, strSelectedCatIds, strSelectedSportingIds, strSelectedGymIds, strSelectedArtsIds,strSelectedRelaxationIds,strSelectedChildrenIds,strSelectedConcernsIds,strSelectedLanguagesIds);

        sellingOrSharingLayout.setVisibility(View.GONE);
        finish();
    }

    public void backPage4(View v){
        sellingOrSharingLayout.setVisibility(View.GONE);
        linkYourAccLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskComplete(String result) {
//        Toast.makeText(this, "The result is " + result, Toast.LENGTH_SHORT).show();
        boolean isNumeric = result.matches("[0-9]*");
        if (isNumeric) {
            lastPersonId = Integer.parseInt(result);
            //Insert data to password table...........

//            personalInfoLayout.setVisibility(View.GONE);
//            aboutYourSelfLayout.setVisibility(View.VISIBLE);

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

    public void dateOfBirthClick(View view){
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
            btDateOfBirth.setText(String.valueOf(year) + "-" +String.format("%02d", monthOfYear) + "-" + String.format("%02d",(dayOfMonth+1)));
        }
    };

    public void genderClick(View view){
        final CharSequence[] items = {"Male", "Female", "Other"};
        final boolean[] states = {false, false, false};
        int selectedItem = 0;
        if (btGender.getText().equals("Male"))
            selectedItem = 0;
        else if (btGender.getText().equals("Female"))
            selectedItem = 1;
        else
            selectedItem = 2;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select your gender");
        builder.setSingleChoiceItems(items,selectedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        btGender.setText("Male");
                        break;
                    case 1:
                        btGender.setText("Female");
                        break;
                    case 2:
                        btGender.setText("Other");
                        break;
                }
                dialog.dismiss();
            }
        });


//        builder.setMultiChoiceItems(items, states, new DialogInterface.OnMultiChoiceClickListener(){
//            public void onClick(DialogInterface dialogInterface, int item, boolean state) {
//            }
//        });
//        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener()
//        {
//            public void onClick(DialogInterface dialog, int id)
//            {
//                SparseBooleanArray CheCked = ((AlertDialog) dialog).getListView().getCheckedItemPositions();
//                if (CheCked.get(0))
//                {
//                    gender = "Male";
//                }
//                if (CheCked.get(1))
//                {
//                    gender = "Female";
//                }
//                if (CheCked.get(2))
//                {
//                    gender = "Other";
//                }
//                btGender.setText(gender);
//                dialog.dismiss();
//            }
//        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
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

    public void locationAllowClick(View view){
        if (btLocAllow.getText().toString().equals("Allow")){
            btLocAllow.setText("  Disallow");
            btLocAllow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_deselect_grey, 0, 0, 0);
        } else {
            btLocAllow.setText("Allow");
            btLocAllow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_select_grey, 0, 0, 0);
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
//                btInterest.setText("Interests");
                dialogInterest.dismiss();
            }
        });

        dlgBtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInterest.dismiss();
//                btInterest.setText(strSelectedSubName);
            }
        });

    }

    @Override
    public void onAllCategoryListCompleted(String[] mAllCategories, int[] mAllCategoriesIds) {
        allCategories = mAllCategories;
        allCategoriesIds = mAllCategoriesIds;
    }

    @Override
    public void sportingCompleted(String[] mSportingInterests, String[] mSportingInterestsIds) {
        sportingInterests = mSportingInterests;
        sportingInterestsIds = mSportingInterestsIds;
    }

    @Override
    public void gymCompleted(String[] mGymInterests, String[] mGymInterestsIds) {
        gymInterests = mGymInterests;
        gymInterestsIds = mGymInterestsIds;
    }

    @Override
    public void artsCompleted(String[] mArtsInterests, String[] mArtsInterestsIds) {
        artsInterests = mArtsInterests;
        artsInterestsIds = mArtsInterestsIds;
    }

    @Override
    public void relaxationCompleted(String[] mRelaxationInterests, String[] mRelaxationInterestsIds) {
        relaxationInterests = mRelaxationInterests;
        relaxationInterestsIds = mRelaxationInterestsIds;
    }

    @Override
    public void childrenCompleted(String[] mChildrenInterests, String[] mChildrenInterestsIds) {
        childrenInterests = mChildrenInterests;
        childrenInterestsIds = mChildrenInterestsIds;
    }

    @Override
    public void onConcernCompleted(String[] mAllConcerns, int[] mAllConcernsIds) {
        allConcerns = mAllConcerns;
        allConcernsIds = mAllConcernsIds;
    }

    @Override
    public void onLanguageListCompleted(String[] mAllLanguages, int[] mAllLanguagesIds) {
        allLanguages = mAllLanguages;
        allLanguagesIds = mAllLanguagesIds;
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

    public void categoriesListClick(View v){ //Category Button click...................
        try {
            if (allCategories.length > 0) {
                final String[] selectedCategories = {""};
                final String[] selectedCategoriesIds = {""};
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Make your selection");
                final boolean[] chk = new boolean[allCategories.length];
                String mCategory = btCategories.getText().toString();

                for (int i = 0; i < allCategories.length; i++) {
                    chk[i] = false;
                }

                String[] selectedArr = mCategory.split(", ");

                for (int i = 0; i < selectedArr.length; i++) {
                    for (int j = 0; j < allCategories.length; j++) {
                        if (selectedArr[i].equals(allCategories[j]))
                            chk[j] = true;
                    }
                }

                builder.setMultiChoiceItems(allCategories, chk,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            String mSelectedCategories = "";

                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                if (isChecked)
                                    chk[item] = true;
                                else chk[item] = false;
                            }
                        });
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < chk.length; i++) {
                                    if (chk[i]) {
                                        selectedCategories[0] += allCategories[i] + ", ";
                                        selectedCategoriesIds[0] += allCategoriesIds[i] + ",";
                                    }
                                }
                                if (selectedCategories[0].length() > 2)
                                    btCategories.setText(selectedCategories[0].substring(0, selectedCategories[0].length() - 2));
                                else btCategories.setText("Categories");
                                //                        ListView list = ((android.app.AlertDialog) dialog).getListView();
                                //ListView has boolean array like {1=true, 3=true}, that shows checked items

                                strSelectedCatIds = "";
                                for(String selectedIds : selectedCategoriesIds){
                                    strSelectedCatIds += selectedIds;
                                }
                                if (strSelectedCatIds.length() > 1)
                                    strSelectedCatIds = "{"+strSelectedCatIds.substring(0,strSelectedCatIds.length()-1)+"}";
                                else strSelectedCatIds = "{}";
//                                Toast.makeText(getApplicationContext(),strSelectedCatIds,Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //                        ((TextView) myFilesActivity.findViewById(R.id.text)).setText("Click here to open Dialog");
                            }
                        });
                //        builder.setItems(allCategories, new DialogInterface.OnClickListener() {
                //            public void onClick(DialogInterface dialog, int item) {
                //                btCategories.setText(allCategories[item]);
                //            }
                //        });

                android.app.AlertDialog alert = builder.create();
                alert.show();
            }else {
                Toast.makeText(this,"No data connection found, try again", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this,"No data connection found, try again",Toast.LENGTH_SHORT).show();
        }
    }

    public void sportingButtonClick(View v){ //Sporting Button click...................
        try {
            if (sportingInterests.length > 0){
                final String[] selectedSportings = {""};
                final String[] selectedSportingsIds = {""};
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Sporting Activities");
                final boolean[] chk = new boolean[sportingInterests.length];
                String mSporting = btSporting.getText().toString();

                for(int i=0; i<sportingInterests.length; i++){
                    chk[i] = false;
                }

                final String[] selectedArr = mSporting.split(", ");
                for(int i=0; i<selectedArr.length; i++){
                    for(int j=0; j<sportingInterests.length; j++){
                        if (selectedArr[i].equals(sportingInterests[j]))
                            chk[j] = true;
                    }
                }

                builder.setMultiChoiceItems(sportingInterests, chk,
                        new DialogInterface.OnMultiChoiceClickListener() {String mSelectedSportings = "";
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                if (isChecked)
                                    chk[item] = true;
                                else chk[item] = false;
                            }
                        });
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < chk.length; i++) {
                                    if (chk[i]) {
                                        selectedSportings[0] += sportingInterests[i] + ", ";
                                        selectedSportingsIds[0] += sportingInterestsIds[i] + ",";
                                    }
                                }
                                if (selectedSportings[0].length() > 2)
                                    btSporting.setText(selectedSportings[0].substring(0, selectedSportings[0].length() - 2));
                                else btSporting.setText("Sporting");

                                strSelectedSportingIds = "";
                                for(String selectedIds : selectedSportingsIds){
                                    strSelectedSportingIds += selectedIds;
                                }
                                if (strSelectedSportingIds.length() > 1)
                                    strSelectedSportingIds = "{"+strSelectedSportingIds.substring(0,strSelectedSportingIds.length()-1)+"}";
                                else strSelectedSportingIds = "{}";
//                                Toast.makeText(getApplicationContext(),strSelectedSportingIds,Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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

    public void gymButtonClick(View v){ //GYM Button click...................
        try {
            if (gymInterests.length > 0){
                final String[] selectedGym = {""};
                final String[] selectedGymIds = {""};
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("GYM Activities");
                final boolean[] chk = new boolean[gymInterests.length];
                String mGym = btGym.getText().toString();

                for(int i=0; i<gymInterests.length; i++){
                    chk[i] = false;
                }

                String[] selectedArr = mGym.split(", ");
                for(int i=0; i<selectedArr.length; i++){
                    for(int j=0; j<gymInterests.length; j++){
                        if (selectedArr[i].equals(gymInterests[j]))
                            chk[j] = true;
                    }
                }

                builder.setMultiChoiceItems(gymInterests, chk,
                        new DialogInterface.OnMultiChoiceClickListener() {String mSelectedGym = "";
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                if (isChecked)
                                    chk[item] = true;
                                else chk[item] = false;
                            }
                        });
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < chk.length; i++) {
                                    if (chk[i]) {
                                        selectedGym[0] += gymInterests[i] + ", ";
                                        selectedGymIds[0] += gymInterestsIds[i] + ",";
                                    }
                                }
                                if (selectedGym[0].length() > 2)
                                    btGym.setText(selectedGym[0].substring(0, selectedGym[0].length() - 2));
                                else btGym.setText("GYM");

                                strSelectedGymIds = "";
                                for(String selectedIds : selectedGymIds){
                                    strSelectedGymIds += selectedIds;
                                }
                                if (strSelectedGymIds.length() > 1)
                                    strSelectedGymIds = "{"+strSelectedGymIds.substring(0,strSelectedGymIds.length()-1)+"}";
                                else strSelectedGymIds = "{}";
//                                Toast.makeText(getApplicationContext(),strSelectedGymIds,Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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

    public void artsButtonClick(View v){ //Arts Button click...................
        try {
            if (artsInterests.length > 0){
                final String[] selectedArts = {""};
                final String[] selectedArtsIds = {""};
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Arts/Culture Activities");
                final boolean[] chk = new boolean[artsInterests.length];
                String mArts = btArts.getText().toString();

                for(int i=0; i<artsInterests.length; i++){
                    chk[i] = false;
                }

                final String[] selectedArr = mArts.split(", ");
                for(int i=0; i<selectedArr.length; i++){
                    for(int j=0; j<artsInterests.length; j++){
                        if (selectedArr[i].equals(artsInterests[j]))
                            chk[j] = true;
                    }
                }

                builder.setMultiChoiceItems(artsInterests, chk,
                        new DialogInterface.OnMultiChoiceClickListener() {String mSelectedArts = "";
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                if (isChecked)
                                    chk[item] = true;
                                else chk[item] = false;
                            }
                        });
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < chk.length; i++) {
                                    if (chk[i]) {
                                        selectedArts[0] += artsInterests[i] + ", ";
                                        selectedArtsIds[0] += artsInterestsIds[i] + ",";
                                    }
                                }
                                if (selectedArts[0].length() > 2)
                                    btArts.setText(selectedArts[0].substring(0, selectedArts[0].length() - 2));
                                else btArts.setText("Arts and Culture");

                                strSelectedArtsIds = "";
                                for(String selectedIds : selectedArtsIds){
                                    strSelectedArtsIds += selectedIds;
                                }
                                if (strSelectedArtsIds.length() > 1)
                                    strSelectedArtsIds = "{"+strSelectedArtsIds.substring(0,strSelectedArtsIds.length()-1)+"}";
                                else strSelectedArtsIds = "{}";
//                                Toast.makeText(getApplicationContext(),strSelectedArtsIds,Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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

    public void relaxationButtonClick(View v){ //Relaxation Button click...................
        try {
            if (relaxationInterests.length > 0){
                final String[] selectedRelaxation = {""};
                final String[] selectedRelaxationIds = {""};
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Relaxation Activities");
                final boolean[] chk = new boolean[relaxationInterests.length];
                String mRelaxation = btRelaxation.getText().toString();

                for(int i=0; i<relaxationInterests.length; i++){
                    chk[i] = false;
                }

                String[] selectedArr = mRelaxation.split(", ");
                for(int i=0; i<selectedArr.length; i++){
                    for(int j=0; j<relaxationInterests.length; j++){
                        if (selectedArr[i].equals(relaxationInterests[j]))
                            chk[j] = true;
                    }
                }

                builder.setMultiChoiceItems(relaxationInterests, chk,
                        new DialogInterface.OnMultiChoiceClickListener() {String mSelectedRelaxation = "";
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                if (isChecked)
                                    chk[item] = true;
                                else chk[item] = false;
                            }
                        });
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < chk.length; i++) {
                                    if (chk[i]) {
                                        selectedRelaxation[0] += relaxationInterests[i] + ", ";
                                        selectedRelaxationIds[0] += relaxationInterestsIds[i] + ",";
                                    }
                                }
                                if (selectedRelaxation[0].length() > 2)
                                    btRelaxation.setText(selectedRelaxation[0].substring(0, selectedRelaxation[0].length() - 2));
                                else btRelaxation.setText("Relaxation");

                                strSelectedRelaxationIds = "";
                                for(String selectedIds : selectedRelaxationIds){
                                    strSelectedRelaxationIds += selectedIds;
                                }
                                if (strSelectedRelaxationIds.length() > 1)
                                    strSelectedRelaxationIds = "{"+strSelectedRelaxationIds.substring(0,strSelectedRelaxationIds.length()-1)+"}";
                                else strSelectedRelaxationIds = "{}";
//                                Toast.makeText(getApplicationContext(),strSelectedRelaxationIds,Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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

    public void childrenButtonClick(View v){ //Children Button click...................
        try {
            if (childrenInterests.length > 0){
                final String[] selectedChildren = {""};
                final String[] selectedChildrenIds = {""};
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Children Activities");
                final boolean[] chk = new boolean[childrenInterests.length];
                String mChildren = btChildren.getText().toString();

                for(int i=0; i<childrenInterests.length; i++){
                    chk[i] = false;
                }

                String[] selectedArr = mChildren.split(", ");
                for(int i=0; i<selectedArr.length; i++){
                    for(int j=0; j<childrenInterests.length; j++){
                        if (selectedArr[i].equals(childrenInterests[j]))
                            chk[j] = true;
                    }
                }

                builder.setMultiChoiceItems(childrenInterests, chk,
                        new DialogInterface.OnMultiChoiceClickListener() {String mSelectedChildren = "";
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                if (isChecked)
                                    chk[item] = true;
                                else chk[item] = false;
                            }
                        });
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < chk.length; i++) {
                                    if (chk[i]) {
                                        selectedChildren[0] += childrenInterests[i] + ", ";
                                        selectedChildrenIds[0] += childrenInterestsIds[i] + ",";
                                    }
                                }
                                if (selectedChildren[0].length() > 2)
                                    btChildren.setText(selectedChildren[0].substring(0, selectedChildren[0].length() - 2));
                                else btChildren.setText("Children");

                                strSelectedChildrenIds = "";
                                for(String selectedIds : selectedChildrenIds){
                                    strSelectedChildrenIds += selectedIds;
                                }
                                if (strSelectedChildrenIds.length() > 1)
                                    strSelectedChildrenIds = "{"+strSelectedChildrenIds.substring(0,strSelectedChildrenIds.length()-1)+"}";
                                else strSelectedChildrenIds = "{}";
//                                Toast.makeText(getApplicationContext(),strSelectedChildrenIds,Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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

    public void possibleIssueButtonClick(View v){ //Possible Issue Button click...................
        try {
            if (allConcerns.length > 0){
                final String[] selectedConcern = {""};
                final String[] selectedConcernIds = {""};
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Possible Issues");
                final boolean[] chk = new boolean[allConcerns.length];
                String mConcern = btPossibleIssues.getText().toString();

                for(int i=0; i<allConcerns.length; i++){
                    chk[i] = false;
                }

                String[] selectedArr = mConcern.split(", ");
                for(int i=0; i<selectedArr.length; i++){
                    for(int j=0; j<allConcerns.length; j++){
                        if (selectedArr[i].equals(allConcerns[j]))
                            chk[j] = true;
                    }
                }

                builder.setMultiChoiceItems(allConcerns, chk,
                        new DialogInterface.OnMultiChoiceClickListener() {String mSelectedConcerns = "";
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                if (isChecked)
                                    chk[item] = true;
                                else chk[item] = false;
                            }
                        });
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < chk.length; i++) {
                                    if (chk[i]) {
                                        selectedConcern[0] += allConcerns[i] + ", ";
                                        selectedConcernIds[0] += allConcernsIds[i] + ",";
                                    }
                                }
                                if (selectedConcern[0].length() > 2)
                                    btPossibleIssues.setText(selectedConcern[0].substring(0, selectedConcern[0].length() - 2));
                                else btPossibleIssues.setText("Possible Issues");

                                strSelectedConcernsIds = "";
                                for(String selectedIds : selectedConcernIds){
                                    strSelectedConcernsIds += selectedIds;
                                }
                                if (strSelectedConcernsIds.length() > 1)
                                    strSelectedConcernsIds = "{"+strSelectedConcernsIds.substring(0,strSelectedConcernsIds.length()-1)+"}";
                                else strSelectedConcernsIds = "{}";
//                                Toast.makeText(getApplicationContext(),strSelectedConcernsIds,Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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

    public void languagesButtonClick(View v){ //Languages Button click...................
        try {
            if (allLanguages.length > 0) {
                final String[] selectedLanguages = {""};
                final String[] selectedLanguagesIds = {""};
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Languages");
                final boolean[] chk = new boolean[allLanguages.length];
                String mLanguage = btLanguages.getText().toString();

                for (int i = 0; i < allLanguages.length; i++) {
                    chk[i] = false;
                }

                String[] selectedArr = mLanguage.split(", ");
                for (int i = 0; i < selectedArr.length; i++) {
                    for (int j = 0; j < allLanguages.length; j++) {
                        if (selectedArr[i].equals(allLanguages[j]))
                            chk[j] = true;
                    }
                }

                builder.setMultiChoiceItems(allLanguages, chk,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            String mSelectedLanguages = "";

                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                if (isChecked)
                                    chk[item] = true;
                                else chk[item] = false;
                            }
                        });
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < chk.length; i++) {
                                    if (chk[i]) {
                                        selectedLanguages[0] += allLanguages[i] + ", ";
                                        selectedLanguagesIds[0] += allLanguagesIds[i] + ",";
                                    }
                                }
                                if (selectedLanguages[0].length() > 2)
                                    btLanguages.setText(selectedLanguages[0].substring(0, selectedLanguages[0].length() - 2));
                                else btLanguages.setText("Languages");

                                strSelectedLanguagesIds = "";
                                for(String selectedIds : selectedLanguagesIds){
                                    strSelectedLanguagesIds += selectedIds;
                                }
                                if (strSelectedLanguagesIds.length() > 1)
                                    strSelectedLanguagesIds = "{"+strSelectedLanguagesIds.substring(0,strSelectedLanguagesIds.length()-1)+"}";
                                else strSelectedLanguagesIds = "{}";
//                                Toast.makeText(getApplicationContext(),strSelectedLanguagesIds,Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(this, "No data connection found, try again", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this,"No data connection found, try again",Toast.LENGTH_SHORT).show();
        }
    }
}
