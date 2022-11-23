
package com.Salesgen.controller;

import com.Salesgen.model.InvoiceSIG;
import com.Salesgen.model.InvoicesgenTableModel;
import com.Salesgen.model.LineSIG;
import com.Salesgen.model.LinesTableModelSIG;
import com.Salesgen.view.InvoiceDialogSG;
import com.Salesgen.view.InvoiceSGFrame;
import com.Salesgen.view.LineDialogSG;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ControllerSIG implements ActionListener,ListSelectionListener {
        
        private InvoiceSGFrame frame;
        private InvoiceDialogSG invoiceDialog;
        private LineDialogSG lineDialog;
        public ControllerSIG(InvoiceSGFrame frame){
         this.frame = frame;
        }
       
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("Action: " + actionCommand);
        switch (actionCommand) {
            case "Load File":
                loadFile();
               break;
               case "Save File":
                   saveFile();
               break;
               case "Create New Invoice":
                   createNewInvoice();
               break;
               case "Delete Invoice":
                   deleteInvoice();
               break;
               case "Create New Item":
                   createNewItem();
               break;
               case "Delete Item":
                   deleteItem();
               break;
               case "createInvoiceCancel":
               createInvoicegenCancel();
               break;
               case "createInvoiceOK":
               createInvoiceOK();
                   break;
               case "createLineOK":
                   createLineOK();
                   break;
               case "createLineCancel":
                   createLineCancel();
                   break;
                   
               
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = frame.getInvoiceTable().getSelectedRow();
        if (selectedIndex != -1){
        System.out.println("You have selected row: " + selectedIndex);
        InvoiceSIG currentInvoicegen = frame.getInvoices().get(selectedIndex);
        frame.getInvoiceNumLabel().setText(""+currentInvoicegen.getNum());
        frame.getInvoiceDateLabel().setText(currentInvoicegen.getDate());
        frame.getCustomerNameLabel().setText(currentInvoicegen.getCustomer());
        frame.getInvoiceTotalLabel().setText(""+currentInvoicegen.getInvoicegenTotal());
        LinesTableModelSIG linesTableModelgen = new LinesTableModelSIG(currentInvoicegen.getLines());
        frame.getLineTable().setModel(linesTableModelgen);
        linesTableModelgen.fireTableDataChanged();
        }
    }
    
    private void loadFile() {
        JFileChooser fc = new JFileChooser();
        try {
        int result = fc.showOpenDialog(frame);
        if (result ==JFileChooser.APPROVE_OPTION){
            File headerFile = fc.getSelectedFile();
            Path headerPath = Paths.get(headerFile.getAbsolutePath());
            List<String> headerLines = Files.readAllLines(headerPath);
            System.out.println("Invoices have been read");
            
            ArrayList<InvoiceSIG> invoicesArray = new ArrayList<>();
            for (String headerLine : headerLines){
               try { 
            String[] headerParts = headerLine.split(",");
            int invoiceNum = Integer.parseInt(headerParts[0]);
            String invoiceDate = headerParts[1];
            String customerName = headerParts[2];
            InvoiceSIG invoice = new  InvoiceSIG(invoiceNum, invoiceDate, customerName);
            invoicesArray.add(invoice);
            } catch (Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
            }
            }
            System.out.println("Check point");
            result = fc.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION){
                File lineFile = fc.getSelectedFile();
                Path linePath = Paths.get(lineFile.getAbsolutePath());
                List<String> lineLines = Files.readAllLines(linePath);
                 System.out.println("Lines have been read");
                 for (String lineLine : lineLines) {
                     try {
                     String lineParts[] = lineLine.split(",");
                   int invoiceNum = Integer.parseInt(lineParts[0]);
                  String itemName = lineParts[1];
                   double itemPrice = Double.parseDouble(lineParts[2]);
                   int count = Integer.parseInt(lineParts[3]);
                   InvoiceSIG inv = null;
                   for (InvoiceSIG invoice : invoicesArray){
                       if (invoice.getNum() == invoiceNum){
                           inv = invoice;
                           break;
                       }
                   }
                  LineSIG line = new LineSIG(itemName, itemPrice, count, inv);
                  inv.getLines().add(line); 
                  
                 } catch (Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
            }
                 }
                 System.out.println("Check point");
            }
            frame.setInvoices(invoicesArray);
            InvoicesgenTableModel invoicesTableModel = new InvoicesgenTableModel(invoicesArray);
            frame.setInvoicesTableModel(invoicesTableModel);
            frame.getInvoiceTable().setModel(invoicesTableModel);
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
        } catch (IOException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "cannot read file", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
         }

    private void saveFile() {
        ArrayList<InvoiceSIG> invoices = frame.getInvoices();
        String headers = "";
        String lines = "";
        for (InvoiceSIG invoice : invoices){
            String invCSV = invoice.getAsVSC();
            headers += invCSV;
            headers += "\n";
            
            for (LineSIG line : invoice.getLines()){
            String lineCSV = line.getAsVSC();
            lines +=lineCSV;
            lines += "\n";
            
                    }
        }
        
        System.out.println("Check it");
        try {
        JFileChooser fc = new JFileChooser();
        int result = fc.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION){
            File headerFile = fc.getSelectedFile();
            FileWriter hfw = new FileWriter(headerFile);
            hfw.write(headers);
            hfw.flush();
            hfw.close();
            result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION){
                File lineFile = fc.getSelectedFile();
           FileWriter lfw = new FileWriter(lineFile);
            lfw.write(lines);
            lfw.flush();
            lfw.close();
            }
        }
      }catch (Exception ex){
                
                }
         }

    private void createNewInvoice() {
        invoiceDialog = new InvoiceDialogSG(frame);
        invoiceDialog.setVisible(true);
         }

    private void deleteInvoice() {
        int selectedRow = frame.getInvoiceTable().getSelectedRow();
        if (selectedRow != -1){
            frame.getInvoices().remove(selectedRow);
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
        }

    private void createNewItem() {
        lineDialog =  new LineDialogSG(frame);
        lineDialog.setVisible(true);
        }

    private void deleteItem() {
        int selectedINV = frame.getInvoiceTable().getSelectedRow();
        int selectedRow = frame.getLineTable().getSelectedRow();
        if (selectedINV != -1 && selectedRow != -1){
            InvoiceSIG invoice = frame.getInvoices().get(selectedINV);
            invoice.getLines().remove(selectedRow);
            LinesTableModelSIG linesTableModelgen = new LinesTableModelSIG(invoice.getLines());
            frame.getLineTable().setModel(linesTableModelgen);
            linesTableModelgen.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
         }

    private void createInvoicegenCancel() {  
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null; 
    }

    private void createInvoiceOK() { 
        String date = invoiceDialog.getInvDateField().getText();
        String customer = invoiceDialog.getCustNameField().getText();
        int num = frame.getNextInvoiceNum();
            try {
         String[] dateParts = date.split("-");
         if (dateParts.length < 3) {
             JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
         } else {     
         int day = Integer.parseInt(dateParts[0]);
         int month = Integer.parseInt(dateParts[1]);
         int year = Integer.parseInt(dateParts[2]);
         if (day > 31 || month > 12){
             JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
         } else {
         InvoiceSIG invoice = new InvoiceSIG(num, date, customer);
        frame.getInvoices().add(invoice);
        frame.getInvoicesTableModel().fireTableDataChanged();
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
           }
         }
            } catch (Exception ex){
                JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
            }
       
    }

    private void createLineOK() { 
        String item = lineDialog.getItemNameField().getText();
        String countStr = lineDialog.getItemCountField().getText();
        String priceStr = lineDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoicegen = frame.getInvoiceTable().getSelectedRow();
        if (selectedInvoicegen != -1){
        InvoiceSIG invoice = frame.getInvoices().get(selectedInvoicegen);
        LineSIG line = new LineSIG (item, price, count, invoice);
        invoice.getLines().add(line);
        LinesTableModelSIG linesTableModelgen = (LinesTableModelSIG) frame.getLineTable().getModel();
        linesTableModelgen.fireTableDataChanged();
        frame.getInvoicesTableModel().fireTableDataChanged();
        
        }
        
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void createLineCancel() {  
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
        
    }

   
    
}
