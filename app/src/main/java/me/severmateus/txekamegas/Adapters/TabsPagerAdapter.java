package me.severmateus.txekamegas.Adapters;

import me.severmateus.txekamegas.Fragments.IntroFirstFragment;
import me.severmateus.txekamegas.Fragments.IntroSecondFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new IntroFirstFragment();
		case 1:
			// Games fragment activity
			return new IntroSecondFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}
