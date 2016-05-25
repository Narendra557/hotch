package au.com.hellopeople.hotch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class UpcomingActivitiesActivity extends Activity {

    private ListView upcomingList;
    private ListView watchingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_activities);

        upcomingList = (ListView) findViewById(R.id.listView2);
        watchingList = (ListView) findViewById(R.id.listView3);

        ScrollView mainScrollView = (ScrollView) findViewById(R.id.scrollView6);
        mainScrollView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // Log.v("PARENT", "PARENT TOUCH");
                findViewById(R.id.listView2).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        upcomingList.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // Log.v("CHILD", "CHILD TOUCH");
                // Disallow the touch request for parent scroll on touch of
                // child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        watchingList.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // Log.v("CHILD", "CHILD TOUCH");
                // Disallow the touch request for parent scroll on touch of
                // child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        CustomArrayAdaptor itemsArrayAdaptor = new CustomArrayAdaptor();
        upcomingList.setAdapter(itemsArrayAdaptor);
        upcomingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("positionssss "+position);
            }
        });

        CustomArrayAdaptor1 itemsArrayAdaptor1 = new CustomArrayAdaptor1();
        watchingList.setAdapter(itemsArrayAdaptor1);
        watchingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("positionaaaa "+position);
            }
        });
    }

    public void sellOrShareActivity(View view) {
        Intent intent = new Intent(this, SellActivity.class);
        startActivity(intent);
        finish();
    }

    public void exploreActivitiesActivity(View view) {
        Intent intent = new Intent(this, ExploreActivity.class);
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

                view = layoutInflater.inflate(R.layout.upcoming_activities_list_row,
                        null);

            }
            TextView editButton = (TextView) view
                    .findViewById(R.id.activity_name);
            editButton.setOnClickListener(viewClickedListener);
            TextView editButton1 = (TextView) view
                    .findViewById(R.id.activity_location);
            editButton1.setOnClickListener(viewClickedListener);
            TextView editButton2 = (TextView) view
                    .findViewById(R.id.datetime);
            editButton2.setOnClickListener(viewClickedListener);
            TextView editButton3 = (TextView) view
                    .findViewById(R.id.details);
            editButton3.setOnClickListener(viewClickedListener);


//            LinearLayout editButton4 = (LinearLayout) view
//                    .findViewById(R.id.lay_1);
//            editButton4.setOnClickListener(viewClickedListener);
//            LinearLayout editButton5 = (LinearLayout) view
//                    .findViewById(R.id.lay_2);
//            editButton5.setOnClickListener(viewClickedListener);
//            LinearLayout editButton6 = (LinearLayout) view
//                    .findViewById(R.id.lay_3);
//            editButton6.setOnClickListener(viewClickedListener);
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

    public View.OnClickListener viewClickedListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            final int position = upcomingList
                    .getPositionForView((View) v.getParent());

            System.out.println("position "+position);

//            StaticData.selectedItemsArray.remove(position);
//            itemsArrayAdaptor.notifyDataSetChanged();
//            list_v.setAdapter(itemsArrayAdaptor);
//            list_v.setOnItemClickListener(MyOrderActivity.this);
//            totalPrice();
        }
    };

    private class CustomArrayAdaptor1 extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {

                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = layoutInflater.inflate(R.layout.explore_activity_list_row,
                        null);

            }
            return view;
        }
    }
}
