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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

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
    public int ballsLeft = 5;
    public double score = 0;
    public String name;
	private Text t = new Text(10, 50, "Baller:" + ballsLeft + "   " + "Poeng:" + score);
	private boolean b = false;
	private boolean ballInPlay = false;
	private boolean gameFinished = false;
	private int i = 0;
	Vector<Bruker> B = new Vector<Bruker>();
	public int antBrukere = 0;
	
	
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
    	// Legg til eksisterende brukere
    	File f = new File("scores.txt");
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            
            Vector<Bruker> deserializeBruker = (Vector<Bruker>)ois.readObject();
            ois.close();
            
            Iterator<Bruker> iter = deserializeBruker.iterator();
            while(iter.hasNext()){
                Bruker s = iter.next();
                B.add(new Bruker(s.getName(), s.getScore()));
                antBrukere++;
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
        
        
        final int ballStartX = 599;
        final int ballStartY = 800;
        
        final Group root = new Group();
        final Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        final Ball[] balls = new Ball[] {
        	new Ball(599, 700),
        	new Ball(599, 730),
        	new Ball(599, 760),
        	new Ball(599, 790),
        	new Ball(599, 800)
        };

        
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
                
                
                for(Ball ball : balls) {
                    Body body = (Body) ball.node.getUserData();
                    float xpos = meterToPixel(body.getPosition().x);
                    float ypos = meterToPixel(-body.getPosition().y);
                    ball.node.setLayoutX(xpos);
                    ball.node.setLayoutY(ypos);
                    
                    if(ball.node.getLayoutY() > 740 && ball.node.getLayoutX() < 580) {
	                	// Sett ballen utenfor skjermen
                    	body.setTransform(new Vec2(5000, 5000), 0);
	                	ballInPlay = false;           
	                } 
                }  
                
                
                if(gameFinished == false && ballInPlay == false && ballsLeft == 0) {
                    b = false;
                	primaryStage.close();
                	NewStage();
                	gameFinished = true;
                } 
                
                if(ballInPlay)
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

        
        for(Ball ball : balls) {
        	root.getChildren().add(ball.node);
        }

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


        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(event -> {
        	
            if (event.getCode() == KeyCode.SPACE) {
            	if(ballInPlay)
            		return;
            	
            	if(i < 5) {
            		balls[i].addForce(new Vec2(0,50));
            		ballInPlay = true;
            	}
            	
                
                b = true;
                ballsLeft--;
                i++;
            }
       
            if (event.getCode() == KeyCode.LEFT) {}
      
            if (event.getCode() == KeyCode.RIGHT) {}
            
            if (event.getCode() == KeyCode.ESCAPE) {
            	ballsLeft = 0;
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
//    		Funket ikke, userdata = null
    	if(b1 == 0.2f & b2 != 0.2f || b1 != 0.2f && b2 == 0.2f) {
    	if(b == true && b1 != 0.6f && b2 != 0.6f) {
    		score+=10;
    		updateScore();
    	}
    	}
    }
	Button button;
	Button top10;
    void NewStage()  {
    	
    		    Stage subStage = new Stage();
    		    subStage.setTitle("Scoreboard navn");
    		            
    		    TextField nameInput = new TextField();
    		  
    		    Text dittNavn = new Text("Din score ble: " + Math.round(score) + "\n" + "Skriv inn ditt navn");
    		    button = new Button("Lagre score");
    		    top10 = new Button("Top 10");
    		    
    		    VBox layout = new VBox(10);
    		    layout.setPadding(new Insets(20,20,20,20));
    		    layout.getChildren().addAll(dittNavn, nameInput, button, top10);
    		    
    		    
    		    Scene scene = new Scene(layout, 300, 200);
    		    subStage.setScene(scene);
    		    subStage.show();
    		    
    		    button.setOnMouseClicked(e -> {
    		    	
    		    	B.add(new Bruker(nameInput.getText(), score));
    		    	
    		        File f = new File("scores.txt");
    		        try {
    		            FileOutputStream fos = new FileOutputStream(f);
    		            ObjectOutputStream oos = new ObjectOutputStream(fos);
    		            oos.writeObject(B);
    		            oos.close();
    		            System.out.println("data write successfully");
    		            
    		        } catch (FileNotFoundException ex) {
    		            ex.printStackTrace();
    		        } catch (IOException ex) {
    		            ex.printStackTrace();
    		        }
    		    
    		    	});
    		    
    		 
    		    top10.setOnMouseClicked(e -> {
    		    	int i = 0;
    		    	SortedMap<Double, String> top10Map = new TreeMap<>(Collections.reverseOrder());
    		    	File f = new File("scores.txt");
    		        try {
    		            FileInputStream fis = new FileInputStream(f);
    		            ObjectInputStream ois = new ObjectInputStream(fis);
    		            
    		            
    		            Vector<Bruker> deserializeBruker = (Vector<Bruker>)ois.readObject();
    		            ois.close();
    		            
    		            Iterator<Bruker> iter = deserializeBruker.iterator();
    		            while(iter.hasNext()){
    		                Bruker s = iter.next();
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
						System.out.println("End of list");
					}
    		    	});
    }
    protected void updateScore() {
    	t.setText("Baller:" + ballsLeft + "   " + "Poeng:" + Math.round(score * 100.0) / 100.0);
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
