package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.*;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;



public class Main extends Application implements ContactListener {

    public static final float PPM = 32.0f;
    public final static World world = new World(new Vec2(0.0f, -5.5f));
    private static final float WIDTH = 600;
    private static final float HEIGHT = 800;
    public int balls = 5;
    public double score = 0;
    public String name;
	private Text t = new Text(10, 50, "Baller:" + balls + "   " + "Poeng:" + score);
	private boolean b = false;
	private int i = 0;
	
	
	
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
        Main.world.setContactListener(this);
        
        
        final Group root = new Group();
        final Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        final Ball[] ball = new Ball[5];
        final Ball ball0 = new Ball(599, 700);
        ball[0] = ball0;
        final Ball ball1 = new Ball(599, 730);
        ball[1] = ball1;
        final Ball ball2 = new Ball(599, 760);
        ball[2] = ball2;
        final Ball ball3 = new Ball(599, 790);
        ball[3] = ball3;
        final Ball ball4 = new Ball(599, 800);
        ball[4] = ball4;
        
        final Square squareLeft = new Square(1, 810, 1, 810, 0, Color.WHITE);
        final Square squareRight = new Square(610, 810, 1, 810, 0, Color.WHITE);
        final Square squareTop = new Square(610, 1, 610, 1, 0, Color.WHITE);
        final Square squareBottom = new Square(610, 810, 610, 1, 0, Color.WHITE);
        final Square kickLane = new Square(587, 810, 1, 710, 0, Color.WHITE);
        
        final Square topKickLane = new Square(660, 20, 200, 20, 43, Color.WHITE);
        final Square topKickLane2 = new Square(660, 72, 200, 20, 43, Color.WHITE);
        
        final RoundThing r1 = new RoundThing(180, 175, 30, Color.RED);
        final RoundThing r2 = new RoundThing(420, 175, 30, Color.RED);
        final RoundThing r3 = new RoundThing(300, 300, 30, Color.RED);
        
        
        
        EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                world.step(2.0f / 60.f, 8, 3);
                Body body = (Body) ball0.node.getUserData();
                float xpos = meterToPixel(body.getPosition().x);
                float ypos = meterToPixel(-body.getPosition().y);
                ball0.node.setLayoutX(xpos);
                ball0.node.setLayoutY(ypos);
                
                Body body1 = (Body) ball1.node.getUserData();
                float xpos1 = meterToPixel(body1.getPosition().x);
                float ypos1 = meterToPixel(-body1.getPosition().y);
                ball1.node.setLayoutX(xpos1);
                ball1.node.setLayoutY(ypos1);
                
                Body body2 = (Body) ball2.node.getUserData();
                float xpos2 = meterToPixel(body2.getPosition().x);
                float ypos2 = meterToPixel(-body2.getPosition().y);
                ball2.node.setLayoutX(xpos2);
                ball2.node.setLayoutY(ypos2);
                
                Body body3 = (Body) ball3.node.getUserData();
                float xpos3 = meterToPixel(body3.getPosition().x);
                float ypos3 = meterToPixel(-body3.getPosition().y);
                ball3.node.setLayoutX(xpos3);
                ball3.node.setLayoutY(ypos3);
                
                Body body4 = (Body) ball4.node.getUserData();
                float xpos4 = meterToPixel(body4.getPosition().x);
                float ypos4 = meterToPixel(-body4.getPosition().y);
                ball4.node.setLayoutX(xpos4);
                ball4.node.setLayoutY(ypos4);
                score+=0.01;
                if(b == true) {
                updateScore();
                }
                
               
            }
        };

     

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        Duration duration = Duration.seconds(1.0 / 60.0);
        KeyFrame frame = new KeyFrame(duration, ae, null, null);

        
        timeline.getKeyFrames().add(frame);
        
        t.setFont(new Font(20));
        t.setFill(Color.WHITE);

        root.getChildren().add(ball0.node);
        root.getChildren().add(ball1.node);
        root.getChildren().add(ball2.node);
        root.getChildren().add(ball3.node);
        root.getChildren().add(ball4.node);
        root.getChildren().add(squareBottom.node);
        root.getChildren().add(squareTop.node);
        root.getChildren().add(squareRight.node);
        root.getChildren().add(squareLeft.node);
        root.getChildren().add(kickLane.node);
        root.getChildren().add(r1.node);
        root.getChildren().add(r2.node);
        root.getChildren().add(r3.node);
        root.getChildren().add(topKickLane.node);
        root.getChildren().add(t);

        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(event -> {
        	
            if (event.getCode() == KeyCode.SPACE) {
            	if(i<5) {
                ball[i].addForce(new Vec2(0,50));
            	}
                System.out.println(i);
                b = true;
                balls--;
                i++;
            	
                if (balls == -1) {
                	b = false;
                	primaryStage.close();
                	NewStage();
                }
                
            
            }
       
            if (event.getCode() == KeyCode.LEFT) {}
      
            if (event.getCode() == KeyCode.RIGHT) {}
            
        });
     


        timeline.playFromStart();
             
    }
    public void beginContact(Contact cp)throws NullPointerException{
        
    	Fixture f1 = cp.getFixtureA();
    	Fixture f2 = cp.getFixtureB();
    	
    	float b1 = f1.getDensity();
    	float b2 = f2.getDensity();
    	
//    	Object o1 = b1.getUserData();
//    	Object o2 = b2.getUserData();
//    		Funket ikke, userdata = null
    	
    	if(b == true && b1 != 0.6f && b2 != 0.6f) {
    		score+=10;
    		updateScore();
    	}
    }
	Button button;
    void NewStage()  {
    	
    		    Stage subStage = new Stage();
    		    subStage.setTitle("Scoreboard navn");
    		            
    		    TextField nameInput = new TextField();
    		  
    		    Text dittNavn = new Text("Din score ble: " + Math.round(score) + "\n" + "Skriv inn ditt navn");
    		    button = new Button("Lagre score");
    		    
    		    VBox layout = new VBox(10);
    		    layout.setPadding(new Insets(20,20,20,20));
    		    layout.getChildren().addAll(dittNavn, nameInput, button);
    		    
    		    
    		    Scene scene = new Scene(layout, 300, 200);
    		    subStage.setScene(scene);
    		    subStage.show();
    }
    
    protected void updateScore() {
    	t.setText("Baller:" + balls + "   " + "Poeng:" + Math.round(score * 100.0) / 100.0);
	}

	public static void main(String[] args) {
        launch(args);
    }


	@Override
	public void endContact(Contact cp) {}


	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}


	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {}
}
