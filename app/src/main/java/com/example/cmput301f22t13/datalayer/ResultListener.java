package com.example.cmput301f22t13.datalayer;

public interface ResultListener {
    void onSuccess();
    void onFailure(Exception e);
}
