package com.example.bluesky.class0917;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.bluesky.class0917.MVVM.View.NPNHomeView;
import com.example.bluesky.class0917.MVVM.VM.NPNHomeViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements NPNHomeView {

    private NPNHomeViewModel mHomeViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mHomeViewModel = new NPNHomeViewModel();
        mHomeViewModel.attach(this,this);
        mHomeViewModel.updateToServer("http://vtvgo.vn/xem-truc-tuyen-kenh-vtv1-1.html");
    }

    @Override
    public void onSuccessUpdateServer(String message){
        Pattern vtvgo = Pattern.compile("(http://.*m3u8)");
        Matcher m = vtvgo.matcher(message);

        if(m.find() == true){
            String videoUrl = m.group(1);
            Log.d("NPNLab",videoUrl);
        }
    }

    @Override
    public void onErrorUpdateServer(String message){

    }
}
