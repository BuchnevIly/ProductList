package ru.cullxdrive.productlist.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.cullxdrive.productlist.CardAdapter;
import ru.cullxdrive.productlist.CardAdapter_local;
import ru.cullxdrive.productlist.DBHelper;
import ru.cullxdrive.productlist.R;
import ru.cullxdrive.productlist.RecipeItem;

public class FragmentMyRecipe extends Fragment {

    DBHelper dbHelper;                                                                              //База данных

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    List<RecipeItem> recipeItems;

    public FragmentMyRecipe() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipe, container, false);

        dbHelper = new DBHelper(view.getContext());
        recipeItems = dbHelper.getRecipes();
        if (!recipeItems.isEmpty()){

            View info = view.findViewById(R.id.my_recipe_info);
            info.setVisibility(View.GONE);

            mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(view.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new CardAdapter_local(recipeItems, getActivity(), false);
            mRecyclerView.setAdapter(mAdapter);
        }
        return view;
    }
}
