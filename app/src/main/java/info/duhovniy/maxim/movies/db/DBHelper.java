package info.duhovniy.maxim.movies.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by maxduhovniy on 10/20/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cmd = "CREATE TABLE " + DBConstants.MOVIES_TABLE_NAME + " (" + DBConstants.MOVIE_ID +
                " INTEGER PRIMARY KEY, " + DBConstants.MOVIE_OMDB_ID + " TEXT, " +
                DBConstants.MOVIE_TITLE + " TEXT," + DBConstants.MOVIE_YEAR + " TEXT, " +
                DBConstants.MOVIE_TYPE + " TEXT, " + DBConstants.MOVIE_PLOT + " TEXT, " +
                DBConstants.MOVIE_URL_POSTER + " TEXT, " + DBConstants.MOVIE_URL_TRAILER + " TEXT, " +
                DBConstants.MOVIE_LOCAL_POSTER_PATH + " TEXT, " + DBConstants.MOVIE_WATCHED + "BOOLEAN);";
        try {
            db.execSQL(cmd);
        } catch (SQLException ex) {
            Log.e(DBConstants.LOG_TAG, "Create table exception: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBConstants.LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ", which will destroy all old date");
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.MOVIES_TABLE_NAME);
        onCreate(db);
    }
}
