package unsw.skydiving;
import java.time.LocalDateTime;


public class TrainingJump extends Jump{
    public String InstructorName; // this will be changed the skydiver object
    public String traineeName; // this will be changed the skydiver object

    public TrainingJump(String type, LocalDateTime starttime, String desination , String MasterName, String traineeName){
    this.type = type;
    this.starttime = starttime; 
    this.desination = desination;
    this.InstructorName = MasterName;
    this.traineeName =  traineeName;
    }
    
}
