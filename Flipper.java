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
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;


public class Flipper {
	public static final float DEG_TO_RAD = 0.01745329f;
	
    public Node node;
    public RevoluteJoint joint;

    public float sWidth;
    public float sHeight;

    private float posX;
    private float posY;
    private float angle;
    private static Color color;
    
    private static Rectangle rc;

    public Flipper(float posX, float posY, float sWidth, float sHeight, float angle, Color color) {
        this.posX = posX;
        this.posY = posY;
        this.sWidth = sWidth;
        this.sHeight = sHeight;
        this.color = color;
        this.angle = angle;
        node = create();
    }

    public Node create() {
        rc = new Rectangle();
        rc.setX(posX - sWidth);
        rc.setY(posY - sHeight);
        rc.setWidth(sWidth*2);
        rc.setHeight(sHeight*2);
        rc.setFill(color);
        rc.setRotate((double)angle);


        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(Main.pixelToMeter(posX), Main.pixelToMeter(-posY));
        

        PolygonShape ps = new PolygonShape();

        ps.setAsBox(Main.pixelToMeter(sWidth), Main.pixelToMeter(sHeight), new Vec2(0.0f, 0.0f),  ((-angle) * DEG_TO_RAD));

        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 0.6f;
        fd.friction = 0.3f;
        fd.isSensor = false;
        

        Body body = Main.world.createBody(bd);
        body.createFixture(fd);
        
        Body pivot = CreateCircle(Main.pixelToMeter(posX), Main.pixelToMeter(-posY), 0);

        RevoluteJointDef revJoint = new RevoluteJointDef();
        revJoint.bodyA = body;
        revJoint.bodyB = pivot;
        revJoint.localAnchorA.set(new Vec2(Main.pixelToMeter(50),Main.pixelToMeter(0)));
        revJoint.lowerAngle = -25 * Main.DEG_TO_RAD;
        revJoint.upperAngle = 25 * Main.DEG_TO_RAD;
        revJoint.enableLimit = true;
        
        revJoint.maxMotorTorque = 1000.0f;
        revJoint.enableMotor = false;

        joint = (RevoluteJoint)Main.world.createJoint(revJoint);
        
        
        rc.setUserData(body);
        return rc;
    }
    
    Body CreateCircle(float x, float y, int radius) {
    	BodyDef bodyDef = new BodyDef();
    	bodyDef.type = BodyType.STATIC;
    	bodyDef.position.set(x, y);
    	
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