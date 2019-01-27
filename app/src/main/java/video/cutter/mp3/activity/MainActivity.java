package video.cutter.mp3.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.devil.videoeditor.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInterstitialAd2;
    private InterstitialAd mInterstitialAd3;
    private InterstitialAd mInterstitialAd4;
    private InterstitialAd mInterstitialAd5;


    ImageView iv1,iv2,iv3,iv4,iv5,share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar()!=null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1423857702585511/3397420196");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd2 = new InterstitialAd(this);
        mInterstitialAd2.setAdUnitId("ca-app-pub-1423857702585511/3397420196");
        mInterstitialAd2.loadAd(new AdRequest.Builder().build());
        mInterstitialAd3 = new InterstitialAd(this);
        mInterstitialAd3.setAdUnitId("ca-app-pub-1423857702585511/3397420196");
        mInterstitialAd3.loadAd(new AdRequest.Builder().build());
        mInterstitialAd4 = new InterstitialAd(this);
        mInterstitialAd4.setAdUnitId("ca-app-pub-1423857702585511/3397420196");
        mInterstitialAd4.loadAd(new AdRequest.Builder().build());
        mInterstitialAd5 = new InterstitialAd(this);
        mInterstitialAd5.setAdUnitId("ca-app-pub-1423857702585511/3397420196");
        mInterstitialAd5.loadAd(new AdRequest.Builder().build());
        iv1 = findViewById(R.id.imageView1);
        iv2 = findViewById(R.id.imageView2);
        iv3 = findViewById(R.id.imageView3);
        iv4 = findViewById(R.id.imageView);
        iv5 = findViewById(R.id.imageView5);
        share = findViewById(R.id.imageView4);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent click1 = new Intent(MainActivity.this,VideoCutterActivity.class);
                startActivity(click1);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent click2 = new Intent(MainActivity.this, AudioVideoMergeActivity.class);
                startActivity(click2);
                if (mInterstitialAd2.isLoaded()) {
                    mInterstitialAd2.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent click3 = new Intent(MainActivity.this, AudioCutterActivity.class);
                startActivity(click3);
                if (mInterstitialAd3.isLoaded()) {
                    mInterstitialAd3.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent click4 = new Intent(MainActivity.this, ExtractAudioActivity.class);
                startActivity(click4);
                if (mInterstitialAd4.isLoaded()) {
                    mInterstitialAd4.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent click5 = new Intent(MainActivity.this, VideoConcatActivity.class);
                startActivity(click5);
                if (mInterstitialAd5.isLoaded()) {
                    mInterstitialAd5.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApplication();
            }
        });
    }
    public void onBackPressed() {
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to rate this app?");
            builder.setCancelable(false);
            builder.setPositiveButton(android.R.string.yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent viewIntent =
                                    new Intent("android.intent.action.VIEW",
                                            Uri.parse("https://play.google.com/store/apps/details?id=video.cutter.mp3"));
                            startActivity(viewIntent);

                            finish();
                        }
                    });
            builder.setNegativeButton(android.R.string.no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.cancel();
                        }

                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void shareApplication() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;
        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("*/*");

        // Append file and send Intent
        File originalApk = new File(filePath);

        try {
            //Make new directory in new location
            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application's name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + getString(app.labelRes).replace(" ","").toLowerCase() + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            //Open share dialog
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            startActivity(Intent.createChooser(intent, "Share app via"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}