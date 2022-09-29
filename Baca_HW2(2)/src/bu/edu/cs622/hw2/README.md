# Kickstarter Search
##### Author: Jared Baca

This application reads the contents of 3 JSON files containing information on various Kickstarter projects. It then writes the contents of those files to a single, combined file. It then performs a search of 4 predefined keywords and displays the name, funding amount, and category of any Kickstarter project containing those keywords. It also keeps a search history of all keywords as well as the number of times they've been searched. After performing the search it prints the search results and the search history.

## Data Members

### history

A HashMap whose keys are the search terms entered and values are the number of times each word has been searched.

### searchResults

An ArrayList of HashMaps, each of which is a single JSON entry (Kickstarter project).

## Methods

### readFile
Takes a Java File object as an argument and throws IOException. It uses a BufferedReader to add each entry of the JSON file to an ArrayList of Strings.

### writeFile
Take an ArrayList of Strings and a Java File object (the destination) as arguments. Uses a BufferedWriter to append the contents of the ArrayList to a new file in the directory.

### search
Takes a String (search term), File object (the combined JSON file output by writeFile) and throws IOException. This method reads each line of the combined JSON file and checks for the given search term. If found, it converts the JSON string to a HashMap using Gson, and pushes that HashMap to the searchResults ArrayList. It also adds the search term to the history HashMap and/or increments the count.

### printHistory
Prints the contents of the search history to the console in for the format "[search_term], [times searched] times"

### printResults
Prints the contents of the searchResults ArrayList to the console. Columns include the name, funding amount, and category of any projects found to contain the keywords.

### main
Main method performs the search. Creates 4 named constants for the search terms, imports the JSON files from the project's directory, and creates the combined JSON file. It then performs the 4 searches, prints the results, and prints the search history.
