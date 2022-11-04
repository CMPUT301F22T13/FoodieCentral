package com.example.cmput301f22t13.uilayer.userlogin;

/** Interface used to validate if a particular method performed in the UI segment passes or fails
 * */

public interface ResultListener {
    /** method to be overridden and fired on successful firebase operation
     *  */
    void onSuccess();

    /** method to be overridden and fired on unsuccessful firebase operation
     *  */
    void onFailure(Exception e);
}
