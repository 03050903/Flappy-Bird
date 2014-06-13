package com.example.bird;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.lxm.tools.BoxWorld;
import com.lxm.tools.MyRandom;

public class RunScreen extends ScreenAdapter implements InputProcessor {
	Vector3 testPoint = new Vector3();
	SpriteBatch Batch;
	private TextureAtlas atlas; // ������Դʹ��
	private Sprite sprite; // ����
	FirstGame Gm; // game�ӿ�
	private OrthographicCamera camera; // �����������Ϊ�˽�����Ͷ��box����������
	private Sprite background; // ���汳��
	private float df_x = 0;
	float df = 0; // Ϊ�˻�ȡС��ĵڼ�֡���ı���
	World world; // box ����
	boolean is_set_impo = false; // ����ʼ���䣬�������µ��ٶȣ���֤��������
	/** our boxes **/
	private Array<MyBody> boxes = new Array<MyBody>();// ��ǰû���ã�����Ϊ�ܵ����Ľṹ
	private AtlasRegion land; // ���
	Array<TextureRegion> Texturelist = new Array<TextureRegion>();
	private Animation birdAni; // С�񶯻�
	Body birdBox; // С���box
	float bird_y; // ��¼С��ĵ�ǰyֵ����Ϊ��������Ƿ�Ϊ����ʹ����
	private Box2DDebugRenderer debugRenderer; // ������Ⱦ��Ϊ�ҳ��������⣬����ģ��
	private boolean isfall;
	private AtlasRegion pipe_down;
	private AtlasRegion pipe_up;
	private short BirdBit = 1 << 0;
	private short GroudBit = 1 << 1;
	private short PipeBit = 1 << 2;
	private boolean isover;
	private float timeforcreatPipe = 0f;
	private boolean isstart = false;
	private Sprite tutorial;
	GameScore GS;
	private Sprite text_ready;
	Stage Ui;
	private Image button_play;
	private Image button_score;

	// �����ܵ�����ǰ��δʵ��
	private void creatPipe() {
		MyBody tmp = new MyBody();
		float pos_f = MyRandom.getInstance().getFloat(7.0f) - 0.4f;
		tmp.body = BoxWorld.createBoxes(world, 0.65f, 4f,
				BodyDef.BodyType.DynamicBody, 8 + 0.65f, pos_f, 1f,
				(short) PipeBit, (short) BirdBit, false);
		tmp.body.setGravityScale(0f);// ʧ�أ���Ҫ������Ӱ��
		tmp.body.setLinearVelocity(-2f, 0f);
		tmp.isup = false;
		tmp.body.setFixedRotation(true);
		boxes.add(tmp);
		tmp = new MyBody();
		tmp.body = BoxWorld.createBoxes(world, 0.65f, 4f,
				BodyDef.BodyType.DynamicBody, 8 + 0.65f,
				(float) (pos_f + 10.67), 1f, (short) PipeBit, (short) BirdBit,
				false);
		tmp.body.setGravityScale(0f);// ʧ�أ���Ҫ������Ӱ��
		tmp.body.setLinearVelocity(-2f, 0f);
		tmp.isup = true;
		tmp.body.setFixedRotation(true);
		boxes.add(tmp);
	}

