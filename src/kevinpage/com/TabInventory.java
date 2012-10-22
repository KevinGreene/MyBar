package kevinpage.com;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

/**
 * This activity handles layout for the tabs under 'Ingredients'
 */
public class TabInventory extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabinventory);

		final TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab
		// Do the same for the other tabs
		intent = new Intent().setClass(this, DontHave.class);
		spec = tabHost.newTabSpec("dont").setIndicator("Add Items", null)
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, Have.class);
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("have").setIndicator("My Inventory", null)
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