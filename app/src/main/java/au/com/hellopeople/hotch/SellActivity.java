package au.com.hellopeople.hotch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import au.com.hellopeople.hotch.register_offer_services.*;

public class SellActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_activity);

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
}
