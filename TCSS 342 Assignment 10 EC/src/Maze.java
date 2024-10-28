import java.util.Random;

/**
 * A simple Maze generator that utilizes a 2D array as the underlying data structure.
 * @author Brandon Ragghianti (braggs03)
 * @version Winter 2024
 */
public class Maze {

    /** A value used to represent North. */
    private static final int NORTH = 0;

    /** A value used to represent East. */
    private static final int EAST = 1;

    /** A value used to represent South. */
    private static final int SOUTH = 2;

    /** A value used to represent West. */
    private static final int WEST = 3;

    /** A Random object used for shuffling directions. */
    private static final Random GENERATOR = new Random();

    /** The width of the maze. */
    private final int myWidth;

    /** The height of the maze. */
    private final int myHeight;

    /** The maze, it is represented as a 2D array of chars. */
    private final char[][] myMaze;

    /** Stores which position in the maze have been visited during creation. */
    private final boolean[][] myVisited;

    /**
     * Creates a maze to the desired height and width.
     * @param theWidth is the width of the maze.
     * @param theHeight is the height of the maze.
     */
    public Maze(final int theWidth, final int theHeight) {
        super();
        final long startTime = System.nanoTime();
        myWidth = theWidth;
        myHeight = theHeight;
        myMaze = new char[theHeight * 2 + 1][theWidth * 2 + 1];
        myVisited = new boolean[theHeight][theWidth];
        buildGraph();
        buildMaze(0, 0);
        solveMaze(0, 1);
        final long time = System.nanoTime() - startTime;
        System.out.println("Runtime of Maze is: "
                           + (time / 1000000)
                           + "."
                           + (time % 1000000)
                           + " milliseconds.");
    }

    private void buildGraph() {
        for (int column = 0; column < myMaze.length; column++) {
            for (int row = 0; row < myMaze[column].length; row++) {
                myMaze[column][row] = (row % 2 == 0 || column % 2 == 0) ? 'X' : ' ';
            }
        }
        myMaze[0][1] = '+';
        myMaze[myMaze.length - 1][myMaze[0].length - 2] = ' ';
    }

    private void buildMaze(final int theRow, final int theCol) {
        myVisited[theCol][theRow] = true;
        final int[] directions = new int[] {NORTH, EAST, SOUTH, WEST};
        shuffle(directions);
        for (final int currentDirection : directions) {
            int changedX = theRow;
            int changedY = theCol;
            switch (currentDirection) {
                case NORTH -> changedY--;
                case SOUTH -> changedY++;
                case WEST -> changedX--;
                case EAST -> changedX++;
                default -> throw new RuntimeException("Something is broken!");
            }
            if (changedY > -1 && changedY < myHeight
                && changedX > -1 && changedX < myWidth
                && !myVisited[changedY][changedX]) {
                myMaze[theCol * 2 + 1 + (changedY - theCol)]
                      [theRow * 2 + 1 + (changedX - theRow)]
                      = ' ';
                buildMaze(changedX, changedY);
            }
        }
    }

    private void shuffle(final int[] theDirections) {
        for (int i = 0; i < theDirections.length; i++) {
            final int k = GENERATOR.nextInt(theDirections.length);
            final int temp = theDirections[i];
            theDirections[i] = theDirections[k];
            theDirections[k] = temp;
        }
    }

    private boolean solveMaze(final int theRow, final int theCol) {
        boolean solved = false;
        myMaze[theRow][theCol] = theRow % 2 == 0 || theCol % 2 == 0 ? '+' : ' ';
        if (theRow == myMaze.length - 1 && myMaze[0].length - 2 == theCol) {
            solved = true;
        } else {
            final int[] directions = {NORTH, EAST, SOUTH, WEST};
            for (final int currentDirection : directions) {
                int changedX = theRow;
                int changedY = theCol;
                switch (currentDirection) {
                    case NORTH -> changedY--;
                    case SOUTH -> changedY++;
                    case WEST -> changedX--;
                    case EAST -> changedX++;
                    default -> throw new RuntimeException("Something is broken!");
                }
                if (canMove(myMaze, changedX, changedY) && !solved) {
                    solved = solveMaze(changedX, changedY);
                    if (!solved) {
                        myMaze[changedX][changedY] = ' ';
                    }
                }
            }
        }
        return solved;
    }

    private boolean canMove(final char[][] theMaze, final int theRow, final int theCol) {
        return theRow > -1 && theCol > -1 && theRow < theMaze.length
               && theCol < theMaze[0].length && theMaze[theRow][theCol] == ' ';
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final char[] row : myMaze) {
            for (final char column : row) {
                builder.append(column);
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    /**
     * Main method for demonstration.
     * @param theArgs The command-line arguments.
     */
    public static void main(final String[] theArgs) {
        final Maze maze = new Maze(10, 15);
        System.out.println(maze);
    }
}