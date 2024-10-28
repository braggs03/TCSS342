import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Decodes a Huffman encoded compressed file.
 * Must include both the compressed file and codes file.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class HuffmanDecoder {

    /**
     * How many bits are in a byte.
     */
    private static final int BYTE_SIZE = 8;

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
    private final MyOrderedList<CodeNode> myCodes;

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
        myCodes = new MyOrderedList<>();
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
            while ((c = reader.read()) != -1) {
                int a;
                final StringBuilder s = new StringBuilder();
                //Discards value as we know it will be a colon
                reader.read();
                while (reader.ready() && (a = reader.read()) != ' ') {
                    s.append((char) a);
                }
                myCodes.add(new CodeNode((char) c, s.toString()));
            }
        } catch (final IOException error) {
            System.out.println("The file does not elxist: " + error);
        }
        System.out.println("Read and built codes in: "
                           + (System.currentTimeMillis() - startTime)
                           + " milliseconds.");
    }

    private String getBit(final int theCurrentPos) {
        return Integer.toString(myEncodedText[theCurrentPos / BYTE_SIZE]
               >> (BYTE_SIZE - (theCurrentPos % BYTE_SIZE + 1)) & 0x0001);
    }

    private void rebuildText() {
        final long startTime = System.currentTimeMillis();
        final StringBuilder bookBuilder = new StringBuilder();
        final StringBuilder currentCharBuilder = new StringBuilder();
        final int limit = myEncodedText.length * BYTE_SIZE;
        for (int i = 0; i < limit; i++) {
            currentCharBuilder.append(getBit(i));
            final CodeNode searchedNode = myCodes.binarySearch(new CodeNode(currentCharBuilder.toString()));
            if (searchedNode != null) {
                currentCharBuilder.setLength(0);
                bookBuilder.append(searchedNode.getCharacter());
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
        } catch (final FileNotFoundException error) {
            System.out.println("Output file was not found: " + error);
        }

        printer.print(book);
        System.out.println("Printed to file in : "
                           + (System.currentTimeMillis() - startTime)
                           + " milliseconds.");
    }

    /**
     * Stores the both the character and code assigned to said character
     */
    private static final class CodeNode implements Comparable<CodeNode> {

        /**
         * The character stored in this CodeNode
         */
        private final Character myCharacter;

        /**
         * The code assigned to myCharacter
         */
        private final String myCode;

        private CodeNode(final Character theChar, final String theCode) {
            super();
            myCharacter = theChar;
            myCode = theCode;
        }

        private CodeNode(final String theCode) {
            super();
            myCharacter = null;
            myCode = theCode;
        }

        private Character getCharacter() {
            return myCharacter;
        }

        @Override
        public String toString() {
            return myCharacter + ":" + myCode;
        }

        @Override
        public int compareTo(final CodeNode theOtherCodeNode) {
            return myCode.compareTo(theOtherCodeNode.myCode);
        }

        @Override
        public boolean equals(final Object theOtherObject) {
            if (this == theOtherObject) {
                return true;
            }
            if (theOtherObject == null || !this.getClass().equals(theOtherObject.getClass())) {
                return false;
            }
            final CodeNode otherCodeNode = (CodeNode) theOtherObject;
            return myCode.equals(otherCodeNode.myCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(myCharacter, myCode);
        }

    }
}
