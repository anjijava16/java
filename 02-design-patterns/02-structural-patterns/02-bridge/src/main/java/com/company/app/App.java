package com.company.app;

import java.util.ArrayList;
import java.util.List;

public class App
{
    interface IDataObject<T>{  // Implementor
        void nextRecord();
        void priorRecord();
        void addRecord(T t);
        void deleteRecord(T t);
        T getCurrentRecord();
        void showRecord();
        void showAllRecords();
    }
    static class CustomersData implements IDataObject<String>{  // ConcreteImplementor
        public String City;
        private List<String> customers;
        private int current = 0;
        public CustomersData(){ }
        public CustomersData(String city){
            this.City = city;
            customers = new ArrayList<String>();
            customers.add("Jim Jones");
            customers.add("Samual Jackson");
            customers.add("Allan Good");
            customers.add("Ann Stills");
            customers.add("Lisa Giolani");
        }
        public void nextRecord() { if(current < customers.size()){ current++; } }
        public void priorRecord() { if(current > 0){ current--; } }
        public void addRecord(String customer) { customers.add(customer); }
        public void deleteRecord(String customer) { customers.remove(customer); }
        public String getCurrentRecord() { return customers.get(current); }
        public void showRecord() { System.out.println(customers.get(current)); }
        public void showAllRecords() {
            System.out.println("Customer Group: " + City);
            for(String customer : customers){
                System.out.println(" " + customer);
            }
        }
    }
    static class CustomersBase{  // Abstraction
        public IDataObject<String> DataObject;
        public void next(){DataObject.nextRecord();}
        public void prior(){DataObject.priorRecord();}
        public void add(String name){DataObject.addRecord(name);}
        public void delete(String name){DataObject.deleteRecord(name);}
        public void show(){DataObject.showRecord();}
        public void showAll(){DataObject.showAllRecords();}
    }
    static class Customers extends CustomersBase{  // Refined Abstraction
        public void showAll(){
            System.out.println("-----------------------");
            super.showAll();
            System.out.println("-----------------------");
        }
    }
    public static void main( String[] args )
    {
        Customers customers = new Customers();
        customers.DataObject = new CustomersData("Chicago");
        // Exercise the bridge
        customers.show();
        customers.next();
        customers.show();
        customers.next();
        customers.show();
        customers.add("Tom Lee");
        customers.showAll();
    }
}