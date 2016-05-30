package au.com.hellopeople.hotch.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import au.com.hellopeople.hotch.ForgotPasswordActivity;
import au.com.hellopeople.hotch.ProfileActivity;
import au.com.hellopeople.hotch.R;
import au.com.hellopeople.hotch.SellActivity;
import au.com.hellopeople.hotch.register.RegisterActivity;
import au.com.hellopeople.hotch.register_offer_services.RegistertoOfferServicesActivity;

public class LoginActivity extends Activity implements View.OnClickListener, LoginCompleted {
    Button btRegister, btLogin, btRememberMe;
    EditText etUserName, etPassword;
    public static final String MyPREF = "MyPref" ;
    public static final String rememberKey = "rememberKey";
    SharedPreferences sharedpreferences;
    int isRemember = 0;
    String userName, password, emailPattern;
    public int personTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btRegister = (Button) findViewById(R.id.register_button);
        btLogin = (Button) findViewById(R.id.login_button);
//        btRememberMe = (Button) findViewById(R.id.login_button);
        etUserName = (EditText) findViewById(R.id.email_text);
        etPassword = (EditText) findViewById(R.id.password_text);
    }

    @Override
    public void onLoginCompleted(String mFirstName, String mLastName, String mUserName, String mMessage, String mProfilePhoto) {
//        if (result.equals("SUCCESS")){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("first_name", mFirstName);
        intent.putExtra("last_name", mLastName);
        intent.putExtra("user_name", mUserName);
        intent.putExtra("message", mMessage);
        intent.putExtra("profile_photo", mProfilePhoto);
        this.finish();
        startActivity(intent);
//        }
    }

    public void openRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
//        this.finish();
        personTypeId = 1;
        intent.putExtra("person_type", personTypeId);
        startActivity(intent);
    }

    public void forgotPassword(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
//        this.finish();
        personTypeId = 1;
        intent.putExtra("person_type", personTypeId);
        startActivity(intent);
    }

    public void openRegisterOfferServices(View v){
        Intent intent = new Intent(this, RegistertoOfferServicesActivity.class);
        personTypeId = 2;
        intent.putExtra("person_type", personTypeId);
        startActivity(intent);
    }

    public void loginValidation(View view) {
        userName = etUserName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (userName.equals(null) || userName.equals("")){
            Toast.makeText(getApplicationContext(), "Please enter your email address", Toast.LENGTH_SHORT).show();
            etUserName.requestFocus();
        } else if (password.equals(null) || password.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter your password", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
        }
        else if (userName.matches(emailPattern)) {
            new LoginValidation(this).execute(userName, password);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid email address or password", Toast.LENGTH_SHORT).show();
            etUserName.requestFocus();
        }

    }

    public void rememberMe(View v){
        sharedpreferences = getSharedPreferences(MyPREF, Context.MODE_PRIVATE);
        isRemember = sharedpreferences.getInt(rememberKey,0);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (isRemember == 1) {
            editor.putInt(rememberKey, 0);
            editor.commit();
            btRememberMe.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_deselect, 0, 0, 0);
//            Toast.makeText(this, String.valueOf(isRemember),Toast.LENGTH_SHORT).show();
        }
        else {
            editor.putInt(rememberKey, 1);
            editor.commit();
            btRememberMe.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_select, 0, 0, 0);
//            Toast.makeText(this, String.valueOf(isRemember),Toast.LENGTH_SHORT).show();
        }
//        editor.commit();
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.login_button) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        } else if (v.getId() == R.id.participate_button) {
//            Intent intent = new Intent(this, RegisterActivity.class);
//            startActivity(intent);
//        } else if (v.getId() == R.id.offer_services_button) {
//            Intent intent = new Intent(this, RegistertoOfferServicesActivity.class);
//            startActivity(intent);
//        }
    }

    public void sellOrShareClicked(View v){
        Intent intent = new Intent(this, SellActivity.class);
            startActivity(intent);
    }
}
