package au.com.hellopeople.hotch.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Danushka on 4/28/2016.
 */
public class AgeGroup {

    private String age_group_id;
    private String age_group_value;

    public String getAge_group_value() {
        return age_group_value;
    }

    public void setAge_group_value(String age_group_value) {
        this.age_group_value = age_group_value;
    }

    public String getAge_group_id() {
        return age_group_id;
    }

    public void setAge_group_id(String age_group_id) {
        this.age_group_id = age_group_id;
    }

    public AgeGroup() {super();}

    public AgeGroup(JSONObject jsonObject) {
        try {
            this.age_group_id = jsonObject.getString("age_group_id");
            this.age_group_value = jsonObject.getString("age_group_value");
        } catch (JSONException e) {

        }
    }

    public AgeGroup(String age_group_id, String age_group_value) {
        this.age_group_id = age_group_id;
        this.age_group_value = age_group_value;
    }
}
