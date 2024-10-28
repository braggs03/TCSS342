/**
 * A data structure used to represent a hash table.
 * @param <K> The generic type of the key.
 * @param <V> The generic type of the value.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class MyHashTable<K extends Comparable<K>, V> {

    /** The capacity of the MyHashTable. */
    private final int myCapacity;

    /** The current size of the MyHashTable. */
    private int mySize;

    /** Stores the keys in the position corresponding to their hash value. */
    private K[] myKeyBucket;

    /** Store the values in the position corresponding to their hash value. */
    private V[] myValueBucket;

    /** Stores all unique keys in the MyHashTable. */
    private MyArrayList<K> myKeys;


    /** Stores all unique keys in the MyHashTable. */
    private MyArrayList<KeyValuePair>[] myChainBucket;

    /** How many comparison were made in the tests. */
    public int comparisons;

    /** The maximum number of probes made to put any key-value pair. */
    public int maxProbe;

    /** Determines if this MyHashTable has chaining enabled. */
    private final boolean myChaining;

    /**
     * Default constructor for the MyHashTable.
     * Default size for the capacity is 2^15.
     */
    public MyHashTable() {
        this(32768, false);
    }

    /**
     * Constructor used to create a MyHashTable of passed size.
     * @param theCapacity The max elements that will be allowed in the MyHashTable.
     */
    @SuppressWarnings("unchecked")
    public MyHashTable(final int theCapacity, final boolean theChaining) {
        super();
        if (theCapacity < 1) {
            throw new IllegalArgumentException();
        }
        myCapacity = theCapacity;
        mySize = 0;
        comparisons = 0;
        maxProbe = 0;
        myChaining = theChaining;
        if (myChaining) {
            myChainBucket = new MyArrayList[myCapacity];
        } else {
            myKeyBucket = (K[]) new Comparable[myCapacity];
            myValueBucket = (V[]) new Object[myCapacity];
            myKeys = new MyArrayList<>();
        }
    }

    private int hash(final K theKey) {
        return Math.abs(theKey.hashCode()) % myCapacity;
    }

    public V get(final K theKey) {
        V result = null;
        if (myChaining) {
            if (theKey != null) {
                int hashOfKey = hash(theKey);
                MyArrayList<KeyValuePair> searchedBucket = myChainBucket[hashOfKey];
                KeyValuePair tempValue = new KeyValuePair(theKey);
                if (searchedBucket != null && searchedBucket.contains(tempValue)) {
                    result =  searchedBucket.get(searchedBucket.indexOf(tempValue)).myValue;
                }
            }
        } else {
            if (theKey != null) {
                int hashOfKey = hash(theKey);
                hashOfKey = hashOfKey >= myCapacity || hashOfKey < 0 ? 0 : hashOfKey;
                if (myKeyBucket[hashOfKey] != null) {
                    K temp = myKeyBucket[hashOfKey];
                    while (temp != null && !temp.equals(theKey)) {
                        temp = myKeyBucket[++hashOfKey];
                    }
                    result = myValueBucket[hashOfKey];
                }
            }
        }
        return result;
    }

    /**
     * Adds the key and the item to the MyHashTable if the key is not present already.
     * @param theKey The key that maps to theValue.
     * @param theValue The value that theKey is mapped to.
     */
    public void put(final K theKey, final V theValue) {
        if (theKey != null && mySize != myCapacity) {
            mySize = mySize + 1;
            int pushes = 1;
            int hashOfKey = hash(theKey);
            if (myChaining) {
                MyArrayList<KeyValuePair> searchedBucket = myChainBucket[hashOfKey];
                if (searchedBucket == null) {
                    MyArrayList<KeyValuePair> newBucket = new MyArrayList<>();
                    myChainBucket[hashOfKey] = newBucket;
                    searchedBucket = newBucket;
                }
                int currentPos = 0;
                while (searchedBucket.get(currentPos) != null) {
                    pushes++;
                    if (searchedBucket.get(currentPos).myKey.equals(theKey)) {
                        break;
                    }
                    currentPos++;
                }
                if (searchedBucket.get(currentPos) == null) {
                    searchedBucket.insert(new KeyValuePair(theKey, theValue), searchedBucket.size());
                } else {
                    searchedBucket.remove(currentPos);
                    searchedBucket.insert(new KeyValuePair(theKey, theValue), searchedBucket.size());
                }
                comparisons += pushes;
                maxProbe = Math.max(maxProbe, pushes);
            } else {
                while (myKeyBucket[hashOfKey] != null) {
                    if (theKey.equals(myKeyBucket[hashOfKey])) {
                        break;
                    } else {
                        pushes++;
                        hashOfKey = hashOfKey < myCapacity - 1 ? ++hashOfKey : 0;
                    }
                }
                comparisons += pushes;
                if (myKeyBucket[hashOfKey] == null) {
                    myKeys.insert(theKey, hashOfKey);
                }
                maxProbe = Math.max(maxProbe, pushes);
                myKeyBucket[hashOfKey] = theKey;
                myValueBucket[hashOfKey] = theValue;
            }
        }
    }

    /**
     * Returns how many elements are in the MyHashTable.
     * @return Return the size of the MyHashTable.
     */
    public int size() {
        return mySize;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int i = 0; i < myCapacity; i++) {
            MyArrayList<KeyValuePair> temp = myChainBucket[i];
            if (temp != null) {
                for (int k = 0; k < temp.size(); k++) {
                    builder.append(temp.get(k).toString()).append(", ");
                }
            }
        }
        return builder.substring(0, builder.length() - 2) + "]";
    }


    private class KeyValuePair implements Comparable<KeyValuePair> {
        private final K myKey;
        private final V myValue;

        private KeyValuePair(final K theKey) {
            this(theKey, null);
        }
        private KeyValuePair(final K theKey, final V theValue) {
            myKey = theKey;
            myValue = theValue;
        }

        @Override
        public boolean equals(final Object theObject) {
            if (this == theObject) {
                return true;
            }
            if (theObject == null || !theObject.getClass().equals(this.getClass())) {
                return false;
            }
            final KeyValuePair otherKeyValuePair = (KeyValuePair) theObject;
            return myKey.equals(otherKeyValuePair.myKey);
        }

        @Override
        public int compareTo(final KeyValuePair theOtherKeyValuePair) {
            return myKey.compareTo(theOtherKeyValuePair.myKey);
        }

        @Override
        public String toString() {
            return myKey.toString() + ":" + myValue.toString();
        }
    }
}
