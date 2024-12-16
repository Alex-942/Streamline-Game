/** your file header here */
/*
 * File Header
 * Author: Alejandro Marquez
 * email: a1marque@ucsd.edu
 * references: tutor hours
 * Date: 2/6/2019
 *
 * This file is meant to recreate our own version of the streamline game. 
 * To accomplish this task this file contains methods to create the gameboard
 * and allow the user to move the charcter.
 */

import java.util.*;

/** your class header here */
/*
 * Class Header
 * The purpose of the class is to create the game board and methods that will
 * add obstacles to the board and enable charcter movement the board. Along
 * with the methods for the actual game, there are method that display the
 * board and perform some other functionalities. 
 */
public class GameState {

    // Used to populate char[][] board below and to display the
    // current state of play.
    final static char TRAIL_CHAR = '.';
    final static char OBSTACLE_CHAR = 'X';
    final static char SPACE_CHAR = ' ';
    final static char CURRENT_CHAR = 'O';
    final static char GOAL_CHAR = '@';
    final static char NEWLINE_CHAR = '\n';
    private static final char HORIZONTAL = '-';
    private static final char VERTICAL = '|';
    private static final int MULTIPLIER_2 = 2;
    private static final int SUBTRACTOR_2 = 2;
    private static final int ADDER_3 = 3;
    
    // This represents a 2D map of the board
    char[][] board;

    // Location of the player
    int playerRow;
    int playerCol;

    // Location of the goal
    int goalRow;
    int goalCol;

    // true means the player completed this level
    boolean levelPassed;

    // initialize a board of given parameters, fill the board with SPACE_CHAR
    // set corresponding fields to parameters.
    
