package com.martin.feeder;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.ViewHolder> {

    private NewsCollection nColl;
    private Fragment parent;
    private String parentName;

    public SiteAdapter(NewsCollection nColl, Fragment parent, String parentName) {
        this.nColl = nColl;
        this.parent = parent;
        this.parentName = parentName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, null);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = view.findViewById(R.id.tvTitle).getTag().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                parent.getActivity().startActivity(i);
                if (parentName.contentEquals("NewsFragment")) {
                    SharedPreferences spLibrary = parent.getActivity().getSharedPreferences("Library", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spLibrary.edit();
                    editor.putBoolean(((TextView) view.findViewById(R.id.tvTitle)).getText().toString(), true);
                    editor.apply();
                    nColl.removeItem(((NewsFragment) parent).mList.getChildPosition(view));
                    notifyDataSetChanged();
                }
            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.mTitle.setText(nColl.getTitles()[i]);
        viewHolder.mContent.setText(nColl.getContents()[i]);
        viewHolder.mTitle.setTag(nColl.getUrls()[i]);
    }

    @Override
    public int getItemCount() {
        return nColl.getTitles().length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle, mContent;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            mContent = (TextView) itemView.findViewById(R.id.tvContent);
        }
    }
}
