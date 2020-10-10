package unsw.skydiving;

public class Skydiver {
    //im thinking have classes that extend skdiver for there licence type but cbs aye

    public String name;
    public String licence;
    public int level;        // i feel it would be easier to have licences as levels so can scan through and go >2
    public String dropzone;  // some wont have one of these

    public Skydiver(String name, String licence, String dropzone){
        this.name = name;
        this.licence = licence;
        this.dropzone = dropzone;
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

    public Skydiver(String name, String licence){
        this.name = name;
        this.licence = licence;
        this.dropzone = "Looser skydiver doesnt have dropzone";
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

}
