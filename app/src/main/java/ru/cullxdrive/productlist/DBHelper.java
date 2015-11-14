package ru.cullxdrive.productlist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


// Класс предоставяляющий интерфейс для работы с базой данных,
// а так же создающий базу данных в случае первого запуска программы
public class DBHelper extends SQLiteOpenHelper{

    public DBHelper(Context context){
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Recipe(" +                                                         //Таблица с рецептом
                "id integer primary key autoincrement," +
                "name text," +
                "description text," +
                "ingredient text," +
                "instruction text," +
                "favorites integer," +
                "url text," +
                "imageUrl text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<RecipeItem> getRecipes()                                                       //Получается список всех рецептов
    {
        ArrayList<RecipeItem> recipeItems = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query("Recipe", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idColIndex          = c.getColumnIndex("id");
            int nameColIndex        = c.getColumnIndex("name");
            int imageUrlColIndex    = c.getColumnIndex("imageUrl");
            int descriptionColIndex = c.getColumnIndex("description");
            int ingredientColIndex  = c.getColumnIndex("ingredient");
            int instructionColIndex = c.getColumnIndex("instruction");
            int favoritesColIndex   = c.getColumnIndex("favorites");
            int urlColIndex         = c.getColumnIndex("url");

            do {
                RecipeItem recipeItem = new RecipeItem();                                           //Создается новая карточка
                recipeItem.setName(c.getString(nameColIndex));                                      //- Устанавливается имя
                recipeItem.setImageUrl(c.getString(imageUrlColIndex));                              //- Устанавливается url картинки
                recipeItem.setUrl(c.getString(urlColIndex));                                        //- Устанавливается url сайта
                recipeItem.setDescription(c.getString(descriptionColIndex));                        //- Устанавливается описание
                recipeItem.setId(c.getInt(idColIndex));                                             //- Устанавливается id
                recipeItem.setIngredient(c.getString(ingredientColIndex));                          //- Устанавливается ингредиент
                recipeItem.setInstruction(c.getString(instructionColIndex));                        //- Устанавливается инструкцию
                recipeItem.setFavorites(c.getInt(favoritesColIndex) != 0);                          //- Избранное или нет

                recipeItems.add(recipeItem);                                                        //карточка добавляется в список
            } while (c.moveToNext());
        }
        c.close();
        return recipeItems;
    }


    public void addRecipe(RecipeItem item){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("url", item.getUrl());
        cv.put("instruction", item.getInstruction());
        cv.put("description", item.getDescription());
        cv.put("ingredient", item.getIngredient());
        cv.put("imageUrl", item.getImageUrl());
        cv.put("name", item.getName());
        cv.put("favorites", item.isFavorites()? 1 : 0);
        db.insert("Recipe", null, cv);
    }

    public void deleteRecipe(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from Recipe where id='" + id + "'");
    }

    public void setFavorits(int id, boolean value){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("favorites", value ? 1 : 0);
        int updCount = db.update("Recipe", cv, "id = ?",
            new String[] { String.valueOf(id) });
    }

    public ArrayList<RecipeItem> getFavoritsRecipes()                                               //Получается список всех рецептов
    {
        ArrayList<RecipeItem> recipeItems = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.query("recipe", null, "favorites = 1" , null, null, null, null);
        if (c.moveToFirst()) {
            int idColIndex          = c.getColumnIndex("id");
            int nameColIndex        = c.getColumnIndex("name");
            int imageUrlColIndex    = c.getColumnIndex("imageUrl");
            int descriptionColIndex = c.getColumnIndex("description");
            int ingredientColIndex  = c.getColumnIndex("ingredient");
            int instructionColIndex = c.getColumnIndex("instruction");
            int favoritesColIndex   = c.getColumnIndex("favorites");
            int urlColIndex         = c.getColumnIndex("url");

            do {
                RecipeItem recipeItem = new RecipeItem();                                           //Создается новая карточка
                recipeItem.setName(c.getString(nameColIndex));                                      //- Устанавливается имя
                recipeItem.setImageUrl(c.getString(imageUrlColIndex));                              //- Устанавливается url картинки
                recipeItem.setUrl(c.getString(urlColIndex));                                        //- Устанавливается url сайта
                recipeItem.setDescription(c.getString(descriptionColIndex));                        //- Устанавливается описание
                recipeItem.setId(c.getInt(idColIndex));                                             //- Устанавливается id
                recipeItem.setIngredient(c.getString(ingredientColIndex));                          //- Устанавливается ингредиент
                recipeItem.setInstruction(c.getString(instructionColIndex));                        //- Устанавливается инструкцию
                recipeItem.setFavorites(c.getInt(favoritesColIndex) != 0);                          //- Избранное или нет

                recipeItems.add(recipeItem);                                                        //карточка добавляется в список
            } while (c.moveToNext());
        }
        c.close();
        return recipeItems;
    }
}
