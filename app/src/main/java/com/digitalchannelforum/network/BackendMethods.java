package com.digitalchannelforum.network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.digitalchannelforum.drupalcon.R;
import com.digitalchannelforum.drupalcon.app.App;

public class BackendMethods {
    public static final String BASE_URL = App.mContext.getString(R.string.api_value_base_url);

    public static boolean getInfo( Response.Listener<String> response, Response.ErrorListener errorListener){
        final String uri;
        uri = String.format(BASE_URL+"getInfo");

        StringRequest sendMessageRequest = new StringRequest(Request.Method.GET, uri, response, errorListener) {

            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }

        };

        App.addToRequestQueue(sendMessageRequest);

        return true;
    }
}
