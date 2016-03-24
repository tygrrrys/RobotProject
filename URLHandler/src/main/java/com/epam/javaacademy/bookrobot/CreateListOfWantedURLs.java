package com.epam.javaacademy.bookrobot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateListOfWantedURLs {

	static HashSet<String> listOfURLs = new HashSet<>();

	final int lvl = 3;
	private static final Pattern URLPATTERN = Pattern.compile(
			"(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
					+ "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{}']*)",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

	public static void main(String[] args) {

		/*Map<String, HashSet<String>> checkingMap;

		ListOfURLToDig bookstores = new ListOfURLToDig();
		HashSet<URL> bookstoresURLs = new HashSet<>();
		bookstoresURLs.addAll(bookstores.URLs);

		Iterator iter = bookstoresURLs.iterator();
		String test = iter.next().toString();

		HashSet<String> initListOfURLs = new HashSet<>();
		HashSet<String> firstIterlistOfURLs = new HashSet<>();
		initListOfURLs.add(test);
		createURLs(initListOfURLs);
		System.out.println(listOfURLs);
		firstIterlistOfURLs.addAll(listOfURLs);
		createURLs(firstIterlistOfURLs);
		System.out.println("\n" + listOfURLs);*/
		
		System.out.println(new CreateListOfWantedURLs().createMap());

	}

	Map<String, HashSet<String>> createMap() {
		Map<String, HashSet<String>> urlMap = new HashMap<>();
		// iteracja po liscie linkow do przeszukania (baza ksiegarni)
		ListOfURLToDig bookstores = new ListOfURLToDig();

		HashSet<String> firstIterlistOfURLs = new HashSet<>();
		HashSet<String> sectIterlistOfURLs = new HashSet<>();
		HashSet<String> thirdIterlistOfURLs = new HashSet<>();
		HashSet<String> initlistOfURLs = new HashSet<>();

		for (int i = 0; i < bookstores.URLs.size(); i++) {

			String key = bookstores.URLs.get(i).toString();
			initlistOfURLs.add(key);
			createURLs(initlistOfURLs);
			firstIterlistOfURLs.addAll(listOfURLs);
			createURLs(firstIterlistOfURLs);
			sectIterlistOfURLs.addAll(listOfURLs);
		/*	createURLs(sectIterlistOfURLs);
			thirdIterlistOfURLs.addAll(listOfURLs);
			createURLs(thirdIterlistOfURLs);*/
			urlMap.put(key, listOfURLs);
		}

		return urlMap;
	}

	private static void createURLs(HashSet<String> urlString) {

		CheckRobotTxt disallowed = new CheckRobotTxt();
		Iterator<String> iter = urlString.iterator();

		try {
			while (iter.hasNext()) {
				URL url;
				String item = iter.next();
				url = new URL(item);
				HashSet<String> disallowedURLs = disallowed.listOfUnallowedURLs(item);
				String webContent = downloadPage(item);
				String hostName = url.getHost().toLowerCase();

				HashSet<String> fetchedURLs = fetchAllURL(webContent, hostName);
				listOfURLs.addAll(filterURLs(fetchedURLs, disallowedURLs));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	private static String downloadPage(String urlString) {

		try {
			URL url = new URL(urlString);
			BufferedReader contest = new BufferedReader(new InputStreamReader(url.openStream()));

			String line;
			StringBuffer buffer = new StringBuffer();

			while ((line = contest.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();

		} catch (IOException e) {
			//System.err.println("Cannot download page.");
		}

		return null;
	}

	private static HashSet<String> fetchAllURL(String webContent, String hostName) {
		HashSet<String> result = new HashSet<String>();

		Matcher matcher;
		try {
			matcher = URLPATTERN.matcher(webContent);
		} catch (Exception e) {
			return new HashSet<String>();
		}
		while (matcher.find()) {
			int matchStart = matcher.start(1);
			int matchEnd = matcher.end();

			String substring = webContent.substring(matchStart, matchEnd);

			if (substring.contains(hostName) && !substring.contains(".jpg") && !substring.contains(".js")
					&& !substring.contains(".png") && !substring.contains(".css") && !substring.contains(".ico")) {
				result.add(substring);
			}
		}

		return result;
	}
	

	private static ArrayList<String> filterURLs(HashSet<String> fetchedURLsSet, HashSet<String> disallowedURLsSet) {
		ArrayList<String> disallowedURLs = new ArrayList<String>();
		disallowedURLs.addAll(disallowedURLsSet);
		ArrayList<String> fetchedURLs = new ArrayList<String>();
		fetchedURLs.addAll(fetchedURLsSet);
		HashSet<String> result = new HashSet<>();
		ArrayList<String> resulting = new ArrayList<>();

		for (int i = 0; i < disallowedURLs.size(); i++) {
			for (int j = 0; j < fetchedURLs.size(); j++) {
				if (fetchedURLs.get(j).contains(disallowedURLs.get(i)))
					fetchedURLs.remove(j);
				else
					result.add(fetchedURLs.get(j));
			}
		}
		resulting.addAll(result);
		return resulting;
	}

}
