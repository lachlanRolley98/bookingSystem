package unsw.skydiving;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class FunJump extends Jump{
    
    public ArrayList<Skydiver> jumpers; // this will be changed the skydiver object

    public FunJump(String type, LocalDateTime starttime,LocalDateTime endtime, String desination, String id){
    this.type = type;
    this.starttime = starttime; 
    this.endtime = endtime;
    this.desination = desination;
    this.id = id;
    this.jumpers = new ArrayList<Skydiver>();
    this.jumpWeighting = 30;
    }

    public void addJumper(Skydiver jumper){      // cos we have no idea how many people gona jump, gota add them individually, not in constructor
        jumpers.add(jumper);
        jumper.earliestJumptime = endtime.plusMinutes(10);
        this.jumpWeighting++;
    }

    @Override
    public int numofpeople(){
        return jumpers.size();
    }

    public ArrayList <Skydiver> orderAlphabetically(){
        ArrayList <Skydiver> orderedFlightRunArray = new ArrayList<Skydiver>();
        //we are just making a copy that we discard each time, doesnt actaully change the jumps in the Flights array
        for(Skydiver diver : jumpers){
            if(orderedFlightRunArray.size() == 0){
                orderedFlightRunArray.add(diver);
            }else{
                for(int x=0 ; x < orderedFlightRunArray.size() ; x++){
                    if((diver.name.compareTo(orderedFlightRunArray.get(x).name)) < 0){
                        orderedFlightRunArray.add(x,diver);
                        break;
                    }
                }
                if(!orderedFlightRunArray.contains(diver)){
                    orderedFlightRunArray.add(diver);
                }
            }      
        }
        return orderedFlightRunArray;

    }

    
    
}
