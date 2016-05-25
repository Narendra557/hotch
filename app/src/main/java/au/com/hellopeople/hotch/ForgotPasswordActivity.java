package au.com.hellopeople.hotch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import au.com.hellopeople.hotch.util.HttpConnectionManager;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }
    public void submitEmail(View view) {
        EditText emailText = (EditText) findViewById(R.id.email_text);
        String email = emailText.getText().toString().trim();

        if (email.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter the e-mail address!",
                    Toast.LENGTH_LONG).show();
        } else {
            if (checkInternetConnection()) {
                verifyPatient(email);
            } else {
                Toast.makeText(this,
                        "Please check your internet connection!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void verifyPatient(final String email) {

        class VerifyPatientAsyncTask extends AsyncTask<Void, Void, String> {

            ProgressDialog dialog = ProgressDialog.show(
                    ForgotPasswordActivity.this, "", "", true);

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                dialog.cancel();
                if (result != null) {
                    try {
                        JSONObject items = new JSONObject(result);
                        int value = items.getInt("value");
                        if (value == 0) {
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Please try again!", Toast.LENGTH_LONG).show();
                        } else {
                            JSONArray patientArray = items
                                    .getJSONArray("person");
                            JSONObject patientJsonObject = patientArray
                                    .getJSONObject(0);

                            sendingMail(patientJsonObject.getString("email"),
                                    patientJsonObject.getString("password"));

                            EditText email = (EditText) findViewById(R.id.email_text);
                            email.setText("");
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
                            "http://www.myhotch.com/app_control/select_person.php?email="
                                    + email);

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

        VerifyPatientAsyncTask verifyPatientAsyncTask = new VerifyPatientAsyncTask();
        verifyPatientAsyncTask.execute((Void) null);
    }

    public void sendingMail(final String email, final String password) {

        class SendingMailAsyncTask extends AsyncTask<Void, Void, String> {

            ProgressDialog dialog = ProgressDialog.show(
                    ForgotPasswordActivity.this, "", "", true);

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                dialog.cancel();
                if (result != null) {
                    try {
                        JSONObject items = new JSONObject(result);
                        int value = items.getInt("value");
                        if (value == 0) {
                            Toast.makeText(
                                    ForgotPasswordActivity.this,
                                    "Message sending failed, please try again!",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Please check your e-mail!", Toast.LENGTH_LONG).show();
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

                String subject = "Login details for Hotch mobile application";
                String fullMessage = "\nWelcome to Hotch.\nPlease use the given password to login to your Hotch application.\nPassword : "
                        + password;
                try {
                    HttpGet httpGet = new HttpGet(
                            "http://www.myhotch.com/app_control/email/mailcustomer.php?email="
                                    + email + "&subject=" + Uri.encode(subject)
                                    + "&msg=" + Uri.encode(fullMessage));

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

        SendingMailAsyncTask sendingMailAsyncTask = new SendingMailAsyncTask();
        sendingMailAsyncTask.execute((Void) null);
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

}
