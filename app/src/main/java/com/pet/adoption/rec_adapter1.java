package com.pet.adoption;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class rec_adapter1 extends RecyclerView.Adapter<rec_adapter1.viewholder>{

    ArrayList<animal_profil> li = new ArrayList<>();
    recycleViewInterface item;

    public rec_adapter1(ArrayList<animal_profil> li, recycleViewInterface item) {
        this.li = li;
        this.item = item ;
    }

    public void setFilterList(ArrayList<animal_profil> animal_profilList){
        this.li = animal_profilList;
        notifyDataSetChanged();
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate =LayoutInflater.from(parent.getContext());

        View v = inflate.inflate(R.layout.allanimal , parent,false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(viewholder holder, int position) {
        Picasso.get().load(li.get(position).image).into(holder.imageRecommendedAnimal1);
        holder.namePet1.setText(li.get(position).nom);
        holder.loc.setText(li.get(position).loc);
        holder.desc.setText(li.get(position).desc);
    }

    @Override
    public int getItemCount() {
        return li.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        RoundedImageView imageRecommendedAnimal1;
        TextView namePet1;
        TextView desc;
        TextView loc;
        CardView cardRecommended1;

        public viewholder(View itemView) {
            super(itemView);

            imageRecommendedAnimal1 = itemView.findViewById(R.id.imageAnimal);
            namePet1 = itemView.findViewById(R.id.nameAnimal);
            desc = itemView.findViewById(R.id.descAnimal);
            loc = itemView.findViewById(R.id.locAnimal);
            cardRecommended1 = itemView.findViewById(R.id.cardAnimal);

            cardRecommended1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.onclick1(getAdapterPosition());
                }
            });
        }
        }
}
