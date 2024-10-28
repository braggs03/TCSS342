import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Decodes a Huffman encoded compressed file.
 * Must include both the compressed file and codes file.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class HuffmanDecoder {

    /**
     * The name of the compressed file
     */
    private final String myCompressedFile;

    /**
     * The name of the codes file
     */
    private final String myCodesFile;

    /**
     * The decompressed name of the output file.
     */
    private final String myOutputFile;

    /**
     * The string of codes from the input codes file.
     */
    private String myCodesString;

    /**
     * The encoded text from the compressed file.
     */
    private byte[] myEncodedText;

    /**
     * A MyOrderedList that stores the codes assigned to each character
     */
    private final MyHashTable<String, String> myCodesHash;

    /**
     * The text after it has been decoded
     */
    public String book;

    /**
     * Default constructor for HuffmanDecoder.
     * With no input file, assume WarAndPeace is text to decode.
     */
    public HuffmanDecoder() {
        this("WarAndPeace");
    }

    /**
     * Decodes the passed in encoded file.
     * Must include both compressed and codes file.
     * Passed file name must be the name of the book,
     * i.e. WarAndPeace, not WarAndPeace-compressed.txt.
     * @param theCompressedFile The encoded text.
     */
    public HuffmanDecoder(final String theCompressedFile) {
        super();
        final long startTime = System.currentTimeMillis();

        myCompressedFile = theCompressedFile + "-compressed.bin";
        myCodesFile = theCompressedFile + "-codes.txt";
        myOutputFile = theCompressedFile + "-decompressed.txt";
        myCodesHash = new MyHashTable<>();
        readFiles();
        rebuildText();
        writeFile();
        System.out.println("Entire decoder took: "
                           + (System.currentTimeMillis() - startTime)
                           + " milliseconds.");

    }

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    private void readFiles() {
        final long startTime = System.currentTimeMillis();
        try {
            myEncodedText = Files.readAllBytes(Path.of(myCompressedFile));
            final BufferedReader reader = new BufferedReader(new FileReader(myCodesFile));
            int c;
            // Discards [.
            reader.read();
            while ((c = reader.read()) != -1) {
                final StringBuilder s = new StringBuilder();
                s.append((char) c);
                int a;
                while (reader.ready() && (a = reader.read()) != ':') {
                    s.append((char) a);
                }
                final String word = s.toString();
                s.setLength(0);
                a = reader.read();
                while (reader.ready() && a != ',' && a != ']') {
                    s.append((char) a);
                    a = reader.read();
                }
                myCodesHash.put(s.toString(), word);
                reader.read();
            }
            reader.close();
        } catch (final IOException error) {
            System.out.println("The file does not exist: " + error);
        }
        System.out.println("Read and built codes in: "
                           + (System.currentTimeMillis() - startTime)
                           + " milliseconds.");
    }

    private String getBit(final int theCurrentPos) {
        return Integer.toString(myEncodedText[theCurrentPos / Byte.SIZE]
               >> (Byte.SIZE - (theCurrentPos % Byte.SIZE + 1)) & 0x0001);
    }

    private void rebuildText() {
        final long startTime = System.currentTimeMillis();
        final StringBuilder bookBuilder = new StringBuilder();
        final StringBuilder currentCharBuilder = new StringBuilder();
        final int limit = (myEncodedText.length) * Byte.SIZE;
        for (int i = 0; i < limit; i++) {
            currentCharBuilder.append(getBit(i));
            final String searchedNode = myCodesHash.get(currentCharBuilder.toString());
            if (searchedNode != null) {
                currentCharBuilder.setLength(0);
                bookBuilder.append(searchedNode);
            }
        }
        book = bookBuilder.toString();
        System.out.println("Rebuild text in: " + (System.currentTimeMillis() - startTime) + " milliseconds.");
    }

    private void writeFile() {
        final long startTime = System.currentTimeMillis();
        PrintStream printer = null;
        try {
            printer = new PrintStream(myOutputFile);
            printer.print(book);
            printer.close();
        } catch (final FileNotFoundException error) {
            System.out.println("Output file was not found: " + error);
        }
        System.out.println("Printed to file in : "
                           + (System.currentTimeMillis() - startTime)
                           + " milliseconds.");
    }

}
