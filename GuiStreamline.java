import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.animation.*;
import javafx.animation.PathTransition.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.*;
import javafx.util.Duration;
/**
 * File Header
 * The purpose of this file is to implement GUI functionalties to the
 * Streamline game we created in pa3. We dont change any of the backend code 
 * for the streamline game but we do make the game prettier by adding colors,
 * animations, shapes, and different effects.
 *
 * Author: Alejandro Marquez
 * email: a1marque@ucsd.edu
 * references: tutor hours
 *
 */

/**
 * Class Header
 * This class focuses on the implementation of the Gui aspect of the Streamline
 * game like updating what should be displayed on the scene as the game 
 * progresses. Important instance variables are the groups, player/goal 
 * rectangle, the shape grid, the handler, and the current and next streamline
 * games.
 */
public class GuiStreamline extends Application {
    static final double SCENE_WIDTH = 500;
    static final double SCENE_HEIGHT = 600;
    static final String TITLE = "CSE 8b Streamline GUI";
    static final String USAGE = 
        "Usage: \n" + 
        "> java GuiStreamline               - to start a game with default" +
            " size 6*5 and random obstacles\n" + 
        "> java GuiStreamline <filename>    - to start a game by reading g" +
            "ame state from the specified file\n" +
        "> java GuiStreamline <directory>   - to start a game by reading a" +
            "ll game states from files in\n" +
        "                                     the specified directory and " +
            "playing them in order\n";

    static final Color TRAIL_COLOR = Color.PALEVIOLETRED;
    static final Color GOAL_COLOR = Color.MEDIUMAQUAMARINE;
    static final Color OBSTACLE_COLOR = Color.DIMGRAY;

    // Trail radius will be set to this fraction of the size of a board square.
    static final double TRAIL_RADIUS_FRACTION = 0.1;

    // Squares will be resized to this fraction of the size of a board square.
    static final double SQUARE_FRACTION = 0.8;
    
    Scene mainScene;
    Group levelGroup;                   // For obstacles and trails
    Group rootGroup;                    // Parent group for everything else
    Player playerRect;                  // GUI representation of the player
    RoundedSquare goalRect;             // GUI representation of the goal

    Shape[][] grid;                     // Same dimensions as the game board
    
    Streamline game;                    // The current level
    ArrayList<Streamline> nextGames;    // Future levels
    
    MyKeyHandler myKeyHandler;          // for keyboard input

    /**
     * Method Header
     * Returns the width of the board for the current level
     * @param none
     * @return int the width of the current level's board
     */
    public int getBoardWidth() {
        /*TODO*/ 
        return this.game.currentState.board[0].length; 
    }

    /**
     * Method Header
     * Returns the height of the board for the current level
     * @param none
     * @return int the height of the current level's board
     */
    public int getBoardHeight() {
    	/*TODO*/
        return this.game.currentState.board.length;  
    }
    /**
     * Method Header
     * This method finds a size for a single square of the board that will fit
     * nicely in the current scene size.
     * @param none
     * @return double the size of the a sqaure given the size of the scene
     */
    public double getSquareSize() {
        /* For example, given a scene size of 1000 by 600 and a board size
           of 5 by 6, we have room for each square to be 200x100. Since we
           want squares not rectangles, return the minimum which is 100 
           in this example. */
        
        /*TODO*/
        int width = getBoardWidth();
        int height = getBoardHeight();
        //Divides both the current scene width and height by the board's width
        //and height
        double squareWidth = mainScene.getWidth() / (double) width ;
        double squareHeight = mainScene.getHeight() / (double) width ;
        //The return square size is determined by which of the two values above
        //is less 
        if(squareWidth >= squareHeight){
            return squareHeight;
        }
        else{
            return squareWidth;
        }
    }

