package sample;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;


public class Square {
   
    public Node node;
    private float posX;
    private float posY;
    private float sWidth;
    private float sHeight;
    private float angle;
    private static Color color;
    private static Rectangle rc;

    /**
     * Creates and initializes the square
     * @param Y and X positions, width and height, angle and color
     * @return Null
     * @author Henrik
    */

    public Square(float posX, float posY, float sWidth, float sHeight, float angle, Color color) {
        this.posX = posX;
        this.posY = posY;
        this.sWidth = sWidth;
        this.sHeight = sHeight;
        this.color = color;
        this.angle = angle;
//        this.bodyType = bodyType;
        node = create();
    }
    
    /**
     * Initializes a square, and sets up the JavaFX-graphic. Sets up the physics object
     * @param Null
     * @return Returns a square with the specified fixture, body and definition
     * @author Henrik
	*/
    public Node create() {
        rc = new Rectangle();
        rc.setX(posX - sWidth);
        rc.setY(posY - sHeight);
        rc.setWidth(sWidth*2);
        rc.setHeight(sHeight*2);
        rc.setFill(color);
        rc.setRotate((double)angle);

        // Create a body definition for JBox2D
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(Main.pixelToMeter(posX), Main.pixelToMeter(-posY));
        
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(Main.pixelToMeter(sWidth), Main.pixelToMeter(sHeight), new Vec2(0.0f, 0.0f),  ((-angle) * Main.DEG_TO_RAD));

        // Set fixture
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 0.6f;
        fd.friction = 0.3f;
        fd.isSensor = false;
        
        // Creating the body
        Body body = Main.world.createBody(bd);
        body.createFixture(fd);
        rc.setUserData(body);
        return rc;
    }
}
