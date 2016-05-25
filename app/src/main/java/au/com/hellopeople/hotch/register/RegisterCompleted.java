package au.com.hellopeople.hotch.register;

import java.util.List;

/**
 * Created by Dell on 4/5/2016.
 */
public interface RegisterCompleted {
    // Define data you like to return from AysncTask
    // When register data inserted................
    public void onTaskComplete(String result);

    // When password data inserted.....
    public void onTaskComplete2(String result);

    public void onMainArrComplete(int result);

    public void onSubArrComplete(int result);

    void onSubArrCompleteAll(String result[]);

    void onSubWiseArrComplete(int result[]);

    void onSubIdArrComplete(int result[]);
}
