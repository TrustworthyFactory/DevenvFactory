/*
 *		OPTET Factory
 *
 *	Class OPAValue 1.0 10 sept. 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.datamodel;

/**
 * @author F. Motte
 *
 */
public class OPAValue {

	public static String getValue(String TWAttribute, String metric){
		
		System.out.println("Search OPA value " + TWAttribute+ "/" + metric);
		
		String result=null;
		switch (TWAttribute+ "/" + metric){
		case "Safety/safety functions ":
			result = "1";
			break;
		case "Security/SecurityMetric":
			result = "1";
			break;
		case "Openness/public interfaces":
			result = "1";
			break;
		case "Reliability/Reliability of Software":
			result = "1";
			break;
		case "Change Cycle/Stability/unit test coverage for stability":
			result = "1";
			break;
		case "Flexibility/Robustness/interceptet errors":
			result = "1";
			break;
		case "Change Cycle/Stability//Compliance with best programing practices":
			result = "0.75";
			break;
		case "Availability/availability of Software":
			result = "0.5";
			break;
		case "Scalability/Scalability of the software":
			result = "0.5";
			break;
		case "Software Complexity/structure of the software":
			result = "1";
			break;
		case "Data Integrity/integrity of data":
			result = "1";
			break;
		case "Data Validity/plausibility of data":
			result = "1";
			break;

		}
		
		System.out.println("return " + result);
		return result;
		
	}
	
}
