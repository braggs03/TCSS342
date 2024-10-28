/**
 * Reads the book War and Peace and adds all unique elements
 * to MyArrayList, MyLinkedList, and MyOrderedList.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class UniqueWords {

    /** A BookReader that contains the contents of passed book. */
    public final BookReader book;

    /** A MyHashTable to store all the unique words in the book. */
    public MyHashTable<String, String> hashOfUniqueWords;

    /** Constructor used to instantiate a new UniqueWords. */
    public UniqueWords() {
        super();
        book = new BookReader();
        hashOfUniqueWords = new MyHashTable<>();
    }

    public void addUniqueWordsToHashTable() {
        final long startTime = System.currentTimeMillis();
        String c = book.words.first();
        while (c != null) {
            if (hashOfUniqueWords.get(c) == null) {
                hashOfUniqueWords.put(c, c);
            }
            c = book.words.next();
        }
        final long runtime = System.currentTimeMillis() - startTime;
        System.out.println("Runtime of MyHashTable: "
                + runtime / 1000 + " seconds and "
                + runtime % 1000 + " milliseconds.");
    }
}
