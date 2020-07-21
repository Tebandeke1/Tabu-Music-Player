package com.example.tabuaimusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class AnotherActivity extends AppCompatActivity {

    private String[] AllItems;

    private String[] Discription;

    private int[] Images;

    private ListView songListView;

    private TabLayout tabLayout;
    private TabItem playList,Mylist,allMusic;
    private ViewPager viewPager;
    private PagerView  pagerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        songListView =  findViewById(R.id.songsList);

        //AppExternalStoragePermision();

        tabLayout = findViewById(R.id.tab_with);
        playList = findViewById(R.id.play_list);
        Mylist = findViewById(R.id.all_music);
        allMusic = findViewById(R.id.all_favourite);
        viewPager = findViewById(R.id.view_pager);

//        Myadaptor myadaptor = new Myadaptor(this, AllItems);
//
//        songListView.setAdapter(myadaptor);

        pagerView = new PagerView(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerView);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }



//    public class Myadaptor extends ArrayAdapter<String> {
//
//        Context context;
//        String songName[];
//
//
////        public Myadaptor(Context context, String[] songName) {
////            super(context,R.layout.nice_row,R.id.song_name,songName);
////
////            this.context=context;
////            this.songName = songName;
////
////        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            View view = layoutInflater.inflate(R.layout.nice_row,parent,false);
//
//            TextView song_name = view.findViewById(R.id.song_name);
//            song_name.setText(songName[position]);
//
//
//            return super.getView(position, convertView, parent);
//        }
//    }

}
