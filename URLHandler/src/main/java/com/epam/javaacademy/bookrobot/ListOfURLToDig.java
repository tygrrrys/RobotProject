package com.epam.javaacademy.bookrobot;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ListOfURLToDig {

	ArrayList<URL> URLs = new ArrayList<>();

	public ListOfURLToDig() {
		try {
			URLs.add(new URL("https://www.nexto.pl"));
		//	URLs.add(new URL("https://woblink.com"));
		//	URLs.add(new URL("http://www.publio.pl"));
		//	URLs.add(new URL("http://virtualo.pl"));
		} catch (MalformedURLException e) {
			System.err.println("Cannot make default list of books!");
		}
	}

	
	public boolean addURL(String website) {
		try {
			URLs.add(new URL(website));
		} catch (MalformedURLException e) {
			System.err.println("Cannot add this URL.");
			return false;
		}
		return true;
	}
	
	public boolean delURL (String website){
		
		if (URLs.contains(website)){
			URLs.remove(URLs.indexOf(website));
			return true;
		}
		return false;
	}
	
	public ArrayList<URL> getURLList (){
		return URLs;
	}

}
