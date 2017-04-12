package com.shakeup.setofthree.ContentProvider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Jayson on 4/12/2017.
 */

@ContentProvider(authority = ScoreProvider.AUTHORITY, database = SetDatabase.class)
public final class ScoreProvider {

    public static final String AUTHORITY = "com.shakeup.setofthree.provider";

    @TableEndpoint(table = SetDatabase.SCORES)
    public static class Scores {

        @ContentUri(
                path = "scores",
                type = "vnd.android.cursor.dir/score",
                defaultSort = ScoreColumns.SCORE + " DESC")
        public static final Uri SCORES = Uri.parse("content://" + AUTHORITY + "/scores");
    }
}
