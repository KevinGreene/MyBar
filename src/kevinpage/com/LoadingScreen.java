package kevinpage.com;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class LoadingScreen extends Activity {

	// private ProgressBar progressBar;
	// private CountDownTimer cdt;
	private SqlDatabase sqlDb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading_screen);

		sqlDb = new SqlDatabase(this);

		/**
		 * Create 6 second timer to allow database to load completely. TODO This
		 * is probably bad practice to have arbitrary waiting time. Find a
		 * better way to wait.
		 */
		new CountDownTimer(6000, 1000) {

			public void onTick(long millisUntilFinished) {
				// Log.d("WHATEVER", "TICK TICK TICK, ZACH");
			}

			public void onFinish() {
				((ProgressBar) findViewById(R.id.progressBar1))
						.setEnabled(false);
				((Button) findViewById(R.id.enterButton))
						.setVisibility(View.VISIBLE);
			}
		}.start();

	}

	/**
	 * Handles button click for enter press
	 * 
	 * @param view
	 *            The view of the button
	 */
	public void onEnter(final View view) {
		Intent myIntent = new Intent(view.getContext(), MyBar.class);
		LoadingScreen.this.startActivity(myIntent);
		this.finish();
	}
}
