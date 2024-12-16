/** your file header here */
/*
 * File Header 
 * Author: Alejandro Marquez
 * email: a1marque@ucsd.edu
 * references: tutor hours
 * Date: 2/12/19
 *
 * This file is meant to complete the implementations neccessary for the 
 * Streamline game. This file deals more with the users input when starting and
 * playing a new Streamline game as well as saving and loading Streamline games
 */

import java.util.*;
import java.io.*;

/** your class header here */
/*
 * Class Header
 * This class implements methods the user can use in order to complete the 
 * Streamline game. Two important instance variables are currentState and
 * previousStates that are used to display the correct verisons of the
 * gameboard while the user is playing the Streamline game
 */
public class Streamline {
    // Used to for the cases in play method as well as initalizing 
    // currentState and previousStates
    final static int DEFAULT_HEIGHT = 6;
    final static int DEFAULT_WIDTH = 5;
    private static final int DEFAULT_OBSTACLES = 3;
    private static final String MOVE_UP = "w";
    private static final String MOVE_LEFT = "a";
    private static final String MOVE_DOWN= "s";
    private static final String MOVE_RIGHT = "d";
    private static final String UNDO = "u";
    private static final String SAVE = "o";
    private static final String EXIT = "q";
    private static final String POINTER = ">";
    private static final String SPACE = " ";
    private static final String PASSED = "Level Passed!";
    private static final String SAVED = "Save current state to: saved_streamline_game";

    final static String OUTFILE_NAME = "saved_streamline_game";
    //GameState we reference in the methods we use
    GameState currentState;
    //List of all game changing moves made by the player
    List<GameState> previousStates;

    /*
     * Constructor Header
     * This constructor initalizes currentState's board by creating a new 
     * GameState object by passing parameters.It also initalizes previousStates
     * to a new empty GameState arrayList
     * @param none 
     * @return none
     */
    public Streamline() {
        // TODO
        // initalize currentState and adds the obstacles
        this.currentState = new GameState(DEFAULT_HEIGHT, DEFAULT_WIDTH,
                DEFAULT_HEIGHT - 1, 0, 0, DEFAULT_WIDTH - 1);
        currentState.addRandomObstacles(DEFAULT_OBSTACLES);
        // initalize previousStates
        previousStates = new ArrayList <GameState> ();
    }
    public Streamline(String filename) {
        try {
            loadFromFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Method Header
     * This method reads a file and collects the data in the file to load a 
     * prexisting Streamline game based on the contents of the file
     * @param filename the file to read 
     * @return none
     */
    protected void loadFromFile(String filename) throws IOException {
        // TODO
        Scanner load = new Scanner(new File(filename));
        //The following variables are the contents of the file we loaded in
        int vertical = 0;
        int horizontal = 0;
        int playerRow = 0;
        int playerCol = 0;
        int goalRow = 0;
        int goalCol = 0;
        while(load.hasNext()){
            //set the variables to their according values as read in from file
            vertical = load.nextInt();
            horizontal = load.nextInt();
            playerRow = load.nextInt();
            playerCol = load.nextInt();
            goalRow = load.nextInt();
            goalCol = load.nextInt();
            break;
        }
        //move the cursor the beginning of the next line
        load.nextLine();
        //initalize currentState by creating a new GameState with the 
        //parameters that derive from the file we read in 
        this.currentState = new GameState(vertical, horizontal, playerRow,
                playerCol, goalRow, goalCol);
        previousStates = new ArrayList <GameState> ();
        //The following nested for loop reads in the remaining lines and 
        //sets each char in the lines to the chars in the game board
        for(int row = 0; row < vertical; row++){
            String line = load.nextLine();
            for(int col = 0; col < line.length(); col++){
                this.currentState.board[row][col] = line.charAt(col);

            }
        }


    }

    /*
     * Method Header
     * This method records all moves the player makes if the moves change the 
     * player's position on the board and then moves the player. This helper 
     * neccessary is neccessary for the undo method
     * @param direction the direction to pass in for the move method 
     * @return none
     */
    void recordAndMove(Direction direction) {
        // TODO
        // edge case: if direction is null do nothing
        if(direction == null){
            return;
        }
        // make a copy of currentState and move the copy with move(direction)
        GameState copy = new GameState(currentState);
        copy.move(direction);
        // if there is no change from the move and previous state of the game 
        // then do nothing
        if(copy.equals(this.currentState)){
            return;
        }
        // if there is a change add the previous state into previousStates
        // and set currentState the copy which has already been moved
        previousStates.add(this.currentState);
        this.currentState = copy;
    } 

    /*
     * Method Header
     * This method undos the previous move of the player by retriving the
     * previous gameboard and updating it as the current gameboard
     * @param none
     * @return none
     */
    void undo() {
        // TODO
        // Edge case: do nothing if there have been no recorded moves
        if(this.previousStates.isEmpty()){
            return;
        }
        // The follwoing lines of code set currentState to the
        // most recent previousState of the game and deletes the previousState
        int last_move = this.previousStates.size() - 1;
        this.currentState = this.previousStates.get(last_move);
        this.previousStates.remove(last_move);
    }

    /*
     * Method Header
     * This method allows the player to interact with the game by prompting
     * the player to input commands in order to play and complete the game.
     * @param none
     * @return none
     */
    void play() {
        // TODO
        Scanner user = new Scanner(System.in);
        //While loop keeps asking for an input until level is passed or until
        //player exits the game
        while(this.currentState.levelPassed == false){
        System.out.print(currentState.toString());
        System.out.print(POINTER);
        String input = user.next();
        // end the game if player inputs the exit key
        if(input.equals(EXIT)){
            break;
        }
        // All potential cases the player can input and their according methods
        // to execute based on the input, if the user inputs an invalid input 
        // then it asks for another input
            switch(input){
                case MOVE_UP: this.recordAndMove(Direction.UP);
                              break;
                case MOVE_LEFT: this.recordAndMove(Direction.LEFT);
                                break;
                case MOVE_DOWN: this.recordAndMove(Direction.DOWN);
                                break;
                case MOVE_RIGHT: this.recordAndMove(Direction.RIGHT);
                                 break;
                case UNDO: this.undo();
                           break;
                case SAVE: this.saveToFile();
                           break;
                case EXIT: break;
                default : continue;

            }
        }
        //print Level Passed if the player reached the goal
        if(this.currentState.levelPassed == true){
            System.out.println(currentState.toString());
            System.out.println(PASSED);
        }
    }

    /*
     * Method Header
     * saves the current game as a file which can be reloaded at a different 
     * time using the loadFromFile method
     * @param none
     * @return none
     */
    void saveToFile() {
        File readIn = new File(OUTFILE_NAME);
        GameState saved = new GameState(this.currentState);
        try {
            // TODO: use OUTFILE_NAME as the filename and save
            PrintWriter output = new PrintWriter(readIn);
            // The following prints out the information of currentState
            // according to format of the sample data  
            output.println(saved.board.length + SPACE + saved.board[0].length);
            output.println(saved.playerRow + SPACE + saved.playerCol);
            output.println(saved.goalRow + SPACE + saved.goalCol);
            // for loop prints the contents on the board 
            for(int row = 0; row < saved.board.length; row++){
                output.println(saved.board[row]);  
            }
            output.close();
            System.out.println(SAVED);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
