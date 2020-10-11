package unsw.skydiving;
import java.time.LocalDateTime;
import java.time.Month;

public class Skydiver {
    //im thinking have classes that extend skdiver for there licence type but cbs aye
    //the skydiver actually doesnt know what flights hes one, just the earliest he can book the next one
    public String name;
    public String licence;
    public int level;        // i feel it would be easier to have licences as levels so can scan through and go >2
    public String dropzone;  // some wont have one of these
    public LocalDateTime earliestJumptime ; //initiialy this will be really early, just check that the jump is after this (+ brief)

    public Skydiver(String name, String licence, String dropzone){
        this.name = name;
        this.licence = licence;
        this.dropzone = dropzone;
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

}
