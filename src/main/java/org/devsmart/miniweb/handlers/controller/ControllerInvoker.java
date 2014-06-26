package org.devsmart.miniweb.handlers.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;

public class ControllerInvoker implements HttpRequestHandler {

    public final Logger logger = LoggerFactory.getLogger(ControllerInvoker.class);

    public String pathEndpoint;
    public Object instance;
    public Method method;
    public ParamHandler[] paramHandlers;
    public boolean serializeRetval;
    private Gson mGson;

    private Gson getGson() {
        if(mGson == null){
            mGson = new GsonBuilder().create();
        }
        return mGson;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

        Object[] params = new Object[paramHandlers.length];
        for(int i=0;i<params.length;i++){
            params[i] = paramHandlers[i].createParam(request, response, context);
        }

        try {
            Object retval = method.invoke(instance, params);
            if(serializeRetval){
                Gson gson = getGson();
                String respStr = gson.toJson(retval);
                StringEntity respEntity = new StringEntity(respStr, ContentType.APPLICATION_JSON);
                response.setEntity(respEntity);
            }
        } catch (Exception e) {
            logger.error("error invoking controller method: {}:{}", instance.getClass().getName(), method.getName(), e);
            throw new HttpException();
        }
    }



}