package com.znkj.rchl_hz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.znkj.rchl_hz.R;
import com.znkj.rchl_hz.model.PersonBean;

import java.util.List;


/**
 * Created by whieenz on 2018/3/16.
 * 作用：
 */

public class FuzzMatchPersonAdapter extends BaseRecyclerAdapter<PersonBean, FuzzMatchPersonAdapter.ViewHolder> {


    public FuzzMatchPersonAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_cell_select_single, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.info.setText(datas.get(position).getName());
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.tv_select_info);
        }

        TextView info;
    }
}
