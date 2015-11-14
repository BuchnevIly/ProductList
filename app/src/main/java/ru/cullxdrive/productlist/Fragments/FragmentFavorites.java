package ru.cullxdrive.productlist.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.cullxdrive.productlist.CardAdapter_local;
import ru.cullxdrive.productlist.DBHelper;
import ru.cullxdrive.productlist.R;
import ru.cullxdrive.productlist.RecipeItem;

public class FragmentFavorites extends Fragment {

    public FragmentFavorites() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorits, container, false);


        DBHelper dbHelper = new DBHelper(view.getContext());
        ArrayList<RecipeItem> recipeItems = dbHelper.getFavoritsRecipes();
        if (!recipeItems.isEmpty()){
            View info = view.findViewById(R.id.favorites_info);
            info.setVisibility(View.GONE);

            RecyclerView mRecyclerView;
            RecyclerView.LayoutManager mLayoutManager;
            RecyclerView.Adapter mAdapter;

            mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(view.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new CardAdapter_local(recipeItems, getActivity(), true);
            mRecyclerView.setAdapter(mAdapter);
        }

        return view;
    }
}
