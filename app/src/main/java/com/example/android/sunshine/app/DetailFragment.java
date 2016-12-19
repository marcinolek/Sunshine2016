package com.example.android.sunshine.app;

/**
 * Created by marcinolek on 17.12.2016.
 */

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final int MY_LOADER_ID = 0;
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private String mForecastStr;
    private Uri mForecastUri;
    private ForecastAdapter mForecastAdapter;
    static final String DETAIL_URI = "URI";
    private static final String[] DETAIL_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_WIND_SPEED = 5;
    static final int COL_DEGREES = 6;
    static final int COL_PRESSURE = 7;
    static final int COL_HUMIDIY = 8;
    static final int COL_WEATHER_CONDITION_ID = 9;
    private ShareActionProvider mShareActionProvider;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle arguments = getArguments();
        if (arguments != null) {
            mForecastUri = arguments.getParcelable(DETAIL_URI);
        }

        mForecastAdapter = new ForecastAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if(intent != null && intent.getData() != null) {
            mForecastUri = Uri.parse(intent.getDataString());


        }


            /*if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                mForecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
                ((TextView) rootView.findViewById(R.id.detail_text))
                        .setText(mForecastStr);
            }*/
        mIconView = (ImageView) rootView.findViewById(R.id.detail_item_icon);
        mDateView = (TextView) rootView.findViewById(R.id.detail_item_date_textview);
        mFriendlyDateView = (TextView) rootView.findViewById(R.id.detail_item_monthday_textview);
        mDescriptionView = (TextView) rootView.findViewById(R.id.detail_item_forecast_textview);
        mHighTempView = (TextView) rootView.findViewById(R.id.detail_item_high_textview);
        mLowTempView = (TextView) rootView.findViewById(R.id.detail_item_low_textview);
        mHumidityView = (TextView) rootView.findViewById(R.id.detail_item_forecast_humidity);
        mWindView = (TextView) rootView.findViewById(R.id.detail_item_forecast_wind);
        mPressureView = (TextView) rootView.findViewById(R.id.detail_item_forecast_pressure);
        mMyView = (MyView) rootView.findViewById(R.id.detail_item_my_view);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (null != mForecastUri) {
            return new CursorLoader(getActivity(),
                    mForecastUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null);
        }
        return null;
    }

    private ImageView mIconView;
    private TextView mFriendlyDateView;
    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mHighTempView;
    private TextView mLowTempView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;
    private MyView mMyView;
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst()) {
            return;
        }
        int weatherId = data.getInt(COL_WEATHER_CONDITION_ID);
        mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));

        // Read date from cursor and update views for day of week and date
        long date = data.getLong(COL_WEATHER_DATE);
        String friendlyDateText = Utility.getDayName(getActivity(), date);
        String dateText = Utility.getFormattedMonthDay(getActivity(), date);
        mFriendlyDateView.setText(friendlyDateText);
        mDateView.setText(dateText);

        // Read description from cursor and update view
        String description = data.getString(COL_WEATHER_DESC);
        mDescriptionView.setText(description);

        // Read high temperature from cursor and update view
        boolean isMetric = Utility.isMetric(getActivity());

        double high = data.getDouble(COL_WEATHER_MAX_TEMP);
        String highString = Utility.formatTemperature(getActivity(), high, isMetric);
        mHighTempView.setText(highString);

        // Read low temperature from cursor and update view
        double low = data.getDouble(COL_WEATHER_MIN_TEMP);
        String lowString = Utility.formatTemperature(getActivity(), low, isMetric);
        mLowTempView.setText(lowString);

        // Read humidity from cursor and update view
        float humidity = data.getFloat(COL_HUMIDIY);
        mHumidityView.setText(getActivity().getString(R.string.format_humidity, humidity));

        // Read wind speed and direction from cursor and update view
        float windSpeedStr = data.getFloat(COL_WIND_SPEED);
        float windDirStr = data.getFloat(COL_DEGREES);
        String wind = Utility.getFormattedWind(getActivity(), windSpeedStr, windDirStr);
        mWindView.setText(wind);

        /*

                TEMP, to test custom views
         */
        float degrees = windDirStr;
        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }

        mMyView.setDirection(direction);
        /*
            end of TEMP
         */
        // Read pressure from cursor and update view
        float pressure = data.getFloat(COL_PRESSURE);
        mPressureView.setText(getActivity().getString(R.string.format_pressure, pressure));

        //((TextView) getView().findViewById(R.id.detail_text)).setText(mForecastStr);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mForecastStr != null ) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "no forecast?");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MY_LOADER_ID, null, this);
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mForecastStr + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    void onLocationChanged(String newLocation) {
        Uri uri = mForecastUri;
        if (null != uri) {
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            mForecastUri = updatedUri;
            getLoaderManager().restartLoader(MY_LOADER_ID, null, this);
        }
    }
}