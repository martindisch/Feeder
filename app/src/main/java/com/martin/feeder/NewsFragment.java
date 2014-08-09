package com.martin.feeder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class NewsFragment extends Fragment {

    private OnProgressChangeListener mCallback;
    public RecyclerView mList;
    private NewsSources nSources;
    private NewsCollection nColl;

    public String name = "NewsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        mList = (RecyclerView) v.findViewById(R.id.rvList);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mList.setLayoutManager(layoutManager);
        nSources = new NewsSources(getActivity());
        loadUnread();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnProgressChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnProgressChangeListener");
        }
    }

    public void loadUnread() {
        if (mCallback.changeVisibility()) {
            getActivity().setProgressBarIndeterminateVisibility(true);
        }
        mCallback.actionStarted();
        new Thread(new Runnable() {

            @Override
            public void run() {
                nColl = nSources.getAllUnread();
                mList.post(new Runnable() {

                    @Override
                    public void run() {
                        mList.setAdapter(new SiteAdapter(nColl, ((Main) getActivity()).findFragmentByPosition(0), "NewsFragment"));
                        if (nColl.getTitles().length == 0) {
                            Toast.makeText(getActivity(), "No news", Toast.LENGTH_SHORT).show();
                        }
                        mCallback.actionFinished();
                        if (mCallback.changeVisibility()) {
                            getActivity().setProgressBarIndeterminateVisibility(false);
                        }
                    }

                });
            }

        }).start();
    }
}
