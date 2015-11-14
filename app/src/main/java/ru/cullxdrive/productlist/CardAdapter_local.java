package ru.cullxdrive.productlist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CardAdapter_local extends RecyclerView.Adapter<CardAdapter_local.ViewHolder> {
    Context mContext;
    List<RecipeItem> mItems;
    Activity activity;
    boolean isFavorite;

    public CardAdapter_local(List<RecipeItem> items, Activity activity, boolean favorite) {
        super();
        this.mItems = items;
        this.activity = activity;
        isFavorite = favorite;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.my_card_view_local, viewGroup, false);

        this.mContext = viewGroup.getContext();

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder( final ViewHolder viewHolder, int i) {
        final RecipeItem recipeItem = mItems.get(i);

        viewHolder.name.setText(recipeItem.getName());
        final ImageView imageView = viewHolder.image;
        Picasso.with(mContext).load(recipeItem.getImageUrl()).into(imageView);


        viewHolder.delete_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Вы действительно хотите удалить рецепт")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DBHelper dbHelper = new DBHelper(mContext);
                                dbHelper.deleteRecipe(recipeItem.getId());
                                ViewParent view = imageView.getParent();

                                if (view instanceof View) {
                                    ((View) view).setVisibility(View.GONE);
                                }
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
            }
        });

        viewHolder.favorite_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(mContext);
                recipeItem.setFavorites(!recipeItem.isFavorites());
                dbHelper.setFavorits(recipeItem.getId(), recipeItem.isFavorites());
                if (recipeItem.isFavorites())
                    viewHolder.favorite_bt.setImageDrawable(activity.getResources()
                        .getDrawable(R.drawable.button_favorit_on));
                else{
                    viewHolder.favorite_bt.setImageDrawable(activity.getResources()
                        .getDrawable(R.drawable.favorit_button));

                    if (isFavorite) {
                        ViewParent view = imageView.getParent();
                        if (view instanceof View) {
                            ((View) view).setVisibility(View.GONE);
                        }
                    }
                }
            }
        });


        viewHolder.share_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, recipeItem.getUrl());
                sendIntent.setType("text/plain");

                if (sendIntent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(sendIntent);
                }
            }
        });

        if (recipeItem.isFavorites())
            viewHolder.favorite_bt.setImageDrawable(activity.getResources()
                    .getDrawable(R.drawable.button_favorit_on));
        else
            viewHolder.favorite_bt.setImageDrawable(activity.getResources()
                    .getDrawable(R.drawable.favorit_button));

        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(activity.getBaseContext(), ActivityRecipe.class);
                intent.putExtra("url", recipeItem.getUrl());
                intent.putExtra("urlImg", recipeItem.getImageUrl());
                intent.putExtra("name", recipeItem.getName());
                intent.putExtra("type", "locale");
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView image;
        public TextView name;
        public ImageButton favorite_bt;
        public ImageButton delete_bt;
        public ImageButton share_bt;
        public ImageButton add_bt;

        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.card_image_local);
            name = (TextView) itemView.findViewById(R.id.card_name_local);
            favorite_bt = (ImageButton) itemView.findViewById(R.id.btFavorite);
            delete_bt = (ImageButton) itemView.findViewById(R.id.btDelete);;
            share_bt = (ImageButton) itemView.findViewById(R.id.btShare);;
            add_bt = (ImageButton) itemView.findViewById(R.id.btLis);;

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