package com.example.akash.wikipedia.ui.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.akash.wikipedia.R;
import com.example.akash.wikipedia.data.remote.model.Page;

import java.util.List;

/**
 * Created by Akash Verma on 13/10/18.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Page> pageList;

    public RecyclerViewAdapter(SearchResultActivity searchResultActivity, List<Page> pageList) {
        this.context = searchResultActivity;
        this.pageList = pageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_search_result_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.tvTitle.setText(pageList.get(position).getTitle());
        if (pageList.get(position).getTerms() != null
                && pageList.get(position).getTerms().getDescription() != null
                && pageList.get(position).getTerms().getDescription().size() > 0) {

            holder.tvDescription.setText(pageList.get(position).getTerms().getDescription().get(0));
        } else
            holder.tvDescription.setText("NA");

        if (pageList.get(position).getThumbnail() != null)
            Glide.with(context)
                    .load(pageList.get(position).getThumbnail().getSource())
                    .into(holder.ivThumbnail);
        else
            Glide.with(context)
                    .load(R.drawable.ic_image_black_24dp)
                    .into(holder.ivThumbnail);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnItemClickListener) context).onItemClick(pageList.get(holder.getAdapterPosition()).getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView ivThumbnail;
        TextView tvTitle;
        TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }

    interface OnItemClickListener {
        void onItemClick(String title);
    }
}
