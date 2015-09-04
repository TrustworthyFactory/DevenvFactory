package com.thalesgroup.optet.devenv.metrictool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExtractMetricElement {

	Map<String, String> metricMap = new HashMap<String, String>();


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

	public  Map<String, String> printRecursive(Object o){
		if (o == null)
			return null;

		if (o instanceof java.util.LinkedList)
			printRecursive((List) o);

		if (o instanceof java.util.HashMap)
			printRecursive((Map) o);

		return metricMap; 

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

		
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			System.out.println(entry.getKey() + " => " + entry.getValue());

			if (entry.getKey()!=null && entry.getValue()!=null)
				metricMap.put(entry.getKey().toString(), entry.getValue().toString());
		}
		

	}
}