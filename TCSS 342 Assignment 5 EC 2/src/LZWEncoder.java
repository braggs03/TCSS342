import java.io.FileNotFoundException;
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

    /** 2^myBitWidth, sets a limit for how many codes can be generated. */
    private final int myLimit;

    /** The input file passed to the constructor. */
    private final String myInputFile;

    /** The name of the generated output file. */
    private final String myOutputFile;

    /** The name of the generated codes file. */
    private final String myCodesFile;

    /** The bit width for the codes for each word. */
    private final int myBitWidth;

    /** A BookReader for the given input. */
    public final BookReader book;

    /**
     * A MyBinarySearchTree that stores the codes assigned
     * to each character or String of characters.
     */
    private final MyBinarySearchTree<CodeNode> myCodes;

    /** The encoded binary String. */
    public byte[] encodedText;

    /** Constructor used to instantiate a new LZWEncoder to the text "War And Peace". */
    public LZWEncoder() {
        this("WarAndPeace.txt", 18);
    }

    /** Constructor used to instantiate a new LZWEncoder to the passed input file. */
    public LZWEncoder(final String theInputFile, final int theBitWidth) {
        super();
        final long startTime = System.currentTimeMillis();
        myInputFile = theInputFile;
        myOutputFile = theInputFile.substring(0, theInputFile.indexOf('.'))
                + "-compressed.bin";
        myCodesFile = theInputFile.substring(0, theInputFile.indexOf('.'))
                + "-codes.txt";
        myBitWidth = theBitWidth;
        myLimit = (int) Math.pow(2, theBitWidth);
        final long startTime2 = System.currentTimeMillis();
        book = new BookReader(myInputFile);
        System.out.println("Reading and parsing book in: "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
        myCodes = new MyBinarySearchTree<>();
        initCodes();
        encode();
        writeFiles();
        System.out.println("Encoder took: "
                           + (System.currentTimeMillis() - startTime)
                           + " milliseconds.");
    }

    private void initCodes() {
        final long startTime = System.currentTimeMillis();
        for (int i = 0; i < book.book.length(); i++) {
            final CodeNode temp = new CodeNode(String.valueOf(book.book.charAt(i)),
                                  extendBinaryString(Integer.toBinaryString(myCodes.size())));
            final CodeNode node = myCodes.find(temp);
            if (node == null) {
                myCodes.add(temp);
            }
        }
        try {
            final PrintStream printer = new PrintStream(myCodesFile);
            printer.print(myCodes.toString());
            printer.close();
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Initializing codes in: "
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
            final CodeNode temp = new CodeNode(
                                  currentText.append(book.book.charAt(n)).toString());
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
        encodedText = new byte[binaryString.length() / Byte.SIZE];
        for (int i = 0; i < binaryString.length(); i += Byte.SIZE) {
            encodedText[i / Byte.SIZE] = (byte) Integer.parseInt(
                                          binaryString.substring(i, i + Byte.SIZE), 2);
        }
        System.out.println("Encoding text in "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
    }

    private void writeFiles() {
        final long startTime = System.currentTimeMillis();
        final PrintStream printer;
        try {
            Files.write(Path.of(myOutputFile), encodedText);
        } catch (final IOException error) {
            System.out.println("The output file was not found: " + error);
        }
        System.out.println("Writing to file in: "
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
