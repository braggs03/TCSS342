import java.io.FileReader;
import java.io.IOException;

/**
 * Used to store and parse a txt document.
 * @author Brandon Ragghiant (braggs)
 * @version Winter 2024
 */
public final class BookReader {

    /** Used to store the contents of the book. */
    public String book;

    /** Stores all words from the parsed txt document. */
    public final MyLinkedList<String> words;

    /** Default constructor if no file name is passed into constructor. */
    public BookReader() {
        this("WarAndPeace.txt");
    }

    /**
     * Creates a BookReader object.
     * @param theFileName the file name of the txt document to be read.
     */
    public BookReader(final String theFileName) {
        super();
        book = "";
        words = new MyLinkedList<>();
        readBook(theFileName);
        parseWords();
    }

    /**
     * Reads all individual characters and concatenates them to the field book.
     * @param theFileName The file name of the document to be
     *                    parsed, must include file extension.
     */
    public void readBook(final String theFileName) {
        final FileReader bookScanner;
        try {
            bookScanner = new FileReader(theFileName);
            final StringBuilder builder = new StringBuilder();
            int c;
            while ((c = bookScanner.read()) != -1) {
                builder.append((char) c);
            }
            bookScanner.close();
            book = builder.toString();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses all words stored in the field book and
     * are to the field MyLinkedList named words.
     */
    public void parseWords() {
        Character character;
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < book.length(); i++) {
            character = book.charAt(i);
            if (character.compareTo('A') >= 0 && character.compareTo('Z') <= 0
                || character.compareTo('a') >= 0 && character.compareTo('z') <= 0
                || character.compareTo('0') >= 0 && character.compareTo('9') <= 0
                || character.equals('\'')) {
                builder.append(character);
            } else if (!builder.isEmpty()) {
                words.addBefore(builder.toString());
                builder.setLength(0);
            }
        }
    }
}
