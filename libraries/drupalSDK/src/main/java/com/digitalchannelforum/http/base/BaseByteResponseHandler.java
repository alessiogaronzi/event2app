/*
 * The MIT License (MIT)
 *  Copyright (c) 2014 Lemberg Solutions Limited
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package com.digitalchannelforum.http.base;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;


public abstract class BaseByteResponseHandler extends ResponseHandler
{

    /**
     * No specifier class supported yet. Just byte[] is returned
     * @param response
     * @param theSpecifier
     * @return
     */
    protected Object itemFromResponseWithSpecifier(NetworkResponse response, Object theSpecifier)
	{
		Object result = null;
		if(response != null && response.data != null && response.data.length > 0)
		{
			result = response.data;
		}
		return result;
	}

    protected Response<ResponseData> parseNetworkResponse(NetworkResponse response,Object responseClassSpecifier)
    {
        ResponseData responseData = new ResponseData();

        responseData.data = this.itemFromResponseWithSpecifier(response, responseClassSpecifier);

        responseData.statusCode = response.statusCode;
        responseData.headers = new HashMap<String, String>(response.headers);

        Response<ResponseData> result = Response.success(responseData, HttpHeaderParser.parseCacheHeaders(response));

        responseData.error = result.error;

        return result;
    };

}
