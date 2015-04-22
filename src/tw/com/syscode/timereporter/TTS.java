package tw.com.syscode.timereporter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;

public class TTS extends Service implements TextToSpeech.OnInitListener {
	private TextToSpeech tts = null;
	private int amStreamMusicMaxVol = -1;

	@Override
	public void onCreate() {
		super.onCreate();
    	
    	if (tts == null) {
    		tts = new TextToSpeech(this, this);
    	}
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
        	tts.stop();
        	tts.shutdown();
        }
        super.onDestroy();
    }
    
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
    @Override
    public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
        	if (Build.VERSION.SDK_INT >= 15) {
        		tts.setOnUtteranceProgressListener(new android.speech.tts.UtteranceProgressListener() {
	        		@Override
	                public void onDone(String utteranceId) {
	        			restoreMaxVol(TTS.this);
	        			stopSelf();
	                }

	                @Override
	                public void onError(String utteranceId) {
	                }

	                @Override
	                public void onStart(String utteranceId) {
	                	//updateMaxVol(context);
	                	
	                }
	        	});
        	} else {
        		tts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                    @Override
                    public void onUtteranceCompleted(String utteranceId) {
                    	restoreMaxVol(TTS.this);
                    	stopSelf();
                    }
                });
        	}
        	playTTS(TTS.this);
        } else {
            //Handle initialization error here
        }
    }
	
	private void updateMaxVol(Context context) {
		AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		amStreamMusicMaxVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);//am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		am.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);
	}
	
	private void restoreMaxVol(Context context) {
		if (amStreamMusicMaxVol >= 0) {
			AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			am.setStreamVolume(AudioManager.STREAM_MUSIC, amStreamMusicMaxVol, 0);
		}
	}
	
	public void playTTS(Context context) {
		if (tts == null) return;
		
		SharedPreferences settings = getSharedPreferences(Global.SharedPreferencesName, 0);
		String language = settings.getString(Global.SpeakLanguage, "ENGLISH");
		
		updateMaxVol(context);
		
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		String text = "";
		
		if (minute > 0) {
			if ("ENGLISH".equals(language)) {
				text = hour + " " + minute;
			} else if ("CHINESE".equals(language)) {
				text = hour + " 點 " + minute + " 分";
			}
		} else {
			if ("ENGLISH".equals(language)) {
				text = hour + " o'clock";
			} else if ("CHINESE".equals(language)) {
				text = hour + " 點整";
			}
		}

		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "stringId");

			if ("ENGLISH".equals(language)) {
				tts.setLanguage(Locale.US);
			} else if ("CHINESE".equals(language)) {
				tts.setLanguage(Locale.CHINESE);
			}
			
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, params);
		} catch (Exception e) {
			
		}
	}
}
