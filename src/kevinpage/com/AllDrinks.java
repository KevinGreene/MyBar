package kevinpage.com;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class AllDrinks extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   	 	setContentView(R.layout.drinks);

   	 	String[] drinkName = new String[data.allDrinks.size()];
   	 	
        for(int i=0; i<data.allDrinks.size();i++) {
        		drinkName[i]=((data.allDrinks.get(i)).getName());
        }
        
        ListView lv = (ListView) findViewById(R.id.makeable_drinks);
	    lv.setAdapter(new ArrayAdapter<String>(this, R.layout.check, drinkName));
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				 	AlertDialog.Builder adb=new AlertDialog.Builder(AllDrinks.this);
					Drink drink = data.allDrinks.get(position);
				 	adb.setTitle(drink.getDisplayTitle());
				 	adb.setMessage(drink.getDisplayMessage());
				 	adb.setPositiveButton("Ok", null);
				 	adb.show();
			}
		});
		
		final Button mainButton = (Button) findViewById(R.id.main_menu);
		mainButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), MyBar.class);
				AllDrinks.this.startActivity(myIntent);
			}
		});
	}
}