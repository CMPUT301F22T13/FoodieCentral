package com.example.cmput301f22t13;

import android.app.Application;

import com.example.cmput301f22t13.datalayer.FireBaseDL;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
       // getFirebaseDL();
    }


//    static private FireBaseDL firebaseDL;
//    public FireBaseDL getFirebaseDL(){
//        if(firebaseDL==null){
//            firebaseDL = new FireBaseDL();
//        }
//        return firebaseDL;
//    }
}
