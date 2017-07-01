package com.example.trikh.focuson.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trikh.focuson.R;
import com.example.trikh.focuson.data.AppInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by trikh on 30-06-2017.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppViewHolder> {

    private Context contextInstance;
    private ArrayList<AppInfo> dataInstance;

    public AppListAdapter(Context context) {
        contextInstance = context;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View list_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_list_item_layout, parent, false);

        return new AppViewHolder(list_item);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        AppInfo currentApp = dataInstance.get(position);
        holder.appName.setText(currentApp.getAppName());
        holder.appIcon.setImageDrawable(currentApp.getAppIcon());
    }

    @Override
    public int getItemCount() {
        if (dataInstance != null) {
            return dataInstance.size();
        } else
            return 0;
    }

    public void setAppList(ArrayList<AppInfo> data) {
        dataInstance = data;
    }

    class AppViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.app_icon)
        ImageView appIcon;
        @BindView(R.id.app_name)
        TextView appName;
        @BindView(R.id.block_unblock_button)
        ImageButton blockButton;

        AppViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
