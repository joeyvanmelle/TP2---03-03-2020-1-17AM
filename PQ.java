package devoir2;
import java.util.PriorityQueue;

public class PQ {

	private PriorityQueue<Event> pQueue = new PriorityQueue<Event>(new EventComparator());

	public PQ(EventComparator e) {
		
	}

	public void insert(Event e) {
		pQueue.add(e); 
	}

	public Event deleteMin() {
		return pQueue.poll();
	}

	public boolean isEmpty(){
		return pQueue.isEmpty();
	}

}
