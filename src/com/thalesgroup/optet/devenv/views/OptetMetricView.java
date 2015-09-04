package com.thalesgroup.optet.devenv.views;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.thalesgroup.optet.common.data.OptetDataModel;
import com.thalesgroup.optet.common.jaxb.optet.OptetType.Metrics.Metric;
import com.thalesgroup.optet.common.views.OptetMetricModel;
import com.thalesgroup.optet.common.views.OptetMetricModelProvider;
import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;
import com.thalesgroup.optet.devenv.views.OptetMetricView.TwAttribute;
import com.thalesgroup.optet.twmanagement.Evidence;
import com.thalesgroup.optet.twmanagement.impl.SettingsModuleImpl;

/**
 * The metric view for optet
 * Contains all the metric entry find by all the audit tools
 * 
 * @author F. Motte
 *
 */
public class OptetMetricView extends ViewPart {

	private TreeViewer	      m_treeViewer;
	String projectID = null;

	public void createPartControl(Composite parent){
		Tree addressTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		addressTree.setHeaderVisible(true);
		m_treeViewer = new TreeViewer(addressTree);

		TreeColumn column1 = new TreeColumn(addressTree, SWT.LEFT);
		addressTree.setLinesVisible(true);
		column1.setAlignment(SWT.LEFT);
		column1.setText("TWAttributes/Evidences");
		column1.setWidth(160);
		TreeColumn column2 = new TreeColumn(addressTree, SWT.RIGHT);
		column2.setAlignment(SWT.LEFT);
		column2.setText("Value");
		column2.setWidth(100);
		TreeColumn column3 = new TreeColumn(addressTree, SWT.RIGHT);
		column3.setAlignment(SWT.LEFT);
		column3.setText("Expected value");
		column3.setWidth(100);


		m_treeViewer.setContentProvider(new AddressContentProvider());
		m_treeViewer.setLabelProvider(new TableLabelProvider());


	}


	public void setFocus(){
		System.out.println("setFocus");
		List<Object> twAttributes = new ArrayList<Object>();

		List<Metric> metricList = OptetDataModel.getInstance().getMetrics();
		Map<String, TwAttribute> twAttributeMap = new HashMap<String, TwAttribute>();
		Map<String, Evidence> evidenceMap = new HashMap<String, Evidence>();

		OptetDataModel.getInstance().computeMetric();

		for (Iterator iterator = metricList.iterator(); iterator.hasNext();) {
			Metric metric = (Metric) iterator.next();
			System.out.println("type " +metric.getType() );
			if (metric.getType()!=null){
				if (metric.getType().equals("Evidence")){
					Evidence twp1 = new Evidence(null, metric.getName(), metric.getResult());
					System.out.println("add evidence " + metric.getName());
					evidenceMap.put(metric.getName(), twp1);
				}
			}
		}

		for (Iterator iterator = metricList.iterator(); iterator.hasNext();) {
			Metric metric = (Metric) iterator.next();
			if (metric.getType()!=null){

				if (metric.getType().equals("TwAttribute")){
					TwAttribute twa = new TwAttribute(metric.getName(), metric.getResult(), OptetDataModel.getInstance().getExpectedTWAttribute(metric.getName()));
					//					if (twAttributeMap.containsKey(metric.getName())){
					//						System.out.println("contain key " + metric.getName());
					//						twAttributeMap.remove(metric.getName());
					//					}

					Set<String> evidencelist =OptetDataModel.getInstance().getEvidenceForTWAttribute(twa.toString());
					for (Iterator iterator2 = evidencelist.iterator(); iterator2
							.hasNext();) {
						String string = (String) iterator2.next();
						System.out.println("add evidence " + string + " for TWAttribute " + twa.toString());




						Evidence ev = evidenceMap.get(string);
						if (ev!=null){
							Evidence ev2 = new Evidence(twa, ev.name, ev.value);
							ev2.setExpectedValue(OptetDataModel.getInstance().getExpectedMetric(ev.toString(), twa.toString() ));
							twa.addTwProperty(ev2);
						}
					}

					twAttributeMap.put(metric.getName(), twa);
					// twAttributeMap.values();

					//					if (twAttributeMap.values() instanceof List)
					//						twAttributes = (List)twAttributeMap.values();
					//					else
					//						twAttributes = new ArrayList(twAttributeMap.values());



				}
			}
		}

		m_treeViewer.setInput(new ArrayList<TwAttribute>(twAttributeMap.values()));
		m_treeViewer.expandAll();
		m_treeViewer.getControl().setFocus();
	}

