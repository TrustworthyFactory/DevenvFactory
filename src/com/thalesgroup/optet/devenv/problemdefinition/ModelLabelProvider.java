package com.thalesgroup.optet.devenv.problemdefinition;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.thalesgroup.optet.devenv.datamodel.Threat;

class ModelLabelProvider extends LabelProvider implements
            ITableLabelProvider {

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            // no image to show
            return null;
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
        	System.out.println("zzzzzzzzzzzzzzzz getColumnText " + columnIndex);
            // each element comes from the ContentProvider.getElements(Object)
            if (!(element instanceof Threat)) {
            	System.out.println("return null");
                return "";
            }
            Threat model = (Threat) element;
            switch (columnIndex) {
            case 0:
                return model.getId();
            case 1:
                return model.getCategory();
            default:
                break;
            }
            System.out.println("return null1");
            return "";
        }
}