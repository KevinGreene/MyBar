package kevinpage.com;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;



public class AddDrinks extends Activity {
private LinearLayout vlayout;
private Button buttonadd;
private Button buttondelete;
private Button buttonsave;
private Button menubutton;
private EditText namefield;
private int ingredientcount;
private int amountcount;
private int ingredientidcounter;
private int amountidcounter;
private int vlayerlevel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        ingredientcount = 1;
        amountcount = 1;
        ingredientidcounter = 0;
        amountidcounter = 0;
        vlayerlevel = 1;
        vlayout = (LinearLayout) findViewById(R.id.vlayout);
        buttonadd = (Button) findViewById(R.id.addingredient);
        buttondelete = (Button) findViewById(R.id.deleteingredient);
        buttonsave = (Button) findViewById(R.id.save);
        menubutton = (Button) findViewById(R.id.menu);
        namefield = (EditText) findViewById(R.id.nametext);
        buttondelete.setVisibility(View.GONE);
        buttonsave.setVisibility(View.GONE);
        
        
        buttonadd.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				LinearLayout newhorizontal = new LinearLayout(v.getContext());
				newhorizontal.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				vlayout.addView(newhorizontal, vlayerlevel++);
				
				EditText addingredient = new EditText(v.getContext());
				EditText addamount = new EditText(v.getContext());
				addingredient.setId(ingredientidcounter++);
				addamount.setId(amountidcounter++);
				addingredient.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				addamount.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				addingredient.setHint("Ingredient " + ingredientcount++);
				addamount.setHint("Amount " + amountcount++);
				newhorizontal.addView(addamount);
				newhorizontal.addView(addingredient);
				buttondelete.setVisibility(View.VISIBLE);
				buttonsave.setVisibility(View.VISIBLE);
				
				
				
			}
		});
        
        
        
        buttondelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vlayout.removeViewAt(vlayerlevel - 1);
				vlayerlevel--;
				ingredientidcounter--;
				amountidcounter--;
				
				if (vlayerlevel == 1){
					buttondelete.setVisibility(View.GONE);
					buttonsave.setVisibility(View.GONE);
				}
				
			}
		});
        
        
        
        
        
        buttonsave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// take text in string text box, add it to database name field
				
				// use some kind of for loop to take each amount and ingredient and associate them with the same name in the database
				
				// take user back to viewing AllDrinks
				
			}
		});
        
        
        
        menubutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), MyBar.class);
				AddDrinks.this.startActivity(myIntent);
				
			}
		});
        
    }

}