    /*
     * Constructor Header
     * @param height the number of rows in the game board
     * @param width the number of columns in the game board
     * @param playerRow index row of the player
     * @param playerCol index column of the player
     * @param goalRow index row of the goal
     * @param goalCol index column of the goal
     * @return none
     * This constructor creates a new GameState object with the parameters
     */
    public GameState(int height, int width, int playerRow, int playerCol, 
                     int goalRow, int goalCol) {
        // TODO
        // Initalize the instance variables
        this.levelPassed = false;
        this.playerRow = playerRow;
        this.playerCol = playerCol;
        this.goalRow = goalRow;
        this.goalCol = goalCol;
        this.board = new char[height][width];
        // for loop that fills this.board with space chars
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                this.board[row][col] = SPACE_CHAR;
            }
        }
    }

    // copy constructor
    /*
     * Constructor Header
     * This constructor is performs essentially the same function as the 
     * previous method but instead of using parameters to intialize the 
     * instance variables this constructor takes information from the 
     * GameState parameter to intialize the instance variables
     */
    public GameState(GameState other) {
        // TODO
        // Initalize the instacne variables
        this.levelPassed = other.levelPassed;
        this.playerRow = other.playerRow;
        this.playerCol = other.playerCol;
        this.goalRow = other.goalRow;
        this.goalCol = other.goalCol;
        this.board = new char[other.board.length][other.board[0].length];
        //fill this.board with the contents of other.board
        for(int row = 0; row < other.board.length; row++){
            for(int col = 0; col < other.board[0].length; col++){
                this.board[row][col] = other.board[row][col];
            }
        }
    }


    // add count random blocks into this.board
    // avoiding player position and goal position
    /*
     * Method Header
     * This method adds count number of obstacles to add onto the gameboard
     * @param count the amount of obstacles to add onto the gameboard
     * @return none
     */
    void addRandomObstacles(int count) {
        // TODO
        int height = this.board.length;
        int width = this.board[0].length;
        //if statement the ensures the the number of obstacles is a valid input
        int area = width * height;
        if(count > area - SUBTRACTOR_2 || count < 0){
            return;
        }
        //random numbers for the location of where to place the obstacles
        Random row_place = new Random();
        Random col_place = new Random();
        //the following loop adds obstacles to this.board as long as
        //the random location is not as the same location as the player or
        //the goal. When the number of obstacles has been added end the loop
        while(count > 0){
            int Random_row = row_place.nextInt(height);
            int Random_col = col_place.nextInt(width);
            if((Random_row != this.playerRow
                        || Random_col != this.playerCol)
                    && (Random_row != this.goalRow 
                        || Random_col != this.goalCol) 
                    && count > 0 
                    && (this.board[Random_row][Random_col]
                        != OBSTACLE_CHAR)){
                    this.board[Random_row][Random_col] = OBSTACLE_CHAR;
                    count = count - 1;
                }
        }
        

    }


    // rotate clockwise once
    // rotation should account for all instance var including board, current 
    // position, goal position
    /*
     * Method Header
     * rotate the board Clockwise once
     * @param none
     * @return none
     */
    void rotateClockwise() {
        // TODO
        // The follwoing the 8 lines of code makes a copy of the current board
        int height = this.board.length;
        int width = this.board[0].length;
        char[][] result = new char[height][width];
        for(int row2 = 0; row2 < height; row2++){
            for(int col2 = 0; col2 < width; col2++){
                result[row2][col2] = this.board[row2][col2];
            }
        }
        //the following statements rotate the copied board and stores the 
        //result into this.board
        board = new char[width][height];
        for(int row = 0; row < width; row++){
            for(int col = 0; col < height; col++){
                this.board[row][col] = result[height-col-1][row];
            }
        }
        //the following variables updates the location of the player
        //and the goal after the rotation NOTE it doesnt change them it adjusts
        //them after the rotation.
        int oldRow = playerRow;
        playerRow = playerCol;
        playerCol = height - oldRow - 1;
        oldRow = goalRow;
        goalRow = goalCol;
        goalCol = height - oldRow - 1;

    }


    // move current position towards right until stopped by obstacle / edge
    // leave a trail of dots for all positions that we're walked through
    // before stopping
    /*
     * Method Header
     * Move the player right until player hits the end of the board or obstacle
     * @param none
     * @return none
     */
    void moveRight() {
        // TODO
        // while loop moves player right until it hits an obstacle or 
        // end of the board
        while((this.playerCol < this.board[0].length - 1)
                && (this.board[this.playerRow][this.playerCol + 1] 
                    == SPACE_CHAR)){
            // updates the player location and leave a trail
            this.board[playerRow][playerCol] = TRAIL_CHAR;
            this.playerCol = this.playerCol + 1;
            // if player reached the goal end loop and dont move 
            if(this.playerCol  == this.goalCol && this.playerRow 
                    == this.goalRow){
                this.levelPassed = true;
                return;
             }
        }

    }


    // move towards any direction given
    // accomplish this by rotating, move right, rotating back
    /*
     * Method Header 
     * This method uses the moveRight and rotate methods to allow the player 
     * to move in any given direction
     * @param direction the direction to move the player in
     * @return none
     */
    void move(Direction direction) {
        // TODO
        // does not move player if direction is null
        if(direction == null){
            return;
        }
        //loop rotates board acorrding to the associated number of rotations
        //found in the direction file then moveright 
        int rotations_left = direction.getRotationCount();
        for(int rotated = 0; rotated <= rotations_left; rotations_left--){
            if(rotations_left == 0){
                break;
            }
            this.rotateClockwise();
        }
        this.moveRight();
        //the following if statements re rotate the board to revert it back to
        //its original orientation
        if(direction == Direction.UP){
            this.rotateClockwise();
            this.rotateClockwise();
            this.rotateClockwise();
        }
        if(direction == Direction.LEFT){
            this.rotateClockwise();
            this.rotateClockwise();
        }
        if(direction == Direction.DOWN){
            this.rotateClockwise();
        }
    }


    @Override
    // compare two game state objects, returns true if all fields match
    /*
     * Method Header
     * This methods compares to gameState objects and returns true if all
     * fields match
     * @param other the object to compare with another GameState object
     * @return boolean indicating of the two objects are equal or not 
     */
    public boolean equals(Object other) {

        // TODO: check for any conditions that should return false

        // We have exhausted all possibility of mismatch, they're identical
        // if statements check if every instance variable of the two GameStates
        // are equal to one another
        if(other instanceof GameState){
            if(this.playerRow == ((GameState)other).playerRow){
                if(this.playerCol == ((GameState)other).playerCol){
                    if(this.goalRow == ((GameState)other).goalRow){
                        if(this.goalCol == ((GameState)other).goalCol){
                            if(this.levelPassed 
                                    == ((GameState)other).levelPassed){
                                if((((GameState)other).board != null 
                                            && this.board != null)){
                                    if(this.board.length 
                                        == ((GameState)other).board.length 
                                        && this.board[0].length 
                                        == ((GameState)other).board[0].length){
                                        for(int row = 0; row
                                                < this.board.length; row++){
                                            for(int col = 0; col
                                                    < this.board[0]
                                                    .length; col++){
                                                if(this.board[row][col]
                                                        != ((GameState)other)
                                                        .board[row][col]){
                                                    return false;
                                                }
                                            }
                                        }
                                    return true;
                                    }
                                }
                            return false;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    @Override
    /*
     * Method Header
     * This method prints out a visualization of the gameboard which includes
     * the player, goal, obstacles, etc.
     * @param none
     * @return the String visualization of the gameboard
     */
    public String toString() {
        // TODO
        int numCol = board[0].length;
        int height = board.length;
        StringBuilder visual = new StringBuilder();
        //for loop that creates the upper horizontal border of the game board
        for(int upper = 0; upper < (MULTIPLIER_2 * numCol + ADDER_3); upper++){
            visual = visual.append(HORIZONTAL);
            if(upper == (MULTIPLIER_2 * numCol + ADDER_3) - 1){
                visual = visual.append(NEWLINE_CHAR);
            }
        }
        //outer for loop appends starting border of the board for every row 
        for(int gameRow = 0; gameRow < height; gameRow++){
            if(gameRow != 0){
                visual.append(NEWLINE_CHAR);
            }
            visual = visual.append(VERTICAL);
            visual = visual.append(SPACE_CHAR);
            //inner for loop appends the contents of each row and column
            //includes goal, player, obtsacles, trail, etc.
            for(int gameCol = 0; gameCol < numCol; gameCol++){
                if(gameCol == playerCol && gameRow == playerRow){
                    visual = visual.append(CURRENT_CHAR);
                    visual = visual.append(SPACE_CHAR);
                }
                else if(gameCol == goalCol && gameRow == goalRow){
                    visual = visual.append(GOAL_CHAR);
                    visual = visual.append(SPACE_CHAR);
                }
                else if((gameCol != goalCol || gameRow != goalRow) 
                        && (gameCol != playerCol || gameRow != playerRow)){
                    visual = visual.append(board[gameRow][gameCol]);
                    visual = visual.append(SPACE_CHAR);

                }
                //encloses the contents of the row with the border
                if(gameCol == numCol - 1){
                    visual = visual.append(VERTICAL);
                }
            }
        }
        visual = visual.append(NEWLINE_CHAR);
        //for loop creates the bottom border of the board game
        for(int lower = 0; lower < (MULTIPLIER_2 * numCol + ADDER_3); lower++){
            visual = visual.append(HORIZONTAL);
        }
        visual = visual.append(NEWLINE_CHAR);
        return visual.toString(); 

    }
    /*
     * Method Header
     * Main method that provides a testing area for the methods writtern above
     * @param args 
     * @return print statements that will aid in testing methods for errors
     */
    /*public static void main(String[] args){
        //create two GameState objects to call methods on
        GameState gamer = new GameState(3,3,0,0,0,1);
        GameState gamerV2 = new GameState(6,5,5,0,0,4);
        //Tests Equals method
        System.out.println("testing Equals Method");
        boolean EQUALS = gamerV2.equals(gamer);
        System.out.println(EQUALS);
        //Tets toString method
        String testToString = gamer.toString();
        System.out.println("testing ToString Method");
        System.out.println(testToString);
        //Test addRandomObstacles method
        System.out.println("Adding Obstacles");
        gamer.addRandomObstacles(7);
        String gamer2 = gamer.toString();
        System.out.println(gamer2);
        //Tests rotateClockwise method
        System.out.println("Roatating Board");
        gamer.rotateClockwise();
        String gamer3 = gamer.toString();
        System.out.println(gamer3);
        //Tests moveRight method
        System.out.println("Moving Right");
        gamer.moveRight();
        String gamer4 = gamer.toString();
        System.out.println(gamer4);
        //Tests move method
        System.out.println("Moving");
        gamerV2.move(Direction.UP);
        String gamer5 = gamerV2.toString();
        System.out.println(gamer5);
        //Tests equals method again with expected true output
        System.out.println("testing ToString Method");
        boolean TESTING_EQUALS = gamerV2.equals(gamerV2);
        System.out.println(TESTING_EQUALS);

    } */
}
