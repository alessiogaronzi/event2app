package com.digitalchannelforum.drupalcon.model.dao;

import com.digitalchannelforum.drupalcon.model.AppDatabaseInfo;
import com.digitalchannelforum.drupalcon.model.data.FloorPlan;
import com.digitalchannelforum.drupalcon.model.database.AbstractEntityDAO;


public class FloorPlanDao extends AbstractEntityDAO<FloorPlan, String> {

    public static final String TABLE_NAME = "table_floor_plans";

    @Override
    protected String getSearchCondition() {
        return FloorPlan.COLUMN_ID + "=?";
    }

    @Override
    protected String[] getSearchConditionArguments(String theId) {
        return new String[]{theId};
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
    protected FloorPlan newInstance() {
        return new FloorPlan();
    }

    @Override
    protected String[] getKeyColumns() {
        return new String[0];
    }
}
