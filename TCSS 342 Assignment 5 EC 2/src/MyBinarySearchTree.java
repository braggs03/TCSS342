/**
 * A data structure used as a naive BinarySearchTree.
 * @param <T> The generic type of this data structure.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class MyBinarySearchTree<T extends Comparable<T>> {

    /** The root of the binary search tree. */
    private Node myRoot;

    /** The size of the binary search tree. */
    private int mySize;

    /** Constructor used to instantiate a new MyBinarySearchTree. */
    public MyBinarySearchTree() {
        super();
        myRoot = null;
        mySize = 0;
    }

    /** Adds the given item to it's correct position. */
    public void add(final T theItem) {
        myRoot = add(theItem, myRoot);
        mySize++;
    }

    private Node add(final T theItem, final Node theSubtree) {
        final Node result;
        if (theSubtree == null) {
            result = new Node(theItem);
        } else if (theSubtree.myItem.compareTo(theItem) > 0) {
            theSubtree.myLeft = add(theItem, theSubtree.myLeft);
            result = theSubtree;
        } else {
            theSubtree.myRight = add(theItem, theSubtree.myRight);
            result = theSubtree;
        }
        updateHeight(result);
        return result;
    }

    /** Removes the given item if found. */
    public void remove(final T theItem) {
        if (theItem.equals(find(theItem))) {
            mySize--;
            myRoot = remove(theItem, myRoot);
        }
    }

    private Node remove(final T theItem, final Node theSubtree) {
        Node result = null;
        if (theSubtree.myItem.compareTo(theItem) == 0) {
            if (theSubtree.myLeft != null && theSubtree.myRight == null) {
                result = theSubtree.myLeft;
            } else if (theSubtree.myLeft == null && theSubtree.myRight != null) {
                result = theSubtree.myRight;
            } else if (theSubtree.myRight != null) {
                final Node rightMinimum = findRightMin(theSubtree.myRight);
                remove(rightMinimum.myItem, theSubtree);
                theSubtree.myItem = rightMinimum.myItem;
                result = theSubtree;
            }
        } else if (theSubtree.myItem.compareTo(theItem) < 0) {
            theSubtree.myRight = remove(theItem, theSubtree.myRight);
            result = theSubtree;
        } else {
            theSubtree.myLeft = remove(theItem, theSubtree.myLeft);
            result = theSubtree;
        }
        updateHeight(theSubtree);
        return result;
    }

    private Node findRightMin(final Node theSubtree) {
        return theSubtree.myLeft == null ? theSubtree : findRightMin(theSubtree.myLeft);
    }

    /**
     * Finds and returns the given item if present.
     * @param theItem element whose presence in this list is to be tested.
     * @return The item if present, null otherwise.
     */
    public T find(final T theItem) {
        return find(theItem, myRoot);
    }

    private T find(final T theItem, final Node theSubtree) {
        T result = null;
        if (theSubtree != null) {
            if (theSubtree.myItem.equals(theItem)) {
                result = theSubtree.myItem;
            } else if (theSubtree.myItem.compareTo(theItem) < 0) {
                result = find(theItem, theSubtree.myRight);
            } else {
                result = find(theItem, theSubtree.myLeft);
            }
        }
        return result;
    }

    private void updateHeight(final Node theNode) {
        if (theNode.myLeft == null && theNode.myRight == null) {
            theNode.myHeight = 0;
        } else if (theNode.myLeft == null) {
            theNode.myHeight = 1 + theNode.myRight.myHeight;
        } else if (theNode.myRight == null) {
            theNode.myHeight = theNode.myLeft.myHeight + 1;
        } else {
            theNode.myHeight = 1 + Math.max(theNode.myLeft.myHeight, theNode.myRight.myHeight);
        }
    }

    /**
     * Returns the myHeight of the binary search tree.
     * @return The myHeight of the binary search tree.
     */
    public int height() {
        return myRoot == null ? -1 : myRoot.myHeight;
    }

    /**
     * Returns the size of the binary search tree.
     * @return The size of the binary search tree.
     */
    public int size() {
        return mySize;
    }

    /**
     * Returns true if this binary search tree contains no elements.
     * @return true if this binary search tree contains no elements.
     */
    public boolean isEmpty() {
        return mySize == 0;
    }

    @Override
    public String toString() {
        String result = "[]";
        if (myRoot != null) {
            final StringBuilder builder = new StringBuilder();
            createString(myRoot, builder);
            result = "[" + builder.substring(0, builder.length() - 2) + "]";
        }
        return result;
    }

    private void createString(final Node theNode, final StringBuilder theBuilder) {
        if (theNode.myLeft != null) {
            createString(theNode.myLeft, theBuilder);
        }
        theBuilder.append(theNode).append(", ");
        if (theNode.myRight != null) {
            createString(theNode.myRight, theBuilder);
        }
    }

    private final class Node {

        /** The item stored in this Node. */
        private T myItem;

        /** The subtree to the left of this Node. */
        private Node myLeft;

        /** The subtree to the right of this Node. */
        private Node myRight;

        /** The height of this Node. */
        private int myHeight;

        private Node(final T theItem) {
            super();
            myItem = theItem;
            myLeft = null;
            myRight = null;
            myHeight = 0;
        }

        @Override
        public String toString() {
            return myItem.toString() + ":H" + myHeight;
        }
    }

}
