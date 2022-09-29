//Reimported project.

package bu.edu.cs622.hw2;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import org.json.simple.*;
import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

/**
 * Program to read the contents of three JSON files, combine them in a single file, and search for predefined keywords.
 * JSON files contain information on Kickstarter projects taken from webrobots.io.
 * It searches the combined JSON file for 4 predefined keywords, prints the name, funding amount, and category 
 * of any projects found to contain those keywords.
 * It also keeps a search history that records each keyword and the number of times it was searched.
 * 
 * @author Jared Baca
 *
 */

public class kickstarterSearch {
	
	/**
	 * The data member of the class are: 
	 * 'history', a HashMap that records search terms and number of times they've been searched.
	 * and 'searchResults', an ArrayList of individual HashMaps, each of which contains 
	 * the data for a single Kickstarter project read from the JSON file.
	 */
	
	public static HashMap<String, Integer> history = new HashMap<String,Integer>();
	public static ArrayList<HashMap<String,Object>> searchResults = new ArrayList<>();
	
	/**
	 * Method to read the contents of a JSON file.
	 * @param source
	 * @return ArrayList of Strings containing each line of the JSON file
	 * @throws IOException
	 */

		public static ArrayList<String> readFile(File source) throws IOException {
			ArrayList<String> data = new ArrayList<String>();
			try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
				String s;
				while((s = reader.readLine()) != null) {
					data.add(s);
				}
			}
			return data;
		}
		
		/**
		 * Method to write the contents of the ArrayList provided by the readFile method 
		 * to a new file.
		 * @param ArrayList of Strings provided by readFile method
		 * @param destination File object
		 * @throws IOException
		 */
		
		public static void writeFile(ArrayList<String> data, File destination) throws IOException {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(destination,true))) {
				for (String s: data) {
					writer.write(s);
					writer.newLine();
				}
				writer.flush();
			}
		}
		
		/**
		 * Method to search for a given search term (String) in the combined JSON file 
		 * written by the writeFile method.
		 * Uses a BufferedReader to read each line of the JSON file. If the file contains
		 * the search term it uses Gson to convert that String into a HashMap (retaining the key/value 
		 * relationship of the original JSON)
		 * It then pushes that HashMap to the global searchResults ArrayList.
		 * @param term
		 * @param combinedFile
		 * @throws IOException
		 */
		
		public static void search(String term, File combinedFile) throws IOException {
			term = term.toLowerCase().trim();
			Gson gson = new Gson();
			HashMap<String,Object> gsonMap = new HashMap<>();
			
			try(BufferedReader searchReader = new BufferedReader(new FileReader(combinedFile))) {
				String line;
				while((line = searchReader.readLine()) != null) {
					if(line.toLowerCase().contains(term)) {
						gsonMap = gson.fromJson(line.substring(line.indexOf("data")+6, line.length()-1), gsonMap.getClass());
						searchResults.add(gsonMap);
						//System.out.println(line);
					}
				}
			}
			
			if(history.containsKey(term)) {
				history.replace(term, history.get(term)+1);
			} else {
				history.put(term, 1);
			}
		}
		
		/**
		 * Method to print the search history.
		 * Search history is a HashMap containing all searched keywords and a count of the number
		 * of times they've been searched.
		 */
		
		public static void printHistory() {
			
			System.out.print("\nSearch History");
			System.out.print("\n______________");
			System.out.println();
			for(String key: history.keySet()) {
				System.out.println(key + ", " + history.get(key) + " time(s)");
			}
		}
		
		/**
		 * Method to print search results contained in the searchResults ArrayList.
		 * Uses type casting to access values that are objects in the JSON
		 */
		
		public static void printResults() {
			System.out.println("Number of results: " + searchResults.size());
			for(HashMap<String,Object> searchItem : searchResults) {
				System.out.printf("%-65s",searchItem.get("name").toString().trim());
				System.out.printf("%-25s",searchItem.get("pledged").toString().trim());
				
				LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) searchItem.get("category");
				System.out.printf("%-15s %n", map.get("parent_name"));
			}
		}
		

	public static void main(String[] args) throws IOException {
		
		//Search terms
		
		final String SEARCH_TERM1 = "wearable";
		final String SEARCH_TERM2 = "fitness";
		final String SEARCH_TERM3 = "ocean";
		final String SEARCH_TERM4 = "Wearable";
		
		//Importing 3 JSON files

		File source1 = new File("Kickstarter_2022-07-14T03_20_06_406Z.json");
		File source2 = new File("Kickstarter_2022-08-11T03_20_04_440Z.json");
		File source3 = new File("Kickstarter_2022-09-15T03_20_10_337Z.json");
		
		//Creating destination file

		File destination = new File("CombinedFile.json");
		
		/*
		 * To be more memory efficient, I opted to appended each of the 3 input files to the output file
		 * one at a time (instead of writing them all to an ArrayList and then copying that entire
		 * list to the output file). 
		 * To do this, I had to pass the 'true' argument to the FileWriter object
		 * so it would not overwrite the file each time. To avoid always appending to my previous 
		 * output file, I included this line to delete the existing combinedFile at the start of each
		 * program loop.
		 */
		
		if(destination.exists()) {
			destination.delete();
		}
		
		//Read each input file and write to the output file one at a time.
		
		ArrayList<String> data = readFile(source1);
		writeFile(data, destination);
		data = readFile(source2);
		writeFile(data, destination);
		data = readFile(source3);
		writeFile(data, destination);
				
		System.out.printf("%-65s","Name");
		System.out.printf("%-25s","Funding Amount");
		System.out.printf("%-15s %n","Category");
		System.out.println("__________________________________________________________________________________________________________");
		
		search(SEARCH_TERM1,destination);
		search(SEARCH_TERM2,destination);
		search(SEARCH_TERM3,destination);
		search(SEARCH_TERM4,destination);
		printResults();
		printHistory();
	}
}