package kevinpage.com;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DontHave extends Activity {
	
	static ListView lvD;
	
	private void fillData(ListView lv, String[] array) {
		ArrayAdapter<String> help=
			new ArrayAdapter<String>(getApplicationContext(), R.layout.check, array);
		lv.setAdapter(help);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
	  	super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients);
    
        for(String ingredient: data.totalIngredients) {
        	if(!data.ownedIngredients.contains(ingredient)) {
        		data.missingIngs.add(ingredient);
        	}
        }
        lvD = (ListView) findViewById(R.id.ingredient_list);
        data.al = (String []) data.missingIngs.toArray (new String [data.missingIngs.size ()]);
        fillData(lvD, data.al);
	    lvD.setTextFilterEnabled(true);
		lvD.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), "Added "+(((TextView) view).getText())+" to Inventory",
						          Toast.LENGTH_SHORT).show();						
						if(!data.ownedIngredients.contains((((TextView) view).getText()).toString())) {
							data.ownedIngredients.add((((TextView) view).getText()).toString());
						}
						data.missingIngs.remove((((TextView) view).getText()).toString());
				        data.al = (String []) data.missingIngs.toArray (new String [data.missingIngs.size ()]);
				        data.al2 = (String []) data.ownedIngredients.toArray (new String [data.ownedIngredients.size ()]);
				        fillData(lvD,data.al);
				        fillData(Have.lvH,data.al2);
			}
		});
		
	    final Button mainButton = (Button) findViewById(R.id.main_menu);
        mainButton.setOnClickListener(new View.OnClickListener() {
      	public void onClick(View v) {
      		data.canMakeDrinks.clear();
            for(Drink drink : data.allDrinks) {
            	if(drink.canMake()) {
            		if(!data.canMakeDrinks.contains(drink))
            			data.canMakeDrinks.add(drink);
            	}
            }
            finish();
      	}
        });
        
        
    }
}
