
package com.Salesgen.model;

import java.util.ArrayList;


public class InvoiceSIG {
    private int num;
    private String date;
    private String customer;
    private ArrayList<LineSIG> lines;
    

    public InvoiceSIG() {
    }

    public InvoiceSIG(int num, String date, String customer) {
        this.num = num;
        this.date = date;
        this.customer = customer;
    }
        public double getInvoicegenTotal(){
            double total = 0.0;
            for (LineSIG line : getLines()){
                total += line.getLineTotal();
            }
            return total;
        }
        
    public ArrayList<LineSIG> getLines() {
        if (lines == null){
            lines = new ArrayList<>();
        }
        return lines;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Invoicegen{" + "num=" + num + ", date=" + date + ", customer=" + customer + '}';
    }
    
    public String getAsVSC(){
        return num + "," + date + "," + customer;
    }


}
