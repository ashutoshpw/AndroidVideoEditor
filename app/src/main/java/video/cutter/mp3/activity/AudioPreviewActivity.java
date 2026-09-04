package video.cutter.mp3.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.devil.videoeditor.R;
import video.cutter.mp3.views.VisualizerView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AudioPreviewActivity extends AppCompatActivity {

    private VisualizerView mVisualizerView;
    Button bt;
    private MediaPlayer mMediaPlayer;
    private Visualizer mVisualizer;
    private static final String FILEPATH = "filepath";
    static final int REQUEST_IMAGE_OPEN = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_preview);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mVisualizerView = findViewById(R.id.visualizerView);

    }


    @Override
    protected void onResume() {
        super.onResume();
        initAudio();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            if (mVisualizer != null) {
                mVisualizer.release();
                mVisualizer = null;
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void initAudio() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        String filePath = getIntent().getStringExtra(FILEPATH);
        TextView tvInstruction = findViewById(R.id.tvInstruction);
        tvInstruction.setText(String.format("Audio stored at path %s", filePath));
        mMediaPlayer = MediaPlayer.create(this, Uri.parse(filePath));
        if (mMediaPlayer == null) {
            return;
        }

        setupVisualizerFxAndUI();
        if (mVisualizer != null) {
            try {
                // Make sure the visualizer is enabled only when you actually want to
                // receive data, and
                // when it makes sense to receive data.
                mVisualizer.setEnabled(true);
            } catch (RuntimeException e) {
                mVisualizer.release();
                mVisualizer = null;
            }
        }
        // When the stream ends, we don't need to collect any more data. We
        // don't do this in
        // setupVisualizerFxAndUI because we want to have more,
        // non-Visualizer related code
        // in this callback.
        mMediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        if (mVisualizer != null) {
                            try {
                                mVisualizer.setEnabled(false);
                            } catch (RuntimeException e) {
                                mVisualizer.release();
                                mVisualizer = null;
                            }
                        }
                    }
                });
        mMediaPlayer.start();
        mMediaPlayer.setLooping(true);

    }

    public static boolean hasUsableAudioSession(int audioSessionId) {
        return audioSessionId > 0;
    }

    private void setupVisualizerFxAndUI() {
        final int audioSessionId = mMediaPlayer.getAudioSessionId();
        if (!hasUsableAudioSession(audioSessionId)) {
            return;
        }

        try {
            // Create the Visualizer object and attach it to our media player.
            mVisualizer = new Visualizer(audioSessionId);
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            mVisualizer.setDataCaptureListener(
                    new Visualizer.OnDataCaptureListener() {
                        public void onWaveFormDataCapture(Visualizer visualizer,
                                                          byte[] bytes, int samplingRate) {
                            if (mVisualizerView != null) {
                                mVisualizerView.updateVisualizer(bytes);
                            }
                        }

                        public void onFftDataCapture(Visualizer visualizer,
                                                     byte[] bytes, int samplingRate) {
                        }
                    }, Visualizer.getMaxCaptureRate() / 2, true, false);
        } catch (RuntimeException e) {
            if (mVisualizer != null) {
                mVisualizer.release();
                mVisualizer = null;
            }
        }
    }
}
