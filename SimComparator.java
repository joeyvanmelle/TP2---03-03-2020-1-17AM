package devoir2;
import java.util.Comparator;

public class SimComparator implements Comparator<Sim> {
	@Override
	public int compare(Sim s1, Sim s2) {  
		if(s1.compareTo(s2) == 0) {
			return 0;
		}
		
		else if(s1.compareTo(s2)>0) {
			return 1; 
		}
			 
		else {
			return -1; 
		}
			 
	}

}