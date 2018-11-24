package com.vcc.hackathon.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public final class PermissionUtils {

	private PermissionUtils() {
	}

	public static boolean hasPermissions(Context context, String... permissions) {
		for (String permission : permissions) {
			if (!hasPermission(context, permission)) {
				return false;
			}
		}
		return true;
	}

	public static boolean hasPermission(Context context, String permission) {
		return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
	}


	public static void requestPermission(Activity activity, int requestCode, String permission) {
		requestPermissions(activity,requestCode, permission );
	}


	public static void requestPermissions(Activity activity, int permissionId, String... permissions) {
		ActivityCompat.requestPermissions(activity,
				permissions,
				permissionId);

	}
}

