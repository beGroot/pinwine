package com.pin.pinwine.page;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.pin.pinwine.R;
import com.pin.pinwine.constant.ConstantUtil;
import com.pin.pinwine.model.ADInfo;
import com.pin.pinwine.view.CycleViewPager;
import com.pin.pinwine.view.CycleViewPager.ImageCycleViewListener;
import com.pin.pinwine.view.ViewFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentCommunity extends Fragment {
	
	private List<ImageView> views = new ArrayList<ImageView>();
	private List<ADInfo> infos = new ArrayList<ADInfo>();
	private CycleViewPager cycleViewPager;

	private String[] imageUrls = {ConstantUtil.SERVER_URL+"/scoresys/img/480/1.jpg",
			ConstantUtil.SERVER_URL+"/scoresys/img/480/2.jpg",
			ConstantUtil.SERVER_URL+"/scoresys/img/480/3.jpg",
			ConstantUtil.SERVER_URL+"/scoresys/img/480/4.jpg",
			ConstantUtil.SERVER_URL+"/scoresys/img/480/5.jpg"};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_community, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		configImageLoader();
		initView();
	}
	
	private void initView()
	{
		cycleViewPager = (CycleViewPager) getFragmentManager()
				.findFragmentById(R.id.fragment_cycle_viewpager_content);
		
		for(int i = 0; i < imageUrls.length; i ++){
			ADInfo info = new ADInfo();
			info.setUrl(imageUrls[i]);
			info.setContent("ͼƬ-->" + i );
			infos.add(info);
		}
		
		// �����һ��ImageView��ӽ���
		views.add(ViewFactory.getImageView(this.getActivity(), infos.get(infos.size() - 1).getUrl()));
		for (int i = 0; i < infos.size(); i++) {
			views.add(ViewFactory.getImageView(this.getActivity(), infos.get(i).getUrl()));
		}
		// ����һ��ImageView��ӽ���
		views.add(ViewFactory.getImageView(this.getActivity(), infos.get(0).getUrl()));
		
		// ����ѭ�����ڵ���setData����ǰ����
		cycleViewPager.setCycle(true);

		// �ڼ�������ǰ�����Ƿ�ѭ��
		cycleViewPager.setData(views, infos, mAdCycleViewListener);
		//�����ֲ�
		cycleViewPager.setWheel(true);

	    // �����ֲ�ʱ�䣬Ĭ��5000ms
		cycleViewPager.setTime(2000);
		//����Բ��ָʾͼ���������ʾ��Ĭ�Ͽ���
		cycleViewPager.setIndicatorCenter();
	}
	
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			if (cycleViewPager.isCycle()) {
				position = position - 1;
			}
			
		}

	};
	
	private void configImageLoader() {
		// ��ʼ��ImageLoader
		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) // ����ͼƬ�����ڼ���ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.icon_empty) // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageOnFail(R.drawable.icon_error) // ����ͼƬ���ػ��������з���������ʾ��ͼƬ
				.cacheInMemory(true) // �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisc(true) // �������ص�ͼƬ�Ƿ񻺴���SD����
				// .displayer(new RoundedBitmapDisplayer(20)) // ���ó�Բ��ͼƬ
				.build(); // �������ù���DisplayImageOption����

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.getActivity().getApplicationContext()).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);		
	}

}
