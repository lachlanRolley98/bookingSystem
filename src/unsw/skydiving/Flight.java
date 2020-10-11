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
        peopleOnboard = 0;
    }

    public void setEndtime(LocalDateTime endtime){
        this.endtime = endtime;
    }

    public void addJump(Jump jump){      // cos we have no idea how many people gona jump, gota add them individually, not in constructor
        jumpsInFlight.add(jump);
        peopleOnboard = peopleOnboard + 2;
    }
	
	
}