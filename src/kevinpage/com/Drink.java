package kevinpage.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.AlertDialog;

public class Drink implements Comparable<Drink>{

	public HashMap<String,String> ingredients = new HashMap<String,String>();
	public String name;
	public int rating;
	public String instructions;

	Drink(String name, int rating, ArrayList<String> ingredientNames, ArrayList<String> amount, String instructions){
		if (ingredientNames.size() != amount.size())
			return; //Add an Exception later
		for(int i = 0; i < ingredientNames.size(); i++) {
			this.ingredients.put(ingredientNames.get(i), amount.get(i));
		}
		this.name = name;
		this.rating = rating;
		this.instructions = instructions;
	}
	
	Drink(){
	}
	
	public String getName() {
		return this.name;
	}

	public boolean canMake() {
		boolean canMake = true;
		Set<String> neededIngredients = this.ingredients.keySet();
		for(String ingredient: neededIngredients) {
			if (!data.ownedIngredients.contains(ingredient)) {
				canMake = false;
				break;
			}
		}
		return canMake;
	}
	
	
	public String toString(){
		return this.name;
	}
	
	public int compareTo(Drink drink){
		return this.name.compareTo(drink.name);
	}
	
	public String getDisplayMessage(){
		String message = "Ingredients:\n";
		for(String ingredient : this.ingredients.keySet()){
			message += ingredient + " - " + this.ingredients.get(ingredient) + "\n";
		}
		message += "\n";
		message += "Instructions: \n";
		message += this.instructions;
		return message;
	}
	
	public String getDisplayTitle(){
		return this.name + " - Rating: " + this.rating;
	}
	
	public static void configureDialog(AlertDialog.Builder adb, Drink drink){
	 	adb.setTitle(drink.getDisplayTitle());
	 	adb.setMessage(drink.getDisplayMessage());
	 	adb.setPositiveButton("Ok", null);
	}
	
	
//	public boolean canMake() {
	//		Set<String> ingred = this.ingredients.keySet();
	//		boolean tf=true;
	//		for(Iterator<String> it = ingred.iterator(); it.hasNext();){
	//			if(!ownedIngredients.contains(it.next())){
				//				tf = false;
	//		}
	//	}
	//	return tf;
	//}
}