package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {


    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }


    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE = 1;
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }


    private boolean mUseTodayLayout;

    public void setmUseTodayLayout(boolean mUseTodayLayout) {
        this.mUseTodayLayout = mUseTodayLayout;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 && mUseTodayLayout) {
            return VIEW_TYPE_TODAY;
        }
        else {
            return VIEW_TYPE_FUTURE;
        }
    }

//    /**
//     * Prepare the weather high/lows for presentation.
//     */
//    private String formatHighLows(Context context, double high, double low) {
//        boolean isMetric = Utility.isMetric(mContext);
//        String highLowStr = Utility.formatTemperature(context, high, isMetric) + "/" + Utility.formatTemperature(context, low, isMetric);
//        return highLowStr;
//    }

//    /*
//        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
//        string.
//     */
//    private String convertCursorRowToUXFormat(Cursor cursor) {
//        // get row indices for our cursor
//        int idx_max_temp = ForecastFragment.COL_WEATHER_MAX_TEMP; cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
//        int idx_min_temp = ForecastFragment.COL_WEATHER_MIN_TEMP;
//        int idx_date = ForecastFragment.COL_WEATHER_DATE;
//        int idx_short_desc = ForecastFragment.COL_WEATHER_DESC;
//
//        String highAndLow = formatHighLows(
//                cursor.getDouble(idx_max_temp),
//                cursor.getDouble(idx_min_temp));
//
//        return Utility.formatDate(cursor.getLong(idx_date)) +
//                " - " + cursor.getString(idx_short_desc) +
//                " - " + highAndLow;
//    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int position = cursor.getPosition();
        int viewType = getItemViewType(position);
        //int layoutId = (viewType == VIEW_TYPE_TODAY) ? R.layout.list_item_forecast : R.layout.detail_list_item_forecast;
        int layoutId = -1;
        if(viewType == VIEW_TYPE_TODAY) {
            layoutId = R.layout.detail_list_item_forecast;
        } else if(viewType == VIEW_TYPE_FUTURE) {
            layoutId = R.layout.list_item_forecast;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        view.setTag(new ViewHolder(view));

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder)view.getTag();

        // Read weather icon ID from cursor
        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);

        int viewType = getItemViewType(cursor.getPosition());
       /* if(viewType == VIEW_TYPE_TODAY) {
            viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
        } else if(viewType == VIEW_TYPE_FUTURE) {
            viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(weatherId));
        }*/

        Glide.with(context)
                .load(Utility.getArtUrlForWeatherCondition(context, weatherId))
                .error(Utility.getArtResourceForWeatherCondition(weatherId))
                .into(viewHolder.iconView);


        // TODO Read date from cursor

        long date = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        viewHolder.dateView.setText(Utility.getFriendlyDayString(context, date));

        // TODO Read weather forecast from cursor

        String forecast = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
       viewHolder.descriptionView.setText(forecast);

        viewHolder.iconView.setContentDescription("icon: " + forecast);
        // Read user preference for metric or imperial temperature units
        boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        viewHolder.highTempView.setText(Utility.formatTemperature(context, high, isMetric));
        viewHolder.highTempView.setContentDescription(context.getString(R.string.maximum_temperature, viewHolder.highTempView.getText()));
        // TODO Read low temperature from cursor
        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        viewHolder.lowTempView.setText(Utility.formatTemperature(context, low, isMetric));
        viewHolder.lowTempView.setContentDescription(context.getString(R.string.minimum_temperature, viewHolder.lowTempView.getText()));
    }
}