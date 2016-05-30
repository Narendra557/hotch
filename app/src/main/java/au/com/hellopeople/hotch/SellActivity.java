package au.com.hellopeople.hotch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import au.com.hellopeople.hotch.choose_category.CategoryCompleted;
import au.com.hellopeople.hotch.choose_category.ChooseCategoryJSON;
import au.com.hellopeople.hotch.register_offer_services.*;

public class SellActivity extends AppCompatActivity implements CategoryCompleted {

    Button btChooseCategory, btDurationType, btTimeSelection, btPickupOption, btEventType, btPictureActivity, btVideoActivity, btCategories;
    EditText etActivityNameText, etKeywordText, etDuration, etPrice, etShortDesc;

    List<String> categoryList;
    String[] allCategories;
    int[] allCategoriesIds;
    String strSelectedCatIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_activity);

        btChooseCategory = (Button)findViewById(R.id.chooseCategoryButton);
        btDurationType = (Button)findViewById(R.id.durationTypeButton);
        btTimeSelection = (Button)findViewById(R.id.timeSelectionButton);
        btPickupOption = (Button)findViewById(R.id.pickUpOptionButton);
        btEventType = (Button)findViewById(R.id.eventTypeButton);
        btPictureActivity = (Button)findViewById(R.id.picturesButton);
        btVideoActivity = (Button)findViewById(R.id.videosButton);

        etActivityNameText = (EditText)findViewById(R.id.activity_name_text);
        etKeywordText = (EditText)findViewById(R.id.keywordsText);
        etDuration = (EditText)findViewById(R.id.durationText);
        etPrice = (EditText)findViewById(R.id.priceText);
        etShortDesc = (EditText)findViewById(R.id.shortDescText);

        btCategories = (Button)findViewById(R.id.chooseCategoryButton);

        new ChooseCategoryJSON(this).execute();
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

    public void categoryListClicked(View v){
        try {
            if (allCategories.length > 0) {
                final String[] selectedCategories = {""};
                final String[] selectedCategoriesIds = {""};
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Make your selection");
                final boolean[] chk = new boolean[allCategories.length];
                String mCategory = btCategories.getText().toString();

                for (int i = 0; i < allCategories.length; i++) {
                    chk[i] = false;
                }

                String[] selectedArr = mCategory.split(", ");

                for (int i = 0; i < selectedArr.length; i++) {
                    for (int j = 0; j < allCategories.length; j++) {
                        if (selectedArr[i].equals(allCategories[j]))
                            chk[j] = true;
                    }
                }

                builder.setMultiChoiceItems(allCategories, chk,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            String mSelectedCategories = "";

                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                if (isChecked)
                                    chk[item] = true;
                                else chk[item] = false;
                            }
                        });
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < chk.length; i++) {
                                    if (chk[i]) {
                                        selectedCategories[0] += allCategories[i] + ", ";
                                        selectedCategoriesIds[0] += allCategoriesIds[i] + ",";
                                    }
                                }
                                if (selectedCategories[0].length() > 2)
                                    btCategories.setText(selectedCategories[0].substring(0, selectedCategories[0].length() - 2));
                                else btCategories.setText("Categories");
                                //                        ListView list = ((android.app.AlertDialog) dialog).getListView();
                                //ListView has boolean array like {1=true, 3=true}, that shows checked items

                                strSelectedCatIds = "";
                                for(String selectedIds : selectedCategoriesIds){
                                    strSelectedCatIds += selectedIds;
                                }
                                if (strSelectedCatIds.length() > 1)
                                    strSelectedCatIds = "{"+strSelectedCatIds.substring(0,strSelectedCatIds.length()-1)+"}";
                                else strSelectedCatIds = "{}";
//                                Toast.makeText(getApplicationContext(),strSelectedCatIds,Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //                        ((TextView) myFilesActivity.findViewById(R.id.text)).setText("Click here to open Dialog");
                            }
                        });
                //        builder.setItems(allCategories, new DialogInterface.OnClickListener() {
                //            public void onClick(DialogInterface dialog, int item) {
                //                btCategories.setText(allCategories[item]);
                //            }
                //        });

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
        builder.setTitle("Select the event type");
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
