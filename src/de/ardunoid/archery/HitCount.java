package de.ardunoid.archery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.R.integer;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import de.ardunoid.archery2.R;

public class HitCount extends Activity {
	final Calendar c = Calendar.getInstance();
	DBAdapter db = new DBAdapter(this);
	final Integer toastlength = 100;
	
	final String tag = "ardunoid";
	
	public static final String PREFS_NAME = "ardunoidArcheryPreferences";

	String Distance = "6 m";
	String Targettype = "1x80";
	String Blindshot = "0";
	
	@SuppressLint("SimpleDateFormat")
	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hit_count);

		// Restore preferences for Radio Buttons + Blindshot
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		Distance = settings.getString("Distance", "6 m");
		Targettype = settings.getString("Targettype","1x80");
		Blindshot = settings.getString("Blindshot","0");

		try {
			updateHits();
		} catch (Exception e) {
			Log.e("ardunoid", "Failed to Update Hits");
		}
	}

	public boolean saveHit(Integer Points) {
		String date = dateFormat.format(new Date());
		String time = String.valueOf(c.get(Calendar.HOUR)) + ":" + String.valueOf(c.get(Calendar.MINUTE)) + ":" + String.valueOf(c.get(Calendar.SECOND));
		RadioGroup radioDistance = (RadioGroup) findViewById(R.id.radioDistance);
		RadioGroup radioTarget = (RadioGroup) findViewById(R.id.radioTargetsize);
		
		db.open();
		try {
			Context context = getApplicationContext();
			Integer Blindshot = 0;

			Distance = ((RadioButton) findViewById(radioDistance.getCheckedRadioButtonId())).getText().toString();
			radioDistance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				public void onCheckedChanged(RadioGroup group,int checkedId) {
					
					Distance = ((RadioButton) findViewById(group.getCheckedRadioButtonId())).getText().toString();
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("Distance", Distance);
					editor.commit();
				}
			});

			Targettype = ((RadioButton) findViewById(radioTarget.getCheckedRadioButtonId())).getText().toString();
			radioDistance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					Targettype = ((RadioButton) findViewById(group.getCheckedRadioButtonId())).getText().toString();
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("Targettype", Targettype);
					editor.commit();

				}
			});

			db.insertHit(String.valueOf(date), String.valueOf(time), Points, Distance, Targettype, Blindshot); // @TODO: get Values from radiobuttons

			Toast toast = Toast.makeText(context, "Hit Saved: " + Points + " Points", toastlength);
			toast.show();

		} catch (Exception e) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "Could not insert the hit.", toastlength);
			toast.show();
		}
		db.close();

		try {
			updateHits();
		} catch (Exception e) {
			Log.e("ardunoid", e.getMessage().toString());
			TextView txtResultArrows = (TextView) findViewById(R.id.textResultArrows);
			txtResultArrows.setText("---");

			TextView txtResultPoints = (TextView) findViewById(R.id.textResultPoints);
			txtResultPoints.setText("---");
		}

		return true;
	}

	public void updateHits() {
		db.open();
		String date = dateFormat.format(new Date());
		TextView txtResultArrows = (TextView) findViewById(R.id.textResultArrows);
		TextView txtResultPoints = (TextView) findViewById(R.id.textResultPoints);
		int resultArrows = -1;
		int resultPoints = -1;

		try {
			resultArrows = db.getArrowsByDate(date);
			resultPoints = db.getPointsByDate(date);
		} catch (Exception e) {
			Log.e("ardunoid", "Error updating Stats. "
					+ e.getMessage().toString());
		}
		try {
			txtResultArrows.setText(String.valueOf(resultArrows));
			txtResultPoints.setText(String.valueOf(resultPoints));
		} catch (Exception e) {
			Log.e("ardunoid", "Error updating textfields. " + e.getMessage().toString());
		}
		db.close();
	}

	public void onClickHitWall(final View view) {
		Boolean saved = saveHit(0);
	}

	public void onClickHit0(final View view) {
		Boolean saved = saveHit(0);
	}

	public void onClickHit1(final View view) {
		Boolean saved = saveHit(1);
	}

	public void onClickHit2(final View view) {
		Boolean saved = saveHit(2);
	}

	public void onClickHit3(final View view) {
		Boolean saved = saveHit(3);
	}

	public void onClickHit4(final View view) {
		Boolean saved = saveHit(4);
	}

	public void onClickHit5(final View view) {
		Boolean saved = saveHit(5);
	}

	public void onClickHit6(final View view) {
		Boolean saved = saveHit(6);

	}

	public void onClickHit7(final View view) {
		Boolean saved = saveHit(7);
	}

	public void onClickHit8(final View view) {
		Boolean saved = saveHit(8);
	}

	public void onClickHit9(final View view) {
		Boolean saved = saveHit(9);
	}

	public void onClickHit10(final View view) {
		Boolean saved = saveHit(10);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hit_count, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.action_stats:
			final Intent intentStatsDay = new Intent(this,
					StatsDayActivity.class);
			startActivity(intentStatsDay);
			break;
		case R.id.action_timer:
			final Intent intentTimer = new Intent(this, TimerActivity.class);
			startActivity(intentTimer);
			break;
		case R.id.action_about:
			final Intent intentAbout = new Intent(this, About.class);
			startActivity(intentAbout);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return false;

	}

}
