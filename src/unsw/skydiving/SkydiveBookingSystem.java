package unsw.skydiving;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList; 

import java.io.File;
import java.io.FileNotFoundException;

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

    ArrayList<Flight> flights ; // care this starts at 0 so flight1 == 0
    ArrayList<Skydiver> skydivers ;
    ArrayList<Jump> jumps ;
    

    public SkydiveBookingSystem() {
        this.flights = new ArrayList<Flight>();
        this.skydivers = new ArrayList<Skydiver>();
        this.jumps = new ArrayList<Jump>();
       
    }

    //aight so a single booking system gets made so in here we can hold an array of all the flights and shit

    

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
                                            
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("flight", flight.id);
                                            jsonObject.put("dropzone", jumpX.desination);
                                            jsonObject.put("status", "success");
                                            System.out.println(jsonObject.toString(2));
                                            
                                                
                                            break;
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                    //System.out.println("\n" );
                    if(status == 1){
                     //   System.out.println("tandem jump was booked" );
                    }else{
                       // System.out.println("couldnt book tandem jump");
                        
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("status", "rejected");
                        System.out.println(jsonObject.toString(2));
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

                                               JSONObject jsonObject = new JSONObject();
                                                jsonObject.put("flight", flight.id);
                                                jsonObject.put("dropzone", jumpY.desination);
                                                jsonObject.put("status", "success");
                                                System.out.println(jsonObject.toString(2));
                                               break;
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                    //System.out.println("\n" );
                    if(status == 1){
                    //    System.out.println("training jump was booked" );
                    }else{
                    //    System.out.println("couldnt book training jump");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("status", "rejected");
                        System.out.println(jsonObject.toString(2));
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
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("flight", flight.id);
                                    jsonObject.put("dropzone", jumpZ.desination);
                                    jsonObject.put("status", "success");
                                    System.out.println(jsonObject.toString(2));
                                    funs_booked = 1;
                                    break;
                                }
                            }
                    }
                    
                    if(funs_booked == 1){
                       // System.out.println("fun jump was booked" );
                    }else{
                     //   System.out.println("couldnt book fun jump");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("status", "rejected");
                        System.out.println(jsonObject.toString(2));
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
        
        case "change":
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
            
            //STEP 2 DELETE THE JUMP 

            
            
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

            String typeC = json.getString("type");
            String J_idC = json.getString("id");
            LocalDateTime starttimeJC = LocalDateTime.parse(json.getString("starttime"));
            String dropzoneJC = "No dropzone yet";  
            //note endtime is created at the end only if we can make it and link it too a flight
            int statusC = 0;
            switch(typeC){
                
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
                        if(diver.name.equals(passengerName) && statusC != 1 ){
                            Tandempassenger = diver;
                            for(Flight flight: flights){                                                              
                                //we gota find a flight whoes starttime is 5mims after starttimeJ(the persons arival time)
                                if((flight.starttime.isAfter(starttimeJC.plusMinutes(5)) || flight.starttime.isEqual(starttimeJC.plusMinutes(5))) && (flight.starttime.getDayOfMonth() == starttimeJC.getDayOfMonth()) && (flight.maxload - flight.peopleOnboard >= 2) && statusC != 1){ // this is like >=
                                    if(Tandempassenger.checkjumpTimeAvaliable(Tandempassenger, flight, starttimeJC, 5, 0)){
                                        //aight we found a flight so gota try find a Jump master that fits
                                        for(Skydiver diver2 : skydivers){                                  
                                            //checkjumpTimeAvaliable(Skydiver jumper, Flight flight, LocalDateTime starttime, int preptime, int posttime)
                                            if(diver2.checkjumpTimeAvaliable(diver2, flight, starttimeJC, 5, 10) && diver2.level == 4 && diver2.dropzone.equals(flight.dropzone) && statusC != 1 && !diver2.equals(Tandempassenger)) {
                                            //we got a match baby book em in
                                            MasterName = diver2;
                                            //when we book the jump, also have to update the peoples info 
                                            TandemJump jumpX = new TandemJump(typeC, starttimeJC, flight.endtime, flight.dropzone, MasterName, Tandempassenger, J_idC);
                                                //System.out.println(MasterName.name + " has a first avaliable time of: " + MasterName.earliestJumptime.toString() );
                                                //System.out.println(Tandempassenger.name + " has a first avaliable time of: " + Tandempassenger.earliestJumptime.toString() );

                                            
                                            jumps.add(jumpX);
                                            flight.addJump(jumpX);
                                            diver2.skydiverJumpsList.add(jumpX);
                                            Tandempassenger.skydiverJumpsList.add(jumpX);
                                            status = 1;

                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("flight", flight.id);
                                            jsonObject.put("dropzone", jumpX.desination);
                                            jsonObject.put("status", "success");
                                            System.out.println(jsonObject.toString(2));

                                            break;
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                   // System.out.println("\n" );
                    if(statusC == 1){
                      //  System.out.println("change was was booked" );
                    }else{
                      //  System.out.println("couldnt change jump");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("status", "rejected");
                        System.out.println(jsonObject.toString(2));
                    }
                    
                    
                break;

                case "training":
                String traineeName = json.getString("trainee");
                Skydiver InstructorName ;
                
                Skydiver trainee;          //put all this method in tandem later
                for(Skydiver diver: skydivers){    // this is really bad style im guessing, fix if can be fucked later                       
                    if(diver.name.equals(traineeName) && statusC != 1 ){
                        trainee = diver;
                        for(Flight flight: flights){                                                              
                            //we gota find a flight whoes starttime is 5mims after starttimeJ(the persons arival time)
                            if((flight.starttime.isAfter(starttimeJC) || flight.starttime.isEqual(starttimeJC)) && (flight.starttime.getDayOfMonth() == starttimeJC.getDayOfMonth()) && (flight.maxload - flight.peopleOnboard >= 2) && statusC != 1){ // this is like >=
                                //aight we found a flight so gota try find a Jump master that fits
                                if(trainee.checkjumpTimeAvaliable(trainee, flight, starttimeJC, 0, 25)){
                                    for(Skydiver diver2 : skydivers){         
                                        //System.out.print("diver2.dropzone is :" + diver2.dropzone +"flight.dropzone is " + flight.dropzone);                         
                                        if(diver2.checkjumpTimeAvaliable(diver2, flight, starttimeJC, 0, 25) && diver2.level >= 3 && diver2.dropzone.equals(flight.dropzone) && statusC != 1 && !diver2.equals(trainee)) {
                                           //we got a match baby book em in
                                           InstructorName = diver2;
                                           //when we book the jump, also have to update the peoples info 
                                           TrainingJump jumpY = new TrainingJump(typeC, starttimeJC, flight.endtime, flight.dropzone , InstructorName, trainee, J_idC);

                                           jumps.add(jumpY);
                                           flight.addJump(jumpY);
                                           diver2.skydiverJumpsList.add(jumpY);
                                           trainee.skydiverJumpsList.add(jumpY);
                                           status = 1;

                                           JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("flight", flight.id);
                                            jsonObject.put("dropzone", jumpY.desination);
                                            jsonObject.put("status", "success");
                                            System.out.println(jsonObject.toString(2));

                                           break;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
              //  System.out.println("\n" );
                if(statusC == 1){
               //     System.out.println("Change was booked" );
                }else{
               //     System.out.println("Couldnt Change jump");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("status", "rejected");
                    System.out.println(jsonObject.toString(2));
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
                        if( (flight.maxload - flight.peopleOnboard >= funJumperArray.size()) && (flight.starttime.isAfter(starttimeJC) || flight.starttime.isEqual(starttimeJC)) && (flight.starttime.getDayOfMonth() == starttimeJC.getDayOfMonth()) && funs_booked == 0){ // gota make sure the flight can take all of them
                            //so we look at this flight which is the right time
                            //run through the people to check if they can make it gona have int that counts how many people can do it. if it equals size, we book
                            for(Skydiver jumper : funJumperArray){
                                if(jumper.checkjumpTimeAvaliable(jumper, flight, starttimeJC, 0, 10)){
                                    all_jumpers_can_jump_check ++;
                                }
                            }
                            if(all_jumpers_can_jump_check == funJumperArray.size()){
                                //this means we can book them all
                                FunJump jumpZ = new FunJump(typeC, starttimeJC, flight.endtime, flight.dropzone, J_idC);
                                for(Skydiver jumper : funJumperArray){
                                    jumpZ.addJumper(jumper);
                                    jumper.skydiverJumpsList.add(jumpZ);
                                }
                                flight.addJump(jumpZ, funJumperArray.size());
                                jumps.add(jumpZ);
                                funs_booked = 1;

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("flight", flight.id);
                                jsonObject.put("dropzone", jumpZ.desination);
                                jsonObject.put("status", "success");
                                System.out.println(jsonObject.toString(2));
                                break;
                            }
                        }
                }
                
                if(funs_booked == 1){
                //    System.out.println("Change was booked" );
                }else{
                //    System.out.println("couldnt change jump");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("status", "rejected");
                    System.out.println(jsonObject.toString(2));
                }    
                
                break;


            
            }//this for the switch statement inside change

            break;//this break is for the change case

        case "jump-run":
            String jumpRun = json.getString("id");
            //JSONArray jsonJumpRunArray = new JSONArray(); // just gona keep making new ones for each jump run
            System.out.print("[");
            for(Flight flight : flights){
                if(flight.id.equals(jumpRun)){
                    //we in here cos we got the flight we are requesting to see, gota make a single JSONObject of all the jumps in it and format it right
                    
                    // STEP 1 - SORT THE THE ORDER OF THE FLIGHTS - Thinking make a new array and put them into it in right order then use that array
                    //ArrayList <Jump> orderedFlightRunArray = new ArrayList<Jump>();
                    //orderedFlightRunArray = flight.orderJumpTime;
                    // STEP 2 - MAKE AN OBJECT FOR EACH JUMP ON THE ARRAY - DIFFERENT JUMP TYPES HAVE DIFFERENT FORMATTING. CAREFULL // this gota be done in function
                    //print it out in the right format once its made, idk how to add fun array to to array so just do individually
                    for(Jump jump: flight.jumpsInFlight){
                        //ok we gota make an object for this jump, each jumptype has a different format
                        if(jump.type.equals("fun")){
                            FunJump thisJump = (FunJump) jump;
                            if(thisJump.jumpers.size() != 0){
                                //System.out.println("the amount of jumpers in this should be: "+ thisJump.jumpers.size());
                                System.out.print("{\"skydivers\""+": [");
                                for(Skydiver diver : thisJump.jumpers){
                                    System.out.println("\"" + diver.name + "\"" + ",");
                                    //jsonObject.put("skydivers", diver.name);
                                }
                                System.out.print("]}");

                            }    
                        }
                        if(jump.type.equals("tandem")){
                            JSONObject jsonObject = new JSONObject();
                            TandemJump thisJump = (TandemJump) jump;
                            jsonObject.put("passenger", thisJump.passengerName.name);
                            jsonObject.put("jump-master", thisJump.MasterName.name);
                            System.out.println(jsonObject.toString(2));
                        }   
                        if(jump.type.equals("training")){
                            JSONObject jsonObject = new JSONObject();
                            TrainingJump thisJump = (TrainingJump) jump;
                            jsonObject.put("trainee", thisJump.traineeName.name);
                            jsonObject.put("instructor", thisJump.InstructorName.name);
                            System.out.println(jsonObject.toString(2));
                        }   
                    }    
                    // STEP 3 - PRINT AN ARRAYLIST INSTEAD OF OBJECT 
                    //printJsonArray(jsonJumpRunArray);
                    System.out.println("]");
                    break; 
                }
            }
            
            break;
        
            


        //this is the bracket for the big switch statment  
        //this is the bracket for the big switch statment    
        }
        /*for(Skydiver diver : skydivers){
            System.out.println("the size of " + diver.name +  " is  " + diver.skydiverJumpsList.size());  
        }*/
    }

    public static void printJsonArray(JSONArray jsonArray){
        
        for(int i = 0; i < jsonArray.length() ; i++){ // normal ArrayList itteration isnt working :/
            System.out.println(jsonArray.getJSONObject(i).toString(2));
        }
        


    }
    //are we allowed to move main out of this class ?? does that fuck things up ??
    public static void main(String[] args) {
        SkydiveBookingSystem system = new SkydiveBookingSystem();

        try{
        
        File json = new File("src/unsw/skydiving/s1.json");
        Scanner sc = new Scanner(json);

        while (sc.hasNextLine()) { // pre sure this just reads line by line and sends each line individually to system.processCommand
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command);
            }
        }
        //here, i think i can print out the json array
        //cant be returning shit dirrectly from here thou cos static, make a function that returns all then call it here.
        //System.out.println("waawawoowwe");
        //printJsonArray(system.jsonArray);
        
        sc.close();
        } catch (FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}
