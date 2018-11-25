package com.vcc.hackathon.datamanager;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {GeoRemainderEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
	public abstract Dao geoRemainderDao();
}
