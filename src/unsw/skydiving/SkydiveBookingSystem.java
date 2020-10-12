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
            String J_id = json.getString("id");
            LocalDateTime starttimeJ = LocalDateTime.parse(json.getString("starttime"));
            String dropzoneJ = "No dropzone yet";  
            //note endtime is created at the end only if we can make it and link it too a flight
            int status = 0;
            switch(type){
                
                case "tandem":
                    String passengerName = json.getString("passenger");
                    Skydiver MasterName ;
                    // Here is where we would make all the checks and find masters and shit
                    //we only use these names to find the skydiver ovject it relates to, then we add the skydiver object, not the string
                    //do like a if good to go make it, 
                    //we just making it now for fun
                    //check if person can jump
                    
                    Skydiver Tandempassenger;          //put all this method in tandem later
                    for(Skydiver diver: skydivers){    // this is really bad style im guessing, fix if can be fucked later                       
                        if(diver.name.equals(passengerName) && status != 1 ){
                            Tandempassenger = diver;
                            for(Flight flight: flights){                                                              
                                //we gota find a flight whoes starttime is 5mims after starttimeJ(the persons arival time)
                                if((flight.starttime.isAfter(starttimeJ.plusMinutes(5)) || flight.starttime.isEqual(starttimeJ.plusMinutes(5))) && (flight.starttime.getDayOfMonth() == starttimeJ.getDayOfMonth()) && (flight.maxload - flight.peopleOnboard >= 2) && status != 1){ // this is like >=
                                    if(Tandempassenger.checkjumpTimeAvaliable(Tandempassenger, flight, starttimeJ, 5, 0)){
                                        //aight we found a flight so gota try find a Jump master that fits
                                        for(Skydiver diver2 : skydivers){                                  
                                            //checkjumpTimeAvaliable(Skydiver jumper, Flight flight, LocalDateTime starttime, int preptime, int posttime)
                                            if(diver2.checkjumpTimeAvaliable(diver2, flight, starttimeJ, 5, 10) && diver2.level == 4 && diver2.dropzone.equals(flight.dropzone) && status != 1 && !diver2.equals(Tandempassenger)) {
                                            //we got a match baby book em in
                                            MasterName = diver2;
                                            //when we book the jump, also have to update the peoples info 
                                            TandemJump jumpX = new TandemJump(type, starttimeJ, flight.endtime, flight.dropzone, MasterName, Tandempassenger, J_id);
                                                //System.out.println(MasterName.name + " has a first avaliable time of: " + MasterName.earliestJumptime.toString() );
                                                //System.out.println(Tandempassenger.name + " has a first avaliable time of: " + Tandempassenger.earliestJumptime.toString() );

                                            
                                            jumps.add(jumpX);
                                            flight.addJump(jumpX);
                                            diver2.skydiverJumpsList.add(jumpX);
                                            Tandempassenger.skydiverJumpsList.add(jumpX);
                                            status = 1;
                                            break;
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                    System.out.println("\n" );
                    if(status == 1){
                        System.out.println("tandem jump was booked" );
                    }else{
                        System.out.println("couldnt book tandem jump");
                    }
                    
                    
                break;






                case "training":
                    String traineeName = json.getString("trainee");
                    Skydiver InstructorName ;
                    
                    Skydiver trainee;          //put all this method in tandem later
                    for(Skydiver diver: skydivers){    // this is really bad style im guessing, fix if can be fucked later                       
                        if(diver.name.equals(traineeName) && status != 1 ){
                            trainee = diver;
                            for(Flight flight: flights){                                                              
                                //we gota find a flight whoes starttime is 5mims after starttimeJ(the persons arival time)
                                if((flight.starttime.isAfter(starttimeJ) || flight.starttime.isEqual(starttimeJ)) && (flight.starttime.getDayOfMonth() == starttimeJ.getDayOfMonth()) && (flight.maxload - flight.peopleOnboard >= 2) && status != 1){ // this is like >=
                                    //aight we found a flight so gota try find a Jump master that fits
                                    if(trainee.checkjumpTimeAvaliable(trainee, flight, starttimeJ, 0, 25)){
                                        for(Skydiver diver2 : skydivers){         
                                            //System.out.print("diver2.dropzone is :" + diver2.dropzone +"flight.dropzone is " + flight.dropzone);                         
                                            if(diver2.checkjumpTimeAvaliable(diver2, flight, starttimeJ, 0, 25) && diver2.level >= 3 && diver2.dropzone.equals(flight.dropzone) && status != 1 && !diver2.equals(trainee)) {
                                               //we got a match baby book em in
                                               InstructorName = diver2;
                                               //when we book the jump, also have to update the peoples info 
                                               TrainingJump jumpY = new TrainingJump(type, starttimeJ, flight.endtime, flight.dropzone , InstructorName, trainee, J_id);
    
                                               jumps.add(jumpY);
                                               flight.addJump(jumpY);
                                               diver2.skydiverJumpsList.add(jumpY);
                                               trainee.skydiverJumpsList.add(jumpY);
                                               status = 1;
                                               break;
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                    System.out.println("\n" );
                    if(status == 1){
                        System.out.println("training jump was booked" );
                    }else{
                        System.out.println("couldnt book training jump");
                    }                                                                                                                              
                break;




                case "fun":
                    JSONArray jArray =  json.getJSONArray("skydivers"); 
                    ArrayList<String> listdata = new ArrayList<String>();     
                    for (int i=0;i<jArray.length();i++){        
                        listdata.add(jArray.getString(i));
                    }
                    ArrayList<Skydiver> funJumperArray = new ArrayList<Skydiver>();     
                    for(String diverName: listdata ){                         
                        for(Skydiver diverObject : skydivers){
                            if(diverName.equals(diverObject.name)){
                                funJumperArray.add(diverObject);
                                //System.out.println(diverObject.name + " has a first avaliable time of: " + diverObject.earliestJumptime.toString() );
                            } 
                        }       
                    }
                    //now we have a ArrayList of the Skydivers called "funJumperArray"
                    //now i think we would run through each one in thingi and check if they can jump, iff all can, make the jump and add all individuall 
                    int funs_booked = 0;
                    for(Flight flight : flights){
                            int all_jumpers_can_jump_check = 0;
                            if( (flight.maxload - flight.peopleOnboard >= funJumperArray.size()) && (flight.starttime.isAfter(starttimeJ) || flight.starttime.isEqual(starttimeJ)) && (flight.starttime.getDayOfMonth() == starttimeJ.getDayOfMonth()) && funs_booked == 0){ // gota make sure the flight can take all of them
                                //so we look at this flight which is the right time
                                //run through the people to check if they can make it gona have int that counts how many people can do it. if it equals size, we book
                                for(Skydiver jumper : funJumperArray){
                                    if(jumper.checkjumpTimeAvaliable(jumper, flight, starttimeJ, 0, 10)){
                                        all_jumpers_can_jump_check ++;
                                    }
                                }
                                if(all_jumpers_can_jump_check == funJumperArray.size()){
                                    //this means we can book them all
                                    FunJump jumpZ = new FunJump(type, starttimeJ, flight.endtime, flight.dropzone, J_id);
                                    for(Skydiver jumper : funJumperArray){
                                        jumpZ.addJumper(jumper);
                                        jumper.skydiverJumpsList.add(jumpZ);
                                    }
                                    flight.addJump(jumpZ, funJumperArray.size());
                                    jumps.add(jumpZ);
                                    funs_booked = 1;
                                }
                            }
                    }
                    
                    if(funs_booked == 1){
                        System.out.println("fun jump was booked" );
                    }else{
                        System.out.println("couldnt book fun jump");
                    }    
                    
                    
                    
                break;
            }
            break;  
        case "cancel":
            //ok so we will have a request to cancel, should first check is valid flight probs then, cbs sheel be right
            // all we get is an id, then we gota find the jump that corresponds too, that jump has people,
            // the places we gota remove the jump from is the (flightArray above), (JumpsArray above) up there, (Skydivers_personal jumps array (This will auto change there avaliabity)) 
            String Jump_id = json.getString("id");
            //System.out.println("the size of jumps arrayList is: " + jumps.size());
            
            for(Jump jump : jumps) {
                if (jump.id.equals(Jump_id)){// ladies and gentleman, we got him
                    jumps.remove(jump);
                    break;
                } 
            }
            for(Flight flight : flights){
                for(Jump jump : flight.jumpsInFlight){
                    if (jump.id.equals(Jump_id)){
                        flight.jumpsInFlight.remove(jump);
                        break;
                    }    
                }
            }
            for(Skydiver skydiver : skydivers){       // if we remove a jump from a skydiver, the way our check avaliabilty works uses current jumps so dont have to change anything else
                for(Jump jump : skydiver.skydiverJumpsList){
                    if (jump.id.equals(Jump_id)){
                        skydiver.skydiverJumpsList.remove(jump);
                        break;
                    }
                }
            }
            //start removing it from everything

            //System.out.println("the size of jumps arrayList is: " + jumps.size());
            
            
            //aight so got the jump, can legit just remove it from the arrayLists
            break;
        
        case "changeNOO":
            //change is basically like, if we delete it then take this as a request, can we do it ?, if not be like nah,
            // ima just make a copy of the jump, delete it, try and add new one
            // if can add, yew, if not, readd old one (no reason why cant add old one back right ?)
            // Time to hardcode the shit out of this yew (put it all in respective classes if have time)

            

            //STEP 1 COPY JUMP 
            String Jump_id2 = json.getString("id");
            Jump jumpCopy;
            int c = 0;
            while(c < jumps.size()){
                if (jumps.get(c).id.equals(Jump_id2)){break;} 
                c++; 
            }
            jumpCopy = jumps.get(c);
            
            //STEP 2 DELETE THE JUMP IT

            
            
            for(Jump jump : jumps) {
                if (jump.id.equals(Jump_id2)){// ladies and gentleman, we got him  
                    jumps.remove(jump);
                    break;
                } 
            }
            for(Flight flight : flights){
                for(Jump jump : flight.jumpsInFlight){
                    if (jump.id.equals(Jump_id2)){
                        flight.jumpsInFlight.remove(jump);
                        break;
                    }    
                }
            }
            for(Skydiver skydiver : skydivers){       // if we remove a jump from a skydiver, the way our check avaliabilty works uses current jumps so dont have to change anything else
                for(Jump jump : skydiver.skydiverJumpsList){
                    if (jump.id.equals(Jump_id2)){
                        skydiver.skydiverJumpsList.remove(jump);
                        break;
                    }
                }
            }

            // STEP3, TRY AND MAKE NEW JUMP !
































            break;
        //this is the bracket for the big switch statment  
        //this is the bracket for the big switch statment    
        }
        /*for(Skydiver diver : skydivers){
            System.out.println("the size of " + diver.name +  " is  " + diver.skydiverJumpsList.size());  
        }*/
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
