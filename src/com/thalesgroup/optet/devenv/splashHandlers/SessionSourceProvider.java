/*
 *		OPTET Factory
 *
 *	Class SessionSourceProvider 1.0 7 nov. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.splashHandlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.preferences.PreferenceConstants;

/**
 * @author F. Motte
 *
 */
public class SessionSourceProvider extends AbstractSourceProvider{

	public SessionSourceProvider() {
		super();
		System.out.println("constructor SessionSourceProvider");
		// TODO Auto-generated constructor stub
	}


	public final static String SESSION_STATE = "com.thalesgroup.session.kind";
	
	private String role = "";
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISourceProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISourceProvider#getCurrentState()
	 */
	@Override
	public Map<String, String> getCurrentState() {
		
		System.out.println("get current state");
		Map<String, String> currentStateMap = new HashMap<String, String>(1);
		String currentState =  role;
		currentStateMap.put(SESSION_STATE, currentState);
		System.out.println("get current state " + SESSION_STATE + "   " + currentState); 
		return currentStateMap;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISourceProvider#getProvidedSourceNames()
	 */
	@Override
	public String[] getProvidedSourceNames() {
		
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		System.out.println("c'est la  " + store.getString(PreferenceConstants.GroupName));
		setLogin(store.getString(PreferenceConstants.GroupName));
		return new String[] {SESSION_STATE};
	}

	
	public void setLogin(String role){
		System.out.println("set role          " + role);
		this.role = role;
		
		fireSourceChanged(ISources.WORKBENCH, SESSION_STATE, role);
	}
}
