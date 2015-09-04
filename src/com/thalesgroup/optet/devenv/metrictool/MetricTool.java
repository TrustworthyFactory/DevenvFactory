package com.thalesgroup.optet.devenv.metrictool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.internal.Workbench;



public class MetricTool  extends Dialog{

	private Shell shlUsdlEditor;
	protected Object result;


	private Map<String, TreeItem> attrItemMap;
	private Map<String, TreeItem> metricItemMap;
	private Map<String, String> attrMap;
	private MetricToolClient client ;

	public MetricTool(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
		client = new MetricToolClient();
		attrItemMap = new HashMap<String, TreeItem>(); 
		metricItemMap = new HashMap<String, TreeItem>(); 
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {

		createContents();
		shlUsdlEditor.open();
		shlUsdlEditor.layout();
		Display display = getParent().getDisplay();
		while (!shlUsdlEditor.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}





	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlUsdlEditor = new Shell(getParent(), getStyle());
		shlUsdlEditor.setSize(800, 600);
		shlUsdlEditor.setText("Metric Tool : Metric definitions");
		final Tree tree = new Tree(shlUsdlEditor, SWT.BORDER | SWT.SINGLE | SWT.H_SCROLL| SWT.V_SCROLL);



		try {
			attrMap = client.getAttributes();

			SortedSet<String> keys = new TreeSet<String>(attrMap.keySet());
			for (String key : keys) { 
				String value = attrMap.get(key);

				//for (Map.Entry<String, String> entry : attrMap.entrySet()) {
				System.out.println("Key : " + key + " Value : "
						+ value);
				TreeItem item0 = new TreeItem(tree, 0);
				item0.setText("Attribute ID : " + key + " -- Attribute Name : " +value);
				item0.setData(key);

				// for the attribute, find the metrics

				//				List<String> listMetrics = client.getMetrics (key);
				//				if (listMetrics!=null){
				//					for (Iterator iterator = listMetrics.iterator(); iterator
				//							.hasNext();) {
				//						String string = (String) iterator.next();
				//						TreeItem item1 = new TreeItem(item0, 0);
				//
				//						Map<String, String> metricAttr = client.getMetric(string);
				//						item1.setText("Metric ID : " + string + " -- Metric Name : " + metricAttr.get("name"));
				//
				//						for (Map.Entry<String, String> entry2 : metricAttr.entrySet()) {
				//							TreeItem item2 = new TreeItem(item1, 0);
				//							item2.setText(entry2.getKey()+ " : " + entry2.getValue() );
				//						}
				//
				//					}
				//				}


			}



		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}






		tree.setBounds(10, 10, 770, 540);
		tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TreeItem[] selection = tree.getSelection();
				for (int i = 0; i < selection.length; i++){
					string += selection[i] + " ";
					System.out.println("Selection={" + string + "}");
					String key = (String) selection[i].getData();
					try {
						List<String> listMetrics = client.getMetrics (key);


						if (listMetrics!=null){
							for (Iterator iterator = listMetrics.iterator(); iterator
									.hasNext();) {
								String string2 = (String) iterator.next();
								TreeItem item1 = new TreeItem(selection[i], 0);
								Map<String, String> metricAttr = client.getMetric(string2);
								item1.setText("Metric ID : " + string2 + " -- Metric Name : "  + metricAttr.get("name"));
								item1.getParentItem().setExpanded(true);




								for (Map.Entry<String, String> entry2 : metricAttr.entrySet()) {
									TreeItem item2 = new TreeItem(item1, 0);
									item2.setText(entry2.getKey()+ " : " + entry2.getValue() );
								}

							}
							tree.redraw();
						}





					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}




			}
		});
		tree.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TreeItem[] selection = tree.getSelection();
				for (int i = 0; i < selection.length; i++)
					string += selection[i] + " ";
				System.out.println("DefaultSelection={" + string + "}");
			}
		});
		tree.addListener(SWT.Expand, new Listener() {
			public void handleEvent(Event e) {
				System.out.println("Expand={" + e.item + "}");
			}
		});
		tree.addListener(SWT.Collapse, new Listener() {
			public void handleEvent(Event e) {
				System.out.println("Collapse={" + e.item + "}");
			}
		});
		tree.getItems()[0].setExpanded(true);

	}


}