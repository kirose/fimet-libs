package com.fimet.client;
	
import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
/**
 * Hello world!
 *
 */
public class Client 
{
    public static void main( String[] args ) throws Exception
    {
    	Client app = new Client();
    	app.sendPost();
    	app.close();
    }
    public void test() {
    	Client obj = new Client();

        try {
            System.out.println("Testing 1 - Send Http GET request");
            obj.sendGet();

            System.out.println("Testing 2 - Send Http POST request");
            obj.sendPost();
        } catch (Exception e) { 
        } finally {
            try {obj.close();} catch(Exception e) {}
        }
    }
    CloseableHttpClient httpClient = HttpClients.createDefault();
    private void close() throws IOException {
        httpClient.close();
    }

    private void sendGet() throws Exception {

        HttpGet request = new HttpGet("http://127.0.0.1/task/download?idTask=f043970f-4c19-433c-a3be-3f51eefe5b6f");

        // add request headers
        request.addHeader("product-key", "vd5A+ZAtDnKpgj8RMGVVpw==");
        request.addHeader("custom-key", "mkyong");
        request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }
        }
    }
//    private void sendPostExample() throws Exception {
//        HttpPost post = new HttpPost("http://127.0.0.1/usecase/execute");
//
//        post.addHeader("product-key", "vd5A+ZAtDnKpgj8RMGVVpw==");
//        // add request parameter, form parameters
//        List<NameValuePair> urlParameters = new ArrayList<>();
//        urlParameters.add(new BasicNameValuePair("idTask", "f043970f-4c19-433c-a3be-3f51eefe5b6f"));
//        post.setEntity(new StringEntity("{\"name\":\"Test\",\"message\": {\"parser\":\"National\",\"adapter\": \"Exclusive\",\"header\": \"ISO023400070\",\"mti\": \"0200\",\"fields\": {\"2\":\"9988773461796711\",\"3\": \"500000\",\"4\": \"000000012300\",\"7\": \"0423172306\",\"11\": \"707361\",\"12\": \"172306\",\"13\": \"0423\",\"17\": \"0423\",\"22\": \"050\",\"32\": \"12\",\"37\": \"001341707361\",\"41\": \"00040805        \",\"48\": \"117812             00000000\",\"49\": \"484\"}},\"simulators\":[{\"model\":\"National\",\"parser\":\"National\",\"address\": \"127.0.0.1\",\"port\": 7251,\"server\":false,\"adapter\":\"Exclusive\",\"extension\":{\"validations\":[\"\\\"00\\\".equals(msg.getValue(39))\",\"\\\"00\\\".equals(msg.getValue(39))\"]}},{\"model\":\"National\",\"parser\":\"National\",\"address\": \"127.0.0.1\",\"port\": 7251,\"server\":true,\"adapter\":\"Exclusive\"}]}"));
//        
//        //post.setEntity(new UrlEncodedFormEntity(urlParameters));
//
//        try (CloseableHttpResponse response = httpClient.execute(post)) {
//
//            System.out.println(EntityUtils.toString(response.getEntity()));
//        }
//    }
    private void sendPost() throws Exception {

        HttpPost post = new HttpPost("http://127.0.0.1/usecase/execute");

        post.addHeader("product-key", "vd5A+ZAtDnKpgj8RMGVVpw==");
        post.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        post.addHeader(HttpHeaders.ACCEPT,"application/json");
        post.setEntity(new StringEntity("{\"name\":\"Test\",\"message\": {\"parser\":\"National\",\"adapter\": \"Exclusive\",\"header\": \"ISO023400070\",\"mti\": \"0200\",\"fields\": {\"2\":\"9988773461796711\",\"3\": \"500000\",\"4\": \"000000012300\",\"7\": \"0423172306\",\"11\": \"707361\",\"12\": \"172306\",\"13\": \"0423\",\"17\": \"0423\",\"22\": \"050\",\"32\": \"12\",\"37\": \"001341707361\",\"41\": \"00040805        \",\"48\": \"117812             00000000\",\"49\": \"484\"}},\"simulators\":[{\"model\":\"National\",\"parser\":\"National\",\"address\": \"127.0.0.1\",\"port\": 7251,\"server\":false,\"adapter\":\"Exclusive\",\"extension\":{\"validations\":[\"\\\"00\\\".equals(msg.getValue(39))\",\"\\\"00\\\".equals(msg.getValue(39))\"]}},{\"model\":\"National\",\"parser\":\"National\",\"address\": \"127.0.0.1\",\"port\": 7251,\"server\":true,\"adapter\":\"Exclusive\"}]}"));
        
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            System.out.println(EntityUtils.toString(response.getEntity()));
        }

    }
}
