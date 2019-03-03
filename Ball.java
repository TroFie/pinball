package sample;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

// The method creates a ball, using JavaFX and CircleShape from JBox2D

public class Ball {

    public static final int BALL_SIZE = 10;
    public Node node;
    private float posX;
    private float posY;
    private int radius;
    private BodyType bodyType;
    private Body body;
    private Color color;

    
    public Ball(float posX, float posY) {
    	
        this(posX, posY, BALL_SIZE, BodyType.DYNAMIC, Color.GRAY);
        this.posX = posX;
        this.posY = posY;
    }

    public Ball(float posX, float posY, int radius, BodyType bodyType, Color color) {
    	
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
        this.bodyType = bodyType;
        this.color = color;
        node = create();
    }

     
    private Node create() {
      
        Circle ball = new Circle();
        ball.setRadius(radius);
        ball.setFill(color); 
        ball.setLayoutX(posX);
        ball.setLayoutY(posY);
        ball.setCache(true); 

       
        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(Main.pixelToMeter(posX), Main.pixelToMeter(-posY));

        CircleShape cs = new CircleShape();
        cs.m_radius = Main.pixelToMeter(radius);  

        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.2f;
        fd.friction = 5f;
        fd.restitution = 0.7f;

        body = Main.world.createBody(bd);
        body.createFixture(fd);
        ball.setUserData(body);
        return ball;
    }

    // Metode som får ballen til å fly oppover. Brukes til utskytning.
    
    public void addForce(Vec2 force) {
        body.applyForce(force, body.getWorldCenter());
    }
}
