package com.devsmart.miniweb;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;

import com.devsmart.miniweb.handlers.controller.Controller;
import com.devsmart.miniweb.handlers.controller.RequestMapping;

import java.io.File;

public class ServerTest {


    @Controller
    public static class MyController {

        @RequestMapping("hello")
        public void handleHello(HttpRequest request, HttpResponse response) throws Exception {
            StringEntity retval = new StringEntity("itworked");
            response.setEntity(retval);
        }
    }

    public static void main(String[] args) throws Exception {
        File fsRoot = new File("public");

        Server server = new ServerBuilder()
                .port(9000)
                .mapController("/cgi/*", new MyController())
                .mapDirectory("/*", fsRoot)
                .create();

        server.start();


        //let it start up
        Thread.sleep(500);

        /*
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:9000/");

        System.out.println("Executing request " + httpget.getRequestLine());
        // Create a custom response handler
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }

        };
        String responseBody = httpclient.execute(httpget, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        */
        boolean running = true;
        while(running) {
            Thread.sleep(1000);
        }

        server.shutdown();

    }
}
