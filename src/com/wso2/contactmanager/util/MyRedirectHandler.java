package com.wso2.contactmanager.util;

import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.protocol.HttpContext;

public class MyRedirectHandler extends DefaultRedirectHandler {

    public URI lastRedirectedUri;

    @Override
    public boolean isRedirectRequested(HttpResponse response, HttpContext context) {

        return super.isRedirectRequested(response, context);
    }

    @Override
    public URI getLocationURI(HttpResponse response, HttpContext context)
            throws ProtocolException {

        lastRedirectedUri = super.getLocationURI(response, context);
        
        return lastRedirectedUri;
    }

}
