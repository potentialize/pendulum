package com.example.pendulum;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pendulum.database.entities.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaneActivityRecyclerAdapter
        extends RecyclerView.Adapter<PaneActivityRecyclerAdapter.ViewHolder>
        implements ItemMoveCallback.ItemTouchHelperContract {

    private static final String TAG = "PaneActivityRecyclerAdapter";





    // PROPERTIES
    private LayoutInflater mInflater;
    private PaneActivityViewModel mViewModel;
    private List<Tile> mTiles = new ArrayList<>();





    // CLASSES
    // ViewHolder manages individual tiles
    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView tileCard;
        TextView tileText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tileCard = itemView.findViewById(R.id.tile_card);
            tileText = itemView.findViewById(R.id.tile_text);
        }
    }





    // CONSTRUCTOR
    public PaneActivityRecyclerAdapter(PaneActivity context) {
        mInflater = LayoutInflater.from(context);
        mViewModel = context.mViewModel;
    }





    // onCreateViewHolder creates each tile view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = mInflater.inflate(R.layout.layout_tile, parent, false);
        return new ViewHolder(view);
    }





    // onBindViewHolder initializes each tile view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        if (mTiles != null) {
            Tile tile = mTiles.get(i);

            String debugText = "";
            debugText += "id: " + tile.id;
            debugText += "\ndb pos: " + tile.position;
//          debugText += "\npos: " + i;
//          debugText += "\n" + tile.name;

            holder.tileText.setText(debugText);
        } else {
            // data (tiles) not ready yet
        }
    }





    void setTiles(List<Tile> tiles) {
        mTiles = tiles;
        notifyDataSetChanged();
    }





    // getItemCount returns the amount of tiles
    @Override
    public int getItemCount() {
        if (mTiles != null) {
            return mTiles.size();
        } else return 0;
    }





    // MOVE INTERFACE IMPLEMENTED BELOW

    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            // move down
            for (int i = fromPosition; i < toPosition; i++) {
                // swap tiles in recycler view
                Collections.swap(mTiles, i, i + 1);
            }
        } else {
            // move up
            for (int i = fromPosition; i > toPosition; i--) {
                // swap tiles in recycler view
                Collections.swap(mTiles, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }





    public void onTileSelected(ViewHolder viewHolder) {
//        viewHolder.tileCard.setBackgroundResource(R.color.colorSurfaceActive);
    }





    public void onTileClear(ViewHolder viewHolder) {
//        viewHolder.tileCard.setBackgroundResource(R.color.colorSurface);

        // write new positions to db
        // NOTE: mTiles contains tiles whose indices indicate the new positions
        mViewModel.persistTilePositions(mTiles);
    }
}
