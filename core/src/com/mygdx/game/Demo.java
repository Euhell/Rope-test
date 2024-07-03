package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.ScreenUtils;

public class Demo extends ApplicationAdapter {
	SpriteBatch batch;
	private World world;
	private Box2DDebugRenderer box2DDebugRenderer;
	private OrthographicCamera camera;

	@Override
	public void create () {
		batch = new SpriteBatch();
		world = new World(new Vector2(0, -9.8f), true);

		box2DDebugRenderer = new Box2DDebugRenderer();

		camera = new OrthographicCamera(Gdx.graphics.getWidth() / 30f,
				Gdx.graphics.getHeight() / 30f);
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(new Vector2(10, -10));

		Body groundBody = world.createBody(groundBodyDef);

		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(50, 10);
		groundBody.createFixture(groundBox, 0);
		groundBox.dispose();

		createRope(new Vector2(0, 10), 10, 0.5f, 0.2f);
	}

	private void createRope(Vector2 startPoint, int segments, float segmentWidth, float segmentHeight) {
		Body prevBody = null;
		for (int i = 0; i < segments; i++) {
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyDef.BodyType.DynamicBody;
			bodyDef.position.set(startPoint.x, startPoint.y - i * segmentHeight);

			Body body = world.createBody(bodyDef);

			PolygonShape shape = new PolygonShape();
			shape.setAsBox(segmentWidth / 2, segmentHeight / 2);
			body.createFixture(shape, 1.0f);

			shape.dispose();

			if (prevBody != null) {
				RevoluteJointDef jointDef = new RevoluteJointDef();
				jointDef.bodyA = prevBody;
				jointDef.bodyB = body;

				jointDef.localAnchorA.set(0, -segmentHeight / 2);
				jointDef.localAnchorB.set(0, segmentHeight /2);

				world.createJoint(jointDef);
			}

			prevBody = body;
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);

		world.step(1 / 60f, 6, 2);
		box2DDebugRenderer.render(world, camera.combined);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
