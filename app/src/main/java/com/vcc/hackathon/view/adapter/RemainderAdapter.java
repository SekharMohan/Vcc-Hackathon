package com.vcc.hackathon.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vcc.hackathon.R;
import com.vcc.hackathon.datamanager.GeoRemainderEntity;
import com.vcc.hackathon.view.adapter.viewholder.RemainderVH;

import java.util.List;


public class RemainderAdapter extends RecyclerView.Adapter<RemainderVH> {

	private List<GeoRemainderEntity> mList;
	private Context mContext;

	public RemainderAdapter(Context context, List<GeoRemainderEntity> list) {
		this.mList = list;
		mContext = context;

	}

	@NonNull
	@Override
	public RemainderVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_remainder, viewGroup, false);
		return new RemainderVH(view);
	}

	@Override
	public void onBindViewHolder(@NonNull RemainderVH remainderVH, int position) {
		final GeoRemainderEntity entry = mList.get(position);
		remainderVH.textViewTask.setText(entry.getTask());
		remainderVH.textViewMsg.setText(entry.getMessage());
		remainderVH.item.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(entry.getLocationUrl()));
				mContext.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}


}
