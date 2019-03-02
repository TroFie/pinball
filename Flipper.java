package sample;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import org.jbox2d.collision.shapes.CircleShape;
import javafx.scene.transform.Rotate;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;


public class Flipper {
    //JavaFX UI for square
    /**
     * 1 define body 2 create body 3 create shape 4 create fixture 5 attach
     * shape to body
     */
	
	public static final float DEG_TO_RAD = 0.01745329f;
	
    public Node node;
    private float posX;
    private float posY;
    private float sWidth;
    private float sHeight;
    private float angle;
    private static Color color;
//    private BodyType bodyType;

    private static Rectangle rc;
//    private float angle;
//    private BodyDef bd;

    public Flipper(float posX, float posY, float sWidth, float sHeight, float angle, Color color) {
        this.posX = posX;
        this.posY = posY;
        this.sWidth = sWidth;
        this.sHeight = sHeight;
        this.color = color;
        this.angle = angle;
//        this.bodyType = bodyType;
        node = create();
    }

    public Node create() {
        //UI for square in JavaFX
        rc = new Rectangle();
        rc.setX(posX - sWidth);
        rc.setY(posY - sHeight);
        rc.setWidth(sWidth*2);
        rc.setHeight(sHeight*2);
        rc.setFill(color);
        rc.setRotate((double)angle);


        //Create an JBox2D body definition for square.
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(Main.pixelToMeter(posX), Main.pixelToMeter(-posY));
        

        PolygonShape ps = new PolygonShape();

        ps.setAsBox(Main.pixelToMeter(sWidth), Main.pixelToMeter(sHeight), new Vec2(0.0f, 0.0f),  ((-angle) * DEG_TO_RAD));

        //fixture for polygon, in this case square
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 0.6f;
        fd.friction = 0.3f;
        fd.isSensor = false;
        

        Body body = Main.world.createBody(bd);
        body.createFixture(fd);
        
        Body pivot = CreateCircle(110, 457, 3);

        RevoluteJointDef revJoint = new RevoluteJointDef();
        revJoint.initialize(body, pivot, pivot.getWorldCenter()); //new org.jbox2d.common.Vec2(20, 20));
        revJoint.motorSpeed = 3.14f * 2;
        revJoint.maxMotorTorque = 1000.0f;
        revJoint.enableMotor = true;
        revJoint.localAnchorA.set(new Vec2(Main.pixelToMeter(10),Main.pixelToMeter(8)));

        Main.world.createJoint(revJoint);
        

        //body.setTransform(body.getPosition(), -(angle * DEG_TO_RAD));
        
        
        rc.setUserData(body);
        return rc;
    }
    
    Body CreateCircle(int x, int y, int radius) {
    	BodyDef bodyDef = new BodyDef();
    	bodyDef.type = BodyType.DYNAMIC;
    	bodyDef.position.set(Main.pixelToMeter(x), Main.pixelToMeter(y));
    	
    	Body body = Main.world.createBody(bodyDef);
    	
    	CircleShape shape = new CircleShape();
    	shape.setRadius(Main.pixelToMeter(radius));
    	FixtureDef fixture = new FixtureDef();
    	fixture.shape = shape;
    	fixture.density = 1.0f;
    	
    	body.createFixture(fixture);
    	
    	return body;
    }
}