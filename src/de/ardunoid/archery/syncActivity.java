package de.ardunoid.archery;

import de.ardunoid.archery.DBAdapter;
import de.ardunoid.archery2.R;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

@SuppressLint("NewApi")
public class syncActivity extends Activity {

	private DBAdapter db;
	private Cursor mHitsCursor;
	private MqttClient client;
	public static final String BROKER_URL = "tcp://37.59.38.38:1883";
	public static String TOPIC = "archery/HUARGH/toserver";
    public static final String SYNCSTRING = "_id::%s#points::%s#date::%s#time::%s#distance::%s#targettype::%s#blindshot::%s#comment::%s";
    
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_sync);
			
			if (android.os.Build.VERSION.SDK_INT > 9) {
			    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			    StrictMode.setThreadPolicy(policy);
			}
			
		} catch (Exception e) {
			Log.e("ardunoid", e.getMessage().toString());
		}


	}

	@SuppressWarnings("deprecation")
	private void syncData() {
		// Get all of the rows from the database and create the item list
		db.open();
		try {
			client = new MqttClient(BROKER_URL, MqttClient.generateClientId(), new MemoryPersistence());
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
		try {
			
			client.connect();
			final MqttTopic syncTopic = client.getTopic(TOPIC);

			mHitsCursor = db.getHits();
			mHitsCursor.moveToFirst();
			
			while (mHitsCursor.isAfterLast() == false) {
				// send here...
				Log.d("ardunoid", "Got Hit");
				String messageToSend = "";
				try {
					messageToSend = String.format(SYNCSTRING,
						String.valueOf(mHitsCursor.getInt(mHitsCursor.getColumnIndex(DBAdapter.KEY_ROWID))),
						String.valueOf(mHitsCursor.getInt(mHitsCursor.getColumnIndex(DBAdapter.KEY_VALUE))),
						mHitsCursor.getString(mHitsCursor.getColumnIndex(DBAdapter.KEY_DATE)),
						mHitsCursor.getString(mHitsCursor.getColumnIndex(DBAdapter.KEY_TIME)),
						String.valueOf(mHitsCursor.getInt(mHitsCursor.getColumnIndex(DBAdapter.KEY_DISTANCE))),
						mHitsCursor.getString(mHitsCursor.getColumnIndex(DBAdapter.KEY_TARGETTYPE)),
						String.valueOf(mHitsCursor.getInt(mHitsCursor.getColumnIndex(DBAdapter.KEY_BLINDSHOT))),
						mHitsCursor.getString(mHitsCursor.getColumnIndex(DBAdapter.KEY_COMMENT))
						);
					Log.d("ardunoid", messageToSend);
				} catch (Exception e) {
					Log.e("archery", "MessageToSend Build Failed!: " + e.getMessage());
				}
				
		        MqttMessage message = new MqttMessage(messageToSend.getBytes());
		        try { 
		        	syncTopic.publish(message);
		        } catch(Exception e){
		        	Log.e("archery", e.getMessage());
		        }
				

				mHitsCursor.moveToNext();
			}
			client.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
		db.close();
	}

	public void onClickSync(final View view) {
			EditText channelName = (EditText)findViewById(R.id.editTextUserid);

			TOPIC = "archery/" + channelName.getText() + "/toserver";

			db = new DBAdapter(this.getApplicationContext());
			db.open();
			syncData();
			db.close();
			try {
		} catch (Exception e) {
			Context context = getApplicationContext();
			CharSequence text = "I'm sorry, there was en Error reading the latest Entry from the Database!";
			int duration = 100;
			Log.e("ardunoid", text.toString());
			
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stats_day, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
