package info.duhovniy.maxim.movies.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import info.duhovniy.maxim.movies.R;
import info.duhovniy.maxim.movies.db.DBHandler;
import info.duhovniy.maxim.movies.db.Movie;

/**
 * Created by maxduhovniy on 23/10/2015.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.RecyclerViewHolder> {

    private ArrayList<Movie> listMovies;
    private Cursor cursorMovies;
    private DBHandler mHandler;
    private View mView;
    private Context mContext;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder  {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView movieTitle, movieYear;
        public ImageView moviePoster;
        public LinearLayout mLayout;
//        public ClickListener clickListener;
        public CardView cv;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public RecyclerViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            movieTitle = (TextView) itemView.findViewById(R.id.title_text);
            movieYear = (TextView) itemView.findViewById(R.id.year_text);
            moviePoster = (ImageView) itemView.findViewById(R.id.poster_image);
            cv = (CardView) itemView.findViewById(R.id.cv);
            cv.setRadius(10);
            mLayout = (LinearLayout) itemView.findViewById(R.id.line_movie);

        }

/*
        */
/* Interface for handling clicks - both normal and long ones. *//*

        public interface ClickListener {

            */
/**
             * Called when the view is clicked.
             *
             * @param v           view that is clicked
             * @param position    of the clicked item
             * @param isLongClick true if long click, false otherwise
             *//*

            public void onClick(View v, int position, boolean isLongClick);

        }

        */
/* Setter for listener. *//*

        public void setupClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {

            // If not long clicked, pass last variable as false.
            clickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {

            // If long clicked, passed last variable as true.
            clickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
*/

    }

    public MyAdapter(ArrayList<Movie> list, View v, Context context) {
        listMovies = list;
        mView = v;
        mContext = context;
    }

    public void setMovieCursor(Cursor movies) {
        cursorMovies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public MyAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the custom layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        RecyclerViewHolder pvh = new RecyclerViewHolder(v);

        // Return a new holder instance
        return pvh;

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, final int position) {

        // Set item views based on the data model
        viewHolder.movieTitle.setText(listMovies.get(position).getTitle());
        viewHolder.movieYear.setText(listMovies.get(position).getYear());
        Picasso.with(mContext).setIndicatorsEnabled(true);
        Picasso.with(mContext)
                .load(listMovies.get(position).getUrlPoster())
                .placeholder(R.drawable.placeholder)
                .resize(100, 100)
                .centerCrop()
                .into(viewHolder.moviePoster);
//        DownloadImageTask downloadImageTask = new DownloadImageTask(viewHolder.moviePoster);
//        downloadImageTask.execute(listMovies.get(position).getUrlPoster());


        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cursorMovies.moveToPosition(position);

                final View.OnClickListener clickListener = new View.OnClickListener() {
                    public void onClick(View v) {
                        listMovies.remove(position);
                        notifyDataSetChanged();
                    }
                };

                Snackbar.make(mView, "Delete " + listMovies.get(position).getTitle() + "?",
                        Snackbar.LENGTH_LONG).setAction("Ok", clickListener).show();
            }

        });


    }


    // Return the total count of items
    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    /*
        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

            private ImageView mImage;

            public DownloadImageTask(ImageView v) {
                mImage = v;
            }

            protected Bitmap doInBackground(String... url) {
                Log.d("doInBackground", "starting download of image");
                Bitmap image = downloadImage(url[0]);
                return image;
            }

            protected void onPreExecute() {
            }

            protected void onPostExecute(Bitmap result) {
                if (result != null) {
                    mImage.setImageBitmap(result);
                } else {
                    mImage.setImageResource(R.drawable.placeholder);
                    // TextView errorMsg = (TextView)mActivity.findViewById(R.id.textView);
                    // errorMsg.setVisibility(View.VISIBLE);
                    // errorMsg.setText("Problem downloading image. Try again later");
                }
            }

            public Bitmap downloadImage(String urlString) {
                URL url;
                try {
                    url = new URL(urlString);
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                    InputStream is = httpCon.getInputStream();
                    int fileLength = httpCon.getContentLength();
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead, totalBytesRead = 0;
                    byte[] data = new byte[2048];
                    // Read the image bytes in chunks of 2048 bytes
                    while ((nRead = is.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                        totalBytesRead += nRead;
                    }
                    buffer.flush();
                    byte[] image = buffer.toByteArray();
                    return BitmapFactory.decodeByteArray(image, 0, image.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

        }
    */

}

