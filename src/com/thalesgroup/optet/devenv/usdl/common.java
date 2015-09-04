/*
 *		OPTET Factory
 *
 *	Class common 1.0 7 août 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.usdl;

import java.util.UUID;

/**
 * @author F. Motte
 *
 */
public class common {
private String ID = UUID.randomUUID().toString();

public String getID() {
	return ID;
}

public void setID(String iD) {
	ID = iD;
}

}
