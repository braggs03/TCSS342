/**
 *
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class MyTrie {

    /** The root of the MyTrie object. */
    private final Node myRoot;

    /** The size of the MyTrie object */
    private int mySize;

    /** How many comparison were made. */
    public long comparisons;

    /** Constructor used to instantiate a new MyTrie. */
    public MyTrie() {
        super();
        myRoot = new Node();
        mySize = 0;
    }

    /** Insert the given String into it's correct position. */
    public void insert(final String theItem) {
        Node currentNode = myRoot;
        for (int i = 0; i < theItem.length(); i++) {
            comparisons++;
            final Node requestedNode = new Node(theItem.charAt(i));
            final Node searchedNode = currentNode.myChildren.binarySearch(requestedNode);
            if (searchedNode == null) {
                currentNode.myChildren.add(requestedNode);
                currentNode = requestedNode;
            } else {
                currentNode = searchedNode;
            }
        }
        if (!currentNode.myTerminal) {
            currentNode.myTerminal = true;
            mySize++;
        }
    }

    /** Removes the given String if present in the MyTrie. */
    public void remove(final String theItem) {
        Node currentNode = myRoot;
        for (int i = 0; i < theItem.length(); i++) {
            final Node requestedNode = new Node(theItem.charAt(i));
            final Node searchedNode = currentNode.myChildren.binarySearch(requestedNode);
            if (searchedNode != null && i + 1 == theItem.length()) {
                searchedNode.myTerminal = false;
            } else if (searchedNode == null) {
                break;
            }
            currentNode = searchedNode;
        }
        mySize--;
    }

    /**
     * Returns if the item is present in the list or not.
     * @param theItem The element whose presence in this list is to be tested
     * @return True is the element is present, false otherwise.
     */
    public boolean find(final String theItem) {
        boolean result = true;
        Node currentNode = myRoot;
        for (int i = 0; i < theItem.length(); i++) {
            comparisons++;
            final Node searchedChar = currentNode.
                                      myChildren.
                                      binarySearch(new Node(theItem.charAt(i)));
            if (searchedChar == null
                || i + 1 == theItem.length()
                && !searchedChar.myTerminal) {
                result = false;
                break;
            }
            currentNode = searchedChar;
        }
        return result;
    }

    /** Returns the size of the MyTrie. */
    public int size() {
        return mySize;
    }

    /**
     * Returns true if this list contains no elements.
     * @return If the list is empty.
     */
    public boolean isEmpty() {
        return mySize == 0;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        addWords(myRoot, "", builder);
        return "[" + builder.substring(0, builder.length() - 2) + "]";
    }

    private void addWords(final Node theCurrentNode,
                          final String theCurrentString,
                          final StringBuilder theBuilder) {
        if (theCurrentNode.myTerminal && theCurrentNode.myChildren.isEmpty()) {
            theBuilder.append(theCurrentString.substring(1)).
                       append(theCurrentNode.myCharacter).
                       append(", ");
        } else if (theCurrentNode.myTerminal) {
            theBuilder.append(theCurrentString.substring(1)).
                       append(theCurrentNode.myCharacter).
                       append(", ");
            for (int i = 0; i < theCurrentNode.myChildren.size(); i++) {
                addWords(theCurrentNode.myChildren.get(i),
                         theCurrentString + theCurrentNode.myCharacter,
                         theBuilder);
            }
        } else {
            for (int i = 0; i < theCurrentNode.myChildren.size(); i++) {
                addWords(theCurrentNode.myChildren.get(i),
                        theCurrentString + theCurrentNode.myCharacter,
                        theBuilder);
            }
        }
    }

    private static final class Node implements Comparable<Node> {

        /** True if myCharacter forms the end of a word. */
        private boolean myTerminal;

        /** The character stored in this node. */
        private final char myCharacter;

        /** All the nodes of children. */
        private final MyOrderedList<Node> myChildren;

        private Node() {
            super();
            myTerminal = false;
            myCharacter = 0;
            myChildren = new MyOrderedList<>();
        }

        private Node(final char theCharacter) {
            super();
            myTerminal = false;
            myCharacter = theCharacter;
            myChildren = new MyOrderedList<>();
        }

        @Override
        public int compareTo(final Node theOtherNode) {
            return myCharacter - theOtherNode.myCharacter;
        }

        @Override
        public boolean equals(final Object theObject) {
            if (this == theObject) {
                return true;
            }

            if (theObject == null || !theObject.getClass().equals(this.getClass())) {
                return false;
            }
            final Node otherNode = (Node) theObject;
            return myCharacter == otherNode.myCharacter;
        }
    }
}
