package au.com.hellopeople.hotch.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Danushka on 4/28/2016.
 */
public class Country {
    private String country_id;
    private String country_name;

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public Country() {
    }

    public Country(JSONObject jsonObject) {
        try {
            this.country_id = jsonObject.getString("country_id");
            this.country_name = jsonObject.getString("country_name");
        } catch (JSONException e) {

        }
    }

    public Country(String country_id, String country_name) {
        this.country_id = country_id;
        this.country_name = country_name;
    }
}
