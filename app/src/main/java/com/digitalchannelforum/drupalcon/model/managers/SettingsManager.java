package com.digitalchannelforum.drupalcon.model.managers;

import com.digitalchannelforum.drupal.AbstractBaseDrupalEntity;
import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.drupalcon.model.PreferencesManager;
import com.digitalchannelforum.drupalcon.model.data.SettingsHolder;
import com.digitalchannelforum.drupalcon.model.requests.SettingsRequest;
import com.digitalchannelforum.utils.DateUtils;

public class SettingsManager extends SynchronousItemManager<SettingsHolder, Object, String> {

    public SettingsManager(DrupalClient client) {
        super(client);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new SettingsRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "settings";
    }

    @Override
    protected boolean storeResponse(SettingsHolder requestResponse, String tag) {
//        String timeZoneNumber = requestResponse.getSettings().getTimeZone();
//        String timeZone = String.format("GMT%s", timeZoneNumber);
        String timeZone = requestResponse.getSettings().getTimeZone();
        PreferencesManager.getInstance().saveTimeZone(timeZone);
        DateUtils.getInstance().setTimezone(timeZone);
        String searchQuery = requestResponse.getSettings().getTwitterSearchQuery();
        PreferencesManager.getInstance().saveTwitterSearchQuery(searchQuery);
        return true;
    }
}
