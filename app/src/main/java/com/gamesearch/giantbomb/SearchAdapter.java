package com.gamesearch.giantbomb;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import Response.SearchItem;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<SearchItem> resultsList;

    SearchAdapter(List<SearchItem> resultsList) {
        this.resultsList = resultsList;
    }

    void updateResults(List<SearchItem> updatedResults) {
        this.resultsList = updatedResults;
        notifyDataSetChanged();
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row_layout, parent, false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        SearchItem searchItem = resultsList.get(position);
        holder.title.setText(searchItem.getName());

        //Glide.with(holder.thumbnail.getContext()).load(searchItem.image.small_url).into(holder.thumbnail);
        Picasso.get().load(searchItem.getImage().getSmall_url())
                .resize(100, 100)
                .centerCrop()
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }


    class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;

        SearchViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
            title = view.findViewById(R.id.title);
        }
    }

}
