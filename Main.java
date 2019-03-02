package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
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
	public static final float DEG_TO_RAD = 0.01745329f;
	public static final float RAD_TO_DEG = 57.2957795f;

    public static final float PPM = 32.0f;
    public final static World world = new World(new Vec2(0.0f, -5.5f));
    private static final float WIDTH = 600;
    private static final float HEIGHT = 800;
    public int balls = 2000;
    public double score = 0;
	private Text t = new Text(10, 50, "Baller:" + balls + "   " + "Poeng:" + score);
	private boolean b = false;
	
    //Convert a JBox2D x coordinate to a JavaFX pixel x coordinat
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
        
        
        final int ballStartX = 599;
        final int ballStartY = 800;
        
        final Group root = new Group();
        final Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        final Ball ball = new Ball(ballStartX, ballStartY);
       
        final Square squareLeft = new Square(1, 810, 1, 810, 0, Color.WHITE);
        final Square squareRight = new Square(610, 810, 1, 810, 0, Color.WHITE);
        final Square squareTop = new Square(610, 1, 610, 1, 0, Color.WHITE);
        final Square squareBottom = new Square(610, 810, 610, 1, 0, Color.WHITE);
        final Square kickLane = new Square(587, 810, 1, 710, 0, Color.WHITE);
     
        final Square ballCatch1 = new Square(100, 650, 110, 1, 20, Color.WHITE);
        final Square ballCatch2 = new Square(484, 650, 110, 1, -20, Color.WHITE);
        final Square ballCatch3 = new Square(202, 750, 1, 62, 0, Color.WHITE);
        final Square ballCatch4 = new Square(382, 750, 1, 62, 0, Color.WHITE);
        
        
        final Square topKickLane2 = new Square(660, 72, 200, 20, 43, Color.WHITE);
        
        final RoundThing r1 = new RoundThing(180, 175, 30, Color.RED);
        final RoundThing r2 = new RoundThing(400, 175, 30, Color.RED);
        final RoundThing r3 = new RoundThing(290, 300, 30, Color.RED);
        
        final Flipper[] flippers = {
    		new Flipper(100, 300, 50, 5, 0, Color.WHITE),
        		
        };
        
        
        
        
        
        EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                world.step(2.0f / 60.f, 8, 3);
                Body body = (Body) ball.node.getUserData();
                float xpos = meterToPixel(body.getPosition().x);
                float ypos = meterToPixel(-body.getPosition().y);
                ball.node.setLayoutX(xpos);
                ball.node.setLayoutY(ypos);
                
                float rotAngle = body.getAngle() * RAD_TO_DEG;
                
                ball.node.setRotate(rotAngle);
                
                score+=0.01;
                
                if(balls > 0) {
	                if(ball.node.getLayoutY() > 740 && ball.node.getLayoutX() < 580) {
	                	balls--;
	                	
	                	if(balls > 0) {
		                	body.setTransform(new Vec2(Main.pixelToMeter(ballStartX), Main.pixelToMeter(-ballStartY)), 0);
		                	body.setLinearVelocity(new Vec2(0, 0));
	                	}
	                	else {
	                      	body.setTransform(new Vec2(Main.pixelToMeter(2000), Main.pixelToMeter(2000)), 0);
		                	body.setLinearVelocity(new Vec2(0, 0));
	                	}
	                }
                }
                else {
                	b = false;
                	t.setText("Du tapte!.!");
                }
                
                
                if(b == true) {
                	updateScore();
                }
                
                
                for(Flipper flipper : flippers) {
                    body = (Body) flipper.node.getUserData();

                       
                    rotAngle = body.getAngle() * RAD_TO_DEG;
                    
                    flipper.node.setRotate(rotAngle);    
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

        root.getChildren().add(ball.node);
        root.getChildren().add(squareBottom.node);
        root.getChildren().add(squareTop.node);
        root.getChildren().add(squareRight.node);
        root.getChildren().add(squareLeft.node);
        root.getChildren().add(kickLane.node);
        root.getChildren().add(r1.node);
        root.getChildren().add(r2.node);
        root.getChildren().add(r3.node);
        root.getChildren().add(topKickLane2.node);
        root.getChildren().add(t);
        root.getChildren().add(ballCatch1.node);
        root.getChildren().add(ballCatch2.node);
        root.getChildren().add(ballCatch3.node);
        root.getChildren().add(ballCatch4.node);
        
        for(Flipper flipper : flippers) {
        	root.getChildren().add(flipper.node);
        }
        
        
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                ball.addForce(new Vec2(0,50));
                b = true;
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
    public void beginContact(Contact cp)throws NullPointerException{
    
    	Fixture f1 = cp.getFixtureA();
    	Fixture f2 = cp.getFixtureB();
    	
    	float b1 = f1.getDensity();
    	float b2 = f2.getDensity();
    	
//    	Object o1 = b1.getUserData();
//    	Object o2 = b2.getUserData();
    	
    	if(b == true && b1 != 0.6f && b2 != 0.6f) {
    		score+=10;
    		updateScore();
    		
    		
    	}
    
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