    /**
     * Method Header
     * This method destroys and recreate grid and all trail and obstacle shapes
     * Assumes the dimensions of the board may have changed.
     * @param none
     * @return none 
     */
    public void resetGrid() {
        
        /*TODO*/

        // Hints: Empty out grid, trailsGroup, and obstaclesGroup.
        // Also makes sure grid is the right size, in case the size of the
        // board changed.

        int newCol = getBoardWidth();
        int newRow = getBoardHeight();
        //set the instance variable grid to a new Shape[][] with demonsions
        //the of the current board
        this.grid = new Shape[newRow][newCol];
        //set the instance variable levelGroup to an empty group
        this.levelGroup.getChildren().clear();
        //loops through the entire current board  and populates the grid with
        //shapes and adds the shapes to levelGroup
        for(int row = 0; row < this.game.currentState.board.length; row++){
            for(int col = 0; col < 
                    this.game.currentState.board[0].length; col++){
                //If current board at [row][col] is a trail char
                //Create a circle, positions it according the row and col value
                //adds the circle to the group and to the grid
                //sets circle color to TRAIL_COlOR since it represents a trail
                if(this.game.currentState.board[row][col] 
                        == this.game.currentState.TRAIL_CHAR){
                    double[] trailPos = boardIdxToScenePos(col, row);
                    Circle trail = new Circle(trailPos[0], trailPos[1],
                            getSquareSize() * TRAIL_RADIUS_FRACTION,
                            TRAIL_COLOR);
                    grid[row][col] = trail;
                    this.levelGroup.getChildren().add(trail);
                }
                //If current board at [row][col] is a space char
                //Create a circle, positions it according the row and col value
                //adds the circle to the group and to the grid
                //sets the circle color to clear since it represents a space
                else if(this.game.currentState.board[row][col] 
                        == this.game.currentState.SPACE_CHAR){
                    double[] emptyPos = boardIdxToScenePos(col, row);
                    Circle empty = new Circle(emptyPos[0], emptyPos[1],
                            getSquareSize() * TRAIL_RADIUS_FRACTION,
                            Color.TRANSPARENT);
                    grid[row][col] = empty;
                    this.levelGroup.getChildren().add(empty);
                }
                //If current board at [row][col] is an obstacle char
                //Create a roundedSqaure, position based on row and col value
                //adds the circle to the group and to the grid
                //sets the circle color to clear since it represents a space
                else if(this.game.currentState.board[row][col] 
                        == GameState.OBSTACLE_CHAR){
                    double[] obstaclePos = boardIdxToScenePos(col, row);
                    RoundedSquare obstacle = new RoundedSquare();
                    obstacle.setSize(getSquareSize());
                    obstacle.setCenterX(obstaclePos[0]);
                    obstacle.setCenterY(obstaclePos[1]);
                    obstacle.setFill(OBSTACLE_COLOR);
                    grid[row][col] = obstacle;
                    this.levelGroup.getChildren().add(obstacle);
                }
            }
        }
    }

    /**
     * Method Header
     * Sets the fill color of all trail Circles making them visible or not
     * depending on if that board position equals TRAIL_CHAR.
     * @param none
     * @return none
     */
    public void updateTrailColors() {
    	/*TODO*/
        //loop through every board position
        for(int row = 0; row < 
                this.game.currentState.board.length; row++){
            for(int col = 0; col < 
                    this.game.currentState.board[0].length ; col++){
                //if the board position is a trail char set the shape in grid 
                //at the same position to TRAIL_COLOR
                if(this.game.currentState.board[row][col] 
                        == this.game.currentState.TRAIL_CHAR){
                    grid[row][col].setFill(TRAIL_COLOR);
                }
                //if the board position is a space char set the shape in grid 
                //at the same position to TRANSPARENT
                else if(this.game.currentState.board[row][col] 
                        == this.game.currentState.SPACE_CHAR){
                    grid[row][col].setFill(Color.TRANSPARENT);
                }
            }
        }
    }
    
    /** 
     * Coverts the given board column and row into scene coordinates.
     * Gives the center of the corresponding tile.
     * 
     * @param boardCol a board column to be converted to a scene x
     * @param boardRow a board row to be converted to a scene y
     * @return scene coordinates as length 2 array where index 0 is x
     */
    static final double MIDDLE_OFFSET = 0.5;
    public double[] boardIdxToScenePos (int boardCol, int boardRow) {
        double sceneX = ((boardCol + MIDDLE_OFFSET) * 
            (mainScene.getWidth() - 1)) / getBoardWidth();
        double sceneY = ((boardRow + MIDDLE_OFFSET) * 
            (mainScene.getHeight() - 1)) / getBoardHeight();
        return new double[]{sceneX, sceneY};
    }

    /**
     * Method Header
     * This method makes trail markers visible and changes player position.
     * To be called when the user moved the player and the GUI needs to be 
     * updated to show the new position.
     * @param fromCol the col position the player started at
     * @param fromRow the row positiob the player started at
     * @param toCol the col position the player moved to
     * @param torow the col position the player moved to
     * @param isUndo boolean checking if it was a undo move
     * @return none
     */
    public void onPlayerMoved(int fromCol, int fromRow, int toCol, int toRow, 
        boolean isUndo)
    {
        // If the position is the same, just return
        if (fromCol == toCol && fromRow == toRow) {
            return;
        }
        /*TODO*/
        //otherwise move the playerRect to its new position on the scene
        //then update the trail colors
        else{
            double[] updatedPlayer = boardIdxToScenePos(toCol, toRow);
            playerRect.setCenterX(updatedPlayer[0]);
            playerRect.setCenterY(updatedPlayer[1]);
            updateTrailColors();
            }
        //after the player positon check if the level is passed, if so then 
        //execute onLevelFinished method
        if(this.game.currentState.levelPassed){
            onLevelFinished();
        }
    }

