package com.example.wguc196;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssessmentDAO {

    @Query("SELECT * FROM assessment_table WHERE course_id_fk = :courseId")
    List<Assessment> getAssessmentsByCourseId(int courseId);

    @Query("SELECT * FROM assessment_table ORDER BY :courseId")
    List<Assessment> getAllAssessmentsOrderById(int courseId);

    //@Query("INSERT INTO assessment_table VALUES(:assessment)")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAssessment(Assessment assessment);

    @Query("SELECT * FROM assessment_table")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM assessment_table WHERE assessment_id = :assId")
    Assessment getSingleAssessment(int assId);

    @Insert
    void insertAssessment(Assessment assessment);

    @Insert
    void insertAssessment(Assessment... assessment);

    @Update
    void updateAssessment(Assessment assessment);

    @Delete
    void deleteAssessment(Assessment assessment);

    @Query("DELETE FROM assessment_table")
    public void nukeAssessmentTable();

}

