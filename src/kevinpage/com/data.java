<<<<<<< HEAD
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
import java.util.TreeSet;

/**
 * Class that acts as shell to access data files
 */
public class data {

	static TreeSet<String> totalIngredients = new TreeSet<String>();
	static TreeSet<String> ownedIngredients = new TreeSet<String>();
	static TreeSet<String> missingIngs = new TreeSet<String>();
	static ArrayList<Drink> allDrinks = new ArrayList<Drink>();
	static ArrayList<String> canMakeDrinks = new ArrayList<String>();
	static boolean generatedDrinks = false;
	static String[] al2 = new String[data.ownedIngredients.size()];
	static String[] al = new String[data.totalIngredients.size()];

=======
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
import java.util.TreeSet;

/**
 * Class that acts as shell to access data files
 * @deprecated This class is no longer being used
 */
public class data {

	static TreeSet<String> totalIngredients = new TreeSet<String>();
	static TreeSet<String> ownedIngredients = new TreeSet<String>();
	static TreeSet<String> missingIngs = new TreeSet<String>();
	static ArrayList<Drink> allDrinks = new ArrayList<Drink>();
	static ArrayList<String> canMakeDrinks = new ArrayList<String>();
	static boolean generatedDrinks = false;
	static String[] al2 = new String[data.ownedIngredients.size()];
	static String[] al = new String[data.totalIngredients.size()];

>>>>>>> 4c0ef4acc9d24a2a7c2ec4412f950d238cfac694
}