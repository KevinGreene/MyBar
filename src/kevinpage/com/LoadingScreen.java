package kevinpage.com;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoadingScreen extends Activity {

	// private ProgressBar progressBar;
	// private CountDownTimer cdt;
	private SqlDatabase sqlDb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading_screen);
		
		final Context context = this;
		
		new AsyncTask<Void, Boolean, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				sqlDb = new SqlDatabase(context);

				return null;
			}
			

			@Override
			protected void onPostExecute(Void result){
				Intent myIntent = new Intent(context, MyBar.class);
				LoadingScreen.this.startActivity(myIntent);
				finish();
			}
		}.execute();
	}
}
