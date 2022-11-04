package com.example.cmput301f22t13.uilayer.userlogin;

/** Interface used to validate if a particular method performed in the UI segment passes or fails
 * */

public interface ResultListener {
    void onSuccess();
    void onFailure(Exception e);
}
