package com.vcc.hackathon.datamanager;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity

public class GeoRemainderEntity {
	@NonNull
	@PrimaryKey(autoGenerate = true)
	private int uid;
	@ColumnInfo(name = "radius")
	private int radius;
	@ColumnInfo(name = "msg")
	private String message;
	@ColumnInfo(name = "task")
	private String task;
	@ColumnInfo(name = "loc_url")
	private String locationUrl;

	@NonNull
	public int getUid() {
		return uid;
	}

	public void setUid(@NonNull int uid) {
		this.uid = uid;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getLocationUrl() {
		return locationUrl;
	}

	public void setLocationUrl(String locationUrl) {
		this.locationUrl = locationUrl;
	}
}
