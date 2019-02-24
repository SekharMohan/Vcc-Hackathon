package com.vcc.hackathon.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.vcc.hackathon.datamanager.AppDatabase;
import com.vcc.hackathon.datamanager.DatabaseClient;
import com.vcc.hackathon.datamanager.GeoRemainderEntity;

import java.util.concurrent.CompletableFuture;

public class SmsReceiver extends BroadcastReceiver {

	private AppDatabase databaseClient;
	private String url;
	private String task;
	private String msg;
	private int radius;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle data = intent.getExtras();
		databaseClient = DatabaseClient.getInstance(context).getAppDatabase();
		Object[] pdus = (Object[]) data.get("pdus");

		for (int i = 0; i < pdus.length; i++) {
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

			String sender = smsMessage.getDisplayOriginatingAddress();
			//Check the sender to filter messages which we require to read

			String message = smsMessage.getDisplayMessageBody();


			if (message.contains("tag:VCC")) {

				String[] lineArr = message.split("\n");
				for (String s : lineArr) {
					String[] keyVal = s.split(":");

					if (keyVal[0].equalsIgnoreCase("Radius")) {
						radius = Integer.parseInt(keyVal[1]);
					} else if (keyVal[0].equalsIgnoreCase("Message")) {
						msg = keyVal[1];
					} else if (keyVal[0].equalsIgnoreCase("Task")) {
						task = keyVal[1];
					} else if (keyVal[0].equalsIgnoreCase("Location url")) {
						url = keyVal[1]+":"+keyVal[2];

					}
				}

				saveToDB();

			}
		}

	}


	private void saveToDB() {
		CompletableFuture.runAsync(new Runnable() {
			@Override
			public void run() {
				GeoRemainderEntity entry = new GeoRemainderEntity();
				entry.setLocationUrl(url);
				entry.setMessage(msg);
				entry.setTask(task);
				entry.setRadius(radius);
				databaseClient.geoRemainderDao().insert(entry);
			}
		});
	}


}

