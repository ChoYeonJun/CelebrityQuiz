package com.example.celebrityquiz.firebaseAccess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.celebrityquiz.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RankAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Rank> rankList;
    RankAdapter(Context context, List<Rank> rankList){
        this.context = context;
        this.rankList = rankList;
    }
    @NonNull
    @Override
    // Build view layout and call ViewHolder, QuizHolder class
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View layoutInflater = LayoutInflater.from(context).
                inflate(R.layout.solution, viewGroup, false);
        return new RecyclerView.ViewHolder(layoutInflater) {};
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

    }
    @Override
    public int getItemCount() {
        if (rankList == null) return 0;
        return rankList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
