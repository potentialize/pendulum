package com.example.pendulum;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PaneRecyclerAdapter extends RecyclerView.Adapter<PaneRecyclerAdapter.ViewHolder> {

    private static final String TAG = "PaneRecyclerAdapter";

    Context mContext;
    ArrayList<String> mTileTexts = new ArrayList<>();

    public PaneRecyclerAdapter(Context context, ArrayList<String> tileTexts) {
        mContext = context;
        mTileTexts = tileTexts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_tile, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called.");
        viewHolder.tileText.setText(mTileTexts.get(i));
    }

    @Override
    public int getItemCount() {
        return mTileTexts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tileText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tileText = itemView.findViewById(R.id.tile_text);
        }
    }
}
