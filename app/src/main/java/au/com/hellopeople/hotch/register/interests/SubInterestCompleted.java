package au.com.hellopeople.hotch.register.interests;

/**
 * Created by Dell on 5/18/2016.
 */
public interface SubInterestCompleted {

    void sportingCompleted(String[] sportingInterests, String[] sportingInterestsIds);
    void gymCompleted(String[] gymInterests, String[] gymInterestsIds);
    void artsCompleted(String[] artsInterests, String[] artsInterestsIds);
    void relaxationCompleted(String[] relaxationInterests, String[] relaxationInterestsIds);
    void childrenCompleted(String[] childrenInterests, String[] childrenInterestsIds);

}
