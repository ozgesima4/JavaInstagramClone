package com.sima.javainstagramclone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sima.javainstagramclone.Model.Post;
import com.sima.javainstagramclone.databinding.RecyclerrowBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private ArrayList<Post>postArrayList;

    public RecyclerAdapter(ArrayList<Post> postArrayList) {
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerrowBinding binding=RecyclerrowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new RecyclerHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        holder.binding.usercomment.setText(postArrayList.get(position).Comment);
        holder.binding.username.setText(postArrayList.get(position).Mail);

        Picasso.get().load(postArrayList.get(position).DownloadUrl).into(holder.binding.imageviewRecycler);  //picasso sın ıfını implement et o verideki urlyi indirip foto ypacak göstercek

    }
    @Override
    public int getItemCount() {
        return postArrayList.size();
    }
    class  RecyclerHolder extends RecyclerView.ViewHolder{
        RecyclerrowBinding binding;
        public RecyclerHolder(RecyclerrowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
