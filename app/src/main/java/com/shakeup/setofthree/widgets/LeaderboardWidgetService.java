package com.shakeup.setofthree.widgets;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.shakeup.setofthree.R;
import com.shakeup.setofthree.common.contentprovider.ScoreColumns;
import com.shakeup.setofthree.common.contentprovider.ScoreProvider;
import com.shakeup.setofthree.common.utilities.Utilities;

import java.util.Locale;

/**
 * Created by Jayson on 4/19/2017.
 * <p>
 * This class handles the creation and use of a RemoteViewsFactory to bind data to
 * the RemoveViews of our widget
 */

public class LeaderboardWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new LeaderboardRemoteViewsFactory(getApplicationContext(), intent);
    }


    class LeaderboardRemoteViewsFactory implements RemoteViewsFactory {

        private Context mContext;
        private int mAppWidgetId;
        private Cursor mCursor;

        public LeaderboardRemoteViewsFactory(Context context, Intent intent) {
            // Grab a reference to the context and the ID of our widget
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            // Set up data cursor
            ContentResolver contentResolver = mContext.getContentResolver();

            // Set up selection string
            String selection =
                    ScoreColumns.MODE
                            + "=? AND "
                            + ScoreColumns.DIFFICULTY
                            + "=?";

            // Set up selection args
            String[] selectionArgs = new String[2];
            selectionArgs[0] = getString(R.string.value_mode_normal);
            selectionArgs[1] = getString(R.string.value_difficulty_normal);

            // Set up the sort order string
            String sortOrder = ScoreColumns.SCORE + " ASC, "
                    + ScoreColumns.TIME + " DESC";

            // Create the cursor for our scores, sorted in ascending order
            mCursor = contentResolver.query(
                    ScoreProvider.Scores.SCORES,
                    ScoreColumns._ALL,
                    selection,
                    selectionArgs,
                    sortOrder
            );
        }

        @Override
        public void onDataSetChanged() {
            // Do nothing
        }

        @Override
        public void onDestroy() {
            if (!mCursor.isClosed()) {
                mCursor.close();
            }
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            // Create our remote view
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.listitem_widget_score);

            mCursor.moveToPosition(position);
            // Get column indexes
            int scoreIndex = mCursor.getColumnIndex(ScoreColumns.SCORE);
            int timeIndex = mCursor.getColumnIndex(ScoreColumns.TIME);

            // Set textviews
            rv.setTextViewText(
                    R.id.text_position,
                    String.format(Locale.getDefault(), "%d", position + 1));
            rv.setTextViewText(
                    R.id.text_score,
                    Utilities.scoreTimeToString(mCursor.getLong(scoreIndex)));
            rv.setTextViewText(
                    R.id.text_date,
                    Utilities.dateToString(mCursor.getLong(scoreIndex)));

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            // We don't provide any loading view
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            int columnIndex = mCursor.getColumnIndex(ScoreColumns._ID);

            return mCursor.getLong(columnIndex);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
