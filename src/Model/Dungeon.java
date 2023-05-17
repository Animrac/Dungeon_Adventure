package src.Model;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class Dungeon implements Serializable {
    /**
     * Default rows for the dungeon maze.
     */
    private static int dungeonRows;

    /**
     * Default columns for the dungeon maze.
     */
    private static int dungeonColumns;

    /**
     * This boolean tells us if the dungeon can be successfully traversed or not.
     */
    private static boolean itTraversed;

    /**
     * The dungeon row the player will start in.
     */
    private static int startRow = -1;

    /**
     * The dungeon column the player will start in.
     */
    private static int startCol = -1;

    /**
     * 2d array of the dungeon layout. This does not contain the rooms.
     */
    final private char[][] myDungeonLayout;

    /**
     * 2d array of the dungeon with Room objects.
     */
    private Room[][] myDungeonRooms;


    /**
     * Percentage that walls appear.
     */
    final private static int PERCENTAGE_OF_WALLS = 45;


    /**
     * The constructor for a default dungeon, which have the dimensions of 12 x 12.
     */
    public Dungeon() {
        this.dungeonRows = 12;
        this.dungeonColumns = 12;
        this.myDungeonLayout = new char[dungeonRows][dungeonColumns];
        this.myDungeonRooms = new Room[dungeonRows][dungeonColumns];
        makeDungeon();
        addRooms();
    }

    /**
     * The constructor for a custom dungeon, which you can input the custom rows and column dimensions.
     */
    public Dungeon(final int theRows, final int theCols) {
        this.dungeonRows = theRows;
        this.dungeonColumns = theCols;
        this.myDungeonLayout = new char[theRows][theCols];
        this.myDungeonRooms = new Room[dungeonRows][dungeonColumns];
        makeDungeon();
        addRooms();
    }

    /**
     * Randomly fills empty space with walls, the four pillars, the start, and the exit.
     */
    private void makeDungeon() {

        int pillarCount = 0; // To keep track that there will be four pillars.

        boolean isFourPillars = false, isExit = false, isStart = false;

        for (int i = 0; i < this.dungeonRows; i++) {
            for (int j = 0; j < this.dungeonColumns; j++) {

                int randomNum = ThreadLocalRandom.current().nextInt(1, 100 + 1);

                //add wall
                if (i == 0 || i == this.dungeonRows - 1 || j == 0 || j == this.dungeonColumns - 1
                        || randomNum <= PERCENTAGE_OF_WALLS) {
                    this.myDungeonLayout[i][j] = 'X';
                }
                //add empty room
                else {
                    this.myDungeonLayout[i][j] = 'O';
                }
            }
        }

        do {
            int randomRow = ThreadLocalRandom.current().nextInt(1, dungeonRows - 2 + 1); //-2 for the walls, +1 for the boundary
            int randomCol = ThreadLocalRandom.current().nextInt(1,  dungeonColumns - 2 + 1);

            if (!(isFourPillars) && this.myDungeonLayout[randomRow][randomCol] == 'O') {
                this.myDungeonLayout[randomRow][randomCol] = 'P';
                pillarCount++;
                if (pillarCount == 4) {
                    isFourPillars = true;
                }
            } else if (!isStart && this.myDungeonLayout[randomRow][randomCol] == 'O') {
                this.myDungeonLayout[randomRow][randomCol] = 'S';
                this.startRow = randomRow;
                this.startCol = randomCol;
                isStart = true;
            } else if (!isExit && this.myDungeonLayout[randomRow][randomCol] == 'O') {
                this.myDungeonLayout[randomRow][randomCol] = 'E';
                isExit = true;
            }
        } while (!(isFourPillars && isExit && isStart));

        ensureTraversable();

    }

    /**
     * Creates a new dungeon if the current dungeon is not traversable.
     */
    private void ensureTraversable() {

        char[][] tempDungeon = new char[0][];
        boolean firstRun = true;

        while (!(this.isItTraversable())) {

            if (!firstRun){
                makeDungeon();
            }

            firstRun = false;

            tempDungeon = new char[this.dungeonRows][this.dungeonColumns]; //to keep the original dungeon

            for (int i = 0; i < this.dungeonRows; i++) {
                for (int j = 0; j < this.dungeonColumns; j++) {
                    tempDungeon[i][j] = this.myDungeonLayout[i][j];
                }
            }

            isTraversable(tempDungeon, this.startRow, this.startCol, 0, false);
        }

    }

    /**
     * Ensures that the player can traverse and access the four pillars, the start, and the exit.
     * @param theDungeon The dungeon being checked.
     * @param theCurrRow The current row being checked.
     * @param theCurrCol The current column being checked.
     * @param theTouchPillars How many pillars can be accessed.
     * @param theTouchExit If the exit can be accessed.
     */
    public static void isTraversable(char[][] theDungeon, int theCurrRow, int theCurrCol,
                                         int theTouchPillars, boolean theTouchExit) {

        if (theDungeon[theCurrRow - 1][theCurrCol] != 'X'){ //Look West.

            if (theDungeon[theCurrRow - 1][theCurrCol] == 'P') {
                theTouchPillars++;
            }
            else if (theDungeon[theCurrRow - 1][theCurrCol] == 'E') {
                theTouchExit = true;
            }

            theDungeon[theCurrRow - 1][theCurrCol] = 'X'; //We don't need to look at it anymore.

            isTraversable(theDungeon, theCurrRow - 1, theCurrCol, theTouchPillars, theTouchExit);

        }

        if (theDungeon[theCurrRow][theCurrCol - 1] != 'X'){ //Look North.

            if (theDungeon[theCurrRow][theCurrCol - 1] == 'P') {
                theTouchPillars++;
            }
            else if (theDungeon[theCurrRow][theCurrCol - 1] == 'E') {
                theTouchExit = true;
            }

            theDungeon[theCurrRow][theCurrCol - 1] = 'X'; //We don't need to look at it anymore.

            isTraversable(theDungeon, theCurrRow, theCurrCol - 1, theTouchPillars, theTouchExit);

        }

        if (theDungeon[theCurrRow + 1][theCurrCol] != 'X'){ //Look East.

            if (theDungeon[theCurrRow + 1][theCurrCol] == 'P') {
                theTouchPillars++;
            }
            else if (theDungeon[theCurrRow + 1][theCurrCol] == 'E') {
                theTouchExit = true;
            }

            theDungeon[theCurrRow + 1][theCurrCol] = 'X'; //We don't need to look at it anymore.

            isTraversable(theDungeon, theCurrRow + 1, theCurrCol, theTouchPillars, theTouchExit);

        }

        if (theDungeon[theCurrRow][theCurrCol + 1] != 'X') { //Look South.

            if (theDungeon[theCurrRow][theCurrCol + 1] == 'P') {
                theTouchPillars++;
            }
            else if (theDungeon[theCurrRow][theCurrCol + 1] == 'E') {
                theTouchExit = true;
            }

            theDungeon[theCurrRow][theCurrCol + 1] = 'X'; //We don't need to look at it anymore.
            
            isTraversable(theDungeon, theCurrRow, theCurrCol + 1, theTouchPillars, theTouchExit);

        }

        if (theTouchPillars == 4 && theTouchExit) {
            itTraversed = true;
        }

    }

    public void addRooms(){
        for (int i = 0; i < myDungeonLayout.length; i++) {
            for (int j = 0; j < myDungeonLayout[i].length; j++) {
                //I don't know if these coordinates (i, j) or (j, i) should be swapped. Seems to work for now.
                myDungeonRooms[i][j] = new Room(myDungeonLayout, i, j, myDungeonLayout[i][j]);
            }
        }
    }

    /* Getters and setters */

    public boolean isItTraversable() {
        return itTraversed;
    }

    public char[][] getMyDungeonLayout() {
        return myDungeonLayout;
    }

    public Room[][] getMyDungeonRooms() {
        return myDungeonRooms;
    }

    public int getPercentageOfWalls() {
        return PERCENTAGE_OF_WALLS;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int theStartRow) {
        startRow = theStartRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartCol(int theStartCol) {
        startCol = theStartCol;
    }

}


