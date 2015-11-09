package info.duhovniy.maxim.movies.ui;

import android.content.Context;
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
import info.duhovniy.maxim.movies.db.Movie;

/**
 * Created by maxduhovniy on 23/10/2015.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.RecyclerViewHolder> {

    private ArrayList<Movie> listMovies;
    private View mView;
    private Context mContext;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView movieTitle, movieYear;
        public ImageView moviePoster;
        public LinearLayout mLayout;
        public CardView cv;

        // We also create a constructor that accepts the entire item search_list_row
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
    }

    public SearchListAdapter(ArrayList<Movie> list, View v, Context context) {
        listMovies = list;
        mView = v;
        mContext = context;
    }

    // Involves inflating a layout from XML and returning the holder
    @Override
    public SearchListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the custom layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_row, parent, false);

        // Return a new holder instance
        return new RecyclerViewHolder(v);

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, final int position) {

        // Set item views based on the data model
        viewHolder.movieTitle.setText(listMovies.get(position).getTitle());
        viewHolder.movieYear.setText(listMovies.get(position).getYear());

        // Set image using Picasso library
        Picasso.with(mContext).setIndicatorsEnabled(true);
        Picasso.with(mContext)
                .load(listMovies.get(position).getUrlPoster())
                .placeholder(R.drawable.placeholder)
                .resize(100, 100)
                .centerCrop()
                .into(viewHolder.moviePoster);

        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View.OnClickListener clickListener = new View.OnClickListener() {
                    public void onClick(View v) {
                        listMovies.remove(position);
                        notifyItemRemoved(position);
                    }
                };

                Snackbar.make(mView, "Delete " + listMovies.get(position).getTitle() + "?",
                        Snackbar.LENGTH_LONG).setAction("Ok", clickListener).show();
            }

        });

        viewHolder.mLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final View.OnClickListener clickListener = new View.OnClickListener() {
                    public void onClick(View v) {
                        listMovies.add(position, new Movie());
                        notifyItemInserted(position);
                    }
                };

                Snackbar.make(mView, "Edit movie " + listMovies.get(position).getTitle() + "?",
                        Snackbar.LENGTH_LONG).setAction("Ok", clickListener).show();

                return true;
            }
        });

    }


    // Return the total count of items
    @Override
    public int getItemCount() {
        return listMovies.size();
    }

}

