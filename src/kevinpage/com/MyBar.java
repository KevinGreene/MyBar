package kevinpage.com;

import android.os.Vibrator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.View;
import android.widget.Button;

/**
 * Main activity for initial View.
 * Sets up buttons and data.
 * Version 2.1
 */
public class MyBar extends Activity {

	private Vibrator myVib; // vibrate object
	private SensorManager mSensorManager;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity
	private MyBarDatabase sqlDb;
	
	/**
	 * Event Listener for accelerator
	 */
	private final SensorEventListener mSensorListener = new SensorEventListener() {

		public void onSensorChanged(SensorEvent se) {
			float x = se.values[0];
			float y = se.values[1];
			float z = se.values[2];
			mAccelLast = mAccelCurrent;
			mAccelCurrent = (float) FloatMath.sqrt((x * x + y * y + z * z));
			float delta = mAccelCurrent - mAccelLast;
			mAccel = mAccel * 0.9f + delta; // perform low-cut filter
			onShake(); // call onShake() method
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

	};
	
	/**
	 * On shake function
	 */
	public void onShake() {
		if (mAccel >= 2.75) {
			myVib.vibrate(50);
			randomDrink();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(mSensorListener);
		super.onStop();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		sqlDb = new MyBarDatabase(this);
		sqlDb.getAllDrinks();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybar);
		
		myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE); // initialize vibrate
		
		/**
		 * Setup sensor elements
		 */
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		mAccel = 0.00f;
		mAccelCurrent = SensorManager.GRAVITY_EARTH;
		mAccelLast = SensorManager.GRAVITY_EARTH;
		
		/**
		 * Button event for "Drinks I Can Make"
		 */
		final Button canMakeButton = (Button) findViewById(R.id.canMake);
		canMakeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sqlDb.getPossibleDrinks() == null) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							MyBar.this);
					adb.setTitle("Sorry");
					adb.setMessage("You can't make any drinks yet!\nUpdate your inventory first");
					adb.setPositiveButton("Ok", null);
					adb.show();
				} else {
					Intent myIntent = new Intent(v.getContext(),
							CanMakeDrinks.class);
					MyBar.this.startActivity(myIntent);
				}
			}
		});

		/**
		 * Button event for "Ingredients".
		 * Sets up tabs for 'Add Items' and 'My Inventory'
		 */
		final Button ingredientButton = (Button) findViewById(R.id.ingredients);
		ingredientButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), TabInventory.class);
				MyBar.this.startActivity(myIntent);
			}
		});
		
		/**
		 * Button event for "Browse Drinks"
		 */
		final Button browseButton = (Button) findViewById(R.id.browse);
		browseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent myIntent = new Intent(v.getContext(), AllDrinks.class);
				Intent myIntent = new Intent(v.getContext(), TabInventory2.class);
				MyBar.this.startActivity(myIntent);
			}
		});

		/**
		 * Button event for "Random Drink"
		 */
		final Button randomAllButton = (Button) findViewById(R.id.random);
		randomAllButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				randomDrink();
			}
		});

		/**
		 * Button event for "Random Drink I Can Make"
		 */
		final Button randomCanMakeButton = (Button) findViewById(R.id.randomCanMake);
		randomCanMakeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sqlDb.getPossibleDrinks() == null) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							MyBar.this);
					adb.setTitle("Sorry");
					adb.setMessage("You can't make any drinks yet!\nUpdate your inventory first");
					adb.setPositiveButton("Ok", null);
					adb.show();
				} else {
					Cursor possibleDrinks = sqlDb.getPossibleDrinks();
					
					int randomIndex = (int) (Math.random() * possibleDrinks.getCount());
					possibleDrinks.moveToPosition(randomIndex);
					AlertDialog.Builder adb = new AlertDialog.Builder(
							MyBar.this);
					
					String drinkName = possibleDrinks.getString(0);
					
					Cursor cDrink = sqlDb.getDrinkInfo(drinkName);
					adb.setTitle(cDrink.getString(0) + " - Rating: " + cDrink.getString(1));
					
					int drink_id = cDrink.getInt(2);
					
					String instructions = cDrink.getString(3);
					
					String message = "Ingredients: \n";
					
					Cursor drinkIngreds = sqlDb.getDrinkIngredientsById(String.valueOf(drink_id));

					for(int i = 0; i < drinkIngreds.getCount() && !(drinkIngreds.isAfterLast()); i++){
						String ingredName = drinkIngreds.getString(0);
						message += ingredName + " - " + drinkIngreds.getString(1) + "\n";
						drinkIngreds.moveToNext();
					}
					message += "\n";
					message += "Instructions: \n";
					message += instructions;
					

					adb.setMessage(message);
					adb.setPositiveButton("Ok", null);
					adb.show();
				}
			}
		});
		
		 /**
		 * Button event for "Next"
		 */
		final Button nextButton = (Button) findViewById(R.id.next);
		nextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), MyFood.class);
				MyBar.this.startActivity(myIntent);
			}
		});

	}
	/**
	 * Function handles call to generate random drink and display.
	 * TODO Implement code to clear previous messages before another shake
	 */
	public void randomDrink(){
		Cursor allDrinks = sqlDb.getAllDrinks();
		int randomIndex = (int) (Math.random() * allDrinks.getCount());
		allDrinks.moveToPosition(randomIndex);
		AlertDialog.Builder adb = new AlertDialog.Builder(MyBar.this);
		
		
		String drinkName = allDrinks.getString(0);
		
		Cursor cDrink = sqlDb.getDrinkInfo(drinkName);
		adb.setTitle(cDrink.getString(0) + " - Rating: " + cDrink.getString(1));
		
		int drink_id = cDrink.getInt(2);
		
		String instructions = cDrink.getString(3);
		
		String message = "Ingredients: \n";
		
		Cursor drinkIngreds = sqlDb.getDrinkIngredientsById(String.valueOf(drink_id));

		for(int i = 0; i < drinkIngreds.getCount() && !(drinkIngreds.isAfterLast()); i++){
			String ingredName = drinkIngreds.getString(0);
			message += ingredName + " - " + drinkIngreds.getString(1) + "\n";
			drinkIngreds.moveToNext();
		}
		message += "\n";
		message += "Instructions: \n";
		message += instructions;
		

		adb.setMessage(message);
		adb.setPositiveButton("Ok", null);
		adb.show();
	}
}