import java.util.Random;

/**
 * Controls the population of Genomes and generates new generations.
 * Class is final to prevent public method override.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public final class Population {

    /** A Random object used for rolling a chance to mutate. */
    private static final Random GENERATOR = new Random();

    /** The list of all current Genomes. */
    public final MyLinkedList<Genome> population;

    /** The Genome from population with the highest fitness. */
    public Genome mostFit;

    /** How many generations how been done. */
    public int generation;

    /** The max size of the population. */
    private int size;

    /** Creates a new population. */
    public Population(final int theSize) {
        super();
        size = theSize;
        population = new MyLinkedList<>();
        for (int i = 0; i < size; i++) {
            population.addBefore(new Genome());
        }
        population.first();
        mostFit = population.current();
    }

    /**
     * Generates a new generation.
     * It does this by deleting half the population with the lowest fitness,
     * then regenerating the population by using the remaining population
     * as possible parents, then mutating those regenerated Genomes.
     */
    public void nextGeneration() {
        population.first();
        final int removeSize = size / 2;
        for (int leastFit = 0; leastFit < removeSize; leastFit++) {
            population.remove();
        }
        final int regenAmount = size - population.size();
        for (int genomeToRegen = 0; genomeToRegen < regenAmount; genomeToRegen++) {
            population.first();
            final int randomSpot = GENERATOR.nextInt(population.size());
            for (int i = 0; i < randomSpot; i++) {
                population.next();
            }
            final Genome newGenome = new Genome(population.current());
            if (GENERATOR.nextBoolean()) {
                population.first();
                for (int i = 0; i < GENERATOR.nextInt(population.size()); i++) {
                    population.next();
                }
                newGenome.crossover(population.current());
                newGenome.mutate();
            } else {
                newGenome.mutate();
            }
            population.addBefore(new Genome(newGenome));
        }
        population.sort();
        for (int i = 0; i < population.size() - 1; i++) {
            population.next();
        }
        mostFit = population.current();
        population.first();
        generation++;
    }

    @Override
    public String toString() {
        return "Generation " + generation + " " + mostFit.toString();
    }

    public static void main(final String[] theArgs) {
        final Population pop  = new Population(100);
        while (pop.mostFit.fitness() < 0) {
            System.out.println("Generation " + pop.generation + " " + pop.mostFit);
            pop.nextGeneration();
        }
        System.out.println("Final Generation: " + pop.mostFit);
    }
}
