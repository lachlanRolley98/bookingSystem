package unsw.skydiving;
import java.time.LocalDateTime;

public class Jump {
    //this gona be my superclass for jumps so i can chuck all the differnt types and shit in one arrayList
    public String type;
    public String id;
    public LocalDateTime starttime; 
    public LocalDateTime endtime;
    public String desination;
    public int jumpWeighting;


    //gota make contructor
    //this will never be called, will always be making the subclasses
    public Jump(){}

    public int numofpeople(){
        return 0;
    }
    
    
    
    
}
