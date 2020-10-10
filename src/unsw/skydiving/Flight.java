package unsw.skydiving;

import java.time.LocalDateTime;


public class Flight {

    //note should probs make all these variables private but cbs
    public String id;
    public String dropzone;  // yo might have to make dropzones its own object ?
    public LocalDateTime starttime;
    public LocalDateTime endtime;
    public int maxload;
    //Note we are also going to have a array of jumps in this 
    
    //assuming we always get good info on flights
    public Flight(String id, int maxload, LocalDateTime starttime, LocalDateTime endtime, String dropzone){
        this.id = id;
        this.maxload = maxload;
        this.starttime = starttime;
        this.endtime = endtime;
        this.dropzone = dropzone;
    }

    public void setEndtime(LocalDateTime endtime){
        this.endtime = endtime;
    }
	
	
}