    /**
     * Method Header
     * This method takes in a keyCode and runs a method from Streamline.java
     * based on that KeyCode
     * @param keyCode the KeyCode to read in an preform a accordingly function
     * @return none
     */

    void handleKeyCode(KeyCode keyCode) {

        /*TODO*/
        //First get the previous playerCol and playerRow before it is updated
        int prevCol = this.game.currentState.playerCol;
        int prevRow = this.game.currentState.playerRow;

        //Switch statement that takes keyCode and preforms a method from 
        //Streamline.java based on the keyCode
        switch (keyCode){
        	/*TODO*/
            case UP:
                this.game.recordAndMove(Direction.UP);
                break;
            case DOWN:
                this.game.recordAndMove(Direction.DOWN);
                break;
            case LEFT:
                this.game.recordAndMove(Direction.LEFT);
                break;
            case RIGHT:
                this.game.recordAndMove(Direction.RIGHT);
                break;
            case U:
                this.game.undo();
                break;
            case O:
                this.game.saveToFile();
            case Q:
                System.exit(0);
            //if the KeyCode is not one of the above options print the
            //following statement
            default:
                System.out.println("Possible commands:\n w - up\n " + 
                    "a - left\n s - down\n d - right\n u - undo\n " + 
                    "q - quit level");
                break;
        }

        //Get the updated playerRow and playerCol after performing the keyCode
        //function
        int currCol = this.game.currentState.playerCol;
        int currRow = this.game.currentState.playerRow;
        // Call onPlayerMoved() to update the GUI to reflect the player's 
        // movement (if any)
        if(prevCol != currCol || prevRow != currRow){
            onPlayerMoved(prevCol, prevRow, currCol, currRow, false);
        }
    }
    /**
     * Class Header
     * This nested class handles keyboard input and calls handleKeyCode().
     * There are no instance variables for this class.
     */
    class MyKeyHandler implements EventHandler<KeyEvent> {

        /**
         * Method Header
         * This method handles what the KeyCode should execute by calling 
         * handleKeyCode method
         * @param e KeyEvent which will be indirectly passed into handleKeyCode
         */
        @Override
        public void handle(KeyEvent e) {
            /*TODO*/
            //call handleKeyCode and pass in the keyEvent's code
            handleKeyCode(e.getCode());
        }
    }

    /**
     * Method Header
     * To be called whenever the UI needs to be completely redone to reflect
     * a new level
     * @param none
     * @return none
     */
    public void onLevelLoaded() {
        //resetGrid since it's a new game
        resetGrid();

        double squareSize = getSquareSize() * SQUARE_FRACTION;

        // Update the player position
        double[] playerPos = boardIdxToScenePos(
            game.currentState.playerCol, game.currentState.playerRow
        );
        playerRect.setSize(squareSize);
        playerRect.setCenterX(playerPos[0]);
        playerRect.setCenterY(playerPos[1]);

        /*TODO*/
        
        //Update the goal postion
        double[] goalPos = boardIdxToScenePos(
                game.currentState.goalCol, game.currentState.goalRow);
        goalRect.setSize(squareSize);
        goalRect.setCenterX(goalPos[0]);
        goalRect.setCenterY(goalPos[1]);
    }

    /**
     * Method Header
     * Called when the player reaches the goal. Shows the winning animation
     * and loads the next level if there is one.
     * @param none
     * @return none
     */
    
    static final double SCALE_TIME = 175;  // milliseconds for scale animation
    static final double FADE_TIME = 250;   // milliseconds for fade animation
    static final double DOUBLE_MULTIPLIER = 2;
    public void onLevelFinished() {
        // Clone the goal rectangle and scale it up until it covers the screen

        // Clone the goal rectangle
        Rectangle animatedGoal = new Rectangle(
            goalRect.getX(),
            goalRect.getY(),
            goalRect.getWidth(),
            goalRect.getHeight()
        );
        animatedGoal.setFill(goalRect.getFill());

        // Add the clone to the scene
        List<Node> children = rootGroup.getChildren();
        children.add(children.indexOf(goalRect), animatedGoal);

        // Create the scale animation
        ScaleTransition st = new ScaleTransition(
            Duration.millis(SCALE_TIME), animatedGoal
        );
        st.setInterpolator(Interpolator.EASE_IN);
        
        // Scale enough to eventually cover the entire scene
        st.setByX(DOUBLE_MULTIPLIER * 
            mainScene.getWidth() / animatedGoal.getWidth());
        st.setByY(DOUBLE_MULTIPLIER * 
            mainScene.getHeight() / animatedGoal.getHeight());

        /*
         * This will be called after the scale animation finishes.
         * If there is no next level, quit. Otherwise switch to it and
         * fade out the animated cloned goal to reveal the new level.
         */
        st.setOnFinished(e1 -> {

            /* TODO: check if there is no next game and if so, quit */
            //If there are no more games to load in then exit the games
            if(nextGames.isEmpty()){
                System.exit(0);
            }

            /* TODO: update the instances variables game and nextGames 
                     to switch to the next level */

            //if there are more games to load in then set the instance variable
            //game to the first index of the arrayList nextGame and remove that
            //index from nextGames
            if(nextGames.size() > 0){
                this.game = nextGames.get(0);
                nextGames.remove(0);
            }

            // Update UI to the next level, but it won't be visible yet
            // because it's covered by the animated cloned goal

            onLevelLoaded();

            /* TODO: use a FadeTransition on animatedGoal, with FADE_TIME as
                     the duration. Use setOnFinished() to schedule code to
                     run after this animation is finished. When the animation
                     finishes, remove animatedGoal from rootGroup. */

            //create a fade transition, play it. then remove it from the group
            FadeTransition fade = new FadeTransition(
                    Duration.millis(FADE_TIME), animatedGoal);
            fade.play();
            rootGroup.getChildren().remove(animatedGoal);
        });
        
        // Start the scale animation
        st.play();
    }

