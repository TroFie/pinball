package sample;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;


public class RoundThing {
    public Node node;
    private float posX;
    private float posY;
    private float sRadius;
    private Color color;

    private static Circle circle;
    /**
     * ???
     * @param Y and X positions, radius and color
     * @return Null
     * @author Henrik
	*/
    public RoundThing(float posX, float posY, float sRadius, Color color) {
        this.posX = posX;
        this.posY = posY;
        this.sRadius = sRadius;
        this.color = color;
        node = create();
    }

    // Layout for circle
    public Node create() {
        circle = new Circle();
        circle.setLayoutX(posX);
        circle.setLayoutY(posY);
        circle.setRadius(sRadius);
        circle.setFill(color);

        // Create a body definition for JBox2D
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(Main.pixelToMeter(posX), Main.pixelToMeter(-posY));

        CircleShape ps = new CircleShape();
        ps.m_radius = Main.pixelToMeter(sRadius);

        // Set fixture
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 0.61f;
        fd.friction = 0.3f;
        fd.isSensor = false;

        // Creating the body
        Body body = Main.world.createBody(bd);
        body.createFixture(fd);
        circle.setUserData(body);
        return circle;
    }
}
