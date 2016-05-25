package au.com.hellopeople.hotch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class RegistertoOfferServicesActivity extends Activity implements View.OnClickListener {

    RelativeLayout yourInfoLayout, contactInfoLayout, paymentInfoLayout, businessInfoLayout, yourInformationContent, contactInformationContent, paymentInformationContent, businessInformationContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerto_offer_services);

        yourInfoLayout = (RelativeLayout) findViewById(R.id.your_info_layout);
        contactInfoLayout = (RelativeLayout) findViewById(R.id.contact_info_layout);
        paymentInfoLayout = (RelativeLayout) findViewById(R.id.payment_info_layout);
        businessInfoLayout = (RelativeLayout) findViewById(R.id.business_info_layout);
        yourInformationContent = (RelativeLayout) findViewById(R.id.your_information_content);
        contactInformationContent = (RelativeLayout) findViewById(R.id.contact_information_content);
        paymentInformationContent = (RelativeLayout) findViewById(R.id.payment_information_content);
        businessInformationContent = (RelativeLayout) findViewById(R.id.business_information_content);

        int []onclickButtonIds = {R.id.top_button_1, R.id.top_button_2, R.id.top_button_3, R.id.top_button_4, R.id.cancel_button, R.id.register_button};
        for (int i = 0; i < onclickButtonIds.length; i++) {
            Button button = (Button) findViewById(onclickButtonIds[i]);
            button.setOnClickListener(this);
        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.top_button_1) {
            yourInfoLayout.setVisibility(View.VISIBLE);
            yourInformationContent.setVisibility(View.VISIBLE);
            contactInfoLayout.setVisibility(View.GONE);
            contactInformationContent.setVisibility(View.GONE);
            paymentInfoLayout.setVisibility(View.GONE);
            paymentInformationContent.setVisibility(View.GONE);
            businessInfoLayout.setVisibility(View.GONE);
            businessInformationContent.setVisibility(View.GONE);
        } else if (v.getId() == R.id.top_button_2) {
            yourInfoLayout.setVisibility(View.GONE);
            yourInformationContent.setVisibility(View.GONE);
            contactInfoLayout.setVisibility(View.VISIBLE);
            contactInformationContent.setVisibility(View.VISIBLE);
            paymentInfoLayout.setVisibility(View.GONE);
            paymentInformationContent.setVisibility(View.GONE);
            businessInfoLayout.setVisibility(View.GONE);
            businessInformationContent.setVisibility(View.GONE);
        } else if (v.getId() == R.id.top_button_3) {
            yourInfoLayout.setVisibility(View.GONE);
            yourInformationContent.setVisibility(View.GONE);
            contactInfoLayout.setVisibility(View.GONE);
            contactInformationContent.setVisibility(View.GONE);
            paymentInfoLayout.setVisibility(View.VISIBLE);
            paymentInformationContent.setVisibility(View.VISIBLE);
            businessInfoLayout.setVisibility(View.GONE);
            businessInformationContent.setVisibility(View.GONE);
        } else if (v.getId() == R.id.top_button_4) {
            yourInfoLayout.setVisibility(View.GONE);
            yourInformationContent.setVisibility(View.GONE);
            contactInfoLayout.setVisibility(View.GONE);
            contactInformationContent.setVisibility(View.GONE);
            paymentInfoLayout.setVisibility(View.GONE);
            paymentInformationContent.setVisibility(View.GONE);
            businessInfoLayout.setVisibility(View.VISIBLE);
            businessInformationContent.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.register_button) {
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.cancel_button) {
            finish();
        } else if (v.getId() == R.id.top_button_2) {

        } else if (v.getId() == R.id.top_button_2) {

        } else if (v.getId() == R.id.top_button_2) {

        }
    }
}
