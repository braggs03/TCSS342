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
    private final K[] myKeyBucket;

    /** Store the values in the position corresponding to their hash value. */
    private final V[] myValueBucket;

    /** Stores all unique keys in the MyHashTable. */
    private final MyArrayList<K> myKeys;

    /** How many comparison were made in the tests. */
    public int comparisons;

    /** The maximum number of probes made to put any key-value pair. */
    public int maxProbe;

    /**
     * Default constructor for the MyHashTable.
     * Default size for the capacity is 2^15.
     */
    public MyHashTable() {
        this(32768);
    }

    /**
     * Constructor used to create a MyHashTable of passed size.
     * @param theCapacity The max elements that will be allowed in the MyHashTable.
     */
    @SuppressWarnings("unchecked")
    public MyHashTable(final int theCapacity) {
        super();
        if (theCapacity < 1) {
            throw new IllegalArgumentException();
        }
        myCapacity = theCapacity;
        mySize = 0;
        myKeyBucket = (K[]) new Comparable[myCapacity];
        myValueBucket = (V[]) new Object[myCapacity];
        myKeys = new MyArrayList<>();
    }

    private int hash(final K theKey) {
        return Math.abs(theKey.hashCode()) % myCapacity;
    }

    /**
     * Returns the value if present in the MyHashTable, null otherwise.
     * @param theKey The key whose presence is to be tested.
     * @return The value if present in the MyHashTable, null otherwise.
     */
    public V get(final K theKey) {
        V result = null;
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
        return result;
    }

    /**
     * Adds the key and the item to the MyHashTable if the key is not present already.
     * @param theKey The key that maps to theValue.
     * @param theValue The value that theKey is mapped to.
     */
    public void put(final K theKey, final V theValue) {
        if (theKey != null) {
            if (mySize != myCapacity) {
                int pushes = 1;
                int hashOfKey = hash(theKey);
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
                    mySize++;
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
            if (myKeyBucket[i] != null) {
                builder.append(myKeyBucket[i]).append(":").append(myValueBucket[i]).append(", ");
            }
        }
        return builder.substring(0, builder.length() - 2) + "]";
    }
}
