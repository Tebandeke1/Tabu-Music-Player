package com.example.tabuaimusicplayer;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tabuaimusicplayer.R.layout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

import static com.example.tabuaimusicplayer.R.layout.nice_row;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicList extends Fragment {

    private ListView songListView;
    private String[] AllItems;
    ArrayAdapter<String> musicArrayAdaptor;
    ArrayList<File> music;


    public MusicList() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);

        songListView =  view.findViewById(R.id.songsList);

//        Myadaptor myadaptor = new Myadaptor( AllItems);
//        songListView.setAdapter(myadaptor);

        try {
            Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response) {

                    music = findMusicFiles(Environment.getExternalStorageDirectory());
                    AllItems = new String[music.size()];

                    for (int i = 0; i < music.size(); i++) {
                        AllItems[i] = music.get(i).getName();
                    }

                    musicArrayAdaptor = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, AllItems);
                    songListView.setAdapter(musicArrayAdaptor);

                    songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            intent.putExtra("song",music);
                            intent.putExtra("position",i);
                            startActivity(intent);
                        }
                    });

                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse response) {

                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).check();
        }catch (NullPointerException e){

        }

        return view;
    }


    private ArrayList<File> findMusicFiles (File file){
        ArrayList<File> allMusicFileObjects = new ArrayList<>();

        File [] files = file.listFiles();
        for(File currentFiles : files){
            if(currentFiles.isDirectory() && !currentFiles.isHidden()){
                allMusicFileObjects.addAll(findMusicFiles(currentFiles));
            }else{
                if(currentFiles.getName().endsWith(".mp3") || currentFiles.getName().endsWith(".wav") || currentFiles.getName().endsWith(".aac") || currentFiles.getName().endsWith(".wma")){
                    allMusicFileObjects.add(currentFiles);
                }

            }
        }
        return allMusicFileObjects;
    }
//    public class Myadaptor extends ArrayAdapter<String> {
//
//        Context context;
//        String songName[];
//
//
//        public Myadaptor(Context context, String[] songName) {
//            super(context, layout.nice_row,R.id.song_name,songName);
//
//            this.context=context;
//            this.songName = songName;
//
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            View view = layoutInflater.inflate(nice_row,parent,false);
//
//            TextView song_name = view.findViewById(R.id.song_name);
//            song_name.setText(songName[position]);
//
//
//            return super.getView(position, convertView, parent);
//        }
//    }

}
