package au.com.hellopeople.hotch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import au.com.hellopeople.hotch.login.LoginActivity;
import au.com.hellopeople.hotch.util.SmartImageView;
import au.com.hellopeople.hotch.util.WebImage;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SmartImageView splashImage = (SmartImageView) findViewById(R.id.imageView);
        splashImage
                .setImageUrl("http://www.myhotch.com/app_control/images/splash.jpg");

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        String dateValue = prefs.getString("Hotch_date_value", "No");

        if (dateValue.equalsIgnoreCase("No")) {
            final Calendar c = Calendar.getInstance();
            Date date1 = c.getTime();
            String dateValue1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").format(date1);
            SharedPreferences.Editor ed = prefs.edit();
            ed.putString("Hotch_date_value", dateValue1);
            ed.commit();
        } else {
            try {
                Date dateValue2 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").parse(dateValue);
                final Calendar c = Calendar.getInstance();
                Date date1 = c.getTime();
                String currentDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").format(date1);
                Date dateValue1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").parse(currentDate);

                long millse = dateValue1.getTime() - dateValue2.getTime();
                long mills = Math.abs(millse);

                int Hours = (int) (mills/(1000 * 60 * 60));

                if (Hours > 24) {
                    SharedPreferences.Editor ed = prefs.edit();
                    ed.putString("Hotch_date_value", currentDate);
                    ed.commit();

                    WebImage webImage = new WebImage("http://www.myhotch.com/app_control/images/splash.jpg");
                    webImage.removeFromCache("http://www.myhotch.com/app_control/images/splash.jpg");

                    SmartImageView splashImage1 = (SmartImageView) findViewById(R.id.imageView);
                    splashImage1
                            .setImageUrl("http://www.myhotch.com/app_control/images/splash.jpg");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//        finish();
    }
}
