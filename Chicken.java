package sample;

import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.nio.file.Paths;

public class Chicken extends Circle{

    private double destinationX;
    private double destinationY;
    private double windowWidth, windowHight;
    private double startPosX, startPosY;
    private int chickensize = 50;
    private final double GESCHWINDIGKEIT = 10;
    private SequentialTransition move;
    

//    private ArrayList<Chicken> chickenList = new ArrayList<>();

    public Chicken(double windowWidth, double windowHight) {
        this.windowWidth = windowWidth;
        this.windowHight = windowHight;
        startPosX = (int) (Math.random() * (windowWidth + 2*windowHight) - windowHight);
        startPosY = outerWindow();
        setFill(Color.RED);
        setRadius(chickensize);
        /*setStyle("-fx-background-radius: 5em; " +
                "-fx-min-width: 100px; " +
                "-fx-min-height: 100px; " +
                "-fx-max-width: 100px; " +
                "-fx-max-height: 100px;");*/
        relocate(startPosX, startPosY);                                      //Spawn koordinaten des Objectes
        standartMovement();
        moveStraight();
    }

    public Chicken(double startPosX, double startPosY, double destinationX, double destinationY, double chickensize){
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        Image image = new Image(Paths.get("image/ChickenFly.png").toUri().toString());
        setFill(new ImagePattern(image)); //new ImagePattern(img)
        setRadius(chickensize);
        relocate(this.startPosX, this.startPosY);                                      //Spawn koordinaten des Objectes
        move = new SequentialTransition();
    }


    private int outerWindow(){
        int y;
        if (startPosX > 0 && startPosX < windowWidth){
            y = (-chickensize);
        }else{
            y = (int) (Math.random() * windowHight + 1);
        }
        return y;
    }

    /**
     * Es fliegt ein einzelnes Huhn durch die Mitte
     */
    private void standartMovement(){
        destinationX = (((windowWidth/2) - startPosX));
        destinationY = (((windowWidth/2) - startPosY));
    }

    void moveStraight(){

        TranslateTransition movement = new TranslateTransition();
        movement.setDuration(Duration.seconds(GESCHWINDIGKEIT));                  //Geschwindigkeit der Bewegung des Objectes (darf nicht negativ sein)
        movement.setToX(destinationX);                                            //Ziel angabe des Hähnchens um durch die mitte zu fliegen
        movement.setToY(destinationY);                                            //Reichweite sollte groß genug sein um aus dem Bild zu fliegen
        movement.setNode(this);
        movement.setInterpolator(Interpolator.LINEAR);                            //Beschleunigung und Bremsweg ist ausgeschaltet bei LINEAR parameter
        move.getChildren().add(movement);
        move.play();
        move.setOnFinished(finish -> absturz());
    }

    void moonwalker(){
        double REICHWEITE = 4;
        TranslateTransition movement = new TranslateTransition();
        movement.setDuration(Duration.seconds(GESCHWINDIGKEIT/REICHWEITE));
        movement.setToX(destinationX/REICHWEITE);
        movement.setToY(destinationY/REICHWEITE);
        movement.setNode(this);
        movement.setAutoReverse(true);                                              //?????????????????????
        movement.setCycleCount(2);
        move.getChildren().add(movement);
        move.play();
        move.setOnFinished(finish -> absturz());
    }

    void drunkenChicken(double windowWidth, double windowHight){
        int directionChange = 100, direction = 100;

        if(startPosY <0){
            drunkMove(directionChange, direction, windowHight);
        }else if(startPosX <0){
            drunkMove(directionChange, direction, windowWidth);
        }else{
            drunkMove(directionChange, -direction, windowWidth);
        }

    }

    private void drunkMove(double directionChanger, double direction, double windowSize){
        SequentialTransition move = new SequentialTransition();
        for (int i = 0; i < windowSize/chickensize; i++) {
            directionChanger = (-directionChanger);
            TranslateTransition movement = new TranslateTransition();
            movement.setDuration(Duration.seconds(0.5));
            movement.setToX((i+1)*direction);
            movement.setToY(directionChanger);
            movement.setNode(this);
            move.getChildren().add(movement);
        }
        move.play();
        move.setOnFinished(finish -> selfDelete());
    }

    void absturz(){
        TranslateTransition movement = new TranslateTransition();
        movement.setDuration(Duration.seconds(GESCHWINDIGKEIT));
        movement.setToY(2000);
        movement.setNode(this);
        movement.play();
        movement.setOnFinished(finish -> absturz());
    }

    private void selfDelete(){
        ((Pane) this.getParent()).getChildren().remove(this);
    }

    public void setColor(Color c){
        setFill(c);
    }

    public void stoppeAnimation(){
        move.stop();
    }


}