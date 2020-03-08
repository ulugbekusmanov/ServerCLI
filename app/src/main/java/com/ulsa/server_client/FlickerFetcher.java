package com.ulsa.server_client;

import android.net.Uri;
import android.util.Log;

import com.ulsa.server_client.GalleryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FlickerFetcher {

    private static final String TAG = "FlickerFetcher";
    private static final String API_KEY = "9b3bab0b995dbfde731dbddde9d58f33";


    public String getJSONString(String UrlSpec) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request =new  Request.Builder()
                .url(UrlSpec)
                .build();
        Response response=client.newCall(request).execute();
        String result =response.body().toString();
        return result;
    }
    public List<GalleryItem> fitchItems(){
        List<GalleryItem> galleryItems = new ArrayList<>();
        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "uri_s")
                    .build().toString();
            String jsonString = getJSONString(url);
            JSONObject jsonObject = new JSONObject(jsonString);
            parseItems(galleryItems, jsonObject);


        }catch (IOException ioe){
            Log.e(TAG, "ошибка закружка данный",ioe);

        }catch (JSONException json){
            Log.e(TAG, "ощибка парсинг Json", json);
        }
        return galleryItems;
    }
    private void  parseItems(List<GalleryItem> items, JSONObject jsonBody)throws  IOException, JSONException{
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        for (int i = 0; i<photoJsonArray.length(); i++){
            JSONObject photoJsonObject  = photoJsonArray.getJSONObject(i);
            GalleryItem item = new  GalleryItem();
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));
            if (!photoJsonObject.has("url_s")){
                continue;
            }
            item.setUrl(photosJsonObject.getString("url_s"));
            items.add(item);

        }
    }
}
