package com.martin.feeder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

public class SourcesFragment extends Fragment {

    private RecyclerView mList;
    private Spinner mSpinner;
    private OnProgressChangeListener mCallback;
    private NewsSources nSources;
    private NewsCollection nColl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sources, container, false);
        mList = (RecyclerView) v.findViewById(R.id.rvList);
        mSpinner = (Spinner) v.findViewById(R.id.spSources);
        return v;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mList.setLayoutManager(layoutManager);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                if (mCallback.changeVisibility()) {
                    getActivity().setProgressBarIndeterminateVisibility(true);
                }
                mCallback.actionStarted();
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        switch (i) {
                            case 0:
                                nColl = nSources.getA3_News();
                                break;
                            case 1:
                                nColl = nSources.getA3_Devhub();
                                break;
                            case 2:
                                nColl = nSources.getSpaceEngineers_News();
                                break;
                            case 3:
                                nColl = nSources.getLayer_News();
                                break;
                        }
                        mList.post(new Runnable() {

                            @Override
                            public void run() {
                                mList.setAdapter(new SiteAdapter(nColl, getActivity()));
                                mCallback.actionFinished();
                                if (mCallback.changeVisibility()) {
                                    getActivity().setProgressBarIndeterminateVisibility(false);
                                }
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        nSources = new NewsSources(getActivity());
    }
}
