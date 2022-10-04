package com.example.wguc196;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@androidx.room.Database(entities = {Term.class, Course.class, Assessment.class, Mentor.class}, exportSchema = false, version = 4)
@TypeConverters({Converters.class})
public abstract class fDatabase extends RoomDatabase {

    private static final String DB_NAME = "wgu_DB.db";
    private static fDatabase instance;

    public static synchronized fDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), fDatabase.class, DB_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }

        return instance;
    }


    public abstract TermDAO termDAO();

    public abstract CourseDAO courseDAO();

    public abstract AssessmentDAO assessmentDAO();

    public abstract MentorDAO mentorDAO();

}