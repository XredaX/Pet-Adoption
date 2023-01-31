package com.pet.adoption;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class rec_adapter extends RecyclerView.Adapter<rec_adapter.viewholder>{

    ArrayList<animal_profil> li = new ArrayList<>();
    recycleViewInterface item;

    public rec_adapter(ArrayList<animal_profil> li, recycleViewInterface item) {
        this.li = li;
        this.item = item ;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate =LayoutInflater.from(parent.getContext());

        View v = inflate.inflate(R.layout.itemanimal , parent,false);
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

            imageRecommendedAnimal1 = itemView.findViewById(R.id.imageRecommendedAnimal1);
            namePet1 = itemView.findViewById(R.id.namePet1);
            desc = itemView.findViewById(R.id.desc);
            loc = itemView.findViewById(R.id.loc);
            cardRecommended1 = itemView.findViewById(R.id.cardRecommended1);

            cardRecommended1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.onclick1(getAdapterPosition());
                }
            });
        }
        }
}
