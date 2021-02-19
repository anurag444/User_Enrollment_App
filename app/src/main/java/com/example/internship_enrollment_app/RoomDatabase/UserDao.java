package com.example.internship_enrollment_app.RoomDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.internship_enrollment_app.Model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Delete
    void delete(User note);

    @Query("SELECT * FROM user_table")
    LiveData<List<User>> getAllUsers();
}
