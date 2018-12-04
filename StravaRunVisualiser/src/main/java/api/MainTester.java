package api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.*;

import java.io.IOException;

public class MainTester {

    final private static String token = "TODO";
    final private static String URLAthleteStats = "https://www.strava.com/api/v3/athletes/32257897/stats?page=&per_page=";

    private static String JsonData;
    private static Boolean JsonDataHasUpdated = false;

    public static void main(String[] args){
        System.out.println(ConnectApi(URLAthleteStats));
        System.out.println("???????");
    }

    /**
     * Function to connect to the api
     */
    public static String ConnectApi(String URL){

        // Create new HTTP client
        OkHttpClient client = new OkHttpClient();

        // Create the request
        Request request = new Request.Builder()
                .url(URL)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        System.out.println(request.toString());

        // Create call with created request
        Call call = client.newCall(request);

        System.out.println("Created Call");

        // Execute the call
        try (Response response = client.newCall(request).execute()){
            if(response.isSuccessful()) {
                return response.body().string();
            }
        }catch (IOException e){
            System.out.println("IO Exception : " + e);
        }
        return null;
    }

    /**
     * Function to parse JSON string
     * @param jsonData string of json data
     * @throws JSONException
     */
    public static JSONObject parseJsonData(String jsonData) throws JSONException{
        JSONObject data = new JSONObject(jsonData);

        return data;
    }

}
