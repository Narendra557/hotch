package au.com.hellopeople.hotch;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import au.com.hellopeople.hotch.Activity.PostingOptionCompleted;
import au.com.hellopeople.hotch.Activity.PostingOptionJSON;
import au.com.hellopeople.hotch.Activity.SellOrShareActivity;
import au.com.hellopeople.hotch.choose_category.CategoryCompleted;
import au.com.hellopeople.hotch.choose_category.ChooseCategoryJSON;

public class SellActivity extends AppCompatActivity implements CategoryCompleted, PostingOptionCompleted {

    Button btChooseCategory, btDurationType, btTimeSelection, btPickupOption, btEventType, btPictureActivity, btVideoActivity, btCategories, btPostingOptions, btPickMyLocation;
    EditText etActivityNameText, etKeywordText, etDuration, etPrice, etShortDesc, etAddress;

    List<String> categoryList;
    String[] allCategories, allPostingOption, allPostingOptionPrices;
    int[] allCategoriesIds, allPostingOptionIds;
    String strSelectedCatIds, activityName, keywords, durationType, timeSelection, eventType, price, shortDesc, activityPictures, activityVideos, postingOption, poPrice;
    int personId, categoryId, duration, pickupOption, poId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_activity);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        personId = pref.getInt("personIdKey", 0);


        btChooseCategory = (Button)findViewById(R.id.chooseCategoryButton);
        btDurationType = (Button)findViewById(R.id.durationTypeButton);
        btTimeSelection = (Button)findViewById(R.id.timeSelectionButton);
        btPickupOption = (Button)findViewById(R.id.pickUpOptionButton);
        btEventType = (Button)findViewById(R.id.eventTypeButton);
        btPictureActivity = (Button)findViewById(R.id.picturesButton);
        btVideoActivity = (Button)findViewById(R.id.videosButton);
        btPostingOptions = (Button)findViewById(R.id.postingOptionsButton);
        btPickMyLocation = (Button)findViewById(R.id.pickCurrentLocationButton);

        etActivityNameText = (EditText)findViewById(R.id.activityNameText);
        etKeywordText = (EditText)findViewById(R.id.keywordsText);
        etDuration = (EditText)findViewById(R.id.durationText);
        etPrice = (EditText)findViewById(R.id.priceText);
        etShortDesc = (EditText)findViewById(R.id.shortDescText);
        etAddress = (EditText)findViewById(R.id.activityAddressText);

        btCategories = (Button)findViewById(R.id.chooseCategoryButton);

        new ChooseCategoryJSON(this).execute();
        new PostingOptionJSON(this).execute();
    }

    public void myProfile(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAllCategoryListCompleted(String[] mAllCategories, int[] mAllCategoriesIds) {
        allCategories = mAllCategories;
        allCategoriesIds = mAllCategoriesIds;
    }

    @Override
    public void onAllPOCompleted(String[] mAllPostingOptions, int[] mAllPostingOptionIds, String[] mAllPostingOptionPrices) {
        allPostingOption = mAllPostingOptions;
        allPostingOptionIds = mAllPostingOptionIds;
        allPostingOptionPrices = mAllPostingOptionPrices;
    }

    public void categoryListClicked(View v){
        try {
            if (allCategories.length > 0) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Make your selection");
                builder.setItems(allCategories, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        btCategories.setText(allCategories[item]);
                        categoryId = allCategoriesIds[item];
                    }
                });

                android.app.AlertDialog alert = builder.create();
                alert.show();
            }else {
                Toast.makeText(this,"No data connection found, try again", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this,"No data connection found, try again",Toast.LENGTH_SHORT).show();
        }
    }

    public void durationClick(View view){
        final CharSequence[] items = {"Hours", "Days"};
        int selectedItem = 0;
        if (btDurationType.getText().equals("Hours"))
            selectedItem = 0;
        else
            selectedItem = 1;

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select the duration");
        builder.setSingleChoiceItems(items,selectedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        btDurationType.setText("Hours");
                        break;
                    case 1:
                        btDurationType.setText("Days");
                        break;
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

//    @Override
//    public void onAllCategoryListCompleted(String[] allCategories, int[] allCategoriesIds) {
//        this.allCategories = allCategories;
//        this.allCategoriesIds = allCategoriesIds;
//    }

//    @Override
//    public void onAllCategoryListCompleted_Ids(int[] allCategoriesIds) {
////        this.allCategoriesIds = allCategoriesIds;
//    }

    public void eventTypeClick(View v){
        final CharSequence[] items = {"Individual", "Group"};
        int selectedItem = 0;
        if (btEventType.getText().equals("Individual"))
            selectedItem = 0;
        else
            selectedItem = 1;

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select the Event Type");
        builder.setSingleChoiceItems(items,selectedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        btEventType.setText("Individual");
                        break;
                    case 1:
                        btEventType.setText("Group");
                        break;
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Select event type");
//        builder.setItems(allCategories, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//                btEventType.setText(allCategories[item]);
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
    }

    public void pickupOptionClick(View view){
        final CharSequence[] items = {"Yes", "No"};
        int selectedItem = 0;
        String x = btPickupOption.getText().toString();
        if (btPickupOption.getText().toString().equals("Yes"))
            selectedItem = 0;
        else
            selectedItem = 1;

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select the Pickup Option");
        builder.setSingleChoiceItems(items,selectedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        btPickupOption.setText("Yes");
                        break;
                    case 1:
                        btPickupOption.setText("No");
                        break;
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public void postingOptionClick(View v){
        try {
            if (allPostingOption.length > 0) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Make your selection");
                builder.setItems(allPostingOption, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        btPostingOptions.setText(allPostingOption[item]);
                        poId = allPostingOptionIds[item];
                        poPrice = allPostingOptionPrices[item];
                    }
                });

                android.app.AlertDialog alert = builder.create();
                alert.show();
            }else {
                Toast.makeText(this,"No data connection found, try again", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this,"No data connection found, try again",Toast.LENGTH_SHORT).show();
        }
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

    public void postActivity(View v){

        activityName = etActivityNameText.getText().toString();
        keywords = etKeywordText.getText().toString();

        if (btPickupOption.getText().toString().equals("Yes")) pickupOption = 1;
        else pickupOption = 0;
        eventType = btEventType.getText().toString();
        price = etPrice.getText().toString();

        new SellOrShareActivity(this).execute(String.valueOf(personId),String.valueOf(categoryId),activityName,keywords,String.valueOf(pickupOption),eventType,price);


    }

    public void pickMyLocationClick(View view){
        if (btPickMyLocation.getText().toString().equals("Pick my current location")){
            btPickMyLocation.setText("Do not pick my location");
            btPickMyLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_deselect_grey, 0, 0, 0);
            etAddress.setEnabled(true);
        } else {
            btPickMyLocation.setText("Pick my current location");
            btPickMyLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.remember_me_select_grey, 0, 0, 0);
            etAddress.setEnabled(false);
        }
    }

}
