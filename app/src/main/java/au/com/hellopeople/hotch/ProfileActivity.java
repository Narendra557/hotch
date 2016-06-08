package au.com.hellopeople.hotch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

import au.com.hellopeople.hotch.Activity.SellActivity;

public class ProfileActivity extends Activity implements View.OnClickListener {

    EditText etFirstName, etLastName, etUserName, etMessage;
    ImageView ivProfileImage;
    Bitmap bitmap;
    ProgressDialog pDialog;
    Button btPhotoGallery, btSettings, btAboutUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etFirstName = (EditText) this.findViewById(R.id.firstNameText);
        etLastName = (EditText) this.findViewById(R.id.lastNameText);
        etUserName = (EditText) this.findViewById(R.id.usernameText);
        etMessage = (EditText) this.findViewById(R.id.personMessageText);
        ivProfileImage = (ImageView) findViewById(R.id.imageView2);

        btPhotoGallery = (Button) findViewById(R.id.textView75);
        btSettings = (Button) findViewById(R.id.textView72);
        btAboutUs = (Button) findViewById(R.id.textView76);

        Intent intent = getIntent();
        String firstName = intent.getStringExtra("first_name");
        String lastName = intent.getStringExtra("last_name");
        String userName = intent.getStringExtra("user_name");
        String message = intent.getStringExtra("message");
        String imagePath = intent.getStringExtra("profile_photo");
        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etUserName.setText(userName);
        etMessage.setText(message);
        imagePath = "http://www.myhotch.com/app_control/profile_pics/"+imagePath;
        new LoadImage().execute(imagePath);
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();
        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if(image != null){
                ivProfileImage.setImageBitmap(image);
                pDialog.dismiss();
            }else{
                pDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openPhotoGalleryClick(View v){
        Intent intent = new Intent(this, PhotoGalleryActivity.class);
        startActivity(intent);
    }
    public void openSettingsClick(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void openAboutUsClick(View v){
        Intent intent = new Intent(this, AboutUsActivity.class);
        startActivity(intent);
    }

    public void sellOrShareActivity(View view) {
        Intent intent = new Intent(this, SellActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {

    }
}
