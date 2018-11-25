package com.vcc.hackathon.view.adapter.viewholder;


import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vcc.hackathon.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RemainderVH extends RecyclerView.ViewHolder {
	@BindView(R.id.textView_task)
	public TextView textViewTask;
	@BindView(R.id.textView_msg)
	public TextView textViewMsg;
	@BindView(R.id.item)
	public ConstraintLayout item;
	public RemainderVH(@NonNull View itemView) {
		super(itemView);
		ButterKnife.bind(this,itemView);
	}
}
