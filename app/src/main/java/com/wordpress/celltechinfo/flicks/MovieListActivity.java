package com.wordpress.celltechinfo.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    public final static String API_KEY_PARAM = "api_key";


    public final static String TAG = "MovieListActivity";

    AsyncHttpClient Client;

    String imageBaseUrl;

    String posterSize;
//comment as commit just to try


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Client = new AsyncHttpClient();

        getConfiguration();

    }

    private void getConfiguration(){
        String url = API_BASE_URL + "/configuration";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        Client.get(url ,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject images = response.getJSONObject("imagess");
                    imageBaseUrl = images.getString("secure_base_url");
                    JSONArray posterSizeOptions = images.getJSONArray(posterSize);
                    posterSize = posterSizeOptions.optString(3, "w342");
                } catch (JSONException e) {
                    LogError("Failed parsing configuration", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogError("Failed getting configuration", throwable, true);
            }
        });

    }

    private void LogError(String message, Throwable error, boolean AlertUser){
        Log.e(TAG, message, error);
        if (AlertUser){
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        }
    }
}
