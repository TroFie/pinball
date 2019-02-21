package sample;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;


public class RoundThing {
    //JavaFX UI for square
    /**
     * 1 define body 2 create body 3 create shape 4 create fixture 5 attach
     * shape to body
     */
    public Node node;
    private float posX;
    private float posY;
    private float sRadius;
    private static Color color;
//    private BodyType bodyType;

    private static Circle circle;
//    private float angle;
//    private BodyDef bd;

    public RoundThing(float posX, float posY, float sRadius, Color color) {
        this.posX = posX;
        this.posY = posY;
        this.sRadius = sRadius;
        this.color = color;
//        this.bodyType = bodyType;
        node = create();
    }

    public Node create() {
        //UI for square in JavaFX
        circle = new Circle();
        circle.setLayoutX(Main.jBoxToFxPosX(posX));
        circle.setLayoutY(Main.jBoxToFxPosY(posY));
        circle.setRadius(sRadius);
        circle.setFill(color);


        //Create an JBox2D body definition for square.
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(posX, posY); //do i have to convert these??

        CircleShape ps = new CircleShape();
        ps.m_radius = sRadius;
        //ps.setAsBox(Main.jBoxtoPixelWidth(sRadius /80),Main.jBoxtoPixelHeight(sHeight/80)); //wrong!! we need to convert to Jbox equiualent

        //fixture for polygon, in this case square
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 0.6f;
        fd.friction = 0.3f;
        fd.isSensor = false;

        Body body = Main.world.createBody(bd);
        body.createFixture(fd);
        circle.setUserData(body);
        return circle;
    }
}