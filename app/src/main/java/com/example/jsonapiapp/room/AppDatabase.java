package com.example.jsonapiapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();


}
