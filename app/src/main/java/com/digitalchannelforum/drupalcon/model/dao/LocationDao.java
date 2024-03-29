package com.digitalchannelforum.drupalcon.model.dao;

import com.digitalchannelforum.drupalcon.model.AppDatabaseInfo;
import com.digitalchannelforum.drupalcon.model.data.Location;
import com.digitalchannelforum.drupalcon.model.database.AbstractEntityDAO;


public class LocationDao extends AbstractEntityDAO<Location, Long> {

    public static final String TABLE_NAME = "table_location";

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
    protected Location newInstance() {
        return new Location();
    }

    @Override
    protected String[] getKeyColumns() {
        return new String[0];
    }
}
