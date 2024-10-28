import java.util.Random;

/**
 * A list of character that tries to mutate to become NAME.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public final class Genome implements Comparable<Genome> {

    /** The mutation target. */
    private static final String NAME = "CHRISTOPHER PAUL MARRIOTT";

    /** A Random object used for rolling a chance to mutate. */
    private static final Random GENERATOR = new Random();

    /** The chance a gene can mutate, range is (0, 1]. */
    private static final double MUTATION_RATE = 0.05;

    /** The list of valid chars, all character in NAME must be in this list. */
    private static final char[] VALID_CHARS = {'A', 'B', 'C', 'D', 'E',
                                               'F', 'G', 'H', 'I', 'J',
                                               'K', 'L', 'M', 'N', 'O',
                                               'P', 'Q', 'R', 'S', 'T',
                                               'U', 'V', 'W', 'X', 'Y',
                                               'Z', ' ', '-', '’'};

    /** The genes of this Genome. The genes are the current mutation of its name. */
    protected MyLinkedList<Character> myGenes;

    /** The genes of the target name. */
    private MyLinkedList<Character> myTarget;

    /** The chance a gene can mutate, range is (0, 1]. */
    private double mutationRate;

    /** Creates a new Genome. */
    public Genome() {
        super();
        myGenes = new MyLinkedList<>();
        initializeMyTarget();
        mutationRate = 0.05;
    }

    /**
     * Creates a Genome copy from the passed Genome.
     * @param theGenome The Genome to be copied.
     */
    public Genome(final Genome theGenome) {
        super();
        myGenes = new MyLinkedList<>();
        theGenome.myGenes.first();
        for (int i = 0; i < theGenome.myGenes.size(); i++) {
            myGenes.addBefore(theGenome.myGenes.current());
            theGenome.myGenes.next();
        }
        initializeMyTarget();
    }

    private void initializeMyTarget() {
        myTarget = new MyLinkedList<>();
        for (int i = 0; i < NAME.length(); i++) {
            myTarget.addBefore(NAME.charAt(i));
        }
    }

    public void mutate() {
        if (GENERATOR.nextDouble() <= MUTATION_RATE) {
            final char newChar = VALID_CHARS[GENERATOR.nextInt(VALID_CHARS.length)];
            final int randomPos = GENERATOR.nextInt(myGenes.size() + 1); // There may be a bug.
            myGenes.first();
            for (int i = 0; i < randomPos; i++) {
                myGenes.next();
            }
            myGenes.addBefore(newChar);
        }
        if (!myGenes.isEmpty() && GENERATOR.nextDouble() <= MUTATION_RATE) {
            myGenes.first();
            final int randomPos = GENERATOR.nextInt(myGenes.size() + 1);
            for (int i = 0; i < randomPos; i++) {
                myGenes.next();
            }
            myGenes.remove();
        }
        myGenes.first();
        for (int i = 0; i < myGenes.size(); i++) {
            if (GENERATOR.nextDouble() <= MUTATION_RATE) {
                myGenes.remove();
                myGenes.addBefore(VALID_CHARS[GENERATOR.nextInt(VALID_CHARS.length)]);
            }
            myGenes.next();
        }
    }

    public void crossover(final Genome theParent) {
        final MyLinkedList<Character> newGenes = new MyLinkedList<>();
        myGenes.first();
        theParent.myGenes.first();
        for (int i = 0; i < Math.min(this.myGenes.size(), theParent.myGenes.size()); i++) {
            newGenes.addBefore(GENERATOR.nextBoolean() ? myGenes.current() : theParent.myGenes.current());
            myGenes.next();
            theParent.myGenes.next();
        }
        myGenes = newGenes;
    }

    /**
     * Calculates the fitness of this Genome. It does this by comparing the size of
     * myGenes and myTarget, and comparing the similar characters in myGenes to myTarget.
     * @return The fitness of this Genome.
     */
    public int fitness() {
        final int lengthDifference = Math.abs(myGenes.size() - myTarget.size());
        int incorrectChars = lengthDifference;
        myGenes.first();
        myTarget.first();
        for (int i = 0; i < Math.min(myGenes.size(), myTarget.size()); i++) {
            if (!(myTarget.current() == myGenes.current())) {
                incorrectChars++;
            }
            myGenes.next();
            myTarget.next();
        }
        return -(lengthDifference + incorrectChars);
    }

    @Override
    public String toString() {
        myGenes.first();
        final StringBuilder builder = new StringBuilder();
        builder.append('(');
        for (int i = 0; i < myGenes.size(); i++) {
            builder.append(myGenes.current());
            myGenes.next();
        }
        return builder.append(',').append(' ').append(fitness()).append(')').toString();
    }

    @Override
    public boolean equals(final Object theObject) {
        if (this == theObject) {
            return true;
        }
        if (theObject == null || !theObject.getClass().equals(this.getClass())) {

            return false;
        }
        final Genome otherGenome = (Genome) theObject;
        return this.fitness() == otherGenome.fitness();
    }

    @Override
    public int compareTo(final Genome theOtherGenome) {
        return this.fitness() - theOtherGenome.fitness();
    }
}
