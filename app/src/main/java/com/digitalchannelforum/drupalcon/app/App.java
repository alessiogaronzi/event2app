package com.digitalchannelforum.drupalcon.app;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.digitalchannelforum.sharedpreferences.MyPreferences;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import com.crashlytics.android.Crashlytics;
import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.drupalcon.R;
import com.digitalchannelforum.drupalcon.model.AppDatabaseInfo;
import com.digitalchannelforum.drupalcon.model.Model;
import com.digitalchannelforum.drupalcon.model.database.LAPIDBRegister;
import com.digitalchannelforum.http.base.BaseRequest;
import com.digitalchannelforum.util.image.DrupalImageView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import android.content.Context;
import androidx.multidex.MultiDexApplication;

import io.fabric.sdk.android.Fabric;

public class App extends MultiDexApplication {

    public static Context mContext;
    public static RequestQueue requestQueue;
    public static MyPreferences myPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
            TwitterAuthConfig authConfig = new TwitterAuthConfig(
                    getString(R.string.api_value_twitter_api_key),
                    getString(R.string.api_value_twitter_secret));

        mContext = getApplicationContext();
        myPreferences = new MyPreferences(this);

        LAPIDBRegister.getInstance().register(mContext, new AppDatabaseInfo(mContext));
        Model.instance(mContext);
        requestQueue = Model.instance().createNewQueue(getApplicationContext());
        DrupalImageView.setupSharedClient(new DrupalClient(null, requestQueue, BaseRequest.RequestFormat.JSON, null));
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
    }
    public static <T> void addToRequestQueue(Request<T> req) {
        requestQueue.add(req);
    }
    public static Context getContext() {
        return mContext;
    }

    public synchronized Tracker getTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        return analytics.newTracker(R.xml.global_tracker);
    }
}
