package com.rex.wepeiyang.ui.gpa.score;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.Gpa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/28.
 */
public class ScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Gpa.Data.Term.Course> dataSet = new ArrayList<>();
    private boolean isOrderByScore = true;
    private boolean isFirstTime = true;

    public ScoreAdapter(Context context) {
        this.context = context;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.tv_course_name)
        TextView tvCourseName;
        @InjectView(R.id.tv_credit)
        TextView tvCredit;
        @InjectView(R.id.tv_score)
        TextView tvScore;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ItemHolder(inflater.inflate(R.layout.item_my_score, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        Gpa.Data.Term.Course item = dataSet.get(position);
        itemHolder.tvCourseName.setText(item.name);
        itemHolder.tvCredit.setText(item.credit + "");
        itemHolder.tvScore.setText(item.score + "");
        if (isOrderByScore) {
            itemHolder.tvScore.setBackgroundColor(ContextCompat.getColor(context, R.color.divider_color));
            itemHolder.tvCredit.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            itemHolder.tvCredit.setBackgroundColor(ContextCompat.getColor(context, R.color.divider_color));
            itemHolder.tvScore.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void refreshItemsByScore(List<Gpa.Data.Term.Course> items) {
        if (isOrderByScore && !isFirstTime) {
            return;
        } else {
            isFirstTime = false;
            isOrderByScore = true;
            dataSet.clear();
            Collections.sort(items, new Comparator<Gpa.Data.Term.Course>() {
                @Override
                public int compare(Gpa.Data.Term.Course course, Gpa.Data.Term.Course t1) {
                    return (int) (t1.score - course.score);
                }
            });
            dataSet.addAll(items);
            notifyDataSetChanged();
        }

    }

    public void refreshItemsByCredit(List<Gpa.Data.Term.Course> items) {
        if (!isOrderByScore) {
            return;
        } else {
            isOrderByScore = false;
            dataSet.clear();
            Collections.sort(items, new Comparator<Gpa.Data.Term.Course>() {
                @Override
                public int compare(Gpa.Data.Term.Course course, Gpa.Data.Term.Course t1) {
                    return (int) (t1.credit - course.credit);
                }
            });
            dataSet.addAll(items);
            notifyDataSetChanged();
        }
    }
}