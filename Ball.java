package sample;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;


public class Ball {

    public static final int BALL_SIZE = 10;
    //JavaFX UI for ball
    public Node node;
    //X and Y position of the ball in JBox2D world
    private float posX;
    private float posY;
    //     private Color color;
    //Ball radius in pixels
    private int radius;
    /**
     * There are three types bodies in JBox2D – Static, Kinematic and dynamic In
     * this application static bodies (BodyType.STATIC – non movable bodies) are
     * used for drawing hurdles and dynamic bodies (BodyType.DYNAMIC–movable
     * bodies) are used for falling balls
     */
    private BodyType bodyType;
    private Body body;
    //Gradient effects for balls
    private Color color;

    public Ball(float posX, float posY) {
        this(posX, posY, BALL_SIZE, BodyType.DYNAMIC, Color.RED);
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

    /**
     * This method creates a ball by using RoundThing object from JavaFX and
     * CircleShape from JBox2D
     */
    private Node create() {
        //Create an UI for ball - JavaFX code
        Circle ball = new Circle();
        ball.setRadius(radius);
        ball.setFill(color); //set look and feel

        /**
         * Set ball position on JavaFX scene. We need to convert JBox2D
         * coordinates to JavaFX coordinates which are in pixels.
         */
        ball.setLayoutX(Main.jBoxToFxPosX(posX));
        ball.setLayoutY(Main.jBoxToFxPosY(posY));

        ball.setCache(true); //Cache this object for better performance

        //Create an JBox2D body defination for ball.
        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(posX, posY);

        CircleShape cs = new CircleShape();
        cs.m_radius = radius * 0.1f;  //We need to convert radius to JBox2D equivalent

        // Create a fixture for ball
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 1.0f;


        /**
         * Virtual invisible JBox2D body of ball. Bodies have velocity and
         * position. Forces, torques, and impulses can be applied to these
         * bodies.
         */

        body = Main.world.createBody(bd);
        body.createFixture(fd);
        ball.setUserData(body);
        return ball;
    }

    public void addForce(Vec2 force) {
        body.applyForce(force, body.getWorldCenter());
    }
}