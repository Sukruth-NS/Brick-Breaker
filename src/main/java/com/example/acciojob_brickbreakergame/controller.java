package com.example.acciojob_brickbreakergame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class controller implements Initializable {
    @FXML
    private Circle circle;

    @FXML

    private AnchorPane scene;

    double deltax = 0.5;
    double deltay = 0.5;

    ArrayList<Rectangle> gameBricks = new ArrayList<>();

    private Rectangle slider;

    private Button left, right;
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            // we need to move the ball.
            circle.setLayoutY(circle.getLayoutY() + deltay);
            circle.setLayoutX(circle.getLayoutX() + deltax);
            //we need to check for collisions every time here.
            //1.collision with walls, which is scene or border.
            checkCollision_with_Wall();

            //2.collision with slider.
            checkCollision_with_Slider();

            //3.collision with bricks
            checkCollision_with_bricks();

            //see if all the bricks are empty, then exit the game.
            if (gameBricks.isEmpty()) {
                System.exit(10);
            }
        }
    }));

    public void checkCollision_with_Wall(){
        Bounds bounce = scene.getBoundsInLocal();
        boolean rightSide = circle.getLayoutX() + circle.getRadius() >= bounce.getMaxX(); //getLayout get centre of the circle
        boolean leftSide = circle.getLayoutX() - circle.getRadius() <= bounce.getMinX(); //getRadius get the radius of the ball Or circle
        boolean topSide = circle.getLayoutY() - circle.getRadius() <= bounce.getMinY();
        boolean bottomSide = circle.getLayoutY() + circle.getRadius() >= bounce.getMaxY();

        if(rightSide || leftSide) deltax *= -1;
        if (topSide) deltay *= -1;
        if (bottomSide) deltay *= -1; //just for testing purpose
//        if(bottomSide) System.exit(10); //end of game
    }

    public void checkCollision_with_Slider(){
        if (circle.getBoundsInParent().intersects(slider.getBoundsInParent())) {
            deltay *= -1;
        }
    }

    public void checkCollision_with_bricks(){
        gameBricks.removeIf(current_brick -> checkCollision_with_Single_brick(current_brick));
    }

    public boolean checkCollision_with_Single_brick(Rectangle brick){
        if(circle.getBoundsInParent().intersects(brick.getBoundsInParent())){
            Bounds bounds = brick.getBoundsInLocal();

            boolean bottomSide = circle.getLayoutY() - circle.getRadius() <= bounds.getMaxY(); //getRadius get the radius of the ball Or circle
            boolean topSide = circle.getLayoutY() - circle.getRadius() >= bounds.getMinY(); //getLayout get centre of the circle
            boolean rightSide = circle.getLayoutX() - circle.getRadius() <= bounds.getMaxX();
            boolean leftSide = circle.getLayoutX() + circle.getRadius() >= bounds.getMinX();

            if(rightSide || leftSide) deltax *= -1;
            if(topSide || bottomSide) deltay *= -1;

            scene.getChildren().remove(brick);
            return true;
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        adding_slider();
        create_bricks();
        adding_buttons();
    }

    public void adding_slider(){
        slider = new Rectangle(250 ,375, 60, 15);
        slider.setFill(Color.BLACK);
        scene.getChildren().add(slider);
    }

    public void adding_buttons(){
        left = new Button("left");
        right = new Button("right");

        left.setLayoutY(350);
        right.setLayoutY(350);

        left.setLayoutX(20);
        right.setLayoutX(540);

        left.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(slider.getLayoutX() > -240) //add limit to slider towards LeftSide so that it doesn't go out off border
                    slider.setLayoutX(slider.getLayoutX() - 20);// give the moment to slider towards LEFT
            }
        });
        right.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(slider.getLayoutX() < 280) //add limit to slider towards RightSide so that it doesn't go out off border
                    slider.setLayoutX(slider.getLayoutX() + 20); // give the moment to slider towards RIGHT
            }
        });

        scene.getChildren().add(left);
        scene.getChildren().add(right);
    }

    public void create_bricks(){
        int counter = 1;
        for(int i = 250; i > 0; i -= 50) //to move up
        {
            for(int j = 508; j>= 0; j -= 30)//to move right
            {
                if(counter % 2 == 1){
                    Rectangle rect = new Rectangle(j, i, 40,40 );//it's to create blocks with the dimension

                    if (counter % 3 == 0) rect.setFill(Color.ROSYBROWN);        //COLOURING THE Rectangle in diagonal
                    else if (counter % 3 == 1) rect.setFill(Color.BLUEVIOLET);
                    else rect.setFill(Color.MEDIUMORCHID);

                    scene.getChildren().add(rect); //adding rectangle to the anchor plane scene
                    gameBricks.add(rect);
                }
                counter++;
            }
        }
    }
}
