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

public class noti_adapter extends RecyclerView.Adapter<noti_adapter.viewholder>{

    ArrayList<notifications> li = new ArrayList<>();
    recycleViewInterface item;

    public noti_adapter(ArrayList<notifications> li, recycleViewInterface item) {
        this.li = li;
        this.item = item ;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate =LayoutInflater.from(parent.getContext());

        View v = inflate.inflate(R.layout.itemnotification , parent,false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(viewholder holder, int position) {
        holder.titleNoti.setText(li.get(position).getTitleNoti());
        holder.dateNoti.setText(li.get(position).getDateNoti());
        holder.descNoti.setText(li.get(position).getDescNoti());
    }

    @Override
    public int getItemCount() {
        return li.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView titleNoti;
        TextView dateNoti;
        TextView descNoti;
        CardView cardRecommended1;

        public viewholder(View itemView) {
            super(itemView);

            titleNoti = itemView.findViewById(R.id.titleNoti);
            dateNoti = itemView.findViewById(R.id.dateNoti);
            descNoti = itemView.findViewById(R.id.descNoti);
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
