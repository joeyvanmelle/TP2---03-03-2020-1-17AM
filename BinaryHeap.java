package devoir2;


public class BinaryHeap<E extends Comparable> {
    private int capacity; //length of the array.
    private int size;     //number of elements actually in the heap.
    private E[] array;    //Array in which the elements are stocked.

    //Constructor
    public BinaryHeap(int capacity) {
        this.capacity = capacity;
        size = 0;
        array = (E[]) new Comparable [capacity];
    }

    private BinaryHeap(E[] array, int size, int capacity) {
        this.array = array;
        this.size = size;
        this.capacity = capacity;
    }

    //Returns a random node of the structure.
	public E randomNode(){
		int random = (int) Math.floor((Math.random() * (size)));
        return array[random];
    }

    //Remove a specific node of the structure.
    public void removeNode(E e) {
        int position = 0; 
        while (position < capacity) {
            if (array[position] == e) break;
            position++;
        }
        //Puts the node to be removed at the end of the heap.
        swap(position, size-1);
        
        //Removes the node.
        array[--size] = null;

        //Adjusts the size of the array.
        if (size <= capacity / 2) {
            E[] newArray = (E[]) new Comparable[capacity * 2];
            for (int i =0; i < size; i++) {
                newArray[i] = array[i];
            }
            capacity /= 2;
            array = newArray;
        }

        //Reposition the node that was displaced.
        sink(position);
    }

    //add the node e to the structure
    public void add(E e) {
        //Adjusts the size of the array.
        if (++size > capacity) {
            E[] newArray = (E[]) new Comparable[capacity * 2];
            for (int i = 0; i < capacity; i++) {
                newArray[i] = array[i];
            }
            capacity *= 2;
            array = newArray;
        }

        //Adds the object at the first available position.
        array[size - 1] = e;

        //Replaces the object at its correct position.
        swim();
    }

    
    //Removes the minimum node.
    public E poll() {
        if (size == 0) return null;
        E removed = array[0]; //The object removed from the structure.

        //The object is swapped with the last object of the structure which is then repositioned.
        swap(0, size - 1);
        array[--size] = null;
        sink(0);

        //Adjusts the size of the array.
        if (size <= capacity / 2) {
            E[] newArray = (E[]) new Comparable[capacity * 2];
            for (int i =0; i < size; i++) {
                newArray[i] = array[i];
            }
            capacity /= 2;
            array = newArray;
        }

		return removed;
    }

    /*
     * Swaps the positions of two objects in the array.
     * The inputs are the positions of the objects.
     */
    private void swap(int posObj1, int posObj2) {
        E container = array[posObj1];
        array[posObj1] = array[posObj2];
        array[posObj2] = container;
    }

    //Repositions a misplaced object placed at the end of the tree.
    private void swim() {
        if (size == 1) return;
        E swimmer = array[size - 1]; //The object to be correctly placed.
        int position = size - 1;     //The current position of the object in the array.
        int parentPosition = (int) Math.floor((position - 1)/2); //The current position of its parent in the array.

        //Applies the swap as long as the swimmer is misplaced.
        while (swimmer.compareTo(array[parentPosition]) < 0) {
            swap(position, parentPosition);
            position = parentPosition;
            parentPosition = (int) Math.floor((position - 1)/2);
        }
    }

    //Repositions a misplaced object placed at the top of the tree.
    private void sink( int position ) { //position is the current position of the node in the array.
        E diver = array[position]; //the object to be correctly placed.
        int childPosition;  //The current position of its younger child in the array.

        //Checks if the children exist and assign childPosition if only one does. If none, stop.
        if (2 * position + 1>= size) return;
        else if (2 * position + 2 >= size ) childPosition = 2 * position + 1;

        //Checks which child as the lowest ranking and chooses this child for the swap.
        else if (array[2 * position + 1].compareTo(array[2 * position + 2])<0) childPosition = 2 * position + 1;
        else childPosition = 2 * position + 2;
        
        //Applies the swap as long as the diver is misplaced.
        while (diver.compareTo(array[childPosition]) > 0) {
            swap(position, childPosition);
            position = childPosition;

            //Checks if the children exist and assign childPosition if only one does. If none, stop.
            if (2 * position + 1>= size) break;
            else if (2 * position + 2 >= size ) childPosition = 2 * position + 1;

            //Checks which child as the lowest ranking and chooses this child for the swap.
            else if (array[2 * position + 1].compareTo(array[2 * position + 2])<0) childPosition = 2 * position + 1;
            else childPosition = 2 * position + 2;
        }
    }

    //Returns a clone of the BinaryHeap.
    public BinaryHeap<E> copy(){
        E[] copiedArray = array.clone();
        BinaryHeap<E> copy = new BinaryHeap<E>(copiedArray, size, capacity);
        return copy;
    }

    //Getters.
    public int size(){
        return size;
    }

    public E[] array() {
        return array;
    }

    public int capacity() {
        return capacity;
    }

    public E getNodeAtIndex(int i) {
    	return array[i];
    }

}
