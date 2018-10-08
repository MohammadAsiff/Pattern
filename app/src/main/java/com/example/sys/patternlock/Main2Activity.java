package com.example.sys.patternlock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;
import com.andrognito.rxpatternlockview.RxPatternLockView;
import com.andrognito.rxpatternlockview.events.PatternLockCompleteEvent;
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent;

import java.util.List;

import io.reactivex.functions.Consumer;

public class Main2Activity extends AppCompatActivity {

    private PatternLockView mPatternLockView2;


    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView2, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView2, pattern));
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);
       /* mPatternLockView = (PatternLockView) findViewById(R.id.patter_lock_view);*/
        mPatternLockView2 = (PatternLockView) findViewById(R.id.patter_lock_view2);
        mPatternLockView2.setDotCount(4);
        mPatternLockView2.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        mPatternLockView2.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        mPatternLockView2.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        mPatternLockView2.setAspectRatioEnabled(true);
        mPatternLockView2.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        mPatternLockView2.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        mPatternLockView2.setDotAnimationDuration(150);
        mPatternLockView2.setPathEndAnimationDuration(100);
        mPatternLockView2.setCorrectStateColor(ResourceUtils.getColor(this, R.color.white));
        mPatternLockView2.setInStealthMode(false);
        mPatternLockView2.setTactileFeedbackEnabled(true);
        mPatternLockView2.setInputEnabled(true);
        mPatternLockView2.addPatternLockListener(mPatternLockViewListener);

        RxPatternLockView.patternComplete(mPatternLockView2)
                .subscribe(new Consumer<PatternLockCompleteEvent>() {
                    @Override
                    public void accept(PatternLockCompleteEvent patternLockCompleteEvent) throws Exception {
                        Log.d(getClass().getName(), "Complete: " + patternLockCompleteEvent.getPattern().toString());
                    }
                });

        RxPatternLockView.patternChanges(mPatternLockView2)
                .subscribe(new Consumer<PatternLockCompoundEvent>() {
                    @Override
                    public void accept(PatternLockCompoundEvent event) throws Exception {
                        if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_STARTED) {
                            Log.d(getClass().getName(), "Pattern drawing started");
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_PROGRESS) {
                            Log.d(getClass().getName(), "Pattern progress: " +
                                    PatternLockUtils.patternToString(mPatternLockView2, event.getPattern()));
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_COMPLETE) {
                            Log.d(getClass().getName(), "Pattern complete: " +
                                    PatternLockUtils.patternToString(mPatternLockView2, event.getPattern()));

                             String s2=PatternLockUtils.patternToString(mPatternLockView2, event.getPattern());
                            Intent intent=getIntent();
                            String s1= intent.getExtras().getString("s");
                            if (s1.equals( s2 )) {
                                Intent intent1=new Intent( Main2Activity.this,Main3Activity.class );
                                intent1.putExtra( "s",s1 );
                                startActivity( intent1);
                            }
                            else {
                                Toast.makeText( getApplicationContext(), "You Entered a wrong Pin", Toast.LENGTH_SHORT ).show();
                            }
                            } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_CLEARED) {
                            Log.d(getClass().getName(), "Pattern has been cleared");
                        }
                    }
                });
               /* findViewById( R.id.btn ).setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=getIntent();
                        String s1= intent.getExtras().getString("s");
                        if (s1==s2)
                            startActivity( new Intent( Main2Activity.this,Main3Activity.class ) );
                        else
                            Toast.makeText( getApplicationContext(),"You Entered a wrong Pin",Toast.LENGTH_SHORT ).show();
                    }
                } );*/

    }
}
