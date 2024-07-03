package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import java.util.ArrayList;
import java.util.List;

public class SoftBody {

    private World world;

    private List<Body> vertices;
    private List<Joint> edges;

    public SoftBody(World world) {
        this.world = world;
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void createVertices(Vector2[] positions) {
        for (Vector2 pos : positions) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(pos);
            Body body = world.createBody(bodyDef);
            CircleShape shape = new CircleShape();
            shape.setRadius(0.5f);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = 0.3f;

            body.createFixture(fixtureDef);

            vertices.add(body);

            shape.dispose();
        }
    }

    public void createEdge(int[][] connections) {
        for (int[] connection : connections) {
            Body bodyA = vertices.get(connection[0]);
            Body bodyB = vertices.get(connection[1]);
            DistanceJointDef jointDef = new DistanceJointDef();
            jointDef.bodyA = bodyA;
            jointDef.bodyB = bodyB;
            jointDef.length = bodyA.getPosition().dst(bodyB.getPosition());
            jointDef.collideConnected = true;

            edges.add(world.createJoint(jointDef));
        }
    }
}
