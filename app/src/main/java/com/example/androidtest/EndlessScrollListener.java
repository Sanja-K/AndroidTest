package com.example.androidtest;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.ListView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    public static final int UNKNOWN_SCROLL_STATE = Integer.MIN_VALUE;
    private static final String TAG ="TAG" ;


    public static boolean loading;

    public EndlessScrollListener(boolean loading) {
        this.loading = loading;
        Log.d(TAG, "SCROLL_STATE_DRAGGING "+ loading);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        int listViewState;
        switch (newState) {
            case RecyclerView.SCROLL_STATE_DRAGGING:

                listViewState = ListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
                Log.d(TAG, "SCROLL_STATE_DRAGGING "+ listViewState);
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                listViewState = ListView.OnScrollListener.SCROLL_STATE_IDLE;
                Log.d(TAG, "SCROLL_STATE_IDLE "+ listViewState);
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                listViewState = ListView.OnScrollListener.SCROLL_STATE_FLING;
                Log.d(TAG, "SCROLL_STATE_SETTLING "+ listViewState);
                break;
            default:
                listViewState = UNKNOWN_SCROLL_STATE;
        }
/**Проверка на положение скрола 1 - до низа -1 до верха: возвращает false если достиг края*/
      //  recyclerView.canScrollVertically(-1);
        Log.d(TAG, "ДОШЁЛ ДО КОНЦА canScrollVertically(-1) "+ recyclerView.canScrollVertically(1));
        Log.d(TAG, "listViewState "+ listViewState);

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

         int pastVisibleItems = 0, visibleItemCount, totalItemCount;

        StaggeredGridLayoutManager mLayoutManager =
                (StaggeredGridLayoutManager) recyclerView.getLayoutManager();

        visibleItemCount = mLayoutManager.getChildCount();
        totalItemCount = mLayoutManager.getItemCount();
        int[] firstVisibleItems = null;
        firstVisibleItems = mLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
        if(firstVisibleItems != null && firstVisibleItems.length > 0) {
            pastVisibleItems = firstVisibleItems[0];
        }


        if (loading) {
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                loading = false;
                Log.d(TAG, "LOAD NEXT ITEM");

                loadMoreItem();
            }
        }

        Log.d(TAG, "onScrolled "+ dx +" dy "+ dy);

    }

    public abstract void loadMoreItem();
}
