package com.digitalchannelforum.drupalcon.model.managers;

import com.digitalchannelforum.drupal.AbstractBaseDrupalEntity;
import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.http.base.ResponseData;
import com.digitalchannelforum.utils.L;

public abstract class SynchronousItemManager<FetchRequestResponseToManage ,ParametersClass,TagClass> {
    private DrupalClient client;

    protected abstract AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, ParametersClass requestParams);

    protected abstract TagClass getEntityRequestTag(ParametersClass params);

    protected abstract boolean storeResponse(FetchRequestResponseToManage requestResponse, TagClass tag);

    public SynchronousItemManager(DrupalClient client) {
        this.client = client;
    }

    public boolean fetchData(ParametersClass requestParams) {
        AbstractBaseDrupalEntity request = getEntityToFetch(this.client, requestParams);
        TagClass tag = getEntityRequestTag(requestParams);
        ResponseData response = request.pullFromServer(true, tag, null);
        int statusCode = response.getStatusCode();
        L.e("Response code = " + statusCode);
        if (statusCode > 0 && statusCode < 400) {

            FetchRequestResponseToManage responseObj = (FetchRequestResponseToManage)response.getData();
            if(responseObj != null) {
                return storeResponse(responseObj, tag);
            }
        }

        return false;
    }

    public boolean fetchData() {
        return fetchData(null);
    }

    public DrupalClient getClient()
    {
        return client;
    }
}
