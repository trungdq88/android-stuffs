package com.hac.apps.khmermovie.components;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.util.Collection;

/**
 * Created by dinhquangtrung on 10/14/14.
 * InfinityGridView
 * When users scroll to the bottom, there will be displayed a button, click to the
 * button will load more item to the grid.
 * Make sure you implement IInfinityAdapter for the adapter to use in this GridView
 */
public class InfinityGridView extends GridView implements AbsListView.OnScrollListener {
    /**
     * Number of items per loading
     */
    private int numPerLoad = 10;
    /**
     * Flag to see if the grid is loading
     */
    private boolean isLoading = false;
    /**
     * Temporary collection to store loaded items each loading
     */
    private Collection _loadedItems;

    /**
     * Responsible to send message to UI thread after heavy item loading
     */
    LoadingHandler mHandler;

    /**
     * Need this variable (instead of getAdapter()) to call notifyDataSetChanged()
     */
    private BaseAdapter adapter;

    /**
     * Loading view, will be set to VISIBLE when loading
     */
    private View loadingView;

    /**
     * Load more button, will be set to VISIBLE when user scroll down the grid's bottom
     */
    private View loadMoreBtn;


    /**
     * Constructors
     * @param context
     */
    public InfinityGridView(Context context) {
        super(context);
        setUpGridView();
    }

    public InfinityGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpGridView();
    }

    public InfinityGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUpGridView();
    }

    /**
     * Set onScroll listener to detech when user scroll to bottom
     * Initiate handler
     */
    private void setUpGridView() {
        setOnScrollListener(this);
        mHandler = new LoadingHandler();
    }

    /**
     * Number of item per loading
     * @param numPerLoad
     */
    public void setNumPerLoad(int numPerLoad) {
        this.numPerLoad = numPerLoad;
    }

    /**
     * This view will be set to VISIBLE when the new items are loading
     * @param loadingView
     */
    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }

    /**
     * Set event for load more button
     * @param loadMoreBtn
     */
    public void setLoadMoreBtn(final View loadMoreBtn) {
        this.loadMoreBtn = loadMoreBtn;
        this.loadMoreBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loadItems(adapter.getCount(), numPerLoad);
                if (loadMoreBtn != null) loadMoreBtn.setVisibility(GONE);
                if (loadingView != null) loadingView.setVisibility(VISIBLE);
            }
        });
    }


    /**
     * Set adapter and load firstLoadingCount items
     * @param _adapter
     */
    @Override
    public void setAdapter(ListAdapter _adapter) {
        super.setAdapter(_adapter);
        if (_adapter == null) return;

        this.adapter = (BaseAdapter) _adapter;
        if (adapter.getCount() == 0) {
            loadItems(0, numPerLoad);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Load item at position `index`, takes `num` result(s)
     * and then add them to the list
     *
     * @param index position of the first item to load
     * @param num number of items to load
     */
    private void loadItems(final int index, final int num) {
        /**  waiting for ending session */
        if (isLoading) return;
        isLoading = true;
        // Add loading effect
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Just to make sure the list is not loading too fast
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                _loadedItems = ((IInfinityAdapter) adapter).loadMore(index, num);
                // Notify data set to UI
                mHandler.sendMessage(mHandler.obtainMessage());
            }
        });
        t.start();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    /**
     * Detect if users have scroll the grid view to bottom, if so, display the load more button
     * @param absListView
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (loadMoreBtn == null || !((IInfinityAdapter) adapter).hasMoreData()) {
            return;
        }

        // get the first Item that currently hide and need to show
        final int firstItemHide = firstVisibleItem + visibleItemCount;
        // Log.d("InfinityGridView", "FirstVisibleItem:" + firstVisibleItem + "  VisibleItemCount:"
        //         + visibleItemCount + "  TotalItemCount:" + totalItemCount);
        if (firstItemHide >= totalItemCount) {
            // Log.d("InfinityGridView", "Load more at " + totalItemCount);
            if (loadMoreBtn.getVisibility() == GONE) {
                loadMoreBtn.setVisibility(VISIBLE);
            }
        } else {
            if (loadMoreBtn.getVisibility() == VISIBLE) {
                loadMoreBtn.setVisibility(GONE);
            }
        }
    }


    /**
     * Responsible to send message to UI thread after heavy item loading
     */
    private class LoadingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            isLoading = false;
            ((IInfinityAdapter) adapter).addItems(_loadedItems);
            _loadedItems.clear();
            adapter.notifyDataSetChanged();
            if (loadingView != null) loadingView.setVisibility(GONE);
        }
    }
    /**
     * This list view require IInfinityAdapter for its adapter
     */
    public interface IInfinityAdapter {
        public void addItems(Collection<Object> objs);

        public Collection loadMore(int offset, int count);

        public boolean hasMoreData();
    }

}