	// //// ����ֵΪ 320*560---8*14
	public RunScreen(FirstGame tGm) {
		// TODO Auto-generated constructor stub
		Batch = new SpriteBatch();
		Gm = tGm;
		// �����
		camera = new OrthographicCamera(8, 14);
		camera.position.set(8 / 2, 14 / 2, 0);
		atlas = new TextureAtlas(Gdx.files.internal("data/bird/birdImgs.atlas"));
		// ����
		if (MyRandom.getInstance().getInt(1) == 0) {
			sprite = atlas.createSprite("bg_day");
		} else {
			sprite = atlas.createSprite("bg_night");
		}
		Ui = new Stage();
		Ui.setCamera(camera);
		float h1 = atlas.findRegion("button_play").getRegionHeight() / 40f;
		float w1 = atlas.findRegion("button_play").getRegionWidth() / 40f;
		button_play = new Image(atlas.findRegion("button_play"));
		button_play.setPosition(8 / 2 - w1 - 0.2f, (12 - h1) / 2);
		button_play.setSize(w1, h1);
		button_play.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				// ���¿�ʼ
				isover = false;
				boxes.clear();
				GS.cleanScore();
				// birdBox.s
				world.destroyBody(birdBox);
				isstart = false;
				isfall = false;
				// ����һ����
				birdBox = BoxWorld.createBoxes(world, 0.3f, 0.3f,
						BodyDef.BodyType.DynamicBody, 3f, 7f, 1f,
						(short) BirdBit, (short) (GroudBit | PipeBit), false);
				Gdx.input.setInputProcessor(RunScreen.this);
			}
		});
		Ui.addActor(button_play);
		h1 = atlas.findRegion("button_score").getRegionHeight() / 40f;
		w1 = atlas.findRegion("button_score").getRegionWidth() / 40f;
		button_score = new Image(atlas.findRegion("button_score"));
		button_score.setPosition(8 / 2 + 0.2f, (12 - h1) / 2);
		button_score.setSize(w1, h1);
		button_score.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gm.ShowOff();
			}
		});
		Ui.addActor(button_score);
		GS = new GameScore(atlas);
		tutorial = atlas.createSprite("tutorial");
		tutorial.setSize(2.85f, 2.45f);
		tutorial.setPosition(2.8f, 5.6f);
		text_ready = atlas.createSprite("text_ready");
		text_ready.setSize(4.9f, 1.3f);
		text_ready.setPosition(1.8f, 8.2f);
		// ���
		land = atlas.findRegion("land");
		sprite.setSize(8, 14);
		pipe_down = atlas.findRegion("pipe_down");// 52/40=1.3f 320/40=8
		pipe_up = atlas.findRegion("pipe_up");
		// ��������box
		world = BoxWorld.createPhysicsWorld();
		world.setContactListener(new ContactListener() {

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub

			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beginContact(Contact contact) {
				// TODO Auto-generated method stub
				// Log.i("lxm", contact.)
				isover = true;
				Gdx.input.setInputProcessor(Ui);
			}
		});
		// ����һ����
		birdBox = BoxWorld.createBoxes(world, 0.3f, 0.3f,
				BodyDef.BodyType.DynamicBody, 3f, 7f, 1f, (short) BirdBit,
				(short) (GroudBit | PipeBit), false);

		// ����һ������
		BoxWorld.createBoxes(world, 5f, 1.4f, BodyDef.BodyType.StaticBody, 5f,
				1.4f, 1f, (short) GroudBit, (short) BirdBit, false);
		creatPipe();
		// С�񶯻�
		int index = MyRandom.getInstance().getInt(2);
		if (index == 0) {
			Texturelist.add(atlas.findRegion("bird0_0"));
			Texturelist.add(atlas.findRegion("bird0_1"));
			Texturelist.add(atlas.findRegion("bird0_2"));
		} else if (index == 1) {
			Texturelist.add(atlas.findRegion("bird1_0"));
			Texturelist.add(atlas.findRegion("bird1_1"));
			Texturelist.add(atlas.findRegion("bird1_2"));
		} else {
			Texturelist.add(atlas.findRegion("bird2_0"));
			Texturelist.add(atlas.findRegion("bird2_1"));
			Texturelist.add(atlas.findRegion("bird2_2"));
		}
		birdAni = new Animation(0.16f, Texturelist, Animation.LOOP);
		// ���׵Ķ���
		background = new Sprite(atlas.findRegion("land")); // ���
		background.setSize(8.4f, 2.8f);
		debugRenderer = new Box2DDebugRenderer();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		df_x -= 1.5f;
		df += delta;
		if (!isstart) {
			Vector2 position = birdBox.getPosition();
			camera.update();
			Batch.setProjectionMatrix(camera.combined);
			Batch.begin();
			sprite.draw(Batch);// ������
			Batch.draw(birdAni.getKeyFrame(df), position.x - 0.6f,
					position.y - 0.6f, 0.6f, 0.6f, 1.2f, 1.2f, 1f, 1f, 0);
			tutorial.draw(Batch);
			text_ready.draw(Batch);
			float x = (df_x % 336) / 40;
			background.setX(x);
			background.draw(Batch);
			background.setX(336 / 40 + x);
			background.draw(Batch);
			GS.draw(Batch);
			Batch.end();
			return;
		}
		timeforcreatPipe += delta;
		if (isover) {
			for (MyBody pipe : boxes) {
				pipe.body.setType(BodyType.StaticBody);

			}
		}
		// ��Ϸδ���������������ܵ�
		if (timeforcreatPipe > 2.5f && !isover && isstart) {
			timeforcreatPipe = 0f;
			creatPipe();
		}

		// �����ײ�������ùܵ���С����ײ������
		if (isover) {
			for (int index = boxes.size - 1; index >= 0; index--) {
				MyBody pipe = boxes.get(index);
				Fixture Fixture = pipe.body.getFixtureList().get(0);
				Filter FilterData = Fixture.getFilterData();
				FilterData.maskBits = PipeBit;
				Fixture.setFilterData(FilterData);
			}
		}
		BoxWorld.renden(world);
		// ������ǰģ��ᶪʧ��ǰ��һ�δ�����˰������ʵ�ֵķǳ��������Ǻ�
		Vector2 position = birdBox.getPosition();
		// �Ƿ�����
		if (bird_y > position.y) {
			// ����
			isfall = true;
			if (!is_set_impo) {
				// birdBox.setLinearVelocity(0, -2.5f);
				is_set_impo = true;

			}
		} else {
			is_set_impo = false; // ������������
		}

		bird_y = position.y; // ��¼�ŵ�ǰС���yֵ
		Vector2 posVector2;
		// ��Щ���࣬��Ҫ�ǲ���ʹ�ã�����������ȥд��
		if (isfall) {
			posVector2 = new Vector2(position.x - 1, position.y);// ��������90��
		} else {
			posVector2 = new Vector2(position.x + 1, position.y + 2f);// ��������45��
		}
		Vector2 toTarget = new Vector2(posVector2.sub(position));
		float desiredAngle = MathUtils.atan2(toTarget.x, toTarget.y);
		float totalRotation = desiredAngle - birdBox.getAngle();
		// Log.i("newAngle----", "desiredAngle=" + desiredAngle
		// * MathUtils.radiansToDegrees + "totalRotation" + totalRotation
		// * MathUtils.radiansToDegrees);
		float angle = MathUtils.radiansToDegrees * birdBox.getAngle();
		while (totalRotation < -180 * MathUtils.degreesToRadians)
			totalRotation += 360 * MathUtils.degreesToRadians;//
		// �����趨�Ƕȵ�-180��180��֮��
		while (totalRotation > 180 * MathUtils.degreesToRadians)
			totalRotation -= 360 * MathUtils.degreesToRadians;
		float change = 8 * MathUtils.degreesToRadians; // ÿ�μ�����������ת�Ƕ�
		if (isfall) {
			change = 10 * MathUtils.degreesToRadians;
		}
		float newAngle = birdBox.getAngle()
				+ Math.min(change, Math.max(-change, totalRotation));
		// ��������׶Σ�
		if (!isfall && newAngle < 0) {
			// Log.i("jiaodu", "" + newAngle * MathUtils.radiansToDegrees);
			newAngle = 25 * MathUtils.degreesToRadians;
		} else {
			// Log.i("jiaodu", "" + newAngle * MathUtils.radiansToDegrees);
		}
		// Log.i("newAngle", newAngle + "--start");
		// ���½Ƕ�
		if (newAngle < -90 * MathUtils.degreesToRadians) {
			newAngle = -90 * MathUtils.degreesToRadians;
			// Log.i("newAngle", "-90");
		}

		if (newAngle > 30 * MathUtils.degreesToRadians) {
			newAngle = 30 * MathUtils.degreesToRadians;
			// Log.i("newAngle", ">30");
		}
		// Log.i("newAngle", newAngle + "--end");
		birdBox.setTransform(birdBox.getPosition(), newAngle);

		position = birdBox.getPosition();

		camera.update();
		Batch.setProjectionMatrix(camera.combined);
		Batch.begin();

		sprite.draw(Batch);// ������

		for (int index = boxes.size - 1; index >= 0; index--) {
			MyBody pipe = boxes.get(index);
			Vector2 pos = pipe.body.getPosition();
			if (pipe.isup) {
				Batch.draw(pipe_down, pos.x - 0.65f, pos.y - 4f, 0.65f, 4f,
						1.3f, 8f, 1f, 1f, 0);
			} else {
				Batch.draw(pipe_up, pos.x - 0.65f, pos.y - 4f, 0.65f, 4f, 1.3f,
						8f, 1f, 1f, 0);
			}
			if (pos.x + 0.65f < 3f) {
				if (!pipe.isScore) {
					GS.addScore(1);
					pipe.isScore = true;
				}
			}
			if (pos.x + 0.65f < 0f) {
				world.destroyBody(pipe.body);
				boxes.removeIndex(index);
			}
		}
		// ������Ҫ������λ�õ�������ȥ��Ŀ�߼���originX originY�Ĵ�С��ԭ����ģ��ͻ������겻һ��
		Batch.draw(birdAni.getKeyFrame(df), position.x - 0.6f,
				position.y - 0.6f, 0.6f, 0.6f, 1.2f, 1.2f, 1f, 1f, angle);
		float x = (df_x % 336) / 40;
		background.setX(x);
		background.draw(Batch);
		background.setX(336 / 40 + x);
		background.draw(Batch);
		GS.draw(Batch);
		Batch.end();
		if (isover) {
			Ui.act();
			Ui.draw();
		}
		// ����ʹ�ô���
		// debugRenderer.render(world, camera.combined);

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		Batch.dispose();
		atlas.dispose();
		debugRenderer.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		testPoint.set(screenX, screenY, 0);
		camera.unproject(testPoint);// ͨ������ӿڣ���ԭʼ����ת��Ϊ�������Ӧ����
		isstart = true;
		if (birdBox.getPosition().y < 14 && !isover) {
			birdBox.setLinearVelocity(0, 7.5f);// (0f, 300f,
			birdBox.setAwake(true);
			// ���С��߶�С�������Ͷ��߶ȣ�����һ�������ٶȣ�y����
		}

		// �ı���Ϊ
		isfall = false;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
