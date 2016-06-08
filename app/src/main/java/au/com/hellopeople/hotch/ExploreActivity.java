package au.com.hellopeople.hotch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import au.com.hellopeople.hotch.Activity.SellActivity;

public class ExploreActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    RelativeLayout listViewLayout, mapViewLayout, filterViewLayout;
    LinearLayout filterHideButtonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        ListView itemList = (ListView) findViewById(R.id.listView2);
        CustomArrayAdaptor itemsArrayAdaptor = new CustomArrayAdaptor();
        itemList.setAdapter(itemsArrayAdaptor);
        itemList.setOnItemClickListener(ExploreActivity.this);
    }

    public void sellOrShareActivity(View view) {
        Intent intent = new Intent(this, SellActivity.class);
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
    public void byCountry(View view) {

    }
    public void byCategory(View view) {

    }
    public void premiumActivities(View view) {

    }
    public void upcomingEvents(View view) {

    }
    public void newActivities(View view) {

    }
    public void recommendedActivities(View view) {

    }
    public void previousSelections(View view) {

    }
    public void cantFindActivity(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class CustomArrayAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 10;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // TODO Auto-generated method stub.

            if (view == null) {

                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = layoutInflater.inflate(R.layout.explore_activity_list_row,
                        null);

            }

//            TextView medicineNameTextView = (TextView) view
//                    .findViewById(R.id.name_of_medicine_text);
//            medicineNameTextView.setText(patientsArray.get(position)
//                    .getPatient_name());
//
//            TextView prescribedDateTextView = (TextView) view
//                    .findViewById(R.id.prescribed_date_text);
//            prescribedDateTextView.setText(patientsArray.get(position)
//                    .getPatient_mobile());
//
//            TextView whenToTakeTextView = (TextView) view
//                    .findViewById(R.id.when_to_take_text);
//            int surgeryId = Integer.parseInt(patientsArray.get(position)
//                    .getPatient_surgery_id());
//            whenToTakeTextView.setText(StaticData.surgeryList[surgeryId]);

            return view;
        }
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.back_button) {
//            finish();
//        } else if (v.getId() == R.id.use_map_button) {
//            listViewLayout.setVisibility(View.GONE);
//            mapViewLayout.setVisibility(View.VISIBLE);
//        } else if (v.getId() == R.id.filter_hide_button) {
//            Button button = (Button) findViewById(R.id.filter_hide_button);
//            if (filterViewLayout.getVisibility() == View.VISIBLE) {
//                filterViewLayout.setVisibility(View.GONE);
//                button.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.double_down_arrow_icon,0,0);
//            } else {
//                filterViewLayout.setVisibility(View.VISIBLE);
//                button.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.double_up_arrow_icon,0,0);
//            }
//        }
    }
}
