/**
 * A data structure used as a dynamically sized array.
 * @param <T> The generic type of this data structure.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class MyArrayList<T extends Comparable<T>> {

    /** The default size the array will be instantiated to. */
    private static final int DEFAULT_SIZE = 16;

    /** The array used to store the elements. */
    private T[] myArray;

    /** The max capacity for the list. */
    private int myCapacity;

    /** How many elements are stored in the list. */
    private int mySize;

    /** Constructor used to instantiate a new MyArrayList. */
    @SuppressWarnings("unchecked")
    public MyArrayList() {
        super();
        myArray = (T[]) new Comparable[DEFAULT_SIZE];
        myCapacity = DEFAULT_SIZE;
        mySize = 0;
    }

    /**
     * Inserts an item at the given index.
     * @param theItem  the item being inserted to the list.
     * @param theIndex the index the item is being inserted at.
     */
    public void insert(final T theItem, final int theIndex) {
        if (theIndex < mySize + 1 && theIndex > -1) {
            if (mySize == myCapacity) {
                resize();
            }
            for (int i = mySize; i > theIndex; i--) {
                myArray[i] = myArray[i - 1];
            }
            myArray[theIndex] = theItem;
            mySize++;
        }
    }

    /** Doubles the size of the array. */
    @SuppressWarnings("unchecked")
    private void resize() {
        myCapacity <<= 2;
        final T[] temp = (T[]) new Comparable[myCapacity];
        System.arraycopy(myArray, 0, temp, 0, mySize);
        myArray = temp;
    }

    /** Sorts the MyArrayList using MergeSort. */
    public void sort() {
        mergeSort(this, 0, mySize - 1);
    }

    private void mergeSort(final MyArrayList<T> theArray,
                           final int theStart,
                           final int theEnd) {
        if (theStart < theEnd) {
            final int mid = ((theStart + theEnd) / 2) + 1;
            mergeSort(this, theStart, mid - 1);
            mergeSort(this, mid, theEnd);
            merge(this, theStart, mid, theEnd);
        }
    }

    private void merge(final MyArrayList<T> theArray, final int theStart,
                       final int theMiddle, final int theEnd) {
        int i = theStart;
        int j = theMiddle;
        final MyArrayList<T> temp = new MyArrayList<>();
        while (i < theMiddle && j < theEnd + 1) {
            temp.insert(theArray.get(i).compareTo(theArray.get(j)) <= 0
                 ? theArray.get(i++) : theArray.get(j++), temp.size());
        }
        while (i < theMiddle) {
            temp.insert(theArray.get(i++), temp.size());
        }
        while (j < theEnd + 1) {
            temp.insert(theArray.get(j++), temp.size());
        }
        for (int k = 0; k < temp.size(); k++) {
            theArray.set(theStart + k, temp.get(k));
        }
    }

    /**
     * Removes the item at the given index.
     * @param theIndex the index of the item being removed.
     */
    public T remove(final int theIndex) {
        T item = null;
        if (mySize > theIndex && theIndex > -1) {
            item = myArray[theIndex];
            for (int i = theIndex; i < mySize - 1; i++) {
                myArray[i] = myArray[i + 1];
            }
            mySize--;
        }
        return item;
    }

    /**
     * If the given item is in the list.
     * @param theItem element whose presence in this list is to be tested.
     * @return true if this list contains the specified element.
     */
    public boolean contains(final T theItem) {
        boolean result = false;
        for (int i = 0; i < mySize; i++) {
            if (myArray[i] != null && myArray[i].equals(theItem)) {
                result = true;
                break;
            } else if (theItem == null) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Finds index of given item or -1 if there is no such index.
     * @param theItem the item to search for.
     * @return the index of the item.
     */
    public int indexOf(final T theItem) {
        int result = -1;
        for (int i = 0; i < myCapacity; i++) {
            if (myArray[i] != null && myArray[i].equals(theItem)) {
                result = i;
            }
        }
        return result;
    }

    /**
     * Returns the item for the given index or null if there is no such index.
     * @param theIndex the index of the item to be retrieved.
     * @return The at the index or null if no such index.
     */
    public T get(final int theIndex) {
        return mySize > theIndex && theIndex > -1 ? myArray[theIndex] : null;
    }

    /**
     * Updates the elements at the given index or if out bounds does nothing.
     * @param theIndex The index for the given item to be set.
     * @param theItem The item to be set of the given index.
     */
    public void set(final int theIndex, final T theItem) {
        if (mySize > theIndex && theIndex > -1) {
            myArray[theIndex] = theItem;
        }
    }

    /**
     * Returns the size of the list.
     * @return The size of the list.
     */
    public int size() {
        return mySize;
    }

    /**
     * Returns if the list is empty.
     * @return returns if the list is empty.
     */
    public boolean isEmpty() {
        return mySize == 0;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append('[');
        if (isEmpty()) {
            builder.append(']');
        } else {
            for (int i = 0; i < mySize - 1; i++) {
                builder.append(myArray[i]).append(", ");
            }
            builder.append(myArray[mySize - 1]).append(']');
        }
        return builder.toString();
    }
}
