package tw.com.syscode.timereporter;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;

public final class Global {
	public static final String KeyName = "tw.com.syscode.time.report";
	public static final String SharedPreferencesName = "tw.com.syscode.timereporter";
	public static final String TimeKeyPrefix = "time_";
	public static final String SoundKeyPrefix = "sound_";
	public static final String SpeechKeyPrefix = "tts_";
	public static final String SpeakLanguage = "speak_language";
	
	public static boolean firstTime(Context context) {
		SharedPreferences settings = context.getSharedPreferences(Global. SharedPreferencesName, 0);
		String firstTime = settings.getString("first_time", "");
		boolean result = false;
		
		if ("".equals(firstTime)) {
			result = true;
		}
		
		return result;
	}
	
	public static void setFirstTime(Context context) {
		SharedPreferences settings = context.getSharedPreferences(Global. SharedPreferencesName, 0);
		settings.edit().putString("first_time", "1").commit();
	}
	
	public static void makeDefaultValue(Context context) {
		SharedPreferences settings = context.getSharedPreferences(Global. SharedPreferencesName, 0);
		SharedPreferences.Editor pe = settings.edit();
		
		for (int w = 1 ; w <= 7 ; w++) {
			for (int i = 0 ; i <= 23 ; i++) {
				String key1 = String.format(Locale.ENGLISH, "%d_%02d_00", w, i);
				String key2 = String.format(Locale.ENGLISH, "%d_%02d_30", w, i);
				String value = "0";
				
				if (!(w == 1 || w == 7) && (i >= 7 && i <= 19)) {
					value = "1";
				}
				
				pe.putString(Global.TimeKeyPrefix + key1, value);
				pe.putString(Global.SoundKeyPrefix + key1, value);
				pe.putString(Global.SpeechKeyPrefix + key1, value);
				
				pe.putString(Global.TimeKeyPrefix + key2, "0");
				pe.putString(Global.SoundKeyPrefix + key2, "0");
				pe.putString(Global.SpeechKeyPrefix + key2, "0");
			}
		}
		
		pe.commit();
	}
}
