package kevinpage.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyFood extends Activity {
    Button seeFoods;
    Button foodAdd;
    Button previousPage;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfood);
        seeFoods = (Button) findViewById(R.id.food);
        foodAdd = (Button) findViewById(R.id.addfood);
        previousPage = (Button) findViewById(R.id.previous);
        
        
        seeFoods.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), AllDrinks.class);
				MyFood.this.startActivity(myIntent);
				
			}
		});
        
        
        
        foodAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), AllDrinks.class);
				MyFood.this.startActivity(myIntent);
				
			}
		});
        
        
        previousPage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), MyBar.class);
				MyFood.this.startActivity(myIntent);
				
			}
		});
        
    }
}
