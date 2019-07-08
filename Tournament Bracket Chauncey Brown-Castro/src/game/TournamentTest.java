package game;

/**
 * This is the driver for my Tournament class, and it sets up a tournament,
 * and goes through round by round until there is only one victor
 * Russian Roulette is a cruel game.
 *
 * @author Chauncey Brown-Castro
 * @version 1.0
 */
public class TournamentTest {

    private static final String SM_LIST = "name_files/names - small.txt";
    private static final String MED_LIST = "name_files/names - medium.txt";
    private static final String LRG_LIST = "name_files/names - large.txt";

    /**
     * This is the entry point into my application
     * @param args command line arguments
     */
    public static void main(String[] args) {

        //LOOK THROUGH THE SMALL LIST

        Tournament game = new Tournament(SM_LIST);

        game.createTree();

        game.determineWinners();

        game.printRounds();

        System.out.println();

        //LOOK THROUGH THE MEDIUM LIST

        game = new Tournament(MED_LIST);

        game.createTree();

        game.determineWinners();

        game.printRounds();

        //LOOK THROUGH THE LARGE LIST

        game = new Tournament(LRG_LIST);

        game.createTree();

        game.determineWinners();

        game.printRounds();
    }
}