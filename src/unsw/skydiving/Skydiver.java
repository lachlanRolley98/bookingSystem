package unsw.skydiving;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;


public class Skydiver {
    //im thinking have classes that extend skdiver for there licence type but cbs aye
    //the skydiver actually doesnt know what flights hes one, just the earliest he can book the next one
    public String name;
    public String licence;
    public int level;        // i feel it would be easier to have licences as levels so can scan through and go >2
    public String dropzone;  // some wont have one of these
    public LocalDateTime earliestJumptime ; //initiialy this will be really early, just check that the jump is after this (+ brief)
    public ArrayList<Jump> skydiverJumpsList;

    public Skydiver(String name, String licence, String dropzone){
        this.name = name;
        this.licence = licence;
        this.dropzone = dropzone;
        this.skydiverJumpsList = new ArrayList<Jump>();
        switch(licence){
            case "student":
            level = 1; break;        
            case "licenced-jumper":
            level = 2;   break;       
            case "instructor":
            level = 3; break;           
            case "tandem-master":
            level = 4; break;   
        }
        // my mindset is always set the earliest time for them really early, 
        //when booking, make sure starttime > earliestJumptime
        // when they do book a flight, we change earliestJumpTime to the endtime of the flight + offset
        earliestJumptime = LocalDateTime.of(1000, Month.APRIL,1, 10, 30); 
    }

    public Skydiver(String name, String licence){
        this.name = name;
        this.licence = licence;
        this.dropzone = "Looser skydiver doesnt have dropzone";
        this.skydiverJumpsList = new ArrayList<Jump>();
        switch(licence){
            case "student":
            level = 1; break;        
            case "licenced-jumper":
            level = 2;   break;       
            case "instructor":
            level = 3; break;           
            case "tandem-master":
            level = 4; break;
            
        }

        
    }

    public int checkAvaliable(Skydiver jumper, Flight flight){  // this check is for the funjumpers
        int avaliable = 1;
        //we wana check start time
        if((jumper.earliestJumptime.isAfter(flight.starttime))){return 0;}
        if(jumper.level < 2){return 0;}
        return avaliable;
    }

    public boolean checkjumpTimeAvaliable(Skydiver jumper, Flight flight, LocalDateTime starttime){  // you still have to check if licence is good enough
        //this will run through all the jumpers jumps and check if this starttime is before (flight start - whatever) or after
        if(starttime.isAfter(flight.starttime)){return false;}
        
        //we are here because we know the startime they have put is before the flight start so we are g
        //now we gota check if they have a (flight earlier than + unpack/debreif )it that ends after the flight.starttime
        if(jumper.skydiverJumpsList.isEmpty()){
            return true;
        }
        for(Jump jump : jumper.skydiverJumpsList){
            //we in here cos know they have a jump, we runthrough jumps now, 
        }
        
        
        
        
        
        
        return false;
    }

}
