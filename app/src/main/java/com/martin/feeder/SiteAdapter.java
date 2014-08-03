package com.martin.feeder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.ViewHolder> {

    private NewsCollection nColl;

    public SiteAdapter(NewsCollection nColl) {
        this.nColl = nColl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, null);
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
