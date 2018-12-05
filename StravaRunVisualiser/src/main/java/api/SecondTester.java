package api;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;

public class SecondTester {

    final private static String URLAthleteStats = "https://www.strava.com/api/v3/athletes/32257897/stats?page=&per_page=";

    private static HttpClient httpClient;
    private static GetMethod method;

    public static void main(String[] args){

        try {

            httpClient = new HttpClient();

            String json = GetJsonData(URLAthleteStats);

            System.out.println(json);

        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }

    }

    public static String GetJsonData(String URL) throws IOException {

        // Get method using passed url
        method = new GetMethod(URL);

        // Add header with token for access
        method.setRequestHeader("Authorization", "Bearer " + Tokens.StravaAPI);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        // Execute the method.
        int statusCode = httpClient.executeMethod(method);

        if (statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed: " + method.getStatusLine());
        }

        // Read the response body and return it as string.
        // This will cause a warning because the body may be to large and inefficiently loaded
        // into memory apache recommend using getResponseBodyAsStream
        return new String(method.getResponseBody());

    }

}
