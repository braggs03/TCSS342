/**
 * A data structure in which the first most element
 * is the element with the least/minimum priority.
 * The underlying data structure is a MyArrayList.
 * @param <T> The generic type.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class MyPriorityQueue<T extends Comparable<T>> {

    /** Used to store all elements added. */
    protected final MyArrayList<T> myHeap;

    /** Constructor used to instantiate a new MyPriorityQueue. */
    public MyPriorityQueue() {
        super();
        myHeap = new MyArrayList<>();
    }

    /**
     * Inserts the item into its correct position in the queue.
     * @param theItem The item being added to the queue.
     */
    public void insert(final T theItem) {
        myHeap.insert(theItem, myHeap.size());
        bubbleUp();
    }

    /** Removes the element with the least priority. */
    public T removeMin() {
        final T temp = myHeap.get(0);
        if (temp != null) {
            myHeap.set(0, myHeap.get(myHeap.size() - 1));
            myHeap.remove(myHeap.size() - 1);
            sinkDown();
        }
        return temp;
    }

    /**
     * Returns the element with the least priority.
     * @return The element with the least priority.
     */
    public T min() {
        return myHeap.get(0);
    }

    /**
     * Returns the size of the list.
     * @return The size of the list.
     */
    public int size() {
        return myHeap.size();
    }

    /**
     * Returns if the list is empty.
     * @return returns if the list is empty.
     */
    public boolean isEmpty() {
        return myHeap.isEmpty();
    }

    @Override
    public String toString() {
        return myHeap.toString();
    }

    protected void bubbleUp() {
        int currentIndex = myHeap.size() - 1;
        final T current = myHeap.get(currentIndex);
        while (currentIndex != 0 && current.compareTo(myHeap.get(parent(currentIndex))) < 0) {
            final int updatedIndex = parent(currentIndex);
            myHeap.set(currentIndex, myHeap.get(updatedIndex));
            myHeap.set(updatedIndex, current);
            currentIndex = updatedIndex;
        }
    }

    protected void sinkDown() {
        sinkDown(0);
    }

    private void sinkDown(final int theCurrentIndex) {
        final int leftChildIndex = left(theCurrentIndex);
        final int rightChildIndex = right(theCurrentIndex);
        if (leftChildIndex < myHeap.size()) {
            final int minChildIndex = rightChildIndex < myHeap.size()
                                      && myHeap.get(rightChildIndex).
                                      compareTo(myHeap.get(leftChildIndex)) < 0
                                      ? rightChildIndex : leftChildIndex;
            final T current = myHeap.get(theCurrentIndex);
            final T minChild = myHeap.get(minChildIndex);
            if (current.compareTo(minChild) > 0) {
                myHeap.set(minChildIndex, current);
                myHeap.set(theCurrentIndex, minChild);
                sinkDown(minChildIndex);
            }
        }
    }

    private int parent(final int theIndex) {
        return (theIndex - 1) / 2;
    }

    private int left(final int theIndex) {
        return (theIndex * 2) + 1;
    }

    private int right(final int theIndex) {
        return (theIndex * 2) + 2;
    }
}
