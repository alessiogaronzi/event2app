package com.digitalchannelforum.drupalcon.model.dao;

import com.digitalchannelforum.drupalcon.model.AppDatabaseInfo;
import com.digitalchannelforum.drupalcon.model.data.Track;
import com.digitalchannelforum.drupalcon.model.database.AbstractEntityDAO;

public class TrackDao extends AbstractEntityDAO<Track, Long> {

    public static final String TABLE_NAME = "table_track";

    @Override
    protected String getSearchCondition() {
        return "_id=?";
    }

    @Override
    protected String[] getSearchConditionArguments(Long theId) {
        return new String[]{theId.toString()};
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getDatabaseName() {
        return AppDatabaseInfo.DATABASE_NAME;
    }

    @Override
    protected Track newInstance() {
        return new Track();
    }

    @Override
    protected String[] getKeyColumns() {
        return new String[0];
    }
}
