package au.com.hellopeople.hotch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        int []onclickButtonIds = {R.id.back_button, R.id.add_activity_button, R.id.my_activities_button, R.id.my_profile_button, R.id.business_profile_button, R.id.my_bookings_button, R.id.settings_button};
        for (int i = 0; i < onclickButtonIds.length; i++) {
            Button button = (Button) findViewById(onclickButtonIds[i]);
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_button) {
            finish();
        } else if (v.getId() == R.id.add_activity_button) {
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.my_activities_button) {
            Intent intent = new Intent(this, MyActivitiesActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.my_profile_button) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.business_profile_button) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.my_bookings_button) {
            Intent intent = new Intent(this, MyBookingsActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.settings_button) {
//            Intent intent = new Intent(this, AddActivity.class);
//            startActivity(intent);
        }
    }
}
