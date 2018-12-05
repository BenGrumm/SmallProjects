package api;

import api.Tokens;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.*;

import java.io.IOException;

public class MainTester {

    final private static String URLAthleteStats = "https://www.strava.com/api/v3/athletes/32257897/stats?page=&per_page=";

    private static OkHttpClient okHttpClient;

    public static void main(String[] args){

        try {
            okHttpClient = new OkHttpClient();
            String API = ConnectApi(URLAthleteStats);
            System.out.println(API);
            parseJsonData(API);
        }catch(IOException | JSONException e){
            System.out.println(e);
        }

        System.out.println("Finish");

        okHttpClient = null;

    }

    /**
     * Function to connect to the api and return json string
     */
    public static String ConnectApi(String URL) throws IOException {

        // Create the request with the url and header with access token
        Request request = new Request.Builder()
                .url(URL)
                .addHeader("Authorization", "Bearer " + Tokens.StravaAPI)
                .build();

        System.out.println(request.toString());

        // Execute the call. If the response is successful return the json string else throw exception
        Response response = okHttpClient.newCall(request).execute();
        if(response.isSuccessful()) {
            return response.body().string();
        }else{
            response.close();
            throw new IOException("Unsuccessful Response");
        }
    }

    /**
     * Function to parse JSON string
     * @param jsonData string of json data
     * @throws JSONException
     */
    public static JSONObject parseJsonData(String jsonData) throws JSONException{

        System.out.println("Switching Data");

        JSONObject data = new JSONObject(jsonData);

        System.out.println("Creating Objects");

        JSONObject recentRun = data.getJSONObject("recent_run_totals");

        System.out.println("1");

        JSONObject yearToDateRun = data.getJSONObject("ytd_run_totals");

        System.out.println("2");

        JSONObject allTimeRun = data.getJSONObject("all_run_totals");

        System.out.println(allTimeRun.getInt("moving_time"));

        return data;
    }

}
