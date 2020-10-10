package unsw.skydiving;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList; 

import org.json.JSONArray;
import org.json.JSONObject;

import jdk.jfr.FlightRecorderListener;

/**
 * Skydive Booking System for COMP2511.
 *
 * A basic prototype to serve as the "back-end" of a skydive booking system. Input
 * and output is in JSON format.
 *
 * @author Matthew Perry
 *
 */


public class SkydiveBookingSystem {

    /**
     * Constructs a skydive booking system. Initially, the system contains no flights, skydivers, jumps or dropzones
     */

    public SkydiveBookingSystem() {
        // TODO Auto-generated constructor stub
    }

    //aight so a single booking system gets made so in here we can hold an array of all the flights

    ArrayList<flight> Flights = new ArrayList<flight>();


    private void processCommand(JSONObject json) {

        

        switch (json.getString("command")) {

        case "flight":
            String id = json.getString("id");          
            int maxload = json.getInt("maxload");
            LocalDateTime starttime = LocalDateTime.parse(json.getString("starttime"));
            LocalDateTime endtime = LocalDateTime.parse(json.getString("endtime"));
            String dropzone = json.getString("dropzone");          

            // ok sick so we just gona have an array of flights, use that for(flight a: Flights to scan through), 
            //they all have a different name inside aswell 
            flight flightX = new flight(id,maxload,starttime,endtime,dropzone);
            Flights.add(flightX); 
            System.out.println("size of the flight array is: " + Flights.size());

            break;

        // TODO Implement other commands
        
        }
    }

 
    //are we allowed to move main out of this class ?? does that fuck things up ??
    public static void main(String[] args) {
        SkydiveBookingSystem system = new SkydiveBookingSystem();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) { // pre sure this just reads line by line and sends each line individually to system.processCommand
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command);
            }
        }
       // for(flight x : Flights){

        
        sc.close();
    }

}
