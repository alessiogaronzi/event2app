package com.digitalchannelforum.drupalcon.model.managers;

import com.digitalchannelforum.drupal.AbstractBaseDrupalEntity;
import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.drupalcon.model.dao.TypeDao;
import com.digitalchannelforum.drupalcon.model.data.Type;
import com.digitalchannelforum.drupalcon.model.requests.TypesRequest;

import java.util.List;

public class TypesManager extends SynchronousItemManager<Type.Holder, Object, String> {

    private TypeDao mTypeDao;

    public TypesManager(DrupalClient client) {
        super(client);
        mTypeDao = new TypeDao();
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new TypesRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "types";
    }

    @Override
    protected boolean storeResponse(Type.Holder requestResponse, String tag) {
        List<Type> types = requestResponse.getTypes();
        if (types == null) return false;

        mTypeDao.saveOrUpdateDataSafe(types);
        for (Type type : types) {
            if (type != null) {
                if (type.isDeleted()) {
                    mTypeDao.deleteDataSafe(type.getId());
                }
            }
        }
        return true;
    }

    public List<Type> getTypes() {
        return mTypeDao.getAllSafe();
    }

    public void clear() {
        mTypeDao.deleteAll();
    }
}
