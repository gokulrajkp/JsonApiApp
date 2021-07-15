package com.example.jsonapiapp.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM RECIPE")
    List<Recipe> getAllUsers();

    @Insert
    void insert(Recipe recipe);

}
