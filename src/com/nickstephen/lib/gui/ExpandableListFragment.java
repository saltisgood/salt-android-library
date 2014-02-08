package com.nickstephen.lib.gui;

import android.view.View;
import android.widget.ExpandableListAdapter;

import org.holoeverywhere.widget.ExpandableListView;

/**
 * Created by Nick on 27/11/13.
 */
public abstract class ExpandableListFragment extends Fragment {
    private ExpandableListAdapter mAdapter;
    private ExpandableListView mListView;

    public ExpandableListFragment() {}

    public void setListAdapter(ExpandableListAdapter adapter) {
        View rootView = this.getView();
        if (rootView == null) {
            throw new RuntimeException("Fragment View not instantiated!");
        }

        setListAdapter(adapter, rootView);
    }

    public void setListAdapter(ExpandableListAdapter adapter, View rootView) {
        mListView = (ExpandableListView) rootView.findViewById(android.R.id.list);
        if (mListView == null) {
            throw new RuntimeException("No view found with id=android.R.id.list!");
        }
        mListView.setAdapter(mAdapter = adapter);
    }

    protected ExpandableListAdapter getAdapter() {
        return mAdapter;
    }

    protected ExpandableListView getListView() {
        return mListView;
    }
}
