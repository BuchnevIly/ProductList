package ru.cullxdrive.productlist.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ru.cullxdrive.productlist.CardAdapter;
import ru.cullxdrive.productlist.R;
import ru.cullxdrive.productlist.RecipeItem;


public class FragmentSearch extends Fragment {

    private Document getResultFrom_EdaRU( String searchText ){
        Document doc = null;
        try {
            searchText = URLEncoder.encode(searchText, "UTF-8");                                    //Перевод в URL encoded
            doc = Jsoup.connect("http://eda.ru/recipesearch?q=" + searchText).get();                //Загрузка страницы с результатом поиска
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }


    LayoutInflater layoutInflater;
    Context        context;
    EditText       searchEditText;
    View           searchButton;
    ProgressBar    progressBar;


    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    List<RecipeItem> recipeItems;

    class LoadFromSite extends AsyncTask<Void, Void, Void> {

        Document doc;
        String   searchText, name, url;

        public LoadFromSite(String stringWithSearchText){
            searchText = stringWithSearchText;
        }

        @Override
        protected Void doInBackground(Void... params) {
            doc = getResultFrom_EdaRU(searchText);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            progressBar.setVisibility(View.GONE);                                                   //Скрыть progressBar
            mRecyclerView.setVisibility(View.VISIBLE);                                              //Отобоазить view с карточками

            recipeItems = new ArrayList<>();                                                        //Инициализация писока с карточками


            Elements div  = doc.getElementsByTag("img");                                            //Получение всех картинок с сайта
            for (Element element: div) {

                name  = element.attr("alt");                                                        //Получение атрибутов тега img
                String photo = element.attr("src");
                url = element.parent().attr("href");                                                //Получение ссылки на рецепт
                if(!name.isEmpty()) {                                                               //Отсеивание картинок без рецептов
                    RecipeItem recipeItem = new RecipeItem();                                       //Создается новая карточка
                    recipeItem.setName(name);                                                       //- Устанавливается имя
                    recipeItem.setImageUrl(photo);                                                  //- Устанавливается url картинки
                    recipeItem.setUrl(url);                                                         //- Устанавливается url сайта
                    recipeItem.setDescription("Нет описания");                                      //- Устанавливается писание

                    recipeItems.add(recipeItem);                                                    //карточка добавляется в список
                }
            }

            mAdapter = new CardAdapter(recipeItems, getActivity());                                 //Вывод карточек
            mRecyclerView.setAdapter(mAdapter);
        }
    }


    public FragmentSearch() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        layoutInflater = inflater;                                                                  //Инициализация полей
        context = getActivity().getApplicationContext();
        searchEditText = (EditText)getActivity().findViewById(R.id.myEditText);
        searchButton = getActivity().findViewById(R.id.search);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.GONE);

        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        searchButton.setOnClickListener(new View.OnClickListener() {                                //Нажатие на кнопку "Поиск"
            @Override
            public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            String searchText = String.valueOf(searchEditText.getText());
            if (!searchText.isEmpty()) {
                LoadFromSite mt = new LoadFromSite(searchText);                                     //Загрузка страницы с результатом поиска
                mt.execute();
            }
            }
        });

        return view;
    }
}

