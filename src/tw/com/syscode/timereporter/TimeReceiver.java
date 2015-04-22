package tw.com.syscode.timereporter;

import java.util.Calendar;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class TimeReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle b = intent.getExtras();

		if (b != null) {
			String msg = b.getString("msg");
			String rkey = Global.KeyName;
			
			if (rkey != null && rkey.equals(msg)) {
				TimeReporterUtils.registerAlarm(context, TimeReceiver.class);
				
				Calendar cal = Calendar.getInstance();
				SharedPreferences settings = context.getSharedPreferences(Global. SharedPreferencesName, 0);
				String key = String.format(Locale.ENGLISH, "%d_%02d_00", cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.HOUR_OF_DAY));
				
				if ("0".equals(settings.getString("STOP_SPEECH", "0"))) {
					String timeValue = settings.getString(Global.TimeKeyPrefix + key, "0");
					if ("1".equals(timeValue)) {
						String soundValue = settings.getString(Global.SoundKeyPrefix + key, "0");
						String speechValue = settings.getString(Global.SpeechKeyPrefix + key, "0");

						TimeReporterUtils.reportNow(context, "1".equals(soundValue), "1".equals(speechValue));
					}
				}
			}
		}
	}
}
