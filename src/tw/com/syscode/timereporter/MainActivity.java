package tw.com.syscode.timereporter;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends Activity {
	private SharedPreferences settings = null;
	private int selectedWeek = -1;
	
	private final int CHECKBOX_TIME_ID[] = {
			R.id.checkBox00_00_enabled, R.id.checkBox01_00_enabled, R.id.checkBox02_00_enabled, R.id.checkBox03_00_enabled,
			R.id.checkBox04_00_enabled, R.id.checkBox05_00_enabled, R.id.checkBox06_00_enabled, R.id.checkBox07_00_enabled,
			R.id.checkBox08_00_enabled, R.id.checkBox09_00_enabled, R.id.checkBox10_00_enabled, R.id.checkBox11_00_enabled,
			R.id.checkBox12_00_enabled, R.id.checkBox13_00_enabled, R.id.checkBox14_00_enabled, R.id.checkBox15_00_enabled,
			R.id.checkBox16_00_enabled, R.id.checkBox17_00_enabled, R.id.checkBox18_00_enabled, R.id.checkBox19_00_enabled,
			R.id.checkBox20_00_enabled, R.id.checkBox21_00_enabled, R.id.checkBox22_00_enabled, R.id.checkBox23_00_enabled,
	};
	
	private final int CHECKBOX_SOUND_ID[] = {
			R.id.checkBox00_00_sound, R.id.checkBox01_00_sound, R.id.checkBox02_00_sound, R.id.checkBox03_00_sound,
			R.id.checkBox04_00_sound, R.id.checkBox05_00_sound, R.id.checkBox06_00_sound, R.id.checkBox07_00_sound,
			R.id.checkBox08_00_sound, R.id.checkBox09_00_sound, R.id.checkBox10_00_sound, R.id.checkBox11_00_sound,
			R.id.checkBox12_00_sound, R.id.checkBox13_00_sound, R.id.checkBox14_00_sound, R.id.checkBox15_00_sound,
			R.id.checkBox16_00_sound, R.id.checkBox17_00_sound, R.id.checkBox18_00_sound, R.id.checkBox19_00_sound,
			R.id.checkBox20_00_sound, R.id.checkBox21_00_sound, R.id.checkBox22_00_sound, R.id.checkBox23_00_sound,
	};
	
	private final int CHECKBOX_TTS_ID[] = {
			R.id.checkBox00_00_tts, R.id.checkBox01_00_tts, R.id.checkBox02_00_tts, R.id.checkBox03_00_tts,
			R.id.checkBox04_00_tts, R.id.checkBox05_00_tts, R.id.checkBox06_00_tts, R.id.checkBox07_00_tts,
			R.id.checkBox08_00_tts, R.id.checkBox09_00_tts, R.id.checkBox10_00_tts, R.id.checkBox11_00_tts,
			R.id.checkBox12_00_tts, R.id.checkBox13_00_tts, R.id.checkBox14_00_tts, R.id.checkBox15_00_tts,
			R.id.checkBox16_00_tts, R.id.checkBox17_00_tts, R.id.checkBox18_00_tts, R.id.checkBox19_00_tts,
			R.id.checkBox20_00_tts, R.id.checkBox21_00_tts, R.id.checkBox22_00_tts, R.id.checkBox23_00_tts,
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		selectedWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		settings = getSharedPreferences(Global.SharedPreferencesName, 0);

		if (Global.firstTime(this)) {
			Global.makeDefaultValue(this);
			Global.setFirstTime(this);
		}

		setTodayInfo(selectedWeek);
		loadValues(selectedWeek);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		TimeReporterUtils.registerAlarm(this.getApplicationContext(), TimeReceiver.class);
		super.onStart();
	}
	
	private void setTodayInfo(int week) {
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] arrayOfWeekDaysNames = dfs.getWeekdays();
		
		TextView textViewToday = (TextView)findViewById(R.id.textViewToday);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		textViewToday.setText(format1.format(cal.getTime()) + "  " + arrayOfWeekDaysNames[week]);
	}
	
	private void loadValues(int week) {
		CheckBox check = null;
		boolean timeEnabled = false;
		
		 ((CheckBox)this.findViewById(R.id.checkBoxStopSpeech)).setChecked("1".equals(getStopSpeech()));
		
		for (int i = 0 ; i <= 23 ; i++) {
			String key = String.format(Locale.ENGLISH, "%d_%02d_00", week, i);
			
			timeEnabled = "1".equals(getValue(Global.TimeKeyPrefix + key, "0"));
			check = (CheckBox)this.findViewById(CHECKBOX_TIME_ID[i]);
			check.setChecked(timeEnabled);
			
			check = (CheckBox)this.findViewById(CHECKBOX_SOUND_ID[i]);
			check.setChecked("1".equals(getValue(Global.SoundKeyPrefix + key, "0")));
			check.setEnabled(timeEnabled);
			
			check = (CheckBox)this.findViewById(CHECKBOX_TTS_ID[i]);
			check.setChecked("1".equals(getValue(Global.SpeechKeyPrefix + key, "0")));
			check.setEnabled(timeEnabled);
		}
	}
	
	private String getValue(String key, String defaultValue) {
		return settings.getString(key, defaultValue);
	}
	
	private void setValue(String key, String value) {
		settings.edit().putString(key, value).commit();
	}
	
	public void onButtonWeekClicked(View view) {
		int tag = Integer.parseInt(view.getTag().toString());
		
		if (tag >= 1 && tag <= 7) {
			selectedWeek = tag;
			setTodayInfo(selectedWeek);
			loadValues(selectedWeek);
		}
	}
	
	public void onButtonDefaultClicked(View view) {
		Global.makeDefaultValue(this.getApplicationContext());
		setTodayInfo(selectedWeek);
		loadValues(selectedWeek);
	}
	
	public void onButtonListenClicked(View view) {
		TimeReporterUtils.reportNow(view.getContext(), true, true);
	}
	
	private void setValueByCheckbox(int id, String keyPrefix, int week, int item) {
		String key = keyPrefix + String.format(Locale.ENGLISH, "%d_%02d_00", week, item);
		CheckBox check = (CheckBox)this.findViewById(id);
		setValue(key, (check.isChecked() ? "1" : "0"));
	}
	
	private void setCheckboxEnable(int id, String keyPrefix, int week, int item) {
		String key = keyPrefix + String.format(Locale.ENGLISH, "%d_%02d_00", week, item);
		CheckBox check = (CheckBox)this.findViewById(id);
		check.setEnabled("1".equals(getValue(key, "0")));
	}

	public void onCheckboxTimeClicked(View view) {
		int tag = Integer.parseInt(view.getTag().toString());
		
		if (tag >= 0 && tag <= 23) {
			setValueByCheckbox(CHECKBOX_TIME_ID[tag], Global.TimeKeyPrefix, selectedWeek, tag);
			setCheckboxEnable(CHECKBOX_SOUND_ID[tag], Global.TimeKeyPrefix, selectedWeek, tag);
			setCheckboxEnable(CHECKBOX_TTS_ID[tag], Global.TimeKeyPrefix, selectedWeek, tag);
		}
	}
	
	public void onCheckboxSoundClicked(View view) {
		int tag = Integer.parseInt(view.getTag().toString());
		
		if (tag >= 0 && tag <= 23) {
			setValueByCheckbox(CHECKBOX_SOUND_ID[tag], Global.SoundKeyPrefix, selectedWeek, tag);
		}
	}
	
	public void onCheckboxSpeechClicked(View view) {
		int tag = Integer.parseInt(view.getTag().toString());
		
		if (tag >= 0 && tag <= 23) {
			setValueByCheckbox(CHECKBOX_TTS_ID[tag], Global.SpeechKeyPrefix, selectedWeek, tag);
		}
	}
	
	public void onCheckboxStopSpeechClicked(View view) {
		setStopSpeech(((CheckBox)view).isChecked());
	}
	
	public void onButtonSettingsClicked(View view) {
		startActivity(new Intent(this, SettingsActivity.class));
	}
	
	private void setStopSpeech(boolean stop) {
		setValue("STOP_SPEECH", (stop ? "1" : "0"));
	}
	
	private String getStopSpeech() {
		return getValue("STOP_SPEECH", "0");
	}
}
