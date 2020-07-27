package de.renz.staticdata;

import java.util.ArrayList;
import java.util.List;

public class Item extends StaticData{

	private List<String> tags = new ArrayList<>();

	public List<String> getTags(){
		return tags;
	}
}
