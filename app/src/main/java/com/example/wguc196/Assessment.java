package com.example.wguc196;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

/*@Entity(tableName = "assessment_table",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "course_id",
                childColumns = "course_id_fk",
                onDelete = CASCADE))*/

@Entity(tableName = "assessment_table",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = {"course_id"},
                childColumns = {"course_id_fk"},
                onDelete = CASCADE))

public class Assessment {

    /*public Assessment(int assessment_id, int course_id_fk, String assessment_title, Date assessment_goal, String assessment_type) {
        this.assessment_id = assessment_id;
        this.course_id_fk = course_id_fk;
        this.assessment_title = assessment_title;
        this.assessment_goal = assessment_goal;
        this.assessment_type = assessment_type;
    }*/

    public Assessment(){

    }

    @PrimaryKey(autoGenerate = true)
    private int assessment_id;

    @ColumnInfo(name = "course_id_fk", index = true)
    private int course_id_fk;

    @ColumnInfo(name = "assessment_title")
    private String assessment_title;

    @ColumnInfo(name = "assessment_goal")
    private Date assessment_goal;

    @ColumnInfo(name = "assessment_type")
    private String assessment_type;

    @ColumnInfo(name = "assessment_alarm")
    private Date assessment_alarm;

    public Date getAssessment_alarm() {
        return assessment_alarm;
    }

    public void setAssessment_alarm(Date assessment_alarm) {
        this.assessment_alarm = assessment_alarm;
    }

    public String getAssessment_status() {
        return assessment_status;
    }

    public void setAssessment_status(String assessment_status) {
        this.assessment_status = assessment_status;
    }

    @ColumnInfo(name = "assessment_status")
    private String assessment_status;

    public int getAssessment_id() {
        return assessment_id;
    }

    public void setAssessment_id(int assessment_id) {
        this.assessment_id = assessment_id;
    }

    public int getCourse_id_fk() {
        return course_id_fk;
    }

    public void setCourse_id_fk(int course_id_fk) {
        this.course_id_fk = course_id_fk;
    }

    public String getAssessment_title() {
        return assessment_title;
    }

    public void setAssessment_title(String assessment_title) {
        this.assessment_title = assessment_title;
    }

    public Date getAssessment_goal() {
        return assessment_goal;
    }

    public void setAssessment_goal(Date assessment_goal) {
        this.assessment_goal = assessment_goal;
    }

    public String getAssessment_type() {
        return assessment_type;
    }

    public void setAssessment_type(String assessment_type) {
        this.assessment_type = assessment_type;
    }

}