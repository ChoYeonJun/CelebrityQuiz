package com.example.celebrityquiz.firebaseAccess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.celebrityquiz.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RankAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Record> recordList;
    RankAdapter(Context context, List<Record> recordList){
        this.context = context;
        this.recordList = recordList;
    }
    @NonNull
    @Override
    // Build view layout and call ViewHolder, QuizHolder class
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View layoutInflater = LayoutInflater.from(context).
                inflate(R.layout.rank, viewGroup, false);
        return new RecyclerView.ViewHolder(layoutInflater) {};

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        TextView sequence = viewHolder.itemView.findViewById(R.id.sequence);
        TextView totalQuizNum = viewHolder.itemView.findViewById(R.id.totalQuizNum);
        TextView elapsedTime = viewHolder.itemView.findViewById(R.id.elapsedTime);
        TextView username = viewHolder.itemView.findViewById(R.id.username);
        if(!recordList.isEmpty()) {
            Record record = recordList.get(position);
            for (int i = 0; i < recordList.size(); i++)
                System.out.println(recordList.get(i).toString());
            // 오름차순 정렬



            username.setText(record.username);
            sequence.setText(Integer.valueOf(position+1).toString());
            totalQuizNum.setText("Right Quiz Counts: " + record.totalQuizNum);
            elapsedTime.setText(("Elapsed Times: " + record.elapsedTime));

        }

    }
    @Override
    public int getItemCount() {
        if (recordList == null) return 0;
        return recordList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}



