package com.shakeup.setofthree.contentprovider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Jayson on 4/12/2017.
 *
 * Interface listing the columns in the Score table
 */

public interface ScoreColumns {

    // The AutoNumber ID of the row
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    // The score in the form of a long. Supports both times and # Sets found
    @DataType(DataType.Type.REAL) @NotNull
    String SCORE = "score";

    // Timestamp of when the record was created.
    @DataType(DataType.Type.REAL) @NotNull
    String TIME = "time";

    // Game mode this score applies to
    @DataType(DataType.Type.TEXT) @NotNull
    String MODE = "mode";

    // Game difficulty the score applies to
    @DataType(DataType.Type.TEXT) @NotNull
    String DIFFICULTY = "difficulty";

    // Whether or not the score has been submitted to the Google Play Leaderboard
    // yet. 0 for no, 1 for yes.
    @DataType(DataType.Type.INTEGER) @NotNull
    String UPLOADED = "uploaded";

    // Default projection
    public static final String[] _ALL = {
            _ID,
            SCORE,
            TIME,
            MODE,
            DIFFICULTY,
            UPLOADED
    };
}
