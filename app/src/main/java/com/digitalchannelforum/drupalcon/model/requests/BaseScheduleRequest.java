package com.digitalchannelforum.drupalcon.model.requests;

import com.digitalchannelforum.drupal.AbstractDrupalEntityContainer;
import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.http.base.BaseRequest;
import com.digitalchannelforum.http.base.ResponseData;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseScheduleRequest<T> extends AbstractDrupalEntityContainer<T> {

    public BaseScheduleRequest(DrupalClient client, T theData) {
        super(client, theData);
    }

    @Override
    protected void consumeObject(ResponseData entity) {
        if(entity.getData() != null) {
            super.consumeObject(entity);
        }
    }

    @Override
    protected Map<String, String> getItemRequestHeaders(BaseRequest.RequestMethod method)
    {
        if(method != BaseRequest.RequestMethod.GET){
            return super.getItemRequestHeaders(method);
        }

//        String lastDate = PreferencesManager.getInstance().getLastUpdateDate();
        Map<String,String> result = new HashMap<>();
//        result.put(UpdatesManager.IF_MODIFIED_SINCE_HEADER, lastDate);
        return super.getItemRequestHeaders(method);
    }
}
