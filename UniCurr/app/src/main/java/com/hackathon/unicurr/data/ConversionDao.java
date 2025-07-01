package com.hackathon.unicurr.data;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ConversionDao {

    @Insert
    void insert(Conversion conversion);

    @Query("SELECT * FROM conversion_table ORDER BY timestamp DESC")
    List<Conversion> getAllConversions();

    @Query("DELETE FROM conversion_table")
    void deleteAll();
}