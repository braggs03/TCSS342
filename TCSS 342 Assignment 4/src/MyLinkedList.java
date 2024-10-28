import java.util.Optional;

/**
 * A data structure used as a linked list.
 * @param <T> The generic type of this data structure.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class MyLinkedList<T extends Comparable<T>> {

    /** The starting Node. */
    private Node<T> myStart;

    /** The previous node from the current Node. */
    private Node<T> myPrevious;

    /** The current node on. */
    private Node<T> myCurrent;

    /** How many elements are stored in the list. */
    private int mySize;

    /** Constructor used to instantiate a new MyLinkedList. */
    public MyLinkedList() {
        super();
        myPrevious = null;
        myStart = null;
        myCurrent = null;
        mySize = 0;
    }

    /**
     * Adds the element before the current node.
     * @param theItem the item being added before the current node.
     */
    public void addBefore(final T theItem) {
        final Node<T> temp = new Node<>();
        temp.myItem = theItem;
        if (myCurrent == null) {
            if (myPrevious == null) {
                myStart = temp;
            } else {
                myPrevious.myNext = temp;
            }
            myPrevious = temp;
        } else {
            if (myCurrent == myStart) {
                temp.myNext = myStart;
                myStart = temp;
            } else {
                myPrevious.myNext = temp;
                temp.myNext = myCurrent;
                myPrevious = temp;
            }
        }
        mySize++;
    }

    /**
     * Adds the element after the current Node.
     * @param theItem The element being added after the current Node.
     */
    public void addAfter(final T theItem) {
        if (myCurrent != null) {
            final Node<T> temp = new Node<>();
            temp.myItem = theItem;
            temp.myNext = myCurrent.myNext;
            myCurrent.myNext = temp;
            mySize++;
        }
    }

    /** Removes the current node. */
    public T remove() {
        T result = null;
        if (myCurrent != null) {
            result = myCurrent.myItem;
            if (myCurrent == myStart) {
                myStart = myCurrent.myNext;
                myCurrent = myStart;
            } else {
                myPrevious.myNext = myCurrent.myNext;
                myCurrent = myPrevious.myNext;
            }
            mySize--;
        }
        return result;
    }

    /**
     * If the given item is in the list.
     * @param theItem element whose presence in this list is to be tested.
     * @return true if this list contains the specified element.
     */
    public boolean contains(final T theItem) {
        boolean result = false;
        Node<T> current = myStart;
        while (current != null) {
            if (current.myItem.compareTo(theItem) == 0) {
                result = true;
                break;
            }
            current = current.myNext;
        }
        return result;
    }

    /** Sort the MyLinkedList. */
    public void sort() {
        Node<T> current = myStart;
        for (int i = 0; i < mySize; i++) {
            Node<T> currentNext = current.myNext;
            while (currentNext != null) {
                if (current.myItem.compareTo(currentNext.myItem) >= 0) {
                    final T temp = current.myItem;
                    current.myItem = currentNext.myItem;
                    currentNext.myItem = temp;
                }
                currentNext = currentNext.myNext;
            }
            current = current.myNext;
        }
    }

    /**
     * Returns the size of the list.
     * @return the size of the list.
     */
    public int size() {
        return mySize;
    }

    /**
     * Returns if the list is empty.
     * @return returns if the list is empty.
     */
    public boolean isEmpty() {
        return myStart == null;
    }


    /**
     * Returns the node at the start of the list.
     * @return The node at the start of the list.
     */
    public T first() {
        myPrevious = null;
        myCurrent = myStart;
        return Optional.ofNullable(myCurrent).
               map(current -> current.myItem).
               orElse(null);
    }

    /**
     * Returns the item at the current node.
     * @return The item at the current node.
     */
    public T current() {
        return Optional.ofNullable(myCurrent).
               map(current -> current.myItem).
               orElse(null);
    }

    /**
     * Returns the item after the current node.
     * @return The item after the current node.
     */
    public T next() {
        T result = null;
        if (myCurrent != null) {
            myPrevious = myCurrent;
            myCurrent = myCurrent.myNext;
            result = Optional.ofNullable(myCurrent).
                     map(current -> current.myItem).
                     orElse(null);
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        if (myStart != null) {
            Node<T> current = myStart;
            while (current.myNext != null) {
                builder.append(current.myItem.toString()).append(", ");
                current = current.myNext;
            }
            builder.append(current.myItem.toString());
        }
        builder.append("]");
        return builder.toString();
    }

    private static final class Node<T> {

        /** The item stored in this Node. */
        private T myItem;

        /** The Node after this Node. */
        private Node<T> myNext;

        @Override
        public String toString() {
            return Optional.ofNullable(myItem).map(Object::toString).orElse(null);
        }
    }
}
