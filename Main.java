package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;



public class Main extends Application implements ContactListener {
	public static final float DEG_TO_RAD = 0.01745329f; // Degrees to radiance
	public static final float RAD_TO_DEG = 57.2957795f; // Radiance to degrees
    public static final float PPM = 32.0f; // Pixel per metre 
    public final static World world = new World(new Vec2(0.0f, -5.5f));
    private static final float WIDTH = 600;
    private static final float HEIGHT = 800;
    private int ballsLeft = 5;
    private double score = 0;
    private boolean b = false;
    private boolean ballInPlay = false;
    private boolean gameFinished = false;
    private int count = 0;
    public Vector<User> B = new Vector<User>();
    private int power = 5;
    private Text text = new Text(10, 50, "Baller:" + ballsLeft + "   " + "Styrke : " + power + "	" + "Poeng:" + Math.round(score * 100.0) / 100.0);
	
    /**
     * Converts JBox2D metres to JavaFX pixels
     * @param an objects meter for converting
     * @return the pixel value of the metre
     * @author Henrik
	*/
    public static float meterToPixel(float meter) {
        float pixel = meter * PPM;
        return pixel;
    }
    
    /**
     * Converts JavaFX pixels to JBox2D metres
     * @param an objects pixel for converting
     * @return the metre value of the pixel
     * @author Henrik
	*/
    public static float pixelToMeter(float pixel) {
        float meter = pixel / PPM;
        return meter;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	// Adds previous users to the Vector-list for providing the top10
    	File file = new File("scores.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
          
            Vector<User> deserializeBruker = (Vector<User>)ois.readObject();
            ois.close();
            
            Iterator<User> iter = deserializeBruker.iterator();
            while(iter.hasNext()){
                User s = iter.next();
                B.add(new User(s.getName(), s.getScore()));
            }
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
              
    	world.setSleepingAllowed(true); 
        primaryStage.setTitle("Pinball");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        Main.world.setContactListener(this);
        
        final Group root = new Group();
        final Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        // Adding balls
        final Ball[] balls = new Ball[] {
        	new Ball(599, 700),
        	new Ball(599, 730),
        	new Ball(599, 760),
        	new Ball(599, 790),
        	new Ball(599, 800)
        };

        // Building walls
        final Square squareLeft = new Square(1, 810, 1, 810, 0, Color.WHITE);
        final Square squareRight = new Square(610, 810, 1, 810, 0, Color.WHITE);
        final Square squareTop = new Square(610, 1, 610, 1, 0, Color.WHITE);
        final Square squareBottom = new Square(610, 810, 610, 1, 0, Color.WHITE);
        final Square kickLane = new Square(587, 810, 1, 730, 0, Color.WHITE);
        
        final Square ballCatch1 = new Square(40, 520, 1, 60, 0, Color.WHITE);
        final Square ballCatch2 = new Square(548, 520, 1, 60, 0, Color.WHITE);
        final Square ballCatch3 = new Square(102, 602, 65, 1, 20, Color.WHITE);
        final Square ballCatch4 = new Square(486, 602, 65, 1, -20, Color.WHITE);
        
        final Square ballCatch5 = new Square(72, 506, 1, 45, 0, Color.WHITE);
        final Square ballCatch6 = new Square(104, 563, 35, 1, 20, Color.WHITE);
        final Square ballCatch7 = new Square(104, 517, 1, 65, -29, Color.WHITE);
        
        final Square ballCatch8 = new Square(516, 506, 1, 45, 0, Color.WHITE);
        final Square ballCatch9 = new Square(484, 563, 35, 1, -20, Color.WHITE);
        final Square ballCatch10 = new Square(484, 517, 1, 65, 29, Color.WHITE);
        
        final Square bottom = new Square(100, 650, 110, 1, 20, Color.WHITE);
        final Square bottom2 = new Square(484, 650, 110, 1, -20, Color.WHITE);
        final Square bottom3 = new Square(202, 750, 1, 62, 0, Color.WHITE);
        final Square bottom4 = new Square(382, 750, 1, 62, 0, Color.WHITE);

        final Square topKickLane = new Square(660, 72, 200, 20, 43, Color.WHITE);
        
        // Building obstacles
        final RoundThing r1 = new RoundThing(190, 200, 30, Color.RED);
        final RoundThing r2 = new RoundThing(390, 200, 30, Color.RED);
        final RoundThing r3 = new RoundThing(290, 300, 30, Color.RED);
        final RoundThing r4 = new RoundThing(230, 430, 20, Color.BLUE);
        //final RoundThing r5 = new RoundThing(300, 480, 20, Color.BLUE);
        final RoundThing r6 = new RoundThing(370, 430, 20, Color.BLUE);
        
        // Building flipperr
        final Flipper[] flippers = {
    		new Flipper(true, 420, 632, 50, 8, 0, Color.GREEN),
    		new Flipper(false, 170, 632, 50, 8, 0, Color.GREEN),
        };
        
        // Handling step and physics
        EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                world.step(2.0f / 60.f, 8, 3);

                for(Ball ball : balls) {
                    Body body = (Body) ball.node.getUserData();
                    float xpos = meterToPixel(body.getPosition().x);
                    float ypos = meterToPixel(-body.getPosition().y);
                    ball.node.setLayoutX(xpos);
                    ball.node.setLayoutY(ypos);
                    
                    // Getting rid of the ball when it falls below the flippers
                    if(ball.node.getLayoutY() > 740 && ball.node.getLayoutX() < 580) {
                    	body.setTransform(new Vec2(5000, 5000), 0);
	                	ballInPlay = false;           
	                } 
                }  
                
                // Handling flipper position
                for(Flipper flipper : flippers) {
                	Body body = (Body)flipper.node.getUserData();
                	flipper.node.setRotate(-(body.getAngle() * RAD_TO_DEG));
                	
                    float xpos = meterToPixel(body.getPosition().x);
                    float ypos = meterToPixel(-body.getPosition().y);
                    ((Rectangle)flipper.node).setX(xpos - flipper.sWidth);
                    ((Rectangle)flipper.node).setY(ypos - flipper.sHeight);
                }
                // Flippers resting until activated
                flippers[0].joint.enableMotor(false);
                flippers[1].joint.enableMotor(false);

                 // Closing the game and opens popup       
                if(gameFinished == false && ballInPlay == false && ballsLeft == 0) {
                    b = false;
                	primaryStage.close();
                	NewStage();
                	gameFinished = true;
                } 
                // Adding to the score if a ball is moving
                if(ballInPlay)
                score+=0.01;
                
                // Updating the score
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
        
        text.setFont(new Font(20));
        text.setFill(Color.WHITE);
        
        // Adding the balls to the game
        for(Ball ball : balls) {
        	root.getChildren().add(ball.node);
        }
        // Adding map elements
        root.getChildren().add(squareBottom.node);
        root.getChildren().add(squareTop.node);
        root.getChildren().add(squareRight.node);
        root.getChildren().add(squareLeft.node);
        root.getChildren().add(kickLane.node);
        root.getChildren().add(r1.node);
        root.getChildren().add(r2.node);
        root.getChildren().add(r3.node);
        root.getChildren().add(r4.node);
        root.getChildren().add(r6.node);
        root.getChildren().add(topKickLane.node);
        root.getChildren().add(text);
        root.getChildren().add(ballCatch1.node);
        root.getChildren().add(ballCatch2.node);
        root.getChildren().add(ballCatch3.node);
        root.getChildren().add(ballCatch4.node);
        root.getChildren().add(ballCatch5.node);
        root.getChildren().add(ballCatch6.node);
        root.getChildren().add(ballCatch7.node);
        root.getChildren().add(ballCatch8.node);
        root.getChildren().add(ballCatch9.node);
        root.getChildren().add(ballCatch10.node);
        root.getChildren().add(bottom.node);
        root.getChildren().add(bottom2.node);
        root.getChildren().add(bottom3.node);
        root.getChildren().add(bottom4.node);
        
        // Adding the flippers to the game
        for(Flipper flipper : flippers) {
        	root.getChildren().add(flipper.node);
        }

        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Adjusting the launch power and launching the ball
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
            	if(ballInPlay)
            		return;
            	if(count < 5 && power == 10) {
            		balls[count].addForce(new Vec2(0,80));
            		ballInPlay = true;
            	}
            	if(count < 5 && power == 9) {
            		balls[count].addForce(new Vec2(0,75));
            		ballInPlay = true;
            	}
            	if(count < 5 && power == 8) {
            		balls[count].addForce(new Vec2(0,70));
            		ballInPlay = true;
            	}
            	if(count < 5 && power == 7) {
            		balls[count].addForce(new Vec2(0,65));
            		ballInPlay = true;
            	}
            	if(count < 5 && power == 6) {
            		balls[count].addForce(new Vec2(0,60));
            		ballInPlay = true;
            	}
            	if(count < 5 && power == 5) {
            		balls[count].addForce(new Vec2(0,55));
            		ballInPlay = true;
            	}
            	if(count < 5 && power == 4) {
            		balls[count].addForce(new Vec2(0,50));
            		ballInPlay = true;
            	}
            	if(count < 5 && power == 3) {
            		balls[count].addForce(new Vec2(0,45));
            		ballInPlay = true;
            	}
            	if(count < 5 && power == 2) {
            		balls[count].addForce(new Vec2(0,40));
            		ballInPlay = true;
            	}
            	if(count < 5 && power == 1) {
            		balls[count].addForce(new Vec2(0,35));
            		ballInPlay = true;
            	}

                b = true;
                ballsLeft--;
                count++;
            }
       
            // Moving the right flipper
            if (event.getCode() == KeyCode.RIGHT) {
                flippers[0].joint.enableMotor(true);
                flippers[0].joint.setMotorSpeed(10);
            }
            // Moving the left flipper
            if (event.getCode() == KeyCode.LEFT) {
                flippers[1].joint.enableMotor(true);
                flippers[1].joint.setMotorSpeed(-10);
            }
            // Quits the game
            if (event.getCode() == KeyCode.ESCAPE) {
            	ballsLeft = 0;
            }
            // Adjusting launch power down
            if (event.getCode() == KeyCode.DIGIT1 && power > 1 ) {
				power--;
				updateScore();
            }
            // Adjusting launch power up
            if (event.getCode() == KeyCode.DIGIT2 && power <= 9) {
				power++;
				updateScore();
            }   
        });
     
        timeline.playFromStart();
             
    }
    /**
     * Registrates collisions
     * @param a contact is made whenever something collides
     * @return null
     * @author Andreas
	*/
    public void beginContact(Contact cp)throws NullPointerException{
        
    	Fixture f1 = cp.getFixtureA();
    	Fixture f2 = cp.getFixtureB();
    	
    	float b1 = f1.getDensity();
    	float b2 = f2.getDensity();
    	
    	// Checking whether the ball hits a ball, wall or obstacle
    	if(b1 == 0.2f & b2 != 0.2f || b1 != 0.2f && b2 == 0.2f) {
    	if(b == true && b1 != 0.6f && b2 != 0.6f) {
    		score+=10;
    		updateScore();
    	}
    	}
    }
	Button button;
	Button top10;
	/**
     * Creates a new scene when the game ends
     * Saves the current user and displaying the top 10 list trough button-events
     * @param null
     * @return null
     * @author Simen
	*/
    void NewStage()  {
    		    Stage subStage = new Stage();
    		    subStage.setTitle("Scoreboard name");
    		            
    		    TextField nameInput = new TextField();
    		  
    		    Text yourname = new Text("Your score: " + Math.round(score) + "\n" + "Enter your name");
    		    button = new Button("Save score");
    		    top10 = new Button("Top 10");
    		    
    		    VBox layout = new VBox(10);
    		    layout.setPadding(new Insets(20,20,20,20));
    		    layout.getChildren().addAll(yourname, nameInput, button, top10);
    		    
    		    Scene scene = new Scene(layout, 300, 200);
    		    subStage.setScene(scene);
    		    subStage.show();
    		    
    		    // Adding the current user to a file
    		    button.setOnMouseClicked(e -> {
    		    	
    		    	B.add(new User(nameInput.getText(), score));
    		    	
    		        File file = new File("scores.txt");
    		        try {
    		            FileOutputStream fos = new FileOutputStream(file);
    		            ObjectOutputStream oos = new ObjectOutputStream(fos);
    		            oos.writeObject(B);
    		            oos.close();
    		            System.out.println("Saved");
    		            
    		        } catch (FileNotFoundException ex) {
    		            ex.printStackTrace();
    		        } catch (IOException ex) {
    		            ex.printStackTrace();
    		        }
    		    
    		    	});
    		    
    		    // Displaying the top 10 list 
    		    top10.setOnMouseClicked(e -> {
    		    	int i = 0;
    		    	SortedMap<Double, String> top10Map = new TreeMap<>(Collections.reverseOrder());
    		    	File file = new File("scores.txt");
    		        try {
    		            FileInputStream fis = new FileInputStream(file);
    		            ObjectInputStream ois = new ObjectInputStream(fis);

    		            Vector<User> deserializeBruker = (Vector<User>)ois.readObject();
    		            ois.close();
    		            
    		            Iterator<User> iter = deserializeBruker.iterator();
    		            while(iter.hasNext()){
    		                User s = iter.next();
    		                top10Map.put(s.getScore(), s.getName());
    		            }
    		            Set set = top10Map.entrySet(); 
    		            Iterator it = set.iterator();
    		            
    		            	while(i<10) {
    		                SortedMap.Entry me = (SortedMap.Entry)it.next(); 
    		                System.out.print(me.getKey() + ": "); 
    		                System.out.println(me.getValue()); 
    		                i++;
    		            	}
    		    
    		        } catch (FileNotFoundException ex) {
    		            ex.printStackTrace();
    		        } catch (IOException ex) {
    		            ex.printStackTrace();
    		        } catch (ClassNotFoundException ex) {
    		            ex.printStackTrace();
    		        }
    		        catch (NoSuchElementException e2) {
						System.out.println("Slutt på liste..");
					}
    		    	});
    }
    /**
     * Updating and Displaying the score
     * @param null
     * @return null
     * @author Andreas
	*/
    protected void updateScore() {
    	text.setText("Balls:" + ballsLeft + "   " + "Power : " + power + "	" + "Points:" + Math.round(score * 100.0) / 100.0);
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
