package com.shakeup.setofthree.contentprovider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;


/**
 * Created by Jayson on 4/12/2017.
 *
 * Database class setting up our content provider
 */

@Database(version = SetDatabase.VERSION)
public final class SetDatabase {

    public static final int VERSION = 1;

    @Table(ScoreColumns.class) public static final String SCORES = "scores";
}