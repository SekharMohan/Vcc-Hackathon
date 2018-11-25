package com.vcc.hackathon.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.vcc.hackathon.datamanager.AppDatabase;
import com.vcc.hackathon.datamanager.DatabaseClient;
import com.vcc.hackathon.datamanager.GeoRemainderEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HomeViewModel extends ViewModel {

	private AppDatabase databaseClient;
	private MutableLiveData<List<GeoRemainderEntity>> remainderListLiveData = new MutableLiveData<>();

	public void initDB(Context context) {
		databaseClient =DatabaseClient.getInstance(context).getAppDatabase();
	}

	public LiveData<List<GeoRemainderEntity>> getRemainderList() {
		return  remainderListLiveData;
	}

	public void refreshDB() {
		CompletableFuture.runAsync(new Runnable() {
			@Override
			public void run() {
				remainderListLiveData.postValue(databaseClient.geoRemainderDao().getAll());
			}
		});
	}
}
