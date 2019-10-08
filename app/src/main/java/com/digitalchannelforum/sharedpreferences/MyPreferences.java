package com.digitalchannelforum.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lorenzo on 08/10/2018 18.36.
 * Email: l.sogliani@gmail.com
 */
public class MyPreferences {
    public static final String MyPREFERENCES = "MY_SHAREDPREFERENCES";
    public static final String LAST_GET_INFO = "LAST_GET_INFO";


    SharedPreferences sharedpreferences;
    Context context = null;

    public MyPreferences(Context c){
        this.context = c;
    }
    public SharedPreferences getSharedPrefs() {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        return sharedpreferences;
    }
    public SharedPreferences.Editor getSharedPrefsEditor() {
        SharedPreferences.Editor editor = this.getSharedPrefs().edit();
        return editor;
    }

    public void setLastGetInfo(String last){
        SharedPreferences.Editor editor = getSharedPrefsEditor();
        editor.putString(LAST_GET_INFO,last);
        editor.commit();
    }
    public String getLastGetInfo(){
        return getSharedPrefs().getString(LAST_GET_INFO,null);
    }


}
