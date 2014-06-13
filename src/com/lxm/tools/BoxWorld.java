package com.lxm.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BoxWorld {

	private static Fixture Fixture;
	private static Filter FilterData;

	// ����һ�����磬����ά���˶���0 -20����˼Ϊx��y����ļ��ٶȣ�trueΪ����ֹͣģ���Ѿ������е�����
	public static World createPhysicsWorld() {
		// we instantiate a new World with a proper gravity vector
		// and tell it to sleep when possible.
		return new World(new Vector2(0f, -30f), true);
	}

	// �������ͣ�world �� ����hx������hy ��box���StaticBody(0), KinematicBody(1),
	// DynamicBody(2)
	// λ�� pos_x pos_y ���� density
	//categoryBits  ����    maskBits ����Щ����ײ���
	public static Body createBoxes(World world, float hx, float hy,
			BodyDef.BodyType type, float pos_x, float pos_y, float density,
			short categoryBits, short maskBits,boolean sensor) {

		// ��״������Ϊ���ӣ��ķ��Σ�hx hyΪһ��ĳ��ȡ���������*2
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(hx, hy);
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = type;
		boxBodyDef.position.x = pos_x;
		boxBodyDef.position.y = pos_y;
		Body boxBody = world.createBody(boxBodyDef);
		// �����ҪĦ���������� �������Զ���оߣ���������Ĳ�����Ȼ���޸ļо߲���
		Fixture = boxBody.createFixture(boxPoly, density);
		// ���Ĳ�����
		FilterData = Fixture.getFilterData();
		FilterData.categoryBits = categoryBits;
		FilterData.maskBits = maskBits;
		Fixture.setSensor(sensor);
		Fixture.setFilterData(FilterData);
		// we are done, all that's left is disposing the boxPoly
		boxPoly.dispose();
		return boxBody;
	}

	/*
	 * 
	 * ���˻��������⣬Box2D��ʹ���˴�����λ����������Լ�������(constraint
	 * solver)��Լ�������ÿ�ν��һ��Լ���������ģ����������е�Լ������
	 * ������Լ�����Ա������Ľ����������ˣ�ÿ�����ǽ����һ��Լ�������������׵�����������Լ����Ϊ�˽���������⣬������Ҫ��α�������Լ����
	 * ��Լ�������(constraint
	 * solver)���������׶�:�ٶȽ׶κ�λ�ý׶Ρ����ٶȽ׶Σ������Ϊʹ�����ܹ���ȷ���ƶ���Ҫ�����Ҫ�ĳ�������λ�ý׶�
	 * ����������������λ������������֮����ص������ӵķ���̶�
	 * (����ע:��������������������ײ��֮�����˲���ص�����������ж�ڵ���������ײ��֮�����˲��ķ�������
	 * �����Ի����ص��ͷ����������)��ÿһ���׶ζ�����������Ҫ�ĵ������������⣬��λ�ý׶����������㹻С�����ܻ������˳�������
	 * Box2D����ĵ����������ٶȽ׶�8��
	 * ��λ�ý׶�3�Ρ�����Ը������ϲ�øı�������֣�����Ҫ��ס������Ҫ��Ч�ʺ;�������һ��Ȩ�⡣�ø��ٵĵ�����������������Ч��
	 * �����ǻ�Ӱ�쾫�ȡ�ͬ�����ø���ĵ��������ή��Ч�ʵ�������ģ������
	 * ����������ߵľ��ȡ���������򵥵����Ӷ��ԣ����ǲ���Ҫ̫��ĵ�������������������ѡ��ĵ���������
	 */
	public static void renden(World world) {
		world.step(Gdx.graphics.getDeltaTime(), 8, 3);
	}

	// box��posλ��Ϊ���ĵ�λ�ã�����16*16�ģ�����λ��8*8���������ĵ��ϡ�

	// boby�ӿ�
	/*
	 * setLinearVelocity ���������ٶ� setBullet ���ó��ӵ���������켣���������жϣ���ֹ�����Խ����
	 * setLinearDamping ��ֱ�������� applyLinearImpulse Ӧ��һ������ setAngularVelocity
	 * �O�ý��ٶ�
	 */
	// �����ؽڵ���·���£�
	/*
	 * //����һ������ؽ� DistanceJointDef distanceJointDef = new DistanceJointDef();//
	 * ʹ�ùؽڶ��壬��ʼ������ distanceJointDef.bodyA = mCircle1; distanceJointDef.bodyB =
	 * mCircle2; //���ó���Ϊ4�� distanceJointDef.length = 4f;
	 * distanceJointDef.collideConnected = true;// �Ƿ���Ҫ������������Ƿ���ײ
	 * mWorld.createJoint(distanceJointDef);// �����ؽ�
	 */
	/*
	 * ��ת�ؽ� RevoluteJointDef jd = new RevoluteJointDef(); jd.collideConnected =
	 * false; jd.initialize(prevBody, body, anchor);// �������������壬һ��ê��
	 * world.createJoint(jd);
	 */

	/*
	 * ���ؽڵĴ��룬���֮������һ�����ؽ� // ���ܲ�ѯ��� QueryCallback callback = new
	 * QueryCallback() {
	 * 
	 * @Override public boolean reportFixture (Fixture fixture) { // if the hit
	 * fixture's body is the ground body // we ignore it if (fixture.getBody()
	 * == groundBody) return true;
	 * 
	 * // if the hit point is inside the fixture of the body // we report it if
	 * (fixture.testPoint(testPoint.x, testPoint.y)) { hitBody =
	 * fixture.getBody(); return false; } else return true; } };
	 * 
	 * @Override public boolean touchDown (int x, int y, int pointer, int
	 * newParam) { // translate the mouse coordinates to world coordinates
	 * testPoint.set(x, y, 0); camera.unproject(testPoint);
	 * 
	 * // ask the world which bodies are within the given // bounding box around
	 * the mouse pointer hitBody = null; world.QueryAABB(callback, testPoint.x -
	 * 0.1f, testPoint.y - 0.1f, testPoint.x + 0.1f, testPoint.y + 0.1f);
	 * 
	 * // if we hit something we create a new mouse joint // and attach it to
	 * the hit body. if (hitBody != null) { MouseJointDef def = new
	 * MouseJointDef(); def.bodyA = groundBody; def.bodyB = hitBody;
	 * def.collideConnected = true; def.target.set(testPoint.x, testPoint.y);
	 * def.maxForce = 1000.0f * hitBody.getMass();
	 * 
	 * mouseJoint = (MouseJoint)world.createJoint(def); hitBody.setAwake(true);
	 * } else { for (Body box : boxes) world.destroyBody(box); boxes.clear();
	 * createBoxes(); }
	 * 
	 * return false; }
	 * 
	 * another temporary vector Vector2 target = new Vector2();
	 * 
	 * @Override public boolean touchDragged (int x, int y, int pointer) { // if
	 * a mouse joint exists we simply update // the target of the joint based on
	 * the new // mouse coordinates if (mouseJoint != null) {
	 * camera.unproject(testPoint.set(x, y, 0));
	 * mouseJoint.setTarget(target.set(testPoint.x, testPoint.y)); } return
	 * false; }
	 * 
	 * @Override public boolean touchUp (int x, int y, int pointer, int button)
	 * { // if a mouse joint exists we simply destroy it if (mouseJoint != null)
	 * { world.destroyJoint(mouseJoint); mouseJoint = null; } return false; }
	 */

}
