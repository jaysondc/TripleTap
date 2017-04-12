package com.shakeup.setofthree.ContentProvider;

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

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(DataType.Type.REAL) @NotNull
    String SCORE = "score";

    @DataType(DataType.Type.REAL) @NotNull
    String TIME = "time";

    @DataType(DataType.Type.TEXT) @NotNull
    String MODE = "mode";

    @DataType(DataType.Type.TEXT) @NotNull
    String DIFFICULTY = "difficulty";
}
