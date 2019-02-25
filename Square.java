package sample;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;


public class Square {
    //JavaFX UI for square
    /**
     * 1 define body 2 create body 3 create shape 4 create fixture 5 attach
     * shape to body
     */
    public Node node;
    private float posX;
    private float posY;
    private float sWidth;
    private float sHeight;
    private static Color color;
//    private BodyType bodyType;

    private static Rectangle rc;
//    private float angle;
//    private BodyDef bd;

    public Square(float posX, float posY, float sWidth, float sHeight, Color color) {
        this.posX = posX;
        this.posY = posY;
        this.sWidth = sWidth;
        this.sHeight = sHeight;
        this.color = color;
//        this.bodyType = bodyType;
        node = create();
    }

    public Node create() {
        //UI for square in JavaFX
        rc = new Rectangle();
        rc.setX(posX - sWidth);
        rc.setY(posY - sHeight);
        rc.setWidth(sWidth);
        rc.setHeight(sHeight);
        rc.setFill(color);


        //Create an JBox2D body definition for square.
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(Main.pixelToMeter(posX), Main.pixelToMeter(-posY));

        PolygonShape ps = new PolygonShape();
        ps.setAsBox(Main.pixelToMeter(sWidth), Main.pixelToMeter(sHeight));

        //fixture for polygon, in this case square
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 0.6f;
        fd.friction = 0.3f;
        fd.isSensor = false;

        Body body = Main.world.createBody(bd);
        body.createFixture(fd);
        rc.setUserData(body);
        return rc;
    }
}