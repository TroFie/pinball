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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class Main extends Application {

    public final static World world = new World(new Vec2(0.0f, -9.81f), true);
    private static final float WIDTH = 800;
    private static final float HEIGHT = 600;

    //Convert a JBox2D x coordinate to a JavaFX pixel x coordinate
    public static float jBoxToFxPosX(float posX) {
        float x = WIDTH * posX / 100.0f;
        return x;
    }

    //Convert a JavaFX pixel x coordinate to a JBox2D x coordinate
    public static float fxToJboxPosX(float posX) {
        float x = (posX * 100.0f * 1.0f) / WIDTH;
        return x;
    }

    //Convert a JBox2D y coordinate to a JavaFX pixel y coordinate
    public static float jBoxToFxPosY(float posY) {
        float y = HEIGHT - (1.0f * HEIGHT) * posY / 100.0f;
        return y;
    }

    //Convert a JavaFX pixel y coordinate to a JBox2D y coordinate
    public static float fxToJboxPosY(float posY) {
        float y = 100.0f - ((posY * 100 * 1.0f) / HEIGHT);
        return y;
    }

    //Convert a JBox2D width to pixel width
    public static float jBoxtoPixelWidth(float width) {
        return WIDTH*width / 100.0f;
    }

    //Convert a JBox2D height to pixel height
    public static float jBoxtoPixelHeight(float height) {
        return HEIGHT*height/100.0f;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("squares DO SOMETHING");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);

        final Group root = new Group();
        final Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        final Ball ball = new Ball(50, 50);

        final Square square = new Square(50.0f, 20.0f, 30.0f, 30.0f, Color.AQUA);

        EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                world.step(2.0f / 60.f, 8, 3);
                Body body = (Body) ball.node.getUserData();
                float xpos = jBoxToFxPosX(body.getPosition().x);
                float ypos = jBoxToFxPosY(body.getPosition().y);
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
        root.getChildren().add(square.node);

        primaryStage.setScene(scene);
        primaryStage.show();

        timeline.playFromStart();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
