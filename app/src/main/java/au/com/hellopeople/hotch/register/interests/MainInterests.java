package au.com.hellopeople.hotch.register.interests;

import java.util.ArrayList;

/**
 * Created by Dell on 4/8/2016.
 */
public class MainInterests {
    private String mainInterests;
    // ArrayList to store child objects
    private ArrayList<SubInterests> subInterests;

    public MainInterests(String mainInterests){
        this.mainInterests = mainInterests;
    }

    public String getMainInterests() {
        return mainInterests;
    }

    public void setMainInterests(String mainInterests) {
        this.mainInterests = mainInterests;
    }

    // ArrayList to store child objects
    public ArrayList<SubInterests> getSub()
    {
        return subInterests;
    }

    public void setSub(ArrayList<SubInterests> subInterests)
    {
        this.subInterests = subInterests;
    }
}
