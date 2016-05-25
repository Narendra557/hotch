package au.com.hellopeople.hotch.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Danushka on 4/28/2016.
 */
public class Currency {

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    private String currency_id;
    private String currency_name;
    private String currency_code;
    private String country_id;

    public Currency() {
        super();
    }

    public Currency(JSONObject jsonObject) {
        try {
            this.currency_id = jsonObject.getString("currency_id");
            this.currency_name = jsonObject.getString("currency_name");
            this.currency_code = jsonObject.getString("currency_code");
            this.country_id = jsonObject.getString("country_id");
        } catch (JSONException e) {

        }
    }

    public Currency(String currency_id, String currency_name, String currency_code, String country_id) {
        this.currency_id = currency_id;
        this.currency_name = currency_name;
        this.currency_code = currency_code;
        this.country_id = country_id;
    }
}
