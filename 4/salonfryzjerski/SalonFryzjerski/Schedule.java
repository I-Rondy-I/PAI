
import java.util.HashMap;
import java.util.Map;

public class Schedule {
    private String available = "Available date.";

    private static Schedule scheduleInstance;

    private Map<Integer, String> planner = new HashMap<>(9);

    private Schedule() {
        planner.put(10, available);
        planner.put(11, available);
        planner.put(12, available);
        planner.put(13, available);
        planner.put(14, available);
        planner.put(15, available);
        planner.put(16, available);
        planner.put(17, available);
        planner.put(18, available);

    }

    public static Schedule getInstance(){
        if (scheduleInstance == null) {
            synchronized (Schedule.class){
                if (scheduleInstance == null) {
                    scheduleInstance = new Schedule();
                }
            }
        }
        return scheduleInstance;
    }

    String printAvailableDate(){
        StringBuffer availableDates = new StringBuffer("Available date(s):\n");
        for(int i = 0; i<planner.size(); i++){
            if (planner.get(i+10).equals(available)){
                availableDates.append((i + 10) + ":00\n");
            }
        }
        return availableDates.substring(0,availableDates.length()-1);
    }

    // returns true if booking date was ok otherwise returns false
    boolean bookDate(int hour, String name){
        if(planner.get(hour).equals(available)){
            planner.replace(hour, name);
            return true;
        }
//      This date has already been taken
        return false;
    }

    // returns true if canceling date was ok otherwise returns false
    boolean cancelDate(int hour, String name){
        if(planner.get(hour).equals(name)){
            planner.replace(hour,available);
//            Date canceled
            return true;
        }
        return false;
    }

}