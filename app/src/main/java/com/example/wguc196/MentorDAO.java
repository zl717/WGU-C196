package com.example.wguc196;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MentorDAO {

    @Insert
    void insertMentor(Mentor mentor);

    @Insert
    void insertAllMentors(Mentor... mentors);

    @Delete
    void deleteMentor(Mentor mentor);

    @Update
    void updateMentor(Mentor mentor);

    @Query("SELECT * FROM mentor_table WHERE mentor_id = :mentorID")
    Mentor getMentorByID(int mentorID);

    @Query("SELECT * FROM mentor_table WHERE course_id_fk = :courseID")
    List<Mentor> getMentorByCourse(int courseID);

    @Query("DELETE FROM mentor_table")
    void nukeMentorTable();


}