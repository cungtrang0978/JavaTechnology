package com.example.tikicloneapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.tikicloneapp.database.DBManager;
import com.example.tikicloneapp.database.DBVolley;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.CatalogAdapter;
import com.example.tikicloneapp.models.Catalog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class Test_Activity extends AppCompatActivity {

    private ArrayList<Catalog> catalogArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CatalogAdapter catalogAdapter;;
    private DBVolley dbVolley;
    public static String jsonOutput = "";
    public static TextView textView;

    public static DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_);



    }

    private void parseJson(){
        Gson gson = new Gson();
        String jsonOutput = "[{\"id\":\"1\",\"name\":\"Samsung\",\"idParents\":\"1\",\"imageUrl\":\"https:\\/\\/cdn.tgdd.vn\\/Brand\\/1\\/Samsung42-b_25.jpg\"},"
                + "{\"id\":\"2\",\"name\":\"Iphone\",\"idParents\":\"1\",\"imageUrl\":\"https:\\/\\/cdn.tgdd.vn\\/Brand\\/1\\/iPhone-(Apple)42-b_16.jpg\"}]";
        Type listType = new TypeToken<List<Catalog>>(){}.getType();
        List<Catalog> catalogList = null;

        jsonOutput = parseUrlToJsonData("http://192.168.1.32/server/getCatalog.php");
        catalogList = gson.fromJson(jsonOutput, listType);
        Log.d("AAA", catalogList.get(0).getmName());
        
    }

    public String parseUrlToJsonData(String reqUrl) {
        String response = null;
        URL url = null;
        try {
            url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }

    public static String jsonGetRequest(String urlQueryString) {
        String json = null;
        try {
            URL url = new URL(urlQueryString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();
            InputStream inStream = connection.getInputStream();
            json = streamToString(inStream); // input stream to string
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    private String toJason() throws IOException {
        String jsonS = "";
        URL url = new URL("http://192.168.1.32/server/getCatalog.php");
        URLConnection conn = url.openConnection();
        conn.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;

        while((inputLine = in.readLine()) != null) {
            jsonS+=inputLine;
        }

        return  jsonS;
    }

    public static String getJsonFromServer(String url) throws IOException {

        BufferedReader inputStream = null;

        URL jsonUrl = new URL(url);
        URLConnection dc = jsonUrl.openConnection();

        dc.setConnectTimeout(5000);
        dc.setReadTimeout(5000);

        inputStream = new BufferedReader(new InputStreamReader(
                dc.getInputStream()));
        // read the JSON results into a string
        String jsonResult = inputStream.readLine();
        return jsonResult;

    }


//    protected void onResume() {
//        super.onResume();
//
//        new DBManager(this).getData_Catalog(new VolleyCallBack() {
//            @Override
//            public void onSuccess(ArrayList<Catalog> result) {
//                catalogArrayList = result;
//                Log.d("AAAA", catalogArrayList.get(0).getmName());
//            }
//        });
//    }

}