	class TwAttribute{

		List<Evidence> evidenceList;
		String name;
		String value;
		String expectedValue;

		public TwAttribute(String name, String value, String expectedValue) {
			super();
			this.name = name;
			this.value = value;
			this.expectedValue = expectedValue;
			evidenceList = new ArrayList<>();
		}

		public void addTwProperty(Evidence prop){
			evidenceList.add(prop);
		}

		public Evidence[] getTwproperties(){
			return  evidenceList.toArray(new Evidence[evidenceList.size()]);
		}

		public String toString(){
			return name;
		}

		public String getValue(){
			return value;
		}

		public String getExpectedValue(){
			return expectedValue;
		}
	}

	class Evidence{
		TwAttribute	TwAttribute;
		String value;
		String name;
		String expectedValue;

		public Evidence(TwAttribute TwAttribute, String name, String value){
			this.TwAttribute = TwAttribute;
			this.name = name;
			this.value = value;

		}

		public String getValue(){
			return value;
		}

		public String toString(){
			System.out.println("Evidence " + name);
			return name;
		}
		public void setExpectedValue(String expectedValue){
			this.expectedValue= expectedValue;
		}

		public String getExpectedValue(){
			return expectedValue;
		}
	}





	class AddressContentProvider implements ITreeContentProvider{
		public Object[] getChildren(Object parentElement){
			if (parentElement instanceof List)
				return ((List<?>) parentElement).toArray();
			if (parentElement instanceof TwAttribute)
				return ((TwAttribute) parentElement).getTwproperties();
			return new Object[0];
		}

		public Object getParent(Object element){
			if (element instanceof Evidence)
				return ((Evidence) element).TwAttribute;

			return null;
		}

		public boolean hasChildren(Object element){
			if (element instanceof List)
				return ((List<?>) element).size() > 0;
				if (element instanceof TwAttribute)
					return ((TwAttribute) element).getTwproperties().length > 0;
					return false;
		}

		public Object[] getElements(Object twAttributes){
			// twAttributes ist das, was oben in setInput(..) gesetzt wurde.
			return getChildren(twAttributes);
		}

		public void dispose(){
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
		}
	}


	class TableLabelProvider implements ITableLabelProvider, ITableFontProvider, ITableColorProvider{

		FontRegistry registry = new FontRegistry();

		public Image getColumnImage(Object element, int columnIndex){
			return null;
		}

		public String getColumnText(Object element, int columnIndex){
			switch (columnIndex){
			case 0: return element.toString();
			case 1:
				if (element instanceof Evidence)
					return ((Evidence)element).getValue();
				if (element instanceof TwAttribute)
					return ((TwAttribute)element).getValue();
			case 2:
				if (element instanceof Evidence)
					return ((Evidence)element).getExpectedValue();
				if (element instanceof TwAttribute)
					return ((TwAttribute)element).getExpectedValue();
			}
			return null;
		}

		public void addListener(ILabelProviderListener listener){
		}

		public void dispose(){
		}

		public boolean isLabelProperty(Object element, String property){
			return false;
		}

		public void removeListener(ILabelProviderListener listener){
		}


		public Font getFont(Object element, int columnIndex) {
			//System.out.println("getFont");
			//			if (true) {
			//				return registry.getBold(Display.getCurrent().getSystemFont().getFontData()[0].getName());
			//			}
			return null;
		}

		public Color getBackground(Object element, int columnIndex) {
			//System.out.println("getBackground");

			switch (columnIndex){
			case 1:
				if (element instanceof TwAttribute){
					if (((TwAttribute)element).getValue()!=null && ((TwAttribute)element).getExpectedValue()!=null){
						if (Float.parseFloat(((TwAttribute)element).getValue().replace(',', '.')) < Float.parseFloat(((TwAttribute)element).getExpectedValue().replace(',', '.')))
							return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
					}
				} else if (element instanceof Evidence){
					if (((Evidence)element).getValue()!=null && ((Evidence)element).getExpectedValue()!=null){
						if (Float.parseFloat(((Evidence)element).getValue().replace(',', '.')) < Float.parseFloat(((Evidence)element).getExpectedValue().replace(',', '.')))
							return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
					}
				}
			}
			return null;

		}

		public Color getForeground(Object element, int columnIndex) {
			//System.out.println("getForeground");
			return null;
		}
	}	
} 