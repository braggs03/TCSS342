import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Decodes an encoded text file using LZW decompression.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class LZWDecoder {

    /** The name of the compressed file. */
    private final String myCompressedFileName;

    /** The name of the codes file. */
    private final String myCodesFileName;

    /** The name of the output file. */
    private final String myOutputFileName;

    /** The bit width of the codes. */
    private final int myBitWidth;

    /** The value of 2^myBitWidth, a limit so the codes don't overflow. */
    private final int myLimit;

    /** The encoded text to be decoded. */
    private byte[] myEncodedText;

    /** The string of codes from myCodesFileName. */
    private String myCodesString;

    /** The list of CodeNodes. */
    private MyOrderedList<CodeNode> myCodes;

    /** The list of parsed encoded text. */
    private MyOrderedList<String> myParsedEncodedText;

    /** The BookReader that hold the uncompressed text of the book. */
    public String book;

    /** Instantiates a new LZWEncoder Object to WarAndPeace.txt. */
    public LZWDecoder() {
        this("WarAndPeace-compressed.bin", "WarAndPeace-codes.txt", 18);
    }

    /**
     * Instantiates a new LZWEncoder Object based the input file.
     * @param theInputFile The name of the input file.
     * @param theCodesFile The name of the codes file.
     */
    public LZWDecoder(final String theInputFile,
                      final String theCodesFile,
                      final int theBitWidth) {
        super();
        final long startTime = System.currentTimeMillis();
        myCompressedFileName = theInputFile;
        myCodesFileName = theCodesFile;
        myBitWidth = theBitWidth;
        myLimit = (int) Math.pow(2, myBitWidth);
        myOutputFileName = theInputFile.substring(
                           0, theInputFile.indexOf('-') + 1) + "Decompressed" + ".txt";
        readFiles();
        buildCodes();
        rebuildText();
        writeFiles();
        System.out.println("Decoder took: "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
    }

    private void readFiles() {
        final long startTime = System.currentTimeMillis();
        try {
            myEncodedText = Files.readAllBytes(Path.of(myCompressedFileName));
            myCodesString = Files.readString(Path.of(myCodesFileName));
        } catch (final IOException e) {
            System.out.println("The input file could not be found: " + e);
        }
        System.out.println("Read in files in: "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
    }

    private void buildCodes() {
        final long startTime = System.currentTimeMillis();
        myCodes = new MyOrderedList<>();
        int currentPosition = 1;
        while (currentPosition != -1) {
            final String codeText = myCodesString.substring(
                                    currentPosition, currentPosition + 1);
            currentPosition += 2;
            final String code = myCodesString.substring(
                                currentPosition, currentPosition + myBitWidth);
            final int commaPosition = myCodesString.indexOf(",", currentPosition);
            // If comma is not found, this is the last code.
            currentPosition = commaPosition == -1 ? -1 : commaPosition + 2;
            myCodes.add(new CodeNode(codeText, code));
        }
        System.out.println("Built codes in: "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
    }

    private void rebuildText() {
        final long startTime = System.currentTimeMillis();
        final StringBuilder builder = new StringBuilder();
        for (final byte b : myEncodedText) {
            builder.append(extendBinaryString(String.format(
                "%8s", Integer.toBinaryString(b & 0xFF)).
                replace(' ', '0'), Byte.SIZE));
        }
        final StringBuilder decodedText = new StringBuilder();
        final int rangeOfCodes = builder.length() - (builder.length() % myBitWidth);
        CodeNode currentCode;
        CodeNode previousCode = myCodes.binarySearch(new CodeNode(
                                                     builder.substring(0, myBitWidth)));
        for (int i = myBitWidth; i < rangeOfCodes; i += myBitWidth) {
            currentCode = myCodes.binarySearch(new CodeNode(
                                               builder.substring(i, i + myBitWidth)));
            if (currentCode == null && myCodes.size() < myLimit) {
                currentCode = new CodeNode(previousCode.myText
                                           + previousCode.myText.charAt(0),
                                           extendBinaryString(
                                           Integer.toBinaryString(
                                           myCodes.size()), myBitWidth));
                myCodes.add(currentCode);
            } else if (myCodes.size() < myLimit) {
                myCodes.add(new CodeNode(previousCode.myText
                                         + currentCode.myText.charAt(0),
                                         extendBinaryString(
                                         Integer.toBinaryString(
                                         myCodes.size()), myBitWidth)));
            }
            decodedText.append(previousCode.myText);
            previousCode = currentCode;
        }
        decodedText.append("\n");
        book = decodedText.toString();
        System.out.println("Decoding text in "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
    }

    private String extendBinaryString(final String theString, final int theLength) {
        String newString = theString;
        if (theString.length() < theLength) {
            newString = "0".repeat(theLength - theString.length()) + theString;
        }
        return newString;
    }

    private void writeFiles() {
        final long startTime = System.currentTimeMillis();
        try {
            final PrintStream printer = new PrintStream(myOutputFileName);
            printer.print(book);
            printer.close();
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Writing to files in: "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
    }

    private static final class CodeNode implements Comparable<CodeNode> {

        /** The character or String of characters stored at this CodeNode. */
        private final String myText;

        /** The code stored for the value assigned to myText. */
        private final String myCode;

        private CodeNode(final String theText, final String theCode) {
            super();
            myText = theText;
            myCode = theCode;
        }

        private CodeNode(final String theCode) {
            super();
            myText = null;
            myCode = theCode;
        }

        @Override
        public int compareTo(final CodeNode theOtherCodeNode) {
            return myCode.compareTo(theOtherCodeNode.myCode);
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
            return myCode.equals(otherCodeNode.myCode);
        }
    }
}
