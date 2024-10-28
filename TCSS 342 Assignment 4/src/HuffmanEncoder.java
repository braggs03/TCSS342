import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * A files compression algorithm that employs the Huffman's coding algorithm.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public final class HuffmanEncoder {

    /** The size of a byte. Used to reduce possibility of typo. */
    private static final int BYTE_SIZE = 8;

    /** A BookReader object initialized to the input file. */
    public final BookReader book;

    /** An ordered list that stores the frequency of each character in the input file. */
    public final MyOrderedList<FrequencyNode> frequencies;

    /** The encoded binary string stored as an array of bytes. */
    public byte[] encodedText;

    /** The name of the input file. */
    private final String myInputFile;

    /** The name of the output file. */
    private final String myOutputFile;

    /** The name of the codes file */
    private final String myCodesFile;

    /**
     * An ordered list storing the codes assigned
     * to each character by the Huffman algorithm.
     */
    private final MyOrderedList<CodeNode> myCodes;

    /** Instantiates a new HuffmanEncoder to default War and Peace text. */
    public HuffmanEncoder() {
        this("WarAndPeace.txt");
    }

    /** Instantiates a new HuffmanEncoder to passed text. */
    public HuffmanEncoder(final String theInputFile) {
        super();
        final long startTime = System.currentTimeMillis();
        myInputFile = theInputFile;
        myOutputFile = theInputFile.substring(0, theInputFile.indexOf('.'))
                       + "-compressed.bin";
        myCodesFile = theInputFile.substring(0, theInputFile.indexOf('.'))
                      + "-codes.txt";
        final long bookStartTime = System.currentTimeMillis();
        book = new BookReader(theInputFile);
        System.out.println("File parsing time took "
                           + (System.currentTimeMillis() - startTime)
                           + " milliseconds.");
        frequencies = new MyOrderedList<>();
        myCodes = new MyOrderedList<>();
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
        final int bookCharacterCount = book.book.length();
        for (int i = 0; i < bookCharacterCount; i++) {
            final FrequencyNode temp = new FrequencyNode(book.book.charAt(i));
            final FrequencyNode node = frequencies.binarySearch(temp);
            if (node == null) {
                frequencies.add(temp);
            } else {
                node.incrementCount();
            }
        }
        System.out.println("Counting frequency of all characters, of which "
                           + frequencies.size() + " are unique in "
                           + (System.currentTimeMillis() - startTime)
                           + " milliseconds.");
    }

    private void buildTree() {
        final long startTime = System.currentTimeMillis();
        final MyPriorityQueue<HuffmanNode> huffManNodes = new MyPriorityQueue<>();
        for (int i = 0; i < frequencies.size(); i++) {
            final FrequencyNode temp = frequencies.get(i);
            huffManNodes.insert(new HuffmanNode(temp.getChar(), temp.getCount()));
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
            if (theNode.myCharacter != null) {
                myCodes.add(new CodeNode(theNode.myCharacter, theCode));
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
        for (int i = 0; i < book.book.length(); i++) {
            final CodeNode node = myCodes.binarySearch(new CodeNode(book.book.charAt(i), ""));
            if (node != null) {
                builder.append(node.getCode());
            }
        }
        final String binaryString = builder.toString();
        final int binaryStringLength = binaryString.length() - BYTE_SIZE;
        encodedText = new byte[(binaryStringLength / BYTE_SIZE) + 2];
        int i;
        for (i = 0; i < binaryStringLength; i += BYTE_SIZE) {
            final byte b = (byte) Integer.parseInt(binaryString.
                                                   substring(i, i + BYTE_SIZE), 2);
            encodedText[i / BYTE_SIZE] = b;
        }
        if (binaryString.length() > BYTE_SIZE) {
            encodedText[i / BYTE_SIZE] = (byte) Integer.parseInt(
                                                binaryString.
                                                substring(i - BYTE_SIZE, binaryStringLength),
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
            for (int i = 0; i < myCodes.size(); i++) {
                printer.print(myCodes.get(i) + " ");
            }
            printer.close();
        } catch (final IOException error) {
            System.out.print("File was not found: " + error);
        }
        System.out.println("Writing to compressed file took "
                           + (System.currentTimeMillis() - startTime)
                           + " milliseconds.");
    }

    private static final class FrequencyNode implements Comparable<FrequencyNode> {

        /** The character of the frequency node */
        private final Character myCharacter;

        /** How many times myCharacter has appeared in the text */
        private Integer myCount;

        FrequencyNode(final char theChar) {
            super();
            myCharacter = theChar;
            myCount = 1;
        }

        private char getChar() {
            return myCharacter;
        }

        private int getCount() {
            return myCount;
        }

        private void incrementCount() {
            myCount++;
        }

        @Override
        public int compareTo(final FrequencyNode theNode) {
            return this.myCharacter - theNode.myCharacter;
        }

        @Override
        public String toString() {
            return myCharacter + ":" + myCount;
        }

        @Override
        public boolean equals(final Object theObject) {
            if (this == theObject) {
                return true;
            }
            if (theObject == null || !theObject.getClass().equals(this.getClass())) {
                return false;
            }
            final FrequencyNode otherFrequencyNode = (FrequencyNode) theObject;
            return myCharacter.equals(otherFrequencyNode.myCharacter)
                    && myCount.equals(otherFrequencyNode.myCount);
        }

        @Override
        public int hashCode() {
            return Objects.hash(myCharacter, myCount);
        }
    }


    /** Used to build a HuffManTree */
    private static final class HuffmanNode implements Comparable<HuffmanNode> {

        /** The character stored in this HuffmanNode. */
        private final Character myCharacter;

        /** The weight of the HuffmanNode. */
        private final Integer myWeight;

        /** The root of the left subtree. */
        private HuffmanNode myLeft;

        /** The root of the right subtree. */
        private HuffmanNode myRight;

        HuffmanNode(final Character theChar, final Integer theInt) {
            super();
            myCharacter = theChar;
            myWeight = theInt;
        }

        HuffmanNode(final HuffmanNode theLeft,
                    final HuffmanNode theRight,
                    final Integer theWeight) {
            super();
            myLeft = theLeft;
            myRight = theRight;
            myCharacter = null;
            myWeight = theWeight;
        }

        private int getWeight() {
            return myWeight;
        }

        @Override
        public String toString() {
            return myCharacter + ":" + myWeight;
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
            return myCharacter == otherHuffmanNode.myCharacter
                   && myWeight.equals(otherHuffmanNode.myWeight)
                   && myLeft.equals(otherHuffmanNode.myLeft)
                   && myRight.equals(otherHuffmanNode.myRight);
        }

        @Override
        public int hashCode() {
            return Objects.hash(myCharacter, myWeight, myLeft, myRight);
        }
    }

    private static final class CodeNode implements Comparable<CodeNode> {

        /** The character stored in this CodeNode. */
        private final Character myCharacter;

        /** The code assigned to myCharacter */
        private final String myCode;

        CodeNode(final Character theCharacter, final String theCode) {
            super();
            myCharacter = theCharacter;
            myCode = theCode;
        }

        private String getCode() {
            return myCode;
        }

        @Override
        public int compareTo(final CodeNode theNode) {
            return myCharacter.compareTo(theNode.myCharacter);
        }

        @Override
        public String toString() {
            return myCharacter + ":" + myCode;
        }

        @Override
        public boolean equals(final Object theObject) {
            if (this == theObject) {
                return true;
            }
            if (theObject == null || !theObject.getClass().equals(this.getClass())) {
                return false;
            }
            final CodeNode otherHuffmanNode = (CodeNode) theObject;
            return myCharacter.equals(otherHuffmanNode.myCharacter)
                   && myCode.equals(otherHuffmanNode.myCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(myCharacter, myCode);
        }
    }
}
