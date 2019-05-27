package com.example.pendulum.paneActivity;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pendulum.R;
import com.example.pendulum.database.entities.Tile;
import com.example.pendulum.timerActivity.TimerActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaneActivityRecyclerAdapter
        extends RecyclerView.Adapter<PaneActivityRecyclerAdapter.TileViewHolder>
        implements ItemMoveCallback.ItemTouchHelperContract {

    private static final String TAG = "PaneActivityRecyclerAdapter";





    // PROPERTIES
    private LayoutInflater mInflater;
    private PaneActivityViewModel mViewModel;
    private PaneActivity mContext;

    private List<Tile> mTiles = new ArrayList<>();
    private List<Long> mActiveTileIds = new ArrayList<>();
    private List<Long> mSelectedTileIds = new ArrayList<>();





    // CONSTRUCTOR
    public PaneActivityRecyclerAdapter(PaneActivity context) {
        mContext = context;
        mViewModel = context.mViewModel;
        mInflater = LayoutInflater.from(context);
    }





    // onCreateViewHolder creates each tile view
    @NonNull
    @Override
    public TileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = mInflater.inflate(R.layout.layout_tile, parent, false);
        return new TileViewHolder(view);
    }





    // onBindViewHolder initializes each tile view
    @Override
    public void onBindViewHolder(@NonNull TileViewHolder holder, int i) {
        if (mTiles != null) {
            Tile tile = mTiles.get(i);

            String debugText =
                    tile.name
                    + "\nid: " + tile.id
                    + " pos: " + tile.position;

            holder.tileText.setText(debugText);

//            if (mActiveTileIds.contains(tile.id)) {
//                Log.d(TAG, "onBindViewHolder: DRAW ACTIVE " + tile.id + "==========================================================");
//                holder.tileCard.setBackgroundResource(R.color.colorSurfaceActive);
//            } else
            if (mSelectedTileIds.contains(tile.id)) {
//                Log.d(TAG, "onBindViewHolder: DRAW SELECTED " + tile.id + "==========================================================");
                holder.tileCard.setBackgroundResource(R.color.colorSurfaceSelected);
            } else {
//                Log.d(TAG, "onBindViewHolder: DRAW DEFAULT " + tile.id + "==========================================================");
                holder.tileCard.setBackgroundResource(R.color.colorSurface);
            }
        } else {
            // data (tiles) not ready yet
        }
    }





    void setTiles(List<Tile> tiles) {
//        Log.d(TAG, "setTiles: RECEIVE NEW TILES ******************************************************");
        mTiles = tiles;
        // TODO: optimalize?
        notifyDataSetChanged();
    }

    void setActiveTileIds(List<Long> ids) {
        mActiveTileIds = ids;
        // TODO: optimalize?
        notifyDataSetChanged();
    }

    void setSelectedTileIds(List<Long> ids) {
        mSelectedTileIds = ids;
        // TODO: optimalize?
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

    public void onTileMoved(TileViewHolder viewHolder, TileViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();

        // move
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





    public void onTileSelected(TileViewHolder viewHolder) {
        // hold tile visual feedback
        viewHolder.tileCard.setBackgroundResource(R.color.colorSurfaceActive);
    }





    public void onTileClear(TileViewHolder viewHolder) {
        // release tile visual feedback
        viewHolder.tileCard.setBackgroundResource(R.color.colorSurface);

        Long tileId = viewHolder.getTile().id;

        // select tile
        if (!mViewModel.isTileSelected(tileId)) {
            mViewModel.setTileSelected(tileId, true);
        }

        // write new positions to db
        // NOTE: mTiles contains tiles whose indices indicate the new positions
        mViewModel.persistTilePositions(mTiles);
    }





    // CLASSES
    // TileViewHolder manages individual tiles
    // NOTE: recycled => cannot contain data!
    public class TileViewHolder extends RecyclerView.ViewHolder {

        CardView tileCard;
        TextView tileText;

        public TileViewHolder(@NonNull View itemView) {
            super(itemView);

            tileCard = itemView.findViewById(R.id.tile_card);
            tileText = itemView.findViewById(R.id.tile_text);

            // handle clicks
            itemView.setOnClickListener((View v) -> {
                Long tileId = this.getTile().id;

                // selected tile => deselect
                if (mViewModel.isTileSelected(tileId)) {
                    mViewModel.setTileSelected(tileId, false);
                } else {
                    // open timer
                    mContext.openTimerActivity(tileId);
                }
            });

            itemView.setOnLongClickListener((View v) -> {
                // consume long click => click not fired anymore
                return true;
            });
        }

        public Tile getTile() {
            return mTiles.get(getAdapterPosition());
        }
    }
}
