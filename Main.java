package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class Main extends Application {

    public static final float PPM = 32.0f;
    public final static World world = new World(new Vec2(0.0f, -9.81f), true);
    private static final float WIDTH = 400;
    private static final float HEIGHT = 600;

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
        primaryStage.setTitle("Pinball");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);

        final Group root = new Group();
        final Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        final Ball ball = new Ball(200, 100);

        //final Square squareBottom = new Square(94.0f, -1.33f, 50.0f, 2.0f, Color.AQUA);
        final Square squareLeft = new Square(220, 300, 50, 50, Color.AQUA);
        final Square squareRight = new Square(11f, -19f, 0.5f, 4.0f, Color.AQUA);
        final Square squareBottomRight = new Square(96.3f, 0.0f, 5.0f, 1.0f, Color.AQUA);


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
        root.getChildren().add(squareBottomRight.node);
        root.getChildren().add(squareLeft.node);
        root.getChildren().add(squareRight.node);


        primaryStage.setScene(scene);
        primaryStage.show();


       /* scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                ball.addForce(new Vec2(1000,3000));

            }
        });
*/

        timeline.playFromStart();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
