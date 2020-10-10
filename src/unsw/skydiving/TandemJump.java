package unsw.skydiving;
import java.time.LocalDateTime;


public class TandemJump extends Jump{
    public String MasterName; // this will be changed the skydiver object
    public String passengerName; // this will be changed the skydiver object

    public TandemJump(String type, LocalDateTime starttime, String desination , String MasterName, String passengerName){
    this.type = type;
    this.starttime = starttime; 
    this.desination = desination;
    this.MasterName = MasterName;
    this.passengerName =  passengerName;
    }
    
}
