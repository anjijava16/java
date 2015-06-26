package com.company.app;
import java.lang.reflect.Method;
import java.util.ArrayList;
public class App
{
    static abstract class Visitor{  // abstract Visitor
        public void reflectiveVisit(IElement element){
            Class[] params = new Class[1];
            params[0] = element.getClass();
            try{
                Method method = this.getClass().getDeclaredMethod("visit", params);
                method.invoke(this, new Object[]{element});
            } catch (Exception e) {
//                e.printStackTrace();  // do nothing when unable to find method for the element type
            }
        }
    }
    static class IncomeVisitor extends Visitor{  // Concrete Visitor
        public void visit(Clerk clerk){ doVisit(clerk); }
        public void visit(Director director){ doVisit(director); }
        private void doVisit(IElement element){
            Employee employee = (Employee)element;
            employee.Income *= 1.10;
            System.out.format("%s %s's new income: %s\n", employee.getClass().getSimpleName(), employee.Name, employee.Income);
        }
    }
    static class VacationVisitor extends Visitor{
        public void visit(Director director){ doVisit(director); }
        private void doVisit(IElement element){
            Employee employee = (Employee)element;
            employee.VacationDays += 3;
            System.out.format("%s %s's new vacation days: %s\n", employee.getClass().getSimpleName(), employee.Name, employee.VacationDays);
        }
    }
    interface IElement{  // Element interface
        void accept(Visitor visitor);
    }
    static class Employee implements IElement{  // Concrete Element
        public String Name;
        public double Income;
        public int VacationDays;
        public Employee(String name, double income, int vacationDays){
            this.Name = name;
            this.Income = income;
            this.VacationDays = vacationDays;
        }
        public void accept(Visitor visitor) { visitor.reflectiveVisit(this); }
    }
    static class Employees extends ArrayList<Employee> {  // Object Structure
        public void attach(Employee employee){ add(employee); }
        public void detach(Employee employee){ remove(employee); }
        public void accept(final Visitor visitor){
            this.forEach(e -> e.accept(visitor));
            System.out.println("");
        }
    }
    static class Clerk extends Employee{
        public Clerk() { super("Hank", 25000.0, 14); }
    }
    static class Director extends Employee{
        public Director() { super("Elly", 35000.0, 16); }
    }
    static class President extends Employee{
        public President() { super("Dick", 45000.0, 21); }
    }
    public static void main( String[] args )
    {
        Employees employees = new Employees();
        employees.attach(new Clerk());
        employees.attach(new Director());
        employees.attach(new President());
        // Employees are 'visited'
        employees.accept(new IncomeVisitor());
        employees.accept(new VacationVisitor());
    }
}
