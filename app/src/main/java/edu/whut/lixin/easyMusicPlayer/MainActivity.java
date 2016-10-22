package edu.whut.lixin.easyMusicPlayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // songs' info
    private String[] names = {"俾面派对", "富士山下", "无心睡眠"};
    private String[] singers = {"BEYOND", "陈奕迅", "张国荣"};
    private Integer[] covers = {R.drawable.c1, R.drawable.c2, R.drawable.c3};
    // ListView to show
    private ListView listView;
    // store info by ArrayList
    private ArrayList<Map<String, Object>> data = new ArrayList<>();

    private ImageButton play;
    private ImageButton pause;
    private ImageButton stop;
    // service stuff
    private MusicService.MusicBinder musicBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
            musicBinder = (MusicService.MusicBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBinder = null;
            Log.d(TAG, "onServiceDisconnected() called with: name = [" + name + "]");
        }
    };

    private static final String TAG = "MainActivity";

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(MainActivity.this, "您选择了标题：" + names[position] + "内容：" + singers[position], Toast.LENGTH_LONG).show();
            Log.d(TAG, "onItemClick() called with: parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");
            Intent intent = new Intent(MainActivity.this, Lyrics.class);
            intent.putExtra("num", position);
            startActivity(intent);
            musicBinder.play(position);
        }
    };

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick() called with: v = [" + v + "]");
            switch (v.getId()) {
                case R.id.pause:
                    musicBinder.pause();
                    break;
                case R.id.stop:
                    musicBinder.stop();
                    break;
                case R.id.play:
                    musicBinder.replay();
                    break;
                default:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.songList);
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("image", covers[i]);
            item.put("title", names[i]);
            item.put("text", singers[i]);
            data.add(item);
        }

        listView.setAdapter(new SimpleAdapter(
                this, data,
                R.layout.song_list,
                new String[]{"image", "title", "text"},
                new int[]{R.id.image, R.id.title, R.id.text}));

        listView.setOnItemClickListener(itemClickListener);

        play = (ImageButton) this.findViewById(R.id.play);
        pause = (ImageButton) this.findViewById(R.id.pause);
        stop = (ImageButton) this.findViewById(R.id.stop);
        play.setOnClickListener(buttonClickListener);
        pause.setOnClickListener(buttonClickListener);
        stop.setOnClickListener(buttonClickListener);

        bindService(new Intent(this, MusicService.class), connection, Context.BIND_AUTO_CREATE);
    }

}
