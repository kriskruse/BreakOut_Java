[![Test](https://github.com/kriskruse/BreakOut_Java/actions/workflows/maven.yml/badge.svg)](https://github.com/kriskruse/BreakOut_Java/actions/workflows/maven.yml)

# Basic Breakout Game
## Running the Game
To run the game, you need to have Java installed on your computer. 

Make sure you are in the directory where the jar file is located. Then run the following command:

If you have a version of java with JavaFx.
```BASH
java -jar BreakOut.jar n m 
```
where n and m are the number of rows and columns of the bricks in the game.

If you do not have JavaFx, you can run the game by running the following command in the terminal:
```BASH
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml,javafx.media -jar Breakout.jar n m
```
where %PATH_TO_FX% is the path to the lib folder of JavaFx.

and n and m are the number of rows and columns of the bricks in the game.


## Source Code

To see the source code included in the jar file, you can un-zip the file using a program like 7-zip or WinRAR.

The source code is located under: - dk -> group12 -> breakout.

Here the graphical java file BreakoutGrapical.java is the main file that runs the game.

And all the game files can be found in the sub folder BreakOutGame.








