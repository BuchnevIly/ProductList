package ru.cullxdrive.productlist;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import ru.cullxdrive.productlist.Fragments.FragmentFavorites;
import ru.cullxdrive.productlist.Fragments.FragmentSearch;
import ru.cullxdrive.productlist.Fragments.FragmentMyRecipe;
import ru.cullxdrive.productlist.Fragments.FavoritesSetting;
import ru.cullxdrive.productlist.Fragments.FavoritesShoppingList;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

                                                                                                    //Фрагменты
    FragmentSearch fragmentSearch;                                                                  //- Поиск
    FragmentMyRecipe myRecipe;                                                                      //- Сохраненные рецепты
    FavoritesSetting setting;                                                                       //- Настройки
    FavoritesShoppingList shoppingList;                                                             //- Список ингредиентов для покупки
    FragmentFavorites favorites;                                                                    //- Избранное

    Toolbar toolbar;
    EditText editText;
    MenuItem itemSearch;

    Activity activity;

    FloatingActionButton fab;
    NavigationView navigationView;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
                                                                                                    //auto generate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {                                         //Открывается Активити с редактором
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ActivityRecipeEdit.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        dbHelper = new DBHelper(this);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        toolbar.setTitle("Мои рецепты");

        editText = (EditText)findViewById(R.id.myEditText);                                         //Инициализация элементов
        activity = this;

        editText.setVisibility(View.GONE);                                                          //Скрываются элементы toolBar необходимые для поиска


        fragmentSearch = new FragmentSearch();                                                      //Инициализация фрагментов
        myRecipe = new FragmentMyRecipe();
        setting = new FavoritesSetting();
        shoppingList = new FavoritesShoppingList();
        favorites = new FragmentFavorites();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();                  //Запуск фрагмента со списком рецептов
        transaction.replace(R.id.content, myRecipe);
        transaction.commit();

        navigationView.getMenu().getItem(0).setChecked(true);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        itemSearch = menu.findItem(R.id.search);
        itemSearch.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {                                                           //Выбран пункт меню настройки
            navigationView.getMenu().getItem(3).setChecked(true);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content, setting);
            transaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        fab.setVisibility(View.INVISIBLE);

        int id = item.getItemId();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (id == R.id.search) {                                                                    //Нажат пункт "Поиск"
            transaction.replace(R.id.content, fragmentSearch);                                      //Вставляю фрагмент с поиском
            toolbar.setTitle("");                                                                   //Скрываю текмтовый заголоваок
            editText.setVisibility(View.VISIBLE);                                                   //Отобоажаю EditText в toolBar
            itemSearch.setVisible(true);                                                            //Отобоажаю кнопку поиска
        } else {
            editText.setVisibility(View.GONE);                                                      //Скрываю EditText в toolBar
            itemSearch.setVisible(false);                                                           //Скрываю кнопку поиска
            if (id == R.id.favorites) {                                                             //Нажат пункт "Избранное"
                toolbar.setTitle("Избранное");
                transaction.replace(R.id.content, favorites);
            } else if (id == R.id.about) {                                                          //Нажат пункт "О программе"

                //TODO Переделать пенкт


                AlertDialog.Builder builder = new AlertDialog.Builder(activity);                    //-Создается диалоговое окно с информацией о прорамме
                builder.setMessage(R.string.dialog_message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                    });
                builder.create().show();

            } else if (id == R.id.my_recipe) {                                                      //Нажат пункт "Мои рецепты"
                toolbar.setTitle("Мои рецепты");
                transaction.replace(R.id.content, myRecipe);
                fab.setVisibility(View.VISIBLE);
            } else if (id == R.id.tools) {                                                          //Нажат пункт "Параметры"
                toolbar.setTitle("Параметры");
                transaction.replace(R.id.content, setting);
            } else if (id == R.id.shopping_list) {                                                  //Нажат пункт "Список покупок"
                toolbar.setTitle("Список покупок");
                transaction.replace(R.id.content, shoppingList);
            }
        }
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
