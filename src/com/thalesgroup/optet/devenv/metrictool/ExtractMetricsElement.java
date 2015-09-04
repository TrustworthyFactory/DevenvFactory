package com.thalesgroup.optet.devenv.metrictool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExtractMetricsElement {

	List<String> metricList = new ArrayList<String>();


	public  void print(Map m){
		Iterator iterator = m.entrySet().iterator();
		print(iterator);
	}

	public  void print(List l){
		Iterator iterator = l.iterator();
		while (iterator.hasNext()){
			System.out.println("->" + iterator.next());
		}
	}

	public  List<String> printRecursive(Object o){
		if (o == null)
			return null;

		if (o instanceof java.util.LinkedList)
			printRecursive((List) o);

		if (o instanceof java.util.HashMap)
			printRecursive((Map) o);

		return metricList; 

	}

	private  void printRecursive(Map map){
		System.out.println("map");
		if (map == null)
			return;

		if (map.size() > 0){
			print(map);

			Iterator iterator = map.values().iterator();
			Object o = null;
			while(iterator.hasNext()){
				o = iterator.next();
				if ((o instanceof java.util.HashMap) || (o instanceof java.util.LinkedList))
					printRecursive(o);
			}
		}
	}

	private  void printRecursive(List list){
		System.out.println("list");
		if (list == null)
			return;

		if (list.size() > 0){
			print(list);

			Iterator iterator = list.iterator();
			Object o = null;
			while(iterator.hasNext()){
				o = iterator.next();
				if ((o instanceof java.util.HashMap) || (o instanceof java.util.LinkedList))
					printRecursive(o);
			}
		}
	}

	private  void print(Iterator iter){
		String id = null;
		String description = null;
		
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			//System.out.println(entry.getKey() + " => " + entry.getValue());
			
			if (entry.getKey().equals("id"))
				id = entry.getValue().toString();
			if (entry.getKey().equals("description"))
				description = entry.getValue().toString();
			
		}
		
		System.out.println("add id to list " + id);
		if (id!=null && description!=null)
			metricList.add(id);
	}
}