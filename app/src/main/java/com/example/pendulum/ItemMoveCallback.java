package com.example.pendulum;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemMoveCallback extends ItemTouchHelper.Callback {
    // PROPERTIES
    public final ItemTouchHelperContract adapter; // PaneActivityRecyclerAdapter

    // INTERFACES
    // ItemTouchHelperContract
    public interface ItemTouchHelperContract {
        void onBindViewHolder(PaneActivityRecyclerAdapter.ViewHolder viewHolder, int position);
        void onRowMoved(int fromPosition, int toPosition);
        void onTileSelected(PaneActivityRecyclerAdapter.ViewHolder viewHolder);
        void onTileClear(PaneActivityRecyclerAdapter.ViewHolder viewHolder);
    }

    // CONSTRUCTOR
    public ItemMoveCallback(ItemTouchHelperContract adapter) {
        this.adapter = adapter;

    }

    // isLongPressDragEnabled
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    // isItemViewSwipeEnabled
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    // getMovementFlags
    // Note: implementation required by abstract parent class
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // set allowed drag directions
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, 0);
    }

    // onSwiped
    // Note: implementation required by abstract parent class
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    // onMove
    // Note: implementation required by abstract parent class
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        adapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    // onSelectedChanged
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof PaneActivityRecyclerAdapter.ViewHolder) {
                PaneActivityRecyclerAdapter.ViewHolder myViewHolder=
                        (PaneActivityRecyclerAdapter.ViewHolder) viewHolder;
                adapter.onTileSelected(myViewHolder);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    // clearView
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof PaneActivityRecyclerAdapter.ViewHolder) {
            PaneActivityRecyclerAdapter.ViewHolder myViewHolder = (PaneActivityRecyclerAdapter.ViewHolder) viewHolder;
            adapter.onTileClear(myViewHolder);
        }
    }
}
