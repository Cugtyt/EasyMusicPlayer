package edu.whut.lixin.easyMusicPlayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Lyrics Activity is to show lyrics
 */
public class Lyrics extends AppCompatActivity {
    // TextView to show
    private TextView lyricsTextView;
    // id of lyrics file in R.raw
    private int[] lyrics_text = {R.raw.l1, R.raw.l2, R.raw.l3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        lyricsTextView = (TextView) findViewById(R.id.lyricsTextView);
        lyricsTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        // read file from R.raw
        BufferedReader reader;
        StringBuffer s = new StringBuffer();
        try {
            String line;
            // get song id in intent from MainActivity
            // read file by BufferedReader
            reader = new BufferedReader(new InputStreamReader(
                    getResources().openRawResource(lyrics_text[getIntent().getIntExtra("num", 0)]), "gbk"));
            while ((line = reader.readLine()) != null) {
                s.append(line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        lyricsTextView.setText(s);
    }
}
