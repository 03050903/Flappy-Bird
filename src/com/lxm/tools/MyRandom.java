package com.lxm.tools;

import java.util.Random;

public class MyRandom {
	private static MyRandom mr;
	private Random r;

	public static MyRandom getInstance() {
		if (mr == null) {
			mr = new MyRandom();// ����һ�������
		}
		return mr;
	}

	public MyRandom() {
		// TODO Auto-generated constructor stub
		r = new Random(1000);// ����һ�������
	}

	// ���� 0-max֮�������
	public int getInt(int max) {
		return r.nextInt(max + 1);

	}

	// ���� min-max֮�������
	public int getInt(int min, int max) {
		return min + r.nextInt(max + 1 - min);

	}

	// ���� min-max֮���float����
	public float getFloat(float min, float max) {
		return min + (max - min) * r.nextFloat();

	}

	// ���� 0-max֮���float����
	public float getFloat(float max) {
		return max * r.nextFloat();

	}

}
