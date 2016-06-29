package cn.com.hgh.playview.imp;

import android.view.View;

import cn.com.hgh.playview.BaseAnimationInterface;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * 具体动画
 */
public class DescriptionAnimation implements BaseAnimationInterface {

	@Override
	public void onPrepareCurrentItemLeaveScreen(View current) {
		// View descriptionLayout =
		// current.findViewById(R.id.description_layout);
		// if (descriptionLayout != null) {
		// current.findViewById(R.id.description_layout).setVisibility(View.INVISIBLE);
		// }
	}

	/**
	 * When next item is coming to show, let's hide the description layout.
	 * 
	 * @param next
	 */
	@Override
	public void onPrepareNextItemShowInScreen(View next) {
		// View descriptionLayout = next.findViewById(R.id.description_layout);
		// if (descriptionLayout != null) {
		// next.findViewById(R.id.description_layout).setVisibility(View.INVISIBLE);
		// }
	}

	@Override
	public void onCurrentItemDisappear(View view) {

	}

	/**
	 * When next item show in ViewPagerEx, let's make an animation to show the
	 * description layout.
	 * 
	 * @param view
	 */
	@Override
	public void onNextItemAppear(View view) {

		// View descriptionLayout = view.findViewById(R.id.description_layout);
		// if (view != null) {
		// float layoutY = ViewHelper.getY(view);
		// view.setVisibility(View.VISIBLE);
		// ValueAnimator animator = ObjectAnimator.ofFloat(
		// view, "y", layoutY + view.getHeight(),
		// layoutY).setDuration(500);
		// animator.start();
		// }

	}
}
