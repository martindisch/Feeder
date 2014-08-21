package com.martin.feeder;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewsFragment extends Fragment {

    private OnProgressChangeListener mCallback;
    public RecyclerView mList;
    private NewsSources nSources;
    private NewsCollection nColl;
    private RelativeLayout mSnackbar;
    private TextView mSnackButton;

    public String name = "NewsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        mList = (RecyclerView) v.findViewById(R.id.rvList);
        mSnackButton = (TextView) v.findViewById(R.id.snackbar_button);
        mSnackbar = (RelativeLayout) v.findViewById(R.id.snackbar);
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

        mSnackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSnackbar();
            }
        });

        // Initialize Snackbar
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());
        mSnackbar.setY(mSnackbar.getY() + px);
        mSnackbar.setVisibility(mSnackbar.VISIBLE);
    }

    public void showSnackbar() {
        float px = -TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());
        moveSnackbar(px);
    }

    private void hideSnackbar() {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());
        moveSnackbar(px);
    }

    private void moveSnackbar(float px) {
        Path p = new Path();
        ObjectAnimator sbAnimator;
        p.moveTo(mSnackbar.getX(), mSnackbar.getY());
        p.rLineTo(0, px);
        sbAnimator = ObjectAnimator.ofFloat(mSnackbar, View.X, View.Y, p);
        sbAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        sbAnimator.start();
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
                           showSnackbar();
                        }
                        else {
                            nSources.setAllNotified(nColl);
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
