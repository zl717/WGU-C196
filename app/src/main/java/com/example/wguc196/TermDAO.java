package com.example.wguc196;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TermDAO {

    @Query("SELECT * FROM term_table ORDER BY term_id")
    List<Term> getTermList();

    //order by what
    @Query("SELECT * FROM term_table WHERE term_id = :termId ORDER BY term_id")
    Term getTerm(int termId);

    @Insert
    void insertTerm(Term term);

    @Insert
    void insertAll(Term... term);

    @Update
    void updateTerm(Term term);

    @Delete
    void deleteTerm(Term term);

    @Query("DELETE FROM term_table")
    public void nukeTermTable();
}

