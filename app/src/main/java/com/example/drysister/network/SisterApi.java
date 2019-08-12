package com.example.drysister.network;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.drysister.Bean.Sister;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SisterApi {
    private static final String TAG = "Network";
    private static final String BASE_URL = "http://gank.io/api/data/福利/";

    /*
     * 查询信息
     */
    public ArrayList<Sister> fetchSister(int count,int page){
        String fetchUrl = BASE_URL +count +"/" + page;
        ArrayList<Sister> sisters = new ArrayList<>();
        try {
            URL url = new URL(fetchUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                   connection.setConnectTimeout(8000);
                   connection.setRequestMethod("GET");
                   int code = connection.getResponseCode();
                   Log.i(TAG, "ResponseCode: "+code);
                   if(code == 200){
                       InputStream inputStream = connection.getInputStream();
                       byte[] data = readFromStream(inputStream);
                       String result = new String(data,"UTF-8");
                       sisters = pareseSister(result);
                   }else {
                       Log.e(TAG, "请求失败：" +code );
                   }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sisters;
    }
    public ArrayList<Sister> pareseSister(String content) throws Exception{
        ArrayList<Sister> sisters = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(content);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        for (int i = 0;i<jsonArray.length();i++){
            JSONObject results = (JSONObject) jsonArray.get(i);
            Sister sister = new Sister();
            sister.set_id(results.getString("_id"));
            sister.setCreatedAt(results.getString("createdAt"));
            sister.setDesc(results.getString("desc"));
            sister.setPublishedAt(results.getString("publishedAt"));
            sister.setSource(results.getString("source"));
            sister.setType(results.getString("type"));
            sister.setUrl(results.getString("url"));
            sister.setUsed(results.getBoolean("used"));
            sister.setWho(results.getString("who"));
            sisters.add(sister);
        }
        return sisters;
    }
    public byte[] readFromStream(InputStream inputStream) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer))!= -1){
            outputStream.write(buffer,0,len);
        }
        inputStream.close();
        return outputStream.toByteArray();
    }

}
