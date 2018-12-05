package api;

import api.Tokens;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.*;

import java.io.IOException;

public class MainTester {

    final private static String token = "TODO";
    final private static String URLAthleteStats = "https://www.strava.com/api/v3/athletes/32257897/stats?page=&per_page=";

    public static void main(String[] args){
        try {
            System.out.println(ConnectApi(URLAthleteStats));
        }catch(IOException e){
            System.out.println(e);
        }
    }

    /**
     * Function to connect to the api and return json string
     */
    public static String ConnectApi(String URL) throws IOException {

        // Create new HTTP client
        OkHttpClient client = new OkHttpClient();

        // Create the request with the url and header with access token
        Request request = new Request.Builder()
                .url(URL)
                .addHeader("Authorization", "Bearer " + Tokens.StravaAPI)
                .build();

        System.out.println(request.toString());

        // Execute the call. If the response is successful return the json string else throw exception
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {
            return response.body().string();
        }else{
            throw new IOException("Unsuccessful Response");
        }
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
