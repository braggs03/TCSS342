import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * An algorithm that compresses the passed text by using the LZW algorithm.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class LZWEncoder {

    /** How many bits make a byte. */
    private static final int BYTE_SIZE = 8;

    /** A BookReader for the given input. */
    public final BookReader book;

    /** The encoded binary String. */
    public byte[] encodedText;

    /** 2^myBitWidth, sets a limit for how many codes can be generated. */
    private final int myLimit;

    /** The name of the generated output file. */
    private final String myOutputFile;

    /** The name of the generated codes file. */
    private final String myCodesFile;

    /** The bit width for the codes for each word. */
    private final int myBitWidth;

    /**
     * A MyBinarySearchTree that stores the codes assigned
     * to each character or String of characters.
     */
    private final MyBinarySearchTree<CodeNode> myCodes;

    /** Constructor used to instantiate a new LZWEncoder to the text "War And Peace". */
    public LZWEncoder() {
        this("WarAndPeace.txt", 18);
    }

    /** Constructor used to instantiate a new LZWEncoder to the passed input file. */
    public LZWEncoder(final String theInputFile, final int theBitWidth) {
        super();
        final long startTime = System.currentTimeMillis();
        myOutputFile = theInputFile.substring(0, theInputFile.indexOf('.'))
                + "-compressed.bin";
        myCodesFile = theInputFile.substring(0, theInputFile.indexOf('.'))
                + "-codes.txt";
        myBitWidth = theBitWidth;
        myLimit = (int) Math.pow(2, theBitWidth);
        book = new BookReader(theInputFile);
        myCodes = new MyBinarySearchTree<>();
        initCodes();
        encode();
        writeFiles();
        System.out.println("Entire Algorithm Took: "
                           + (System.currentTimeMillis() - startTime)
                           + " milliseconds.");
    }

    private void initCodes() {
        final long startTime = System.currentTimeMillis();
        for (int i = 0; i < book.book.length(); i++) {
            final CodeNode temp = new CodeNode(String.valueOf(book.book.charAt(i)), extendBinaryString(Integer.toBinaryString(myCodes.size())));
            final CodeNode node = myCodes.find(temp);
            if (node == null) {
                myCodes.add(temp);
            }
        }
        System.out.println("Initializing Codes Took: "
                           + (System.currentTimeMillis() - startTime)
                           + " milliseconds.");
    }

    private void encode() {
        final long startTime = System.currentTimeMillis();
        final StringBuilder processedText = new StringBuilder();
        final StringBuilder currentText = new StringBuilder();
        int n = 0;
        CodeNode previousNode = null;
        while (n < book.book.length()) {
            final CodeNode temp = new CodeNode(currentText.append(book.book.charAt(n)).
                                      toString());
            final CodeNode node = myCodes.find(temp);
            if (node == null) {
                temp.myCode = extendBinaryString(Integer.toBinaryString(myCodes.size()));
                processedText.append(previousNode.myCode);
                if (myCodes.size() < myLimit) {
                    myCodes.add(temp);
                }
                currentText.setLength(0);
            } else {
                n++;
                previousNode = node;
            }
        }
        if (!currentText.isEmpty()) {
            processedText.append(previousNode.myCode);
        }
        final String binaryString = processedText.toString();
        final int binaryStringLength = binaryString.length() - BYTE_SIZE;
        encodedText = new byte[(binaryStringLength / BYTE_SIZE) + 1];
        for (int i = 0; i < binaryStringLength; i += BYTE_SIZE) {
            final byte by = (byte) Integer.parseInt(binaryString.
                            substring(i, i + BYTE_SIZE), 2);
            encodedText[i / BYTE_SIZE] = by;
        }
        System.out.println("Encoding Took: "
                           + (System.currentTimeMillis() - startTime)
                           + " milliseconds.");
    }

    private void writeFiles() {
        final long startTime = System.currentTimeMillis();
        final PrintStream printer;
        try {
            Files.write(Path.of(myOutputFile), encodedText);
            printer = new PrintStream(myCodesFile);
            printer.print(myCodes.toString());
            printer.close();
        } catch (final IOException error) {
            System.out.println("The output file was not found: " + error);
        }
        System.out.println("Writing to Files Took: "
                           + (System.currentTimeMillis() - startTime)
                           + " milliseconds.");
    }

    private String extendBinaryString(final String theString) {
        String newString = theString;
        if (theString.length() < myBitWidth) {
            newString = "0".repeat(myBitWidth - theString.length()) + theString;
        }
        return newString;
    }

    private static final class CodeNode implements Comparable<CodeNode> {

        /** The character or String of characters stored at this CodeNode. */
        private final String myText;

        /** The code stored for the value assigned to myText. */
        private String myCode;

        private CodeNode(final String theText, final String theCode) {
            super();
            myText = theText;
            myCode = theCode;
        }

        private CodeNode(final String theText) {
            super();
            myText = theText;
            myCode = null;
        }

        @Override
        public int compareTo(final CodeNode theOtherCodeNode) {
            return myText.compareTo(theOtherCodeNode.myText);
        }

        @Override
        public String toString() {
            return myText + ":" + myCode;
        }

        @Override
        public boolean equals(final Object theOtherObject) {
            if (this == theOtherObject) {
                return true;
            }
            if (theOtherObject == null || !theOtherObject.getClass().equals(this.getClass())) {
                return false;
            }
            final CodeNode otherCodeNode = (CodeNode) theOtherObject;
            return myText.equals(otherCodeNode.myText);
        }
    }
}