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

    

    public boolean checkjumpTimeAvaliable(Skydiver jumper, Flight flight, LocalDateTime starttime, int preptime, int posttime){  // you still have to check if licence is good enough
        //this will run through all the jumpers jumps and check if this starttime is before (flight start - whatever) or after
        if(starttime.plusMinutes(preptime).isAfter(flight.starttime)){return false;}
        
        //we are here because we know the startime they have put is before the flight start so we are g
        //now we gota check if they have a (flight earlier than + unpack/debreif )it that ends after the flight.starttime
        if(jumper.skydiverJumpsList.isEmpty()){
            return true;
        }
        for(Jump jump : jumper.skydiverJumpsList){
            //we in here cos know they have a jump, we runthrough jumps now, 
            //need to find out what the jump offset is
            //possible offsets are:
                        //needs to repack parashute after +10 : (everyone exept passenger in tandem)
                        //tandem, need +5 before (everyone)
                        //training need +15 after for debreif (after)
            
            int postoffset = 0;
            
            switch(jump.type){
                case "tandem":
                    if(jump instanceof TandemJump){
                        TandemJump a = (TandemJump) jump;
                        if(a.MasterName == jumper){      // we seeing if he the master and have to repack shoot. should be same object so can == instead of .equals
                            postoffset = 10;
                        }
                        
                    } 
                break;

                case "training":
                    postoffset = 25; // 15 debrief plush both repack
                break;

                case "fun":
                    postoffset = 10;
                break;

            }
                        
            if(starttime.isBefore(jump.starttime) && flight.endtime.plusMinutes(posttime).isBefore(jump.starttime)){
                continue; // the flight is clear of this jump
            }
            if(starttime.isAfter(jump.endtime.plusMinutes(postoffset))){
                continue; // the flight is clear of this jump
            }
            //if we here it means the flight isnt clear of one of tre jump runs so return false
            return false;
        }
        //if we get here and out of the for loop there is no interfering flight so can return true
        
        
        
        return true;
    }


    public int jumpsOnDay(Flight flight){
        int jumps_on_day = 0;
        int day = flight.starttime.getDayOfYear();
        for(Jump jump : skydiverJumpsList ){
            if(jump.starttime.getDayOfYear() == day){
                jumps_on_day++;
            }
        }
        return jumps_on_day;
    }

}
