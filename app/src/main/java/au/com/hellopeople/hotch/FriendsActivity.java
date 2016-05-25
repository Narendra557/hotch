package au.com.hellopeople.hotch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class FriendsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        ListView itemList = (ListView) findViewById(R.id.listView2);
        CustomArrayAdaptor itemsArrayAdaptor = new CustomArrayAdaptor();
        itemList.setAdapter(itemsArrayAdaptor);
        itemList.setOnItemClickListener(FriendsActivity.this);
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

    public void exploreActivitiesActivity(View view) {
        Intent intent = new Intent(this, ExploreActivity.class);
        startActivity(intent);
        finish();
    }
    public void myProfileActivity(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        System.out.println("position "+position);
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

                view = layoutInflater.inflate(R.layout.friends_list_row,
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
}
