package ru.cullxdrive.productlist;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class ActivityRecipe extends AppCompatActivity {

    String url;                                                                                     //Адрес страницы
    String imageUrl;                                                                                //Адрес картинки
    String instruction;                                                                             //Текст инструкцыи
    String ingredient;                                                                              //Текст ингредиентов
    String name;                                                                                    //Название рецепта
    String description;                                                                             //Описание

    TextView textView;                                                                              //Текстовое поле с инструкцией
    TextView textViewWithIngredients;                                                               //Текстовое поле с ингредиентами
    TextView textViewUrl;                                                                           //Текстовое поле с ссылкой на сайт

    DBHelper dbHelper;                                                                              //База данных

    class LoadRecipe extends AsyncTask<Void, Void, Void> {                                          //Ассинхронная загрузка страницы


        @Override
        protected Void doInBackground(Void... params) {                                             //выполняется в фоне
            try {


                Document doc = Jsoup.connect(url).get();                                            //Загрузка страницы

                ingredient = "";                                                                    //Получение ингредиентов со страницы
                Elements elements = doc.getElementsByClass("ingredient");
                for (Element tr: elements)
                    ingredient += tr.text().toString() + "\n";

                instruction = "";                                                                   //Получение инструкции со страницы
                Element ui = doc.getElementsByClass("s-photo-multiply-gall-parent").first();        //-Получаю блок с инструкцией
                Elements li = ui.getElementsByTag("li");                                            //-Получаю все элементы списка
                for (Element element: li)
                    instruction += element.text().toString()+ "\n\n";

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {                                                 //После завершения фоновой работы
            super.onPostExecute(result);
            textView.setText(instruction);                                                          //Устанавливается в Activity текст с инструкция
            textViewWithIngredients.setText(ingredient);                                            //Устанавливается в Activity текст с ингредиентами
            textViewUrl.setText(url);                                                               //Устанавливается в Activity ссылку на источник


            textViewUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(myIntent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recept_open);                                              //Auto generate
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(getBaseContext());


        url = getIntent().getStringExtra("url");                                                    //Инициализация полей
        imageUrl = getIntent().getStringExtra("urlImg");

        textView = (TextView) findViewById(R.id.instruction);
        textViewWithIngredients = (TextView) findViewById(R.id.ingredients);
        textViewUrl = (TextView) findViewById(R.id.url);

        ImageView imageView = (ImageView)findViewById(R.id.image_recipe_tool_bar);                  //Установка картинки в ToolBar
        Picasso.with(getBaseContext()).load(imageUrl).into(imageView);

        name = getIntent().getStringExtra("name");
        setTitle(name);                                                                             //Меняю заголовок

        description = getIntent().getStringExtra("description");                                    //Получаю описание


        LoadRecipe lr = new LoadRecipe();                                                           //Запуск паралельного процесса с загрузкой страницей
        lr.execute();


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);                   //Сохраняю рецепт в базу
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RecipeItem item = new RecipeItem();
                item.setDescription(description);
                item.setFavorites(false);
                item.setInstruction(instruction);
                item.setName(name);
                item.setImageUrl(imageUrl);
                item.setUrl(url);
                item.setIngredient(ingredient);

                dbHelper.addRecipe(item);

                fab.setImageDrawable(getResources().getDrawable(R.drawable.edit_buttom));


                Snackbar.make(view, "Рецепт сохранен", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
