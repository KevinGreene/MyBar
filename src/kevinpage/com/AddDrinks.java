package kevinpage.com;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddDrinks extends Activity {
	private LinearLayout vlayout;
	private LinearLayout hlayout;
	private Button buttonadd;
	private Button buttondelete;
	private Button buttonsave;
	private Button menubutton;
	private EditText namefield;
	private EditText ratingField;
	private EditText instructField;
	
	private EditText addingredient;
	
	private int ingredientcount;
	private int amountcount;
	private int ingredientidcounter;
	private int amountidcounter;
	private int vlayerlevel;
	private int hlayoutId;
	
	private MyBarDatabase sqlDb;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form);
		
		sqlDb = new MyBarDatabase(this);
		
		context = this;
		
		hlayoutId = 0;
		ingredientcount = 1;
		amountcount = 1;
		ingredientidcounter = 0;
		amountidcounter = 0;
		vlayerlevel = 3;
		vlayout = (LinearLayout) findViewById(R.id.vlayout);
		buttonadd = (Button) findViewById(R.id.addingredient);
		buttondelete = (Button) findViewById(R.id.deleteingredient);
		buttonsave = (Button) findViewById(R.id.save);
		menubutton = (Button) findViewById(R.id.menu);
		namefield = (EditText) findViewById(R.id.nametext);
		ratingField = (EditText) findViewById(R.id.rating);
		instructField = (EditText) findViewById(R.id.instructions);
		buttondelete.setVisibility(View.GONE);
		buttonsave.setVisibility(View.GONE);

		buttonadd.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				hlayout = new LinearLayout(v.getContext());
				hlayout.setId(hlayoutId++);
				hlayout.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				vlayout.addView(hlayout, vlayerlevel++);

				EditText addingredient = new EditText(v.getContext());
				EditText addamount = new EditText(v.getContext());
				
				addingredient.setId(ingredientidcounter++);
				addamount.setId(amountidcounter++);
				addingredient.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				addamount.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				addingredient.setHint("Ingredient " + ingredientcount++);
				addamount.setHint("Amount " + amountcount++);
				hlayout.addView(addamount);
				hlayout.addView(addingredient);
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
				ingredientcount--;
				amountcount--;
				hlayoutId--;

				if (vlayerlevel == 3) {
					buttondelete.setVisibility(View.GONE);
					buttonsave.setVisibility(View.GONE);
				}

			}
		});

		buttonsave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// take text in string text box, add it to database name field
				ArrayList<String> ingreds = new ArrayList<String>();
				ArrayList<String> amounts = new ArrayList<String>();
				for(int i = 3; i < hlayoutId + 3; i ++){
					LinearLayout vert = (LinearLayout)vlayout.getChildAt(i);
					EditText horizAmount = (EditText) vert.getChildAt(0);
					EditText horizIngred = (EditText) vert.getChildAt(1);
					
					Log.d("Horiz Ingred: ", horizIngred.getText().toString());
					Log.d("Horiz Amount: ", horizAmount.getText().toString());
					
					ingreds.add(horizIngred.getText().toString());
					amounts.add(horizAmount.getText().toString());
				}
				try{
					if(namefield.getText().toString().equals("") || ratingField.getText().toString().equals("")
							|| instructField.getText().toString().equals("")){
						Toast.makeText(context, "Please fill all fields and/or remove dups", Toast.LENGTH_SHORT).show();
					}
					else{sqlDb.insertDrink(namefield.getText().toString(), Integer.valueOf(ratingField.getText().toString()),
							instructField.getText().toString(), ingreds, amounts);}
						
				}catch(SQLiteConstraintException ex){
					Toast.makeText(context, "Please fill all fields and/or remove dups", Toast.LENGTH_SHORT).show();
				}
				
				
				Intent myIntent = new Intent(v.getContext(), TabInventory2.class);
				AddDrinks.this.startActivity(myIntent);
				// use some kind of for loop to take each amount and ingredient
				// and associate them with the same name in the database

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
