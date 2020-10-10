package unsw.skydiving;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList; 

import java.io.File;

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

    //aight so a single booking system gets made so in here we can hold an array of all the flights and shit

    ArrayList<Flight> flights = new ArrayList<Flight>(); // care this starts at 0 so flight1 == 0
    ArrayList<Skydiver> skydivers = new ArrayList<Skydiver>();
    ArrayList<Jump> jumps = new ArrayList<Jump>();

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
            Flight flightX = new Flight(id,maxload,starttime,endtime,dropzone);
            flights.add(flightX); 
           // System.out.println("size of the flight array is: " + flights.size());

            break;
        case "skydiver":   
            String name = json.getString("skydiver");
            String licence = json.getString("licence");
            String home_dropzone = "this silly billy doesnt have a home dropzone";
            if(json.has("dropzone")){home_dropzone = json.getString("dropzone");}              
            
            Skydiver skydiverX = new Skydiver(name,licence,home_dropzone);
            
            skydivers.add(skydiverX);
            //System.out.println("the amount of skydivers is: " + skydivers.size());

            //System.out.println("skydiver: " + skydivers.get(0).name + " has a level " + skydivers.get(0).level + " Licence " );
            

            break;
        case "request":   // atm im just making jumps, doesnt actually check if u can do it uwu
            String type = json.getString("type");
            LocalDateTime starttimeJ = LocalDateTime.parse(json.getString("starttime"));
            String dropzoneJ = "No dropzone yet";  
            //note endtime is created at the end only if we can make it and link it too a flight
            switch(type){
                case "tandem":
                    String passengerName = json.getString("passenger");
                    String MasterName = "Master not found yet";
                    // Here is where we would make all the checks and find masters and shit
                    //we only use these names to find the skydiver ovject it relates to, then we add the skydiver object, not the string
                    //do like a if good to go make it, 
                    //we just making it now for fun
                    TandemJump jumpX = new TandemJump(type, starttimeJ, dropzoneJ, MasterName, passengerName);
                    //set endtime we can also make the endtime and dropzone first then add them into the constructor
                    //set dropzone
                    jumps.add(jumpX);
                break;
                case "training":
                    String traineeName = json.getString("trainee");
                    String InstructorName = "Master not found yet";
                    //do all our checks and shit
                    //if all good
                    TrainingJump jumpY = new TrainingJump(type, starttimeJ, dropzoneJ , InstructorName, traineeName);
                    //set endtime
                    //set dropzone

                    jumps.add(jumpY);
                break;

                case "fun":
                    JSONArray jArray =  json.getJSONArray("skydivers"); 
                    ArrayList<String> listdata = new ArrayList<String>();     
                    for (int i=0;i<jArray.length();i++){        
                        listdata.add(jArray.getString(i));
                    }
                    //do all checks and shit
                    //set endtime
                    //set dropzone
                    FunJump jumpZ = new FunJump(type, starttimeJ,dropzoneJ);
                    
                    for(String jumperW : listdata){
                        jumpZ.addJumper(jumperW);
                    }
                break;
            }
            

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
