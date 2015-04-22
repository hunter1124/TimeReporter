package tw.com.syscode.timereporter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RebootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String intentAction = intent.getAction();

		if (intentAction.equals(Intent.ACTION_BOOT_COMPLETED) || 
				((intentAction.equals(Intent.ACTION_PACKAGE_ADDED) || intentAction.equals(Intent.ACTION_PACKAGE_REPLACED))
						&& intent.getDataString().contains("tw.com.syscode.timereporter"))) {
			if (Global.firstTime(context)) {
				Global.makeDefaultValue(context);
				Global.setFirstTime(context);
			}
			
			TimeReporterUtils.registerAlarm(context, TimeReceiver.class);
			//TimeReporterUtils.reportNow(context, true, false);
		}
	}
}
