package com.yotor.solution.mytimetable.ui.home.recycler;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private int mCurrentPosition;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public void onBind(int pos){
        mCurrentPosition = pos;
    }

    public int getmCurrentPosition() {
        return mCurrentPosition;
    }
}
