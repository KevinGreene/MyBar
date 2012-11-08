package kevinpage.com;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

/**
 * This activity handles layout for the tabs under 'Ingredients'
 */
public class TabInventory2 extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabinventory2);

		final TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Reusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab
		// Do the same for the other tabs
		intent = new Intent().setClass(this, AddDrinks.class);
		spec = tabHost.newTabSpec("add").setIndicator("Add Drinks", null)
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, AllDrinks.class);
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("all").setIndicator("Drinks", null)
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setSaveEnabled(true);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			public void onTabChanged(String tabId) {
				
			}
		});

		tabHost.setCurrentTab(1);
	}

}