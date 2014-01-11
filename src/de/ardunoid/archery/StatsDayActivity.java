package de.ardunoid.archery;

import de.ardunoid.archery.DBAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class StatsDayActivity extends ListActivity {

	private DBAdapter DBAdapter;
	private Cursor mNotesCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats_day);
		try {
			DBAdapter = new DBAdapter(this);
			DBAdapter.open();
			fillData();
			DBAdapter.close();
		} catch (Exception e) {
			Context context = getApplicationContext();
			CharSequence text = "I'm sorry, there was en Error reading the latest Entry from the Database!";
			int duration = 100;
			Log.e("ardunoid", text.toString() + " " + e.getMessage().toString());
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}

	}

	private void fillData() {
		
		// Get all of the rows from the database and create the item list
		mNotesCursor = DBAdapter.getStatsGroupedBy(1); //1=by date
		startManagingCursor(mNotesCursor);

		String[] from = new String[] { de.ardunoid.archery.DBAdapter.KEY_DATE, de.ardunoid.archery.DBAdapter.KEY_COUNT, de.ardunoid.archery.DBAdapter.KEY_SUM };
		int[] to = new int[] { R.id.textDate, R.id.textCount, R.id.textSum };
		// Now create a simple cursor adapter and set it to display
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.activity_stats_day, mNotesCursor, from, to);
		setListAdapter(notes);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stats_day, menu);
		return true;
	}

}
