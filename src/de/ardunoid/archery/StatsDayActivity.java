package de.ardunoid.archery;

import de.ardunoid.archery.DBAdapter;
import de.ardunoid.archery2.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class StatsDayActivity extends ListActivity {

	private DBAdapter db;
	private Cursor mNotesCursor;

	private static final int ACTIVITY_EDIT_DAY = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats_day);
		try {
			db = new DBAdapter(this);
			fillData();
			//DBAdapter.close();
		} catch (Exception e) {
			Context context = getApplicationContext();
			CharSequence text = "I'm sorry, there was en Error reading the latest Entry from the Database!";
			int duration = 100;
			Log.e("ardunoid", text.toString() + " " + e.getMessage().toString());
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} finally {
			
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	    Cursor c = mNotesCursor;
	    c.moveToPosition(position);
	    
	    Intent i = new Intent(this, HitsDayActivity.class);
	    try {
	    	i.putExtra(de.ardunoid.archery.DBAdapter.KEY_DATE, c.getString(c.getColumnIndex(de.ardunoid.archery.DBAdapter.KEY_DATE)));
	    } catch(Exception e) {
	    	Log.e("ardunoid", "Could not put extra" + e.getMessage().toString());
	    }
	    startActivityForResult(i, ACTIVITY_EDIT_DAY);
	}
	
	@SuppressWarnings("deprecation")
	private void fillData() {
		db.open();	
		// Get all of the rows from the database and create the item list
		mNotesCursor = db.getStatsGroupedBy(1); //1=by date
		startManagingCursor(mNotesCursor);

		String[] from = new String[] { de.ardunoid.archery.DBAdapter.KEY_DATE, de.ardunoid.archery.DBAdapter.KEY_COUNT, de.ardunoid.archery.DBAdapter.KEY_SUM };
		int[] to = new int[] { R.id.textDate, R.id.textCount, R.id.textSum };
		// Now create a simple cursor adapter and set it to display
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.activity_stats_day, mNotesCursor, from, to);
		setListAdapter(notes);
		db.close();
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
	
	@SuppressWarnings("deprecation")
	@Override
	public void onPause() {
	    super.onPause();
	    try{
	    	stopManagingCursor(mNotesCursor);
	    } catch (Exception e){
	    	
	    }
	}


}
