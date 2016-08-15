package com.bytesnmaterials.zro.repositories;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.features.register.RegisterActivity;
import com.bytesnmaterials.zro.listeners.LoadingListener;
import com.bytesnmaterials.zro.util.ZeroNetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * This class is used as model class regarding geolocation fetch.
 * This also contains methods to fetch country from device's ip.
 *
 * @author mitesh
 * @version 1.0
 * @since 25/7/16
 */
public class GeoBaseRepository extends BaseRepository implements IGeoBaseRepository{

    private Context _context;
    private LoadingListener _loadingListener;

    public GeoBaseRepository(Context context) {
        super(context);
        _context = context;
    }

    /**
     * This method is used to get country using device's ip address.
     *
     * @param loadingListener : loading view while calling the api
     */
    @Override
    public void getCountryFromIp(LoadingListener loadingListener) {
        _loadingListener = loadingListener;
        if(ZeroNetUtil.isConnected(_context)){
            new HttpAsyncTask().execute();
        }
    }

    /**
     * This method is the http get call to get details of country using api(http://ip-api.com/json)
     *
     * @return Country name as string
     * @throws Exception
     */
    private String getCountry() throws Exception {

        URL obj = new URL(Constants.URL_LOCATION_FROM_IP);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        System.out.println("Sending 'GET' request to URL : " + obj);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Log.e("Response","=> "+response);

        return response.toString();
    }

    /**
     * This is an AsyncTask to do network operation regarding http get call.
     */
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _loadingListener.onLoadingStarted();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return getCountry();
            } catch (Exception e) {
                e.printStackTrace();
                _loadingListener.onLoadingFinished();
                return "";
            }

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            _loadingListener.onLoadingFinished();
            if(result!=null && !result.equals("")){
                try {
                    JSONObject jObj = new JSONObject(result);
                    String country = jObj.getString("country");
                    Log.e("country","=> "+country);
                    ((RegisterActivity)_context).edtCountry.setText(country);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