    /** 
     * Performs file IO to populate game and nextGames using filenames from
     * command line arguments.
     */
    public void loadLevels() {
        game = null;
        nextGames = new ArrayList<Streamline>();
        
        List<String> args = getParameters().getRaw();
        if (args.size() == 0) {
            System.out.println("Starting a default-sized random game...");
            game = new Streamline();
            return;
        }

        // at this point args.length == 1
        
        File file = new File(args.get(0));
        if (!file.exists()) {
            System.out.printf("File %s does not exist. Exiting...", 
                args.get(0));
            return;
        }

        // if is not a directory, read from the file and start the game
        if (!file.isDirectory()) {
            System.out.printf("Loading single game from file %s...\n", 
                args.get(0));
            game = new Streamline(args.get(0));
            return;
        }

        // file is a directory, walk the directory and load from all files
        File[] subfiles = file.listFiles();
        Arrays.sort(subfiles);
        for (int i=0; i<subfiles.length; i++) {
            File subfile = subfiles[i];
            
            // in case there's a directory in there, skip
            if (subfile.isDirectory()) continue;

            // assume all files are properly formatted games, 
            // create a new game for each file, and add it to nextGames
            System.out.printf("Loading game %d/%d from file %s...\n",
                i+1, subfiles.length, subfile.toString());
            nextGames.add(new Streamline(subfile.toString()));
        }

        // Switch to the first level
        game = nextGames.get(0);
        nextGames.remove(0);
    }
    
    /**
     * Method Header
     * This method provides the main entry point for all JavaFX Applications
     * Initializes instance variables, creates the scene, and sets up the UI
     * @param primaryStage the stage to display the GUI
     * @return none
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Populate game and nextGames
        loadLevels();

        // Initialize the scene and our groups
        rootGroup = new Group();
        mainScene = new Scene(rootGroup, SCENE_WIDTH, SCENE_HEIGHT, 
            Color.GAINSBORO);
        levelGroup = new Group();
        rootGroup.getChildren().add(levelGroup);
        
        //TODO: initialize goalRect and playerRect, add them to rootGroup,
        //      call onLevelLoaded(), and set up keyboard input handling
        
        //initalize goalRect and its properties such as color, position, etc
        //then add it to rootGroup
        double[] goalPos = boardIdxToScenePos(game.currentState.goalCol,
                game.currentState.goalRow);
        goalRect = new RoundedSquare();
        goalRect.setSize(getSquareSize());
        goalRect.setCenterX(goalPos[0]);
        goalRect.setCenterY(goalPos[1]);
        goalRect.setFill(GOAL_COLOR);
        rootGroup.getChildren().add(goalRect);

        //intialize playerRect as new player object and set is positon and size
        //then add it to rootGroup
        double[] playerPos = boardIdxToScenePos(game.currentState.playerCol,
                game.currentState.playerRow);
        playerRect = new Player();
        playerRect.setSize(getSquareSize());
        playerRect.setCenterX(playerPos[0]);
        playerRect.setCenterY(playerPos[1]);
        rootGroup.getChildren().add(playerRect);

        //execute onLevelLoaded method
        onLevelLoaded();


        // Make the scene visible
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();

        //create a MyKeyHandler which will translate key presses to functions
        myKeyHandler = new MyKeyHandler();
        mainScene.setOnKeyPressed(myKeyHandler);
    }

    /** 
     * Execution begins here, but at this point we don't have a UI yet
     * The only thing to do is call launch() which will eventually result in
     * start() above being called.
     */
    public static void main(String[] args) {
        if (args.length != 0 && args.length != 1) {
            System.out.print(USAGE);
            return;
        }
        
        launch(args);
    }
}
