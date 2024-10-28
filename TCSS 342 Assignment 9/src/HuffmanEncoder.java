import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * A files compression algorithm that employs the Huffman's coding algorithm.
 * A boolean flag can be set in the constructor for whether this algorithm encode
 * based on words and separators or individual character.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class HuffmanEncoder {

    /** A boolean for if the HuffmanEncoder should encode single character or whole words. */
    private final boolean myWordCodes;

    /** Stores the words and the frequency of the words. */
    public final MyHashTable<String, Integer> frequenciesHash;

    /** Stores the codes and the text after the huffman tree is complete. */
    private final MyHashTable<String, String> myCodesHash;

    /** A BookReader object initialized to the input file. */
    public final BookReader book;

    /** The encoded binary string stored as an array of bytes. */
    public byte[] encodedText;

    /** The name of the input file. */
    private final String myInputFile;

    /** The name of the output file. */
    private final String myOutputFile;

    /** The name of the codes file */
    private final String myCodesFile;

    /** Instantiates a new HuffmanEncoder to default War and Peace text. */
    public HuffmanEncoder() {
        this("WarAndPeace.txt");
    }

    /** Instantiates a new HuffmanEncoder to passed text. */
    public HuffmanEncoder(final String theInputFile) {
        super();
        final long startTime = System.currentTimeMillis();
        myWordCodes = true;
        myInputFile = theInputFile;
        myOutputFile = theInputFile.substring(0, theInputFile.indexOf('.'))
                + "-compressed.bin";
        myCodesFile = theInputFile.substring(0, theInputFile.indexOf('.'))
                + "-codes.txt";
        final long bookStartTime = System.currentTimeMillis();
        book = new BookReader(theInputFile);
        frequenciesHash = new MyHashTable<>();
        myCodesHash = new MyHashTable<>();
        System.out.println("File parsing time took "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
        countFrequency();
        buildTree();
        encode();
        writeFiles();
        System.out.println("Entire encoding algorithm took: "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
    }

    private void countFrequency() {
        final long startTime = System.currentTimeMillis();
        String currentWord = book.wordsAndSeparators.first();
        while (currentWord != null) {
            final Integer searchedNode = frequenciesHash.get(currentWord);
            if (searchedNode == null) {
                frequenciesHash.put(currentWord, 1);
            } else {
                frequenciesHash.put(currentWord, frequenciesHash.get(currentWord) + 1);
            }
            currentWord = book.wordsAndSeparators.next();
        }
        System.out.println("Counting frequency of all words and separators, of which "
                + frequenciesHash.size() + " are unique in "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
    }

    private void buildTree() {
        final long startTime = System.currentTimeMillis();
        final MyPriorityQueue<HuffmanNode> huffManNodes = new MyPriorityQueue<>();
        for (int i = 0; i < frequenciesHash.size(); i++) {
            final String key = frequenciesHash.myKeys.get(i);
            final int frequency = frequenciesHash.get(key);
            huffManNodes.insert(new HuffmanNode(key, frequency));
        }
        HuffmanNode newHuffmanNode = null;
        while (huffManNodes.size() != 1) {
            final HuffmanNode temp1 = huffManNodes.removeMin();
            final HuffmanNode temp2 = huffManNodes.removeMin();
            newHuffmanNode = new HuffmanNode(temp1, temp2,
                    temp1.getWeight() + temp2.getWeight());
            huffManNodes.insert(newHuffmanNode);
        }
        extractCodes(newHuffmanNode, "");
        System.out.println("Building Huffman tree and reading codes in "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
    }

    private void extractCodes(final HuffmanNode theNode, final String theCode) {
        if (theNode != null) {
            if (theNode.myWord != null) {
                myCodesHash.put(theNode.myWord, theCode);
            } else {
                if (theNode.myLeft != null) {
                    extractCodes(theNode.myLeft, theCode + "0");
                }
                if (theNode.myRight != null) {
                    extractCodes(theNode.myRight, theCode + "1");
                }
            }
        }
    }

    private void encode() {
        final long startTime = System.currentTimeMillis();
        final StringBuilder builder = new StringBuilder();
        book.wordsAndSeparators.first();
        for (int i = 0; i < book.wordsAndSeparators.size(); i++) {
            builder.append(myCodesHash.get(book.wordsAndSeparators.current()));
            book.wordsAndSeparators.next();
        }
        final String binaryString = builder.toString();
        final int binaryStringLength = binaryString.length() - Byte.SIZE;
        encodedText = new byte[(binaryStringLength / Byte.SIZE) + 2];
        int i;
        for (i = 0; i < binaryStringLength; i += Byte.SIZE) {
            final byte b = (byte) Integer.parseInt(binaryString.
                    substring(i, i + Byte.SIZE), 2);
            encodedText[i / Byte.SIZE] = b;
        }
        if (binaryString.length() > Byte.SIZE) {
            encodedText[i / Byte.SIZE] = (byte) Integer.parseInt(
                    binaryString.
                            substring(i - Byte.SIZE, binaryStringLength),
                    2);
        }
        System.out.println("Encoding text in "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
    }

    private void writeFiles() {
        final long startTime = System.currentTimeMillis();
        try {
            Files.write(Path.of(myOutputFile), encodedText);
            final PrintStream printer = new PrintStream(myCodesFile);
            printer.print(myCodesHash.toString());
            printer.close();
        } catch (final IOException error) {
            System.out.print("File was not found: " + error);
        }
        System.out.println("Writing to compressed file took "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
    }


    /** Used to build a HuffManTree */
    private static final class HuffmanNode implements Comparable<HuffmanNode> {

        /** The character stored in this HuffmanNode. */
        private final String myWord;

        /** The weight of the HuffmanNode. */
        private final int myWeight;

        /** The root of the left subtree. */
        private HuffmanNode myLeft;

        /** The root of the right subtree. */
        private HuffmanNode myRight;

        HuffmanNode(final String theWord, final int theInt) {
            super();
            myWord = theWord;
            myWeight = theInt;
        }

        HuffmanNode(final HuffmanNode theLeft,
                    final HuffmanNode theRight,
                    final int theWeight) {
            super();
            myLeft = theLeft;
            myRight = theRight;
            myWord = null;
            myWeight = theWeight;
        }

        private int getWeight() {
            return myWeight;
        }

        @Override
        public String toString() {
            return myWord + ":" + myWeight;
        }

        @Override
        public int compareTo(final HuffmanNode theNode) {
            return this.myWeight - theNode.myWeight;
        }

        @Override
        public boolean equals(final Object theObject) {
            if (this == theObject) {
                return true;
            }
            if (theObject == null || !theObject.getClass().equals(this.getClass())) {
                return false;
            }
            final HuffmanNode otherHuffmanNode = (HuffmanNode) theObject;
            return myWeight == otherHuffmanNode.myWeight;
        }

        @Override
        public int hashCode() {
            return Objects.hash(myWord, myWeight, myLeft, myRight);
        }
    }

    public static void main(String[] theArgs) {
        HuffmanEncoder e = new HuffmanEncoder();
    }
}
