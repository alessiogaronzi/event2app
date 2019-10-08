package com.digitalchannelforum.drupalcon.model.managers;

import com.digitalchannelforum.drupal.AbstractBaseDrupalEntity;
import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.drupalcon.app.App;
import com.digitalchannelforum.drupalcon.model.dao.EventDao;
import com.digitalchannelforum.drupalcon.model.dao.SpeakerDao;
import com.digitalchannelforum.drupalcon.model.data.Speaker;
import com.digitalchannelforum.drupalcon.model.requests.SpeakersRequest;

import java.util.List;

public class SpeakerManager extends SynchronousItemManager<Speaker.Holder, Object, String> {

    private SpeakerDao mSpeakerDao;

    public SpeakerManager(DrupalClient client) {
        super(client);
        mSpeakerDao = new SpeakerDao(App.getContext());
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new SpeakersRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "speakers";
    }

    @Override
    protected boolean storeResponse(Speaker.Holder requestResponse, String tag) {
        List<Speaker> speakers = requestResponse.getSpeakers();
        if (speakers == null) {
            return false;
        }

        EventDao eventDao = new EventDao(App.getContext());
        mSpeakerDao.saveOrUpdateDataSafe(speakers);
        for (Speaker speaker : speakers) {
            if (speaker != null) {
                if (speaker.isDeleted()) {
                    mSpeakerDao.deleteDataSafe(speaker.getId());
                    eventDao.deleteEventAndSpeakerBySpeaker(speaker.getId());
                }
            }
        }

        return true;
    }

    public List<Speaker> getSpeakers() {
        return mSpeakerDao.selectSpeakersOrderedByName();
    }

    public List<Speaker> getSpeakers(long speakerId) {
        return mSpeakerDao.getDataSafe(speakerId);
    }

    public List<Speaker> getSpeakersByEventId(long eventId) {
        return mSpeakerDao.getSpeakersByEventId(eventId);
    }

    public Speaker getSpeakerById(long id) {
        List<Speaker> speakerById = mSpeakerDao.getSpeakerById(id);
        if (!speakerById.isEmpty()){
            return speakerById.get(0);
        } else {
            return null;
        }
    }

    public void clear() {
        mSpeakerDao.deleteAll();
    }
}
