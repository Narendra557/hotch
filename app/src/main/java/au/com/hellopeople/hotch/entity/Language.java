package au.com.hellopeople.hotch.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Danushka on 4/28/2016.
 */
public class Language {
    private String language_id;
    private String language_name;

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public String getLanguage_name() {
        return language_name;
    }

    public void setLanguage_name(String language_name) {
        this.language_name = language_name;
    }

    public Language() {
    }

    public Language(JSONObject jsonObject) {
        try {
            this.language_id = jsonObject.getString("language_id");
            this.language_name = jsonObject.getString("language_name");
        } catch (JSONException e) {

        }
    }

    public Language(String language_id, String language_name) {
        this.language_id = language_id;
        this.language_name = language_name;
    }
}
