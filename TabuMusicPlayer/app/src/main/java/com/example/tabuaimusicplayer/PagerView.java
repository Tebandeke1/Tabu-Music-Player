package com.example.tabuaimusicplayer;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerView extends FragmentPagerAdapter {

    int tabCounts;

    public PagerView(FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.tabCounts = behavior;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new MusicList();
            case 1:
                return new myPlayList();
            case 2:
                return new CurrentMusic();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCounts;
    }
}
