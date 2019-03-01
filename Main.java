package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class Main extends Application {

    public static final float PPM = 32.0f;
    public final static World world = new World(new Vec2(0.0f, -5.5f));
    private static final float WIDTH = 600;
    private static final float HEIGHT = 800;

    //Convert a JBox2D x coordinate to a JavaFX pixel x coordinate
    public static float meterToPixel(float meter) {
        float pixel = meter * PPM;
        return pixel;
    }


    //Convert a JavaFX pixel y coordinate to a JBox2D y coordinate
    public static float pixelToMeter(float pixel) {
        float meter = pixel / PPM;
        return meter;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	world.setSleepingAllowed(true);
        primaryStage.setTitle("Pinball");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);

        final Group root = new Group();
        final Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        final Ball ball = new Ball(599, 800);
        
        
        final Square squareBottom = new Square(610, 810, 610, 1, 0, Color.WHITE);
        final Square squareTop = new Square(610, 1, 610, 1, 0, Color.WHITE);
        final Square squareRight = new Square(610, 810, 1, 810, 0, Color.WHITE);
        final Square squareLeft = new Square(1, 810, 1, 810, 0, Color.WHITE);
        final Square kickLane = new Square(587, 810, 1, 710, 0, Color.WHITE);
        
        
        final Square topKickLane = new Square(660, 20, 200, 20, 43, Color.WHITE);
        final Square topKickLane2 = new Square(660, 72, 200, 20, 43, Color.WHITE);
        
        
        //final RoundThing upperRightCorner = new RoundThing(610, 20, 20, Color.RED);
        final RoundThing r1 = new RoundThing(180, 250, 30, Color.RED); // random ball
        final RoundThing r2 = new RoundThing(420, 250, 30, Color.RED);
        final RoundThing r3 = new RoundThing(300, 350, 30, Color.RED);
        


        EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                world.step(2.0f / 60.f, 8, 3);
                Body body = (Body) ball.node.getUserData();
                float xpos = meterToPixel(body.getPosition().x);
                float ypos = meterToPixel(-body.getPosition().y);
                ball.node.setLayoutX(xpos);
                ball.node.setLayoutY(ypos);
            }
        };



        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        Duration duration = Duration.seconds(1.0 / 60.0);
        KeyFrame frame = new KeyFrame(duration, ae, null, null);

        timeline.getKeyFrames().add(frame);

        root.getChildren().add(ball.node);
        root.getChildren().add(squareBottom.node);
        root.getChildren().add(squareTop.node);
        root.getChildren().add(squareRight.node);
        root.getChildren().add(squareLeft.node);
        root.getChildren().add(kickLane.node);
        //root.getChildren().add(upperRightCorner.node);
        root.getChildren().add(r1.node);
        root.getChildren().add(r2.node);
        root.getChildren().add(r3.node);
        root.getChildren().add(topKickLane.node);
        


        primaryStage.setScene(scene);
        primaryStage.show();


        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                ball.addForce(new Vec2(0,50));

            }
       
            if (event.getCode() == KeyCode.LEFT) {
                ball.addForce(new Vec2(-10,0));

            }
      
            if (event.getCode() == KeyCode.RIGHT) {
                ball.addForce(new Vec2(10,0));

            }
        });


        timeline.playFromStart();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
