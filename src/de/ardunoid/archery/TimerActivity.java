package de.ardunoid.archery;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);

		final long ArrowCount = 3;
		
		final long preparationTime = 9 * 1000;
		final long arrowTime = 40 * 1000;
		final long warnThreshhold = 20 * 1000;
		final long totalTime = preparationTime + (arrowTime * ArrowCount);
		final long pureShootingTime = totalTime - preparationTime;
		final long interval = 1000;



		final TextView timerView = (TextView) findViewById(R.id.txtTimeleft);
		
		final CountDownTimer timer = new CountDownTimer(totalTime, interval) {

			public void onTick(long millisUntilFinished) {
				if ((millisUntilFinished) >= pureShootingTime) {
					timerView.setBackgroundColor(getResources().getColor(R.color.BlanchedAlmond));
					timerView.setText(String.valueOf(((millisUntilFinished) - pureShootingTime) / 1000));
				} else if ((millisUntilFinished) >= warnThreshhold) {
					timerView.setBackgroundColor(getResources().getColor(R.color.LightGreen));
					timerView.setText(String.valueOf((millisUntilFinished / 1000)));
				} else {
					timerView.setBackgroundColor(getResources().getColor(R.color.LimeGreen));
					timerView.setText(String.valueOf((millisUntilFinished / 1000)));
				}
				Log.d("ardunoid Timer",	"Seconds: " + String.valueOf((millisUntilFinished / 1000)));
			}
			public void onFinish() {
				timerView.setText("Finished");
			}
		};
			

		/** Set Buttons **/
		final Button btnTimerStart = (Button) findViewById(R.id.btnTimerStart);
		final Button btnTimerStop = (Button) findViewById(R.id.btnTimerStop);

		btnTimerStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				timer.start();
				btnTimerStop.setEnabled(true);
				btnTimerStart.setEnabled(false);
			}
		});
		btnTimerStop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				timer.cancel();
				btnTimerStop.setEnabled(false);
				btnTimerStart.setEnabled(true);
			}
		});
		/** Set Buttons End**/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timer, menu);
		return true;
	}
}
