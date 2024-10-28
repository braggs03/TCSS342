/**
 * A data structure where when an element is accessed,
 * it is moved to the front of the list. The
 * underlying data structure is a MyArrayList.
 * @param <T> The type of the MTFList.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class MTFList<T extends Comparable<T>> {

    /** Where the elements added are stored. */
    private final MyLinkedList<T> myList;

    /**
     * Constructor used to instantiate a new MTFList.
     */
    public MTFList() {
        super();
        myList = new MyLinkedList<>();
    }

    /**
     * Adds the item to the front of the MTFList.
     * @param theItem The item being added to the MTFList.
     */
    public void add(final T theItem) {
        myList.first();
        myList.addBefore(theItem);
    }

    /**
     * Removes and returns the item if presents, returns null otherwise.
     * @param theItem The item being removed.
     * @return The item is present, null otherwise.
     */
    public T remove(final T theItem) {
        T current = myList.first();
        while (current != null) {
            if (current.compareTo(theItem) == 0) {
                myList.remove();
                break;
            }
            current = myList.next();
        }
        return current;
    }


    /**
     * Finds and returns the element if present, returns null otherwise.
     * When an element is found, it is moved to the front of the list.
     * @param theItem The item to be searched for.
     * @return The item is present, null otherwise.
     */
    public T find(final T theItem) {
        T current = myList.first();
        while (current != null) {
            if (current.compareTo(theItem) == 0) {
                myList.remove();
                myList.first();
                myList.addBefore(theItem);
                break;
            }
            current = myList.next();
        }
        return current;
    }

    /**
     * Returns the size of the MTFList.
     * @return The size of the MTFList.
     */
    public int size() {
        return myList.size();
    }

    /**
     * Returns if the list is empty.
     * @return returns if the list is empty.
     */
    public boolean isEmpty() {
        return myList.isEmpty();
    }

    @Override
    public String toString() {
        return myList.toString();
    }
}
