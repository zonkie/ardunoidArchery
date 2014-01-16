package de.ardunoid.archery;

import de.ardunoid.archery.DBAdapter;
import de.ardunoid.archery2.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class HitsDayActivity extends ListActivity {

	private DBAdapter DBAdapter;
	private Cursor mHitsCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_hits_day);
			
		} catch (Exception e) {
			Log.e("ardunoid", e.getMessage().toString());
		}
		
		String date = "0000-00-00";
	
		try {
			Log.d("ardunoid", "Try to get the hit.. ");
			Bundle bundle = getIntent().getExtras();
			date = bundle.getString(de.ardunoid.archery.DBAdapter.KEY_DATE);
			Log.d("ardunoid", date);
			DBAdapter = new DBAdapter(this);
			DBAdapter.open();
			fillData(date);
			DBAdapter.close();
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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	    Cursor c = mHitsCursor;
	    c.moveToPosition(position);
	    
	    try {
		    final int entryId = c.getInt(c.getColumnIndex(de.ardunoid.archery.DBAdapter.KEY_ROWID));
		    final String date = c.getString(c.getColumnIndex(de.ardunoid.archery.DBAdapter.KEY_DATE));
		//        	DBAdapter.open();
		//        	DBAdapter.deleteEntry(entryId);	 

	    	
	    	new AlertDialog.Builder(this).setTitle(R.string.txtDialogDeleteHead)
	    	.setMessage(R.string.txtDialogDeleteMessage)
	    	.setNegativeButton(R.string.txtDialogBtnNegative, null)
	    	.setPositiveButton(R.string.txtDialogBrnPositive, 
	    			new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							try {
								DBAdapter.open();
								DBAdapter.deleteEntry(entryId);
								fillData(date); //reload data after Delete...
						    } catch(Exception e) {
						    	Log.e("ardunoid", "Could not delete entry: " + e.getMessage().toString());
						    }

						}
	    			}
				).show();
	    	
   	
	    } catch(Exception e) {
	    	Log.e("ardunoid", "Could not delete entry: " + e.getMessage().toString());
	    } finally {
	    	
	    }
	}
	


	@SuppressWarnings("deprecation")
	private void fillData(String date) {
		
		// Get all of the rows from the database and create the item list
		DBAdapter.open();

		mHitsCursor = DBAdapter.getHitsByDate(date);
		startManagingCursor(mHitsCursor);
		
		String[] from = new String[] { de.ardunoid.archery.DBAdapter.KEY_DATE, de.ardunoid.archery.DBAdapter.KEY_VALUE, de.ardunoid.archery.DBAdapter.KEY_DISTANCE, de.ardunoid.archery.DBAdapter.KEY_TARGETTYPE}; //, de.ardunoid.archery.DBAdapter.KEY_BLINDSHOT 
		int[] to = new int[] { R.id.textDate, R.id.textScore, R.id.textDistance, R.id.textTargettype};//, R.id.textBlindshot
		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.activity_hits_day, mHitsCursor, from, to);
		setListAdapter(notes);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stats_day, menu);
		return true;
	}

}
