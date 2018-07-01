package com.example.khaled.shopz.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khaled.shopz.Interface.ItemClickListener;
import com.example.khaled.shopz.R;

public class ItemViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {


    public ImageView itemImage;
    public TextView itemName;
    public TextView itemPrice;
    public TextView itemDescription;
    public ImageButton editBtn;
    public ImageView deleteBtn;

    private ItemClickListener itemClickListener;

    public ItemViewHolder(View itemView) {
        super(itemView);

        itemImage = (ImageView) itemView.findViewById(R.id.card_image);
        itemName = (TextView) itemView.findViewById(R.id.card_name);
        itemPrice = (TextView) itemView.findViewById(R.id.card_price);
        itemDescription = (TextView) itemView.findViewById(R.id.card_description_tv);
        editBtn = (ImageButton) itemView.findViewById(R.id.edit_item_btn);
        deleteBtn = (ImageButton) itemView.findViewById(R.id.delete_item_btn);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onCLick(view , getAdapterPosition(),false);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemClickListener.onCLick(view , getAdapterPosition() , true);
                return true;
            }
        });

    }

    public void setItemclickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;

    }



    @Override
    public void onClick(View view) {

        itemClickListener.onCLick(view,getAdapterPosition(),false);


    }
}