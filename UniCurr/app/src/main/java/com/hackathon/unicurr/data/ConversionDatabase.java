package com.hackathon.unicurr.data;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Conversion.class}, version = 1)
public abstract class ConversionDatabase extends RoomDatabase {

    private static volatile ConversionDatabase INSTANCE;

    public abstract ConversionDao conversionDao();

    public static ConversionDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ConversionDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ConversionDatabase.class, "conversion_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}