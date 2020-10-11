package unsw.skydiving;
import java.time.LocalDateTime;


public class TrainingJump extends Jump{
    public Skydiver InstructorName; 
    public Skydiver traineeName; 

    public TrainingJump(String type, LocalDateTime starttime, LocalDateTime endtime , String desination , Skydiver InstructorName, Skydiver traineeName){
    this.type = type;
    this.starttime = starttime; 
    this.endtime = endtime;
    this.desination = desination;
    this.InstructorName = InstructorName;
    this.traineeName =  traineeName;
    updateSkydiverAvaliability();
    }

    public void updateSkydiverAvaliability(){
        InstructorName.earliestJumptime = endtime.plusMinutes(25); // he gota repack, the passenger doesnt
        traineeName.earliestJumptime = endtime.plusMinutes(15);
    }
    
}
