package au.com.hellopeople.hotch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int []onclickButtonIds = {R.id.settings_button, R.id.profile_button, R.id.explore_button, R.id.about_us_button, R.id.contact_us_button};
        for (int i = 0; i < onclickButtonIds.length; i++) {
            Button button = (Button) findViewById(onclickButtonIds[i]);
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.settings_button) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.profile_button) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.explore_button) {
            Intent intent = new Intent(this, ExploreActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.about_us_button) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.contact_us_button) {
            Intent intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
        }
    }
}
