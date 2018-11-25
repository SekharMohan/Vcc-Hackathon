package com.vcc.hackathon.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle data = intent.getExtras();

		Object[] pdus = (Object[]) data.get("pdus");

		for (int i = 0; i < pdus.length; i++) {
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

			String sender = smsMessage.getDisplayOriginatingAddress();
			//Check the sender to filter messages which we require to read

			String message = smsMessage.getDisplayMessageBody();


			if (message.contains("tag:VCC")) {

				String[] lineArr = message.split("\n");
				for(String s:lineArr) {
					String[] keyVal = s.split(":");
					System.out.println(keyVal[0]+"--->"+keyVal[1]);
				}
				System.out.println(message);
			}
		}

	}


}

