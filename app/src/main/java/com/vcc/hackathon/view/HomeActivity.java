package com.vcc.hackathon.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vcc.hackathon.R;
import com.vcc.hackathon.datamanager.GeoRemainderEntity;
import com.vcc.hackathon.view.adapter.RemainderAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

	@BindView(R.id.fab_add_loc)
	FloatingActionButton fabAddLoc;
	@BindView(R.id.remaidarList)
	RecyclerView remaidarList;

	private HomeViewModel viewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ButterKnife.bind(this);
		viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
		viewModel.initDB(this);
		remaidarList.setLayoutManager(new LinearLayoutManager(this));
		ItemOffsetDecoration offsetDecoration = new ItemOffsetDecoration(this,R.dimen.offset);
		remaidarList.addItemDecoration(offsetDecoration);
		viewModel.getRemainderList().observe(this, new Observer<List<GeoRemainderEntity>>() {
			@Override
			public void onChanged(@Nullable List<GeoRemainderEntity> geoRemainderEntities) {
				RemainderAdapter adapter = new RemainderAdapter(HomeActivity.this,geoRemainderEntities);
				remaidarList.setAdapter(adapter);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		viewModel.refreshDB();
	}

	@OnClick(R.id.fab_add_loc)
	public void onViewClicked() {
		startActivity(new Intent(this, MainActivity.class));
	}
}
