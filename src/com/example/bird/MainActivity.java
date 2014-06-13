package com.example.bird;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import cn.waps.AdInfo;
import cn.waps.AppConnect;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class MainActivity extends AndroidApplication {
	Game Gm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		AppConnect.getInstance("dca242344a805d9afae7b59c263148f6", "default",
				this);
		AppConnect.getInstance(this).initPopAd(this);
		AppConnect.getInstance(this).initUninstallAd(this);
		Gm = new FirstGame(MainActivity.this);
		initialize(Gm, false);
		mContext = this;
	}

	private static Handler handler = handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {

			case 0:
				// ��ʾ�Ƽ��б��ۺϣ�
				AppConnect.getInstance(mContext).showOffers(mContext);
				break;
			case 1:
				// ��ʾ�������
				// �жϲ�������Ƿ��ѳ�ʼ����ɣ�����ȷ���Ƿ��ܳɹ����ò������
				boolean hasPopAd = AppConnect.getInstance(mContext).hasPopAd(
						mContext);
				if (hasPopAd) {
					AppConnect.getInstance(mContext).showPopAd(mContext);
				}
				break;
			case 2:
				// ��ʾ�Ƽ��б������
				AppConnect.getInstance(mContext).showAppOffers(mContext);
				break;
			case 3:
				// ��ʾ�Ƽ��б���Ϸ��
				AppConnect.getInstance(mContext).showGameOffers(mContext);
				break;

			case 8:
				// ��ʾ�Լ�Ӧ���б�
				AppConnect.getInstance(mContext).showMore(mContext);
				break;
			case 9:
				// ����ָ����Ӧ��app_idչʾ������
				AppConnect.getInstance(mContext).showMore(mContext,
						"dca242344a805d9afae7b59c263148f6");
				break;
			case 10:
				// ���ù��ܹ��ӿڣ�ʹ��������ӿڣ�
				String uriStr = "http://www.baidu.com";
				AppConnect.getInstance(mContext).showBrowser(mContext, uriStr);
				break;
			case 11:
				// �û�����
				AppConnect.getInstance(mContext).showFeedback(mContext);
				break;

			}
		}
	};
	private static Context mContext;

	public static void showAdStatic(int adTag) {
		Message msg = handler.obtainMessage();
		msg.what = adTag; // ˽�о�̬�����ͱ����������������ж���ֵ
		handler.sendMessage(msg);
	}

}
