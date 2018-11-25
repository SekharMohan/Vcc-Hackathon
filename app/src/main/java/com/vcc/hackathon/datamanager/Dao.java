package com.vcc.hackathon.datamanager;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@android.arch.persistence.room.Dao
public interface Dao {
		@Query("SELECT * FROM GeoRemainderEntity")
		List<GeoRemainderEntity> getAll();
		@Insert
		void insert(GeoRemainderEntity remainderEntity);

		@Delete
		void delete(GeoRemainderEntity remainderEntity);
}
