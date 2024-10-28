/**
 * Reads the book War and Peace and adds all unique elements
 * to MyArrayList, MyLinkedList, and MyOrderedList.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class UniqueWords {

    /**
     * A BookReader that contains the contents of passed book.
     */
    public final BookReader book;

    /**
     * A MyArrayList object that is used to store the unique words of the book.
     */
    public final MyArrayList<String> alOfUniqueWords;

    /**
     * A MyLinkedList object that is used to store the unique words of the book.
     */
    public final MyLinkedList<String> llOfUniqueWords;

    /**
     * A MyOrderedList object that is used to store the unique words of the book.
     */
    public final MyOrderedList<String> olOfUniqueWords;

    /**
     * A MyTie object that is used to store the unique words of the book.
     */
    public final MyTrie trieOfUniqueWords;

    /** Constructor used to instantiate a new UniqueWords. */
    public UniqueWords() {
        super();
        book = new BookReader();
        alOfUniqueWords = new MyArrayList<>();
        llOfUniqueWords = new MyLinkedList<>();
        olOfUniqueWords = new MyOrderedList<>();
        trieOfUniqueWords = new MyTrie();
    }

    /**
     * Adds all unique words of the book to the MyArrayList.
     */
    public void addUniqueWordsToArrayList() {
        final long startTime = System.currentTimeMillis();
        String c = book.words.first();
        while (c != null) {
            if (!alOfUniqueWords.contains(c)) {
                alOfUniqueWords.insert(c, alOfUniqueWords.size());
            }
            c = book.words.next();
        }
        long runtime = (System.currentTimeMillis() - startTime);
        System.out.println("Runtime of MyArrayList: "
                + runtime / 1000 + " seconds and "
                + runtime % 1000 + " milliseconds.");
    }

    /**
     * Adds all unique words of the book to the MyLinkedList.
     */
    public void addUniqueWordsToLinkedList() {
        final long startTime = System.currentTimeMillis();
        String c = book.words.first();
        while (c != null) {
            if (!llOfUniqueWords.contains(c)) {
                llOfUniqueWords.addBefore(c);
            }
            c = book.words.next();
        }
        long runtime = (System.currentTimeMillis() - startTime);
        System.out.println("Runtime of MyLinkedList: "
                + runtime / 1000 + " seconds and "
                + runtime % 1000 + " milliseconds.");
    }

    /**
     * Adds all unique words of the book to the MyOrderedList.
     */
    public void addUniqueWordsToTrie() {
        final long startTime = System.currentTimeMillis();
        String c = book.words.first();
        while (c != null) {
            if (!trieOfUniqueWords.find(c)) {
                trieOfUniqueWords.insert(c);
            }
            c = book.words.next();
        }
        long runtime = (System.currentTimeMillis() - startTime);
        System.out.println("Runtime of MyTrie: "
                           + runtime / 1000 + " seconds and "
                           + runtime % 1000 + " milliseconds.");
    }

}
