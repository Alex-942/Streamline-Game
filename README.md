Author: Alejandro Marquez
email: a1marque@ucsd.edu
## Program Description
The code for this assingment build off of the code we had written in a previous
assingment where we created the streamline game. In this code however instead 
of actually creating the game we edit the game by making it more visually 
appealing. We complete this task by collecting the data of the streamline game
and translating that information to shapes and colors displayed on a window.
Where each of the shapes represent information for the streamline game. 

## Unix/Linux
1. mkdir -p foobar/dirDir
2. rm *.java *would remove all files that end with .java in the current 
   directory
3. ls -R

## JavaFX
1. You could add and Action Listener or rather a keyListener where it would be
   executed by putting something similar to the following: EventHandler.create(
   KeyListener.class , e, "getCode") where it would create an Key Handler that
   would get the code from the Key pressed.

2. The purpose of the group class is to contain all the objects we want to 
   display. But to display we first need to add the objects to a group, then 
   add the group to a scene and then display the scene on a stage which is 
   essentially a window. Heirarchy: group -> scene -> stage

