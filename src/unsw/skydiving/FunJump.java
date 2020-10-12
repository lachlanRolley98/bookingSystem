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
    }

    public void addJumper(Skydiver jumper){      // cos we have no idea how many people gona jump, gota add them individually, not in constructor
        jumpers.add(jumper);
        jumper.earliestJumptime = endtime.plusMinutes(10);
    }

    

    
    
}
