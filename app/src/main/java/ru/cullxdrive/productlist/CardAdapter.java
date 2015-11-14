package ru.cullxdrive.productlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Илья on 04.11.2015.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{
    Context mContext;
    List<RecipeItem> mItems;
    Activity activity;

    public CardAdapter(List<RecipeItem> items, Activity activity) {
        super();
        this.mItems = items;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.my_card_view, viewGroup, false);

        this.mContext = viewGroup.getContext();

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final RecipeItem item = mItems.get(i);

        viewHolder.name.setText(item.getName());
        viewHolder.description.setText(item.getDescription());
        ImageView imageView = viewHolder.image;
        Picasso.with(mContext).load(item.getImageUrl()).into(imageView);

        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(activity.getBaseContext(), ActivityRecipe.class);
                intent.putExtra("url", item.getUrl());
                intent.putExtra("urlImg", item.getImageUrl());
                intent.putExtra("name", item.getName());
                intent.putExtra("description", item.getDescription());
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView image;
        public TextView name;
        public TextView description;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.card_image);
            name = (TextView)itemView.findViewById(R.id.card_name);
            description = (TextView)itemView.findViewById(R.id.card_description);

            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }
    }
}
