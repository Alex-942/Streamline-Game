import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

/**
 * File Header
 * The only purpose of this file to set the properties of a object that is 
 * a Player type. It sets all objects of type player to the properties we
 * listed below
 *
 * Author: Alejandro Marquez
 * email: a1marque@ucsd.edu
 * references: Tutor Hours
 */

/**
 * Class Header
 * This class sets the properties of the Player object as listed down below
 * There are no instance variables to initialize
 */

public class Player extends RoundedSquare {
    final static double STROKE_FRACTION = 0.1;

    /**
     * Constructor Header
     * This constructor sets the fill, stroke color, and stroke type of 
     * Player objects
     * @param none
     * @return newly constructed player object
     */
    public Player() {
        //TODO: set a fill color, a stroke color, and set the stroke type to
        //      centered
        //sets the fill stroke color and stroke type
        setFill(GuiStreamline.TRAIL_COLOR);
        setStroke(Color.DEEPPINK);
        setStrokeType(StrokeType.CENTERED);
    }
    
    /**
     * Method Header
     * This method sets the size for a player object
     * @param size the size to set the object's size to
     * @return none
     */
    @Override
    public void setSize(double size) {
        //TODO: 1. update the stroke width based on the size and 
        //         STROKE_FRACTION
        //      2. call super setSize(), bearing in mind that the size
        //         parameter we are passed here includes stroke but the
        //         superclass's setSize() does not include the stroke

        //Sets the stroke width and then call super's setSize to setSize of
        //player objects.
        setStrokeWidth(size * STROKE_FRACTION);
        super.setSize(size);
    }
}
