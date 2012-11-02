package kevinpage.com;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This activity occurs when client is under the 'My Inventory'
 * tab under 'Ingredients'
 */
public class Have extends Activity {
	
	private SqlDatabase sqlDb;
	static ListView lvH;
	
	/**
	 * Updates the ListView elements
	 * @param lv The ListView display
	 * @param array The array of data to display
	 */
	private void fillData(ListView lv, ArrayList<String> array) {
		ArrayAdapter<String> help = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.check, array);
		lv.setAdapter(help);
	}

	/**
	 * Fills a String array based on a cursor
	 * @param cursor The cursor to parse through
	 * @return A String array based on data in cursor
	 */
	private ArrayList<String> fillArray(Cursor cursor){
		ArrayList<String> temp;
		if(cursor == null){
			temp = new ArrayList<String>();
		}
		else{
			temp = new ArrayList<String>(cursor.getCount());
			for(int i = 0; i<cursor.getCount() && !(cursor.isLast()); i++){
				if(cursor.isNull(0)){
					break;
				}
				temp.add(cursor.getString(0));
				cursor.moveToNext();
			}
		}
		return temp;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingredients);
		
		
		sqlDb = new SqlDatabase(this);
		
		Cursor ingreds = sqlDb.getHasIngredients("1");		
		
		ArrayList<String> array = fillArray(ingreds);		
		
		lvH = (ListView) findViewById(R.id.ingredient_list);
		fillData(lvH, array);//

		lvH.setTextFilterEnabled(true);
		
		/**
		 * Handles event when user clicks ingredient to remove.
		 */
		lvH.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Toast.makeText(
						getApplicationContext(),
						"Removed " + (((TextView) view).getText())
								+ " from Inventory", Toast.LENGTH_SHORT).show();
				

				sqlDb.updateHasValue(0, (((TextView) view).getText()).toString());	
				
				/** Fill in ingredients they now have */
				Cursor haveCursor = sqlDb.getHasIngredients("1");
				ArrayList<String> haveArray = fillArray(haveCursor);	
				fillData(lvH, haveArray);
				
				/** Fill in missing ingredients */
				Cursor missingCursor = sqlDb.getHasIngredients("0");
				ArrayList<String> missingArray = fillArray(missingCursor);
				fillData(DontHave.lvD, missingArray);
				
				
			}
		});

		/**
		 * Handles event when user clicks to go back to main menu.
		 * Reconfigure the drinks they are able to make.
		 */
		final Button mainButton = (Button) findViewById(R.id.main_menu);
		mainButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//data.canMakeDrinks.clear();
				/*for (Drink drink : data.allDrinks) {
					if (drink.canMake()) {
						if (!data.canMakeDrinks.contains(drink))
							data.canMakeDrinks.add(drink);
					}
				}*/
				finish();
			}
		});

	}
	
	@Override
	public void onResume(){
		super.onResume();
		Cursor ingreds = sqlDb.getHasIngredients("1");		
		
		ArrayList<String> array = fillArray(ingreds);		
		
		lvH = (ListView) findViewById(R.id.ingredient_list);
		fillData(lvH, array);
	}
}