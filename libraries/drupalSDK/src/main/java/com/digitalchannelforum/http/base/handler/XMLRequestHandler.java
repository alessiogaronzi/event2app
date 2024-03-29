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

package com.digitalchannelforum.http.base.handler;


import com.digitalchannelforum.http.base.IPostableItem;
import com.digitalchannelforum.http.base.RequestHandler;

import java.io.UnsupportedEncodingException;

class XMLRequestHandler extends RequestHandler
{
	@Override
	public String stringBodyFromItem()
	{
        if(implementsPostableInterface())
        {
            IPostableItem item = (IPostableItem)this.object;
            return item.toXMLString();
        }else{
            return this.object.toString();
        }
	}

    @Override
    public String getBodyContentType(String defaultCharset) {
        return Handler.PROTOCOL_REQUEST_APP_TYPE_XML +  Handler.CONTENT_TYPE_CHARSET_PREFIX + this.getCharset(defaultCharset);
    }

    @Override
    public byte[] getBody(String defaultCharset) throws UnsupportedEncodingException {
        String content = this.stringBodyFromItem();
        return content.getBytes(getCharset(defaultCharset));
    }
}
