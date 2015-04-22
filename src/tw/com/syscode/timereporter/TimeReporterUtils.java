package tw.com.syscode.timereporter;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;

public class TimeReporterUtils {
	public static void registerAlarm(Context context, Class<?> _class) {
		Calendar cal = Calendar.getInstance();
		int nowHour = cal.get(Calendar.HOUR_OF_DAY);
		int nowMinute = cal.get(Calendar.MINUTE);

		boolean isTomorrow = false;
		
		if (++nowHour >= 24) {
			isTomorrow = true;
			nowHour = 0;
		}
		nowMinute = 0;
		
	    cal.set(Calendar.HOUR_OF_DAY, nowHour);
	    cal.set(Calendar.MINUTE, nowMinute);
	    cal.set(Calendar.SECOND, 0);
	    
	    if (isTomorrow) cal.add(Calendar.DATE, 1);

	    Intent intentAlarm = new Intent(context, _class);
	   
	    //intentAlarm.addCategory("tw.com.syscode.timereporter");
	    intentAlarm.putExtra("msg", Global.KeyName);
	    PendingIntent pi = PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_ONE_SHOT);
	    
	    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
	}
	
	private static MediaPlayer mediaPlayer = null;
	
	public static void playSound(Context context) {
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mediaPlayer.release();
					mediaPlayer = null;
				}
			});
		}

		try {
			mediaPlayer.setDataSource(context, Uri.parse("android.resource://tw.com.syscode.timereporter/" + R.raw.beep1));
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {
		}
	}

	public static void reportNow(final Context context, boolean sound, boolean speech) {
		if (sound) playSound(context);
		if (speech) {
			context.startService(new Intent(context, TTS.class));
		}
	}
}
