package sample;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.nio.file.Paths;
import java.util.Random;

public class FlugSequenz {


    Thread flightShow;
    private ChickenWarsView view;
    private double windowWidth, windowHight;
    private final int REICHWEITE = 3;
    private double chickenSize, startPosX, startPosY, destinationX, destinationY;
    private final double UPPERQUARTER = 4, HALF = 2, BOTTOMQUARTER = 1.5;

    public FlugSequenz(ChickenWarsView view){
        this.view = view;
    }

    public void newRandomFlightSequence(){
        int formation = (int) (Math.random() * 3);
        newFlightSequence(formation);
    }

    public void newFlightSequence(int formation){
        int defaultEnemyNumber = 5;
        Random extender = new Random();
        boolean extended = extender.nextBoolean();
        if(extended){
            defaultEnemyNumber = defaultEnemyNumber*2;
        }
        switch(formation){
            case 0:
                hFormation(defaultEnemyNumber);
                break;
            case 1:
                xFormation(defaultEnemyNumber);
                break;
            case 2:
                Random rand = new Random();
                fountainFormation(defaultEnemyNumber, rand.nextBoolean());
                break;
            case 3:
            case 5:
            case 4:
                break;
        }
    }

    private void hFormation(int enemyNumber){
        flightShow = new Thread(() ->{
            int walkstyle = (int) (Math.random()*3);
            int finalEnemyNumber = Math.max(enemyNumber, 6);
            Chicken[] chickens = new Chicken[finalEnemyNumber];
            for (int i = 0; i < finalEnemyNumber; i++) {
                if (i % 2 == 0){
                    startPosX = (-chickenSize*2);
                    destinationX = (windowWidth + chickenSize) * REICHWEITE;
                }else{
                    startPosX = (windowWidth + chickenSize*2);
                    destinationX = (-windowWidth - chickenSize) * REICHWEITE;
                }
                startPosY = ((chickenSize*2)*i);
                destinationY = 0;
                chickenRunner(walkstyle, chickens, i);
            }
        });
        flightShow.start();
    }

    private void chickenRunner(int walkstyle, Chicken[] chickens, int i) {
        chickens[i] = new Chicken(startPosX, startPosY, destinationX, destinationY, chickenSize);

        if(walkstyle == 0){
            chickens[i].moveStraight();
        }else if(walkstyle == 1){
            chickens[i].drunkenChicken(windowWidth,windowHight);
        }else{
            chickens[i].moonwalker();
        }
        Platform.runLater(() -> {
            view.getActionPane().getChildren().add(chickens[i]);
            final boolean[] onehit = {true};
            chickens[i].setOnMousePressed(e -> {
                if(onehit[0]) {
                    chickens[i].stoppeAnimation();
                    Image image = new Image(Paths.get("image/ChickenFlyBlack.png").toUri().toString());
                    chickens[i].setFill(new ImagePattern(image));
                    view.chickenWarsController.getGame().addPoint();
                    chickens[i].absturz();
                    onehit[0] = false;
                }
            });
        });
    }

    private void xFormation(int enemyNumber){
        flightShow = new Thread(() -> {
            int walkstyle = (int) (Math.random()*3);
            int finalEnemyNumber = Math.max(enemyNumber, 6);
            Chicken[] chickens = new Chicken[finalEnemyNumber];
            for (int i = 0; i < finalEnemyNumber; i++) {
                if (i % 2 == 0){
                    startPosX = (-chickenSize*2);
                    destinationX = (windowWidth + chickenSize) * REICHWEITE;
                }else{
                    startPosX = (windowWidth + chickenSize*2);
                    destinationX = (-windowWidth - chickenSize) * REICHWEITE;
                }
                startPosY = (windowHight/2 + (i*2) * chickenSize) + (6 - enemyNumber)*25;
                destinationY = (-windowHight/2) * REICHWEITE + (6 - enemyNumber)*25;
                chickenRunner(walkstyle, chickens, i);
            }
        });
            flightShow.start();
    }

    private void fountainFormation(int enemynumber,boolean seite){

        flightShow = new Thread(() -> {
            Random moonwalk = new Random();
            boolean moonwalker = moonwalk.nextBoolean();
            startPointSide(seite, BOTTOMQUARTER);
            destinationTopMid(seite, BOTTOMQUARTER);
            for (int i = 0, j = 0; i < enemynumber; i++, j++) {
                if(enemynumber > 5 && i > 5){j-=2;}
                destinationY = (-windowHight/1.5)*REICHWEITE + j * (windowHight/1.5);
                try {
                    Thread.sleep(500);
                    Chicken chick = new Chicken(startPosX, startPosY, destinationX, destinationY, chickenSize);
                    if(moonwalker) {
                        chick.moonwalker();
                    }else {
                        chick.moveStraight();
                    }
                    Platform.runLater(()->  {
                        view.getActionPane().getChildren().add(chick);
                        final boolean[] onehit = {true};
                        chick.setOnMousePressed(e -> {
                            if(onehit[0]) {
                                chick.stoppeAnimation();
                                Image image = new Image(Paths.get("image/ChickenFlyBlack.png").toUri().toString());
                                chick.setFill(new ImagePattern(image));
                                view.chickenWarsController.getGame().addPoint();
                                chick.absturz();
                                onehit[0] = false;
                            }
                        });
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        flightShow.start();
    }

    /**
     * @param seite (startpunkt links oder rechts)
     * @param location (oberes Viertel:location = 4, hälfte: location = 2, unteres Viertel: location 1.5)
     */
    private void startPointSide(boolean seite, double location){
        if(seite){
            startPosX = windowWidth + chickenSize;
        }else{
            startPosX = (-(chickenSize*2));
        }
        startPosY = windowHight/location;
    }

    /**
     *
     * @param seite (startpunkt links oder rechts)
     * @param location  (für Oben Mitte muss location = location der startPointSide sein)
     */
    private void destinationTopMid(boolean seite, double location){
        if(seite){
            destinationX = (-windowWidth)*REICHWEITE;
        }else{
            destinationX = (windowWidth)*REICHWEITE;
        }
        destinationY = (-(windowHight*2)/location)*REICHWEITE;

    }

    public void setWindowWidth(double windowWidth) {
        this.windowWidth = windowWidth;
    }

    public void setWindowHight(double windowHight) {
        this.windowHight = windowHight;
        this.chickenSize = windowHight/25;
    }

    public void removeChicken(Chicken chicken){
        view.getChildren().remove(chicken);
    }
}
