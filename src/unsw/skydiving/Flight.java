package unsw.skydiving;
import java.util.ArrayList;
import java.time.LocalDateTime;


public class Flight {

    //note should probs make all these variables private but cbs
    public String id;
    public String dropzone;  // yo might have to make dropzones its own object ? probs dont have to thou
    public LocalDateTime starttime;
    public LocalDateTime endtime;
    public int maxload;
    public ArrayList<Jump> jumpsInFlight;    // also gona have to reorder this at the end
    public int peopleOnboard;

    //assuming we always get good info on flights
    public Flight(String id, int maxload, LocalDateTime starttime, LocalDateTime endtime, String dropzone){
        this.id = id;
        this.maxload = maxload;
        this.starttime = starttime;
        this.endtime = endtime;
        this.dropzone = dropzone;
        this.jumpsInFlight = new ArrayList<Jump>();
        peopleOnboard = 0; // get gota change this when people cancell
    }

    public void setEndtime(LocalDateTime endtime){
        this.endtime = endtime;
    }

    public void addJump(Jump jump){      
        jumpsInFlight.add(jump);
        peopleOnboard = peopleOnboard + 2;
    }

    public void addJump(Jump jumpToAdd, int size){      // cos we have no idea how many people gona jump, gota add them individually, not in constructor
        //we can add the flight in the order it should be in i think
        
        
        jumpsInFlight.add(jumpToAdd);
        peopleOnboard = peopleOnboard + size;
    }

    public ArrayList <Jump> orderJumpTime(){
        ArrayList <Jump> orderedFlightRunArray = new ArrayList<Jump>();
        //we are just making a copy that we discard each time, doesnt actaully change the jumps in the Flights array
        for(Jump jump : jumpsInFlight){
            if(orderedFlightRunArray.size() == 0){
                orderedFlightRunArray.add(jump);
            }else{
                for(int x=0 ; x < orderedFlightRunArray.size() ; x++){
                    if(jump.jumpWeighting > orderedFlightRunArray.get(x).jumpWeighting){
                        orderedFlightRunArray.add(x,jump);
                        break;
                    }
                }
                if(!orderedFlightRunArray.contains(jump)){
                    orderedFlightRunArray.add(jump);
                }
            }      
        }
        
            
        
        
        //if its not added in the array
        //if(!jumpsInFlight.contains(jumpToAdd)){jumpsInFlight.add(jumpToAdd);}


        return orderedFlightRunArray; 
    }


	
}