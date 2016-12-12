package pl.marcinolek.sunshine;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        String extra = intent.getStringExtra(Intent.EXTRA_TEXT);
        TextView tv = (TextView) rootView.findViewById(R.id.detailed_forecast);
        tv.setText(extra);


        setHasOptionsMenu(true);
        return rootView;
    }

    /*
     Intent shareIntent = new Intent(Intent.ACTION_SEND);
 shareIntent.setType("image/*");
 Uri uri = Uri.fromFile(new File(getFilesDir(), "foo.jpg"));
 shareIntent.putExtra(Intent.EXTRA_STREAM, uri));
     */

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private ShareActionProvider mShareActionProvider;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String extra = ((TextView) getView().findViewById(R.id.detailed_forecast)).getText().toString();
        shareIntent.putExtra(Intent.EXTRA_TEXT, extra + " " + getString(R.string.share_hashtag));
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        setShareIntent(shareIntent);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}