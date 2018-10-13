package com.roman.themoviedb.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roman.themoviedb.R;
import com.roman.themoviedb.api.RequestsData;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by Roman on 07/04/2018.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {


    private LinkedHashMap<String, String> cast;
    private ArrayList<String> names = new ArrayList<>();
    private final Context context;
    private OnActorClickedListener listener;

    public CastAdapter(final Context context, @NotNull LinkedHashMap<String, String> cast, OnActorClickedListener listener) {
        this.context = context;
        this.cast = cast;
        this.listener = listener;
        Set<String> castKeySet = cast.keySet();
        names.addAll(castKeySet);
    }

    @NonNull
    @Override
    public CastAdapter.CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actor_layout, parent, false);
        return new CastAdapter.CastViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CastAdapter.CastViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.name.setText(names.get(position));
        try {
            Picasso.with(context)
                    .load(getPhotoURL(names.get(position)))
                    .into(holder.photo);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return names!=null ? names.size() : 0;
    }

    protected class CastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView photo;
        TextView name;

        CastViewHolder(View view) {
            super(view);
            this.photo = view.findViewById(R.id.photo);
            this.name = view.findViewById(R.id.name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onActorClicked(getAdapterPosition(), v);
        }
    }

    @NotNull
    private String getPhotoURL(String name){
        return RequestsData.CAST_IMAGE_BASE_URL+cast.get(name);
    }

    public interface OnActorClickedListener{
        void onActorClicked(int position, View v);
    }
}


