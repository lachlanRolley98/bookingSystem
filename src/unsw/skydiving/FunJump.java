package unsw.skydiving;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class FunJump extends Jump{
    
    public ArrayList<String> jumpers; // this will be changed the skydiver object

    public FunJump(String type, LocalDateTime starttime, String desination){
    this.type = type;
    this.starttime = starttime; 
    this.desination = desination;
    this.jumpers = new ArrayList<String>();
    }

    public void addJumper(String jumper){      // cos we have no idea how many people gona jump, gota add them individually, not in constructor
        jumpers.add(jumper);
    }
    
}
