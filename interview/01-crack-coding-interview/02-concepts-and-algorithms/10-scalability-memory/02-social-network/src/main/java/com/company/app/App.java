package com.company.app;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.*;

/*
  Q: design the data structures for a very large social network like Facebook or LinkedIn.
     Describe how you would design an algorithm to show the connection, or path, between two people.
 */
public class App 
{
    static class Person{
        private ArrayList<Integer> friends = new ArrayList<Integer>();
        private int personID;
        private String info;
        public String getInfo(){return info;}
        public void setInfo(String info){this.info = info;}
        public int[] getFriends(){
            int[] temp = new int[friends.size()];
            for(int i = 0; i < temp.length; i++) temp[i] = friends.get(i);
            return temp;
        }
        public int getID(){return personID;}
        public void addFriend(int id){friends.add(id);}
        public Person(int id){this.personID = id;}
    }
    static class Machine{
        public HashMap<Integer, Person> persons = new HashMap<Integer, Person>();
        public int machineID;
        public Person getPersonWithID(int personID){ return persons.get(personID); }
        public Machine(int id){this.machineID = id;}
    }
    static class Server{
        HashMap<Integer, Machine> machines = new HashMap<Integer, Machine>();
        HashMap<Integer, Integer> personToMachineMap = new HashMap<Integer, Integer>();
        public Machine getMachineWithId(int machineID){ return machines.get(machineID); }
        public int getMachineIDForUser(int personID){
            Integer machineID = personToMachineMap.get(personID);
            return machineID == null ? -1 : machineID;
        }
        public Person getPersonWithID(int personID){
            Integer machineID = personToMachineMap.get(personID);
            if(machineID == null)return null;
            Machine machine = getMachineWithId(machineID);
            if(machine == null)return null;
            return machine.getPersonWithID(personID);
        }
    }
    static Server server = new Server();  // Server = Graph here
    public static void main( String[] args )
    {
        // create a graph
        /* Graph.nodes: {{1,[1]},{2,[2]},{3,[3]},{4,[4]},{5,{[5]},{6,[6]}}
          [1]-[2]
            \ |
             \|
         [4]-[5]   [3]
          |
         [6]
         */

        Person p1 = new Person(1);
        p1.setInfo("1");
        Person p2 = new Person(2);
        p2.setInfo("2");
        Person p3 = new Person(3);
        p3.setInfo("3");
        Person p4 = new Person(4);
        p4.setInfo("4");
        Person p5 = new Person(5);
        p5.setInfo("5");
        Person p6 = new Person(6);
        p6.setInfo("6");

        p1.addFriend(p2.getID());
        p1.addFriend(p5.getID());

        p2.addFriend(p1.getID());
        p2.addFriend(p5.getID());

        p4.addFriend(p5.getID());
        p4.addFriend(p6.getID());

        p5.addFriend(p1.getID());
        p5.addFriend(p2.getID());
        p5.addFriend(p4.getID());

        p6.addFriend(p4.getID());

        Machine m1 = new Machine(1);
        Machine m2 = new Machine(2);

        m1.persons.put(p1.getID(), p1);
        m1.persons.put(p2.getID(), p2);
        m1.persons.put(p3.getID(), p3);

        m2.persons.put(p4.getID(), p4);
        m2.persons.put(p5.getID(), p5);
        m2.persons.put(p6.getID(), p6);

        server.machines.put(m1.machineID, m1);
        server.machines.put(m2.machineID, m2);

        // store each person to a machine in a hashmap
        for(int key : m1.persons.keySet()){
            server.personToMachineMap.put(key, m1.machineID);
        }
        for(int key : m2.persons.keySet()){
            server.personToMachineMap.put(key, m2.machineID);
        }

        // get machine1 from server, and get person3 from machine1
//        Machine m = server.getMachineWithId(1);
//        System.out.println(m.machineID);
//
//        Person p = m.getPersonWithID(3);
//        System.out.println(p.getInfo());

        System.out.println("\nis p1 reachable to p6?: " + reachable(p1, p6));
    }

    private static boolean reachable(Person p1, Person p6) {
        Person src = p1;
        Person dest = p6;
        // use set because personID is unique
        Set<Integer> hashSet = new HashSet<Integer>();
        Queue<Person> queue = new LinkedList<Person>();
        hashSet.add(src.getID());
        queue.add(src);
        while (!queue.isEmpty()){
            Person tmp = queue.remove();
            for(int personID : tmp.getFriends()){
                Person person = getPerson(personID);
                if(person == dest) return true;
                if(!hashSet.contains(personID)){
                    queue.add(person);
                }
            }
        }
        return false;
    }

    private static Person getPerson(int personID) {
        // 1, For a person ID: get machine_index from it
        int machine_index = server.getMachineIDForUser(personID);
        System.out.format("friendID=%s's machine_index is %s", personID, machine_index);

        // 2, Go to the machine
        Machine machine = server.machines.get(machine_index);
        System.out.format("\nmachineID=%s", machine.machineID);

        // 3, Get person from that machine
        Person person = machine.getPersonWithID(personID);
        System.out.format("\npersonID=%s", person.getID());

        return person;
    }}
