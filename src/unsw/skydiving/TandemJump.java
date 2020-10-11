package unsw.skydiving;
import java.time.LocalDateTime;


public class TandemJump extends Jump{
    public Skydiver MasterName; // this will be changed the skydiver object
    public Skydiver passengerName; // this will be changed the skydiver object

    public TandemJump(String type, LocalDateTime starttime, LocalDateTime endtime,String desination , Skydiver MasterName, Skydiver passengerName){
    this.type = type;
    this.starttime = starttime; 
    this.endtime = endtime;
    this.desination = desination;
    this.MasterName = MasterName;
    this.passengerName =  passengerName;
    updateSkydiverAvaliability(MasterName, passengerName);
    }

    public void updateSkydiverAvaliability(Skydiver Master, Skydiver passenger){
        Master.earliestJumptime = endtime.plusMinutes(10); // he gota repack, the passenger doesnt
        passenger.earliestJumptime = endtime;
    }
    
}
