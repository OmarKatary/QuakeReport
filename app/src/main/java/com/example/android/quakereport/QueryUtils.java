package com.example.android.quakereport;



import android.text.TextUtils;
import android.util.Log;



import org.json.JSONArray;

import org.json.JSONException;

import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;


/**

 * Helper methods related to requesting and receiving earthquake data from USGS.

 */

public final class QueryUtils {



    /** Sample JSON response for a USGS query */

    public static final String USGS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=5&limit=20";

    /**

     * Create a private constructor because no one should ever create a {@link QueryUtils} object.

     * This class is only meant to hold static variables and methods, which can be accessed

     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).

     */

    private QueryUtils() {

    }



    /**

     * Return a list of {@link Earthquake} objects that has been built up from

     * parsing a JSON response.

     */

    public static ArrayList<Earthquake> extractEarthquakes(String jsonString) {

        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to

        ArrayList<Earthquake> earthquakes = new ArrayList<>();



        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON

        // is formatted, a JSONException exception object will be thrown.

        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            JSONObject root = new JSONObject(jsonString);

            // build up a list of Earthquake objects with the corresponding data.
            JSONArray features = root.getJSONArray("features");

            for (int i=0 ; i < features.length() ; i++){
                JSONObject jsonEarthquake = features.getJSONObject(i);
                JSONObject properties = jsonEarthquake.getJSONObject("properties");
                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");


                Earthquake earthquake = new Earthquake(location, magnitude, time, url);
                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {

            // If an error is thrown when executing any of the above statements in the "try" block,

            // catch the exception here, so the app doesn't crash. Print a log message

            // with the message from the exception.

            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);

        }



        // Return the list of earthquakes

        return earthquakes;

    }



    public static String makeHttpRequest(URL url){
        String jsonString = "";
        if (url == null){
            return jsonString;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonString = readFromStream(inputStream);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonString;

    }

    public static URL createUrl(String string){

        URL url = null;
        try{
            url = new URL(string);
        }catch (MalformedURLException e){
           return null;
        }
        return url;
    }
    /**
     * Query the USGS dataset and return a list of {@link Earthquake} objects.
     */
    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = makeHttpRequest(url);

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Earthquake> earthquakes = extractEarthquakes(jsonResponse);

        // Return the list of {@link Earthquake}s
        return earthquakes;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}