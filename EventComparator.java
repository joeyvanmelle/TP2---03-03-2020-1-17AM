package devoir2;
import java.util.Comparator;

public class EventComparator implements Comparator<Event> {
	@Override
	public int compare(Event e1, Event e2) {  
		if(e1.time==e2.time) {
			return 0;
		}
		
		else if(e1.time>e2.time) {
			return 1; 
		}
			 
		else {
			return -1; 
		}
			 
	}

}