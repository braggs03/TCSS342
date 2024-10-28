/**
 * A data structure in which all elements added to it
 * will be place in there ordered based of compareTo.
 * The underlying data structure is a MyArrayList.
 * @param <T> The generic type.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class MyOrderedList<T extends Comparable<T>> {

    /** How many comparison were made in the test. */
    public int comparisons;

    /** Used to store all elements added. */
    private final MyArrayList<T> myList;

    /** Constructor used to instantiate a new MyOrderedList. */
    public MyOrderedList() {
        super();
        myList = new MyArrayList<>();
        comparisons = 0;
    }

    /**
     * Adds theItem to its correct position.
     * @param theItem The item being added.
     */
    public void add(final T theItem) {
        if (!myList.isEmpty()) {
            myList.insert(theItem, myList.size());
            int i = 1;
            final int size = myList.size();
            while (size != i && myList.get(size - i).
                   compareTo(myList.get(size - (i + 1))) < 0) {
                final T temp = myList.get(size - (i + 1));
                myList.set(size - (i + 1), myList.get(size - i));
                myList.set(size - i, temp);
                i++;
            }
        } else {
            myList.insert(theItem, 0);
        }
    }

    /**
     * Removes theItem if present in the list.
     * @param theItem The item being removed.
     * @return The item if present in the list, null if not.
     */
    public T remove(final T theItem) {
        return myList.remove(myList.indexOf(theItem));
    }

    /**
     * Searched for theItem using binary search.
     * @param theItem The item being searched for.
     * @return A boolean of if the item is in the list.
     */
    public T binarySearch(final T theItem) {
        return binarySearch(theItem, 0, myList.size() - 1);
    }

    private T binarySearch(final T theItem, final int theStart, final int theFinish) {
        T result = null;
        if (theStart <= theFinish) {
            comparisons++;
            final int mid = (theStart + theFinish) / 2;
            final int c = myList.get(mid).compareTo(theItem);
            if (c == 0) {
                result = myList.get(mid);
            } else if (c > 0) {
                result = binarySearch(theItem, theStart, mid - 1);
            } else {
                result = binarySearch(theItem, mid + 1, theFinish);
            }
        }
        return result;
    }

    /**
     * Returns the item at the given index, null if index is out of bounds.
     * @param theIndex the index of the item to be retrieved.
     * @return The item at the given index, null if index is out of bounds.
     */
    public T get(final int theIndex) {
        return myList.get(theIndex);
    }

    /**
     * Returns the size of the list.
     * @return The size of the list.
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
