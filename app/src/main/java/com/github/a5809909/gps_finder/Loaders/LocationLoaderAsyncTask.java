package com.github.a5809909.gps_finder.Loaders;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.github.a5809909.gps_finder.Fragment.DatabaseFragment;
import com.github.a5809909.gps_finder.Model.LocationModel;
import com.github.a5809909.gps_finder.Sql.DatabaseHelper;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.github.a5809909.gps_finder.Utilities.Constants.NOTIFICATION_ID.FETCHR_API_KEY;
import static com.github.a5809909.gps_finder.Utilities.Constants.NOTIFICATION_ID.FETCHR_URI;
import static com.github.a5809909.gps_finder.Utilities.Constants.NOTIFICATION_ID.GOOGLE_GEOCODING_API_KEY;
import static com.github.a5809909.gps_finder.Utilities.Constants.NOTIFICATION_ID.GOOGLE_GEOCODING_URI;
import static com.github.a5809909.gps_finder.Utilities.Constants.NOTIFICATION_ID.GOOGLE_GEOLOCATE_API_KEY;
import static com.github.a5809909.gps_finder.Utilities.Constants.NOTIFICATION_ID.GOOGLE_GEOLOCATE_URI;
import static com.github.a5809909.gps_finder.Utilities.Constants.NOTIFICATION_ID.PHOTO_COUNT;

public class LocationLoaderAsyncTask extends AsyncTask<LocationModel, Void, LocationModel> {

    private static final String TAG = LocationLoaderAsyncTask.class.getSimpleName();
    private LocationModel mLocationModel;
    Context instance;
    private IAsyncTaskListener mIAsyncTaskListener;

    public LocationLoaderAsyncTask(Context pContext, IAsyncTaskListener pIAsyncTaskListener) {
        mIAsyncTaskListener = pIAsyncTaskListener;
        instance = pContext;
    }

    @Override
    protected LocationModel doInBackground(LocationModel... params) {
        mLocationModel = params[0];
        final HttpClient httpclient = new DefaultHttpClient();
        final HttpPost httpost = new HttpPost(GOOGLE_GEOLOCATE_URI + GOOGLE_GEOLOCATE_API_KEY);
        final StringEntity stringEntity;

        try {
            final JSONObject cellTower = new JSONObject();
            cellTower.put("cellId", mLocationModel.getCellId());
            cellTower.put("locationAreaCode", mLocationModel.getLac());
            cellTower.put("mobileCountryCode", mLocationModel.getMcc());
            cellTower.put("mobileNetworkCode", mLocationModel.getMnc());

            Log.i(TAG, "cellId: " + mLocationModel.getCellId() +
                    ", locationAreaCode: " + mLocationModel.getLac() +
                    ", mobileCountryCode: " + mLocationModel.getMcc() +
                    ", mobileNetworkCode: " + mLocationModel.getMnc());
            final JSONArray cellTowers = new JSONArray();
            cellTowers.put(cellTower);

            final JSONObject rootObject = new JSONObject();
            rootObject.put("cellTowers", cellTowers);

            stringEntity = new StringEntity(rootObject.toString());
            stringEntity.setContentType("application/json");

            httpost.setEntity(stringEntity);
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            Log.i(TAG, "cellTower: " + cellTower);
            Log.i(TAG, "rootObject.toString(): " + rootObject.toString());
            mLocationModel.setJson_first(rootObject.toString());
            final ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httpost, responseHandler);
            final JSONObject jsonResult = new JSONObject(response);
            final JSONObject location = jsonResult.getJSONObject("location");
            final String acc = jsonResult.getString("accuracy");
            final String lat = location.getString("lat");
            final String lng = location.getString("lng");

            final String latlng = lat + "," + lng;

            final String url = Uri.parse(GOOGLE_GEOCODING_URI)
                    .buildUpon()
                    .appendQueryParameter("key", GOOGLE_GEOCODING_API_KEY)
                    .appendQueryParameter("latlng", latlng)
                    .appendQueryParameter("language", "ru")
                    .build().toString();

            final String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            final JSONObject jsonBody = new JSONObject(jsonString);
            final JSONArray addressJsonArray = jsonBody.getJSONArray("results");
            final JSONObject addressObject = addressJsonArray.getJSONObject(0);

            final String address = addressObject.getString("formatted_address");

            final String urlLocPlaces = Uri.parse(FETCHR_URI)
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.places.findByLatLon")
                    .appendQueryParameter("api_key", FETCHR_API_KEY)
                    .appendQueryParameter("lat", lat)
                    .appendQueryParameter("lon", lng)
                    .appendQueryParameter("language", "ru")
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .build().toString();
            final String jsonStringPlaces = getUrlString(urlLocPlaces);
            Log.i(TAG, "Received JSON Photo: " + jsonStringPlaces);
            final JSONObject jsonBodyLocPhoto = new JSONObject(jsonStringPlaces);
            final JSONObject placesJsonObject = jsonBodyLocPhoto.getJSONObject("places");
            final JSONArray placeJsonArray = placesJsonObject.getJSONArray("place");
            final JSONObject placeJsonObject = placeJsonArray.getJSONObject(0);
            final String place_id = placeJsonObject.getString("place_id");

            final String urlPhotos = Uri.parse(FETCHR_URI)
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.search")
                    .appendQueryParameter("api_key", FETCHR_API_KEY)
                    .appendQueryParameter("place_id", place_id)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString();
            final String jsonStringPhotos = getUrlString(urlPhotos);
            Log.i(TAG, "Received JSON: " + jsonStringPhotos);
            final JSONObject jsonBodyPhotos = new JSONObject(jsonStringPhotos);
            final JSONObject photosJsonObject = jsonBodyPhotos.getJSONObject("photos");
            final JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");
            int lengthPhotoArray = photoJsonArray.length();
            final StringBuilder stringBuilder = new StringBuilder();
            if (lengthPhotoArray == 0) {
                mLocationModel.setUrlPhotos("http://epam.com");
            } else {

                if (lengthPhotoArray > PHOTO_COUNT) {
                    lengthPhotoArray = PHOTO_COUNT;
                }
                for (int i = 0; i < lengthPhotoArray; i++) {
                    final JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
                    if (!photoJsonObject.has("url_s")) {
                        continue;
                    }
                    stringBuilder.append(photoJsonObject.getString("url_s") + ",");
                }

                mLocationModel.setUrlPhotos(stringBuilder.toString());
            }
            Log.i(TAG, "Json getPhotos " + mLocationModel.getUrlPhotos());
            mLocationModel.setAcc(acc);
            mLocationModel.setLat(lat);
            mLocationModel.setLng(lng);
            mLocationModel.setAddress(address);

            final DatabaseHelper databaseHelper = new DatabaseHelper(instance);
            databaseHelper.addUser(mLocationModel);
            databaseHelper.close();
        } catch (Exception e) {
            final String err = e.getMessage();
            Log.i(TAG, "doInBackground: " + err);
            mLocationModel.setErrors(err);
        }
        return mLocationModel;
    }

    @Override
    protected void onPostExecute(LocationModel pLocationModel) {
        super.onPostExecute(pLocationModel);
        mIAsyncTaskListener.finishedAsyncTask();
    }

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

}
