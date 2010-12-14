/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kevinpage.com;

import java.util.ArrayList;


public class IngredientsDatabase {

	static ArrayList<String> totalIngredients = new ArrayList<String>();
	static ArrayList<String> ownedIngredients = new ArrayList<String>();
	static ArrayList<Drink> allDrinks = new ArrayList<Drink>();
	static ArrayList<Drink> canMakeDrinks = new ArrayList<Drink>();
    
    public static ArrayList<String> getTotalIngredients() {
    	return totalIngredients;
    }
    
    public static ArrayList<String> getOwnedIngredients() {
    	return ownedIngredients;
    }
    public static ArrayList<Drink> getAllDrinks() {
    	return allDrinks;
    }
    public static ArrayList<Drink> getCanMakeDrinks() {
    	return canMakeDrinks;
    }
}