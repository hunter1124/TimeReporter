package tw.com.syscode.timereporter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RadioGroup;
import android.widget.RadioButton;

public class SettingsActivity extends Activity {
	private SharedPreferences settings = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		settings = getSharedPreferences(Global.SharedPreferencesName, 0);
		
		RadioGroup radiogroup1 = (RadioGroup)findViewById(R.id.radiogroup1);
		radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override  
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				String type = "*";
				
				if (checkedId == R.id.speakEnglishRadioButton) {
	            	  type = "ENGLISH";
	            } else if (checkedId == R.id.speakChineseRadioButton) {
	            	  type = "CHINESE";
	            }
	            
				setValue(Global.SpeakLanguage, type);
			}
		});
		
		String language = getValue(Global.SpeakLanguage, "ENGLISH");
		if ("ENGLISH".equalsIgnoreCase(language)) {
			((RadioButton)findViewById(R.id.speakEnglishRadioButton)).setChecked(true);
		} else if ("CHINESE".equalsIgnoreCase(language)) {
			((RadioButton)findViewById(R.id.speakChineseRadioButton)).setChecked(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
	
	private String getValue(String key, String defaultValue) {
		return settings.getString(key, defaultValue);
	}
	
	private void setValue(String key, String value) {
		settings.edit().putString(key, value).commit();
	}
}
