package kevinpage.com;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Inv extends ListActivity {
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
	  	super.onCreate(savedInstanceState);

		String al [] = (String []) data.totalIngredients.toArray (new String [data.totalIngredients.size ()]);
	    setListAdapter(new ArrayAdapter<String>(this, R.layout.check, al));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if(!data.ownedIngredients.contains((((TextView) view).getText()).toString())) {
						Toast.makeText(getApplicationContext(), "Added "+(((TextView) view).getText())+" to Inventory",
						          Toast.LENGTH_SHORT).show();
						view.setBackgroundColor(0xffffffff);
						data.ownedIngredients.add((((TextView) view).getText()).toString());
					} else {
						Toast.makeText(getApplicationContext(), "Removed "+(((TextView) view).getText())+" from Inventory",
					          Toast.LENGTH_SHORT).show();
	                    view.setBackgroundColor(0x00000000);
						data.ownedIngredients.remove((((TextView) view).getText()));
            }
			}
		});
    }
}