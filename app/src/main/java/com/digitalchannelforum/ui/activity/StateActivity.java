package com.digitalchannelforum.ui.activity;

import com.digitalchannelforum.drupalcon.model.Model;
import com.digitalchannelforum.drupalcon.model.UpdatesManager;

import android.content.ComponentCallbacks2;
import androidx.appcompat.app.AppCompatActivity;

public class StateActivity extends AppCompatActivity {

    private static boolean wasInBackground = false;

    @Override
    public void onTrimMemory(int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            wasInBackground = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasInBackground) {
            wasInBackground = false;
            checkForUpdates();
        }
    }

    private void checkForUpdates() {
        UpdatesManager manager = Model.instance().getUpdatesManager();
        manager.startLoading(null);
    }
}
