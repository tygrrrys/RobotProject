package com.epam.javaacademy.bookrobot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompleteListOfURLs {
	
	Map <URL, ArrayList <String> > mapOfURLs = new HashMap<>();
	
	private static final Pattern URLPATTERN = Pattern.compile(
			"(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
					+ "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
					+ "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{}']*)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	
	public static void main(String[] args) {
		CompleteListOfURLs test = new CompleteListOfURLs();
		System.out.println(test.createMap().toString());
	}
	
	

	public Map <URL, ArrayList <String> > createMap () {
		
		ArrayList<URL> URLs;
		ListOfURLToDig listOfStartWebsites = new ListOfURLToDig();
		URLs = listOfStartWebsites.getURLList();
		
		for (URL url : URLs) {
			mapOfURLs.put(url, createURLs(url));
		}
		return mapOfURLs;
	}
	
	private static ArrayList <String> createURLs (URL url){
		ArrayList <String> result = null;
		
		CheckRobotTxt disallowed = new CheckRobotTxt();
		HashSet<String> disallowedURLs = disallowed.listOfUnallowedURLs(url.toString());
		String webContent = downloadPage (url);
		
		ArrayList<String> fetchedURLs =  fetchAllURL(webContent);
		result = filterURLs(fetchedURLs, disallowedURLs);
				
		return result;
	}

	private static ArrayList<String> fetchAllURL(String webContent) {
		ArrayList<String> result = new ArrayList<String>();
		
		Matcher matcher = URLPATTERN.matcher(webContent);
		while (matcher.find()) {
		    int matchStart = matcher.start(1);
		    int matchEnd = matcher.end();

		    String substring = webContent.substring(matchStart, matchEnd);
		    
		    if (substring.contains(".png") || substring.contains(".jpg"))
		    	continue;

		    result.add(substring);
		}
		
		return result;
	}
	
	private static ArrayList<String> filterURLs(ArrayList<String> fetchedURLs, HashSet <String> disallowed) {
		ArrayList<String> resulting = new ArrayList<>();
		HashSet<String> result = new HashSet <>();
		ArrayList<String> disallowedURLs = new ArrayList<String>();
		disallowedURLs.addAll(disallowed);
		
		for (int i = 0; i < disallowedURLs.size(); i++) {
			for (int j = 0; j < fetchedURLs.size(); j++) {
				if (fetchedURLs.get(j).contains(disallowedURLs.get(i)))
					fetchedURLs.remove(j);
				else result.add(fetchedURLs.get(j));
			}
		}
		resulting.addAll(result);
		return resulting;
	}
	

	private static String downloadPage(URL url) {
		
		try {
			BufferedReader contest = new BufferedReader(new InputStreamReader(url.openStream()));
			
			String line;
			StringBuffer buffer = new StringBuffer();
			
			while ((line = contest.readLine()) != null){
				buffer.append(line);
			}
			return buffer.toString();
			
		} catch (IOException e) {
			System.err.println("Cannot download page.");
		}
		
		return null;
	}
	
}