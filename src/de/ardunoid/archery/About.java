package de.ardunoid.archery;

import de.ardunoid.archery.R;
import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class About extends Activity {

	@Override
	public void onCreate(Bundle icicle){
		
		super.onCreate(icicle);
		setContentView(R.layout.about);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
