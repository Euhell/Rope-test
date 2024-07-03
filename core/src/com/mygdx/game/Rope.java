package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import java.util.ArrayList;
import java.util.List;

public class Rope {

    private World world;
    private List<Body> segments;

    public Rope(World world) {
        this.world = world;
        segments = new ArrayList<>();
    }

    public void createSegments(Vector2 startPoint, Vector2 endPoint, float lenghtSegment) {
        int segmentCount = (int)(startPoint.dst(endPoint) / lenghtSegment);
        Vector2 direction = new Vector2(endPoint).sub(startPoint).nor();

        for (int i = 0; i <= segmentCount; i++) {
            Vector2 pos = new Vector2(startPoint).add(direction.scl(i * lenghtSegment));

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(pos);
            Body body = world.createBody(bodyDef);

            CircleShape shape = new CircleShape();
            shape.setRadius(0.2f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = 0.3f;

            body.createFixture(fixtureDef);

            segments.add(body);
            shape.dispose();

            if (i > 0) {
                Body prevBody = segments.get(i - 1);
                DistanceJointDef jointDef = new DistanceJointDef();

                jointDef.bodyA = prevBody;
                jointDef.bodyB = body;

                jointDef.length = lenghtSegment;
                jointDef.collideConnected = true;
                world.createJoint(jointDef);
            }
        }

    }
}
