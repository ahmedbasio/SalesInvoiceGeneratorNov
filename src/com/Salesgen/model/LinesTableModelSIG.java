
package com.Salesgen.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class LinesTableModelSIG extends AbstractTableModel {
   
    private ArrayList<LineSIG> lines;
    private String[] columns = {"No.", "Item Name", "Item Price", "Count", "Item Total"};

    public LinesTableModelSIG(ArrayList<LineSIG> lines) {
        this.lines = lines;
    }
    
    
    @Override
    public int getRowCount() {
        return lines.size();
    }

    @Override
    public int getColumnCount() {
       return columns.length;
    }
    
     @Override
    public String getColumnName(int Y){
       return columns[Y];
    }
            
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LineSIG line = lines.get(rowIndex);
        
        switch(columnIndex){
            case 0: return line.getInvoice().getNum();
            case 1: return line.getItem();
            case 2: return line.getPrice();
            case 3: return line.getCount();
            case 4: return line.getLineTotal();
            default : return "";
            
        }
    }

}
