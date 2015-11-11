package info.duhovniy.maxim.movies.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.util.Locale;

import info.duhovniy.maxim.movies.R;
import info.duhovniy.maxim.movies.db.DBConstants;
import info.duhovniy.maxim.movies.db.DBHandler;
import info.duhovniy.maxim.movies.db.DBHelper;

/**
 * Created by maxduhovniy on 30/10/2015.
 */
public class SettingsPreference extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }

    @Override
    protected void onPause() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        setLocal(prefs.getString("language", "en"));
        String newBase = prefs.getString(DBConstants.TABLE_NAME_MARKER,
                DBConstants.DEFAULT_TABLE_NAME);
        DBHelper.setBaseName(newBase);
        DBHandler.setBaseName(newBase);
        super.onPause();
    }

    private void setLocal(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
    }

}
