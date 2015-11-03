package info.duhovniy.maxim.movies.network;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import info.duhovniy.maxim.movies.db.Movie;
import info.duhovniy.maxim.movies.ui.MyAdapter;

/**
 * Created by maxduhovniy on 23/10/2015.
 */
public class SearchMovie {

    private static final String TAG = "URL Connection";

    public static MyAdapter myAdapter;
    public static ArrayList<Movie> searchResult = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Context mContext;
    // private View mView;
    // private RecyclerView mRecyclerView;

    public SearchMovie(String searchRequest, RecyclerView rv, Context context)
            throws UnsupportedEncodingException {
        String url = "http://www.omdbapi.com/?s=" + URLEncoder.encode(searchRequest, "utf-8");
        mRecyclerView = rv;
        mContext = context;
        MyTask task = new MyTask();
        task.execute(new String[]{url});
    }

    public ArrayList<Movie> getSearchResult() {
        return searchResult;
    }

    class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {

            String response = sendHttpRequest(strings[0]);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            String[] posterURLs = new String[10];

            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("Search");

                for (int i = 0; i < array.length(); i++) {
                    String title = array.getJSONObject(i).getString("Title");
                    String year = array.getJSONObject(i).getString("Year");
                    String imdbID = array.getJSONObject(i).getString("imdbID");
                    String type = array.getJSONObject(i).getString("Type");
                    String posterURL = array.getJSONObject(i).getString("Poster");

                    Movie movie = new Movie(imdbID, title, year, type, null, posterURL, null, null);
                    searchResult.add(movie);

                }

                myAdapter = new MyAdapter(searchResult, mRecyclerView, mContext);
                mRecyclerView.setAdapter(myAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private String sendHttpRequest(String urlString) {
            BufferedReader input = null;
            HttpURLConnection httpCon = null;
            InputStream input_stream = null;
            InputStreamReader input_stream_reader = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(urlString);
                httpCon = (HttpURLConnection) url.openConnection();
                if (httpCon.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Cannot Connect to : " + urlString);
                    return null;
                }

                input_stream = httpCon.getInputStream();
                input_stream_reader = new InputStreamReader(input_stream);
                input = new BufferedReader(input_stream_reader);
                String line;
                while ((line = input.readLine()) != null) {
                    response.append(line + "\n");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input_stream_reader.close();
                        input_stream.close();
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (httpCon != null) {
                        httpCon.disconnect();
                    }
                }
            }
            return response.toString();
        }

    }

}


