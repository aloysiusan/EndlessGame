package com.theendlessgame.app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private static MainActivity _Instance = null;
    public static synchronized MainActivity getInstance(){
        return _Instance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _Instance = this;
        //Resources res = getResources();
        //RelativeLayout rLayout = (RelativeLayout) findViewById (R.id.rLayout);
        //Drawable drawable = res.getDrawable(R.drawable.s_Freeway_1);
        //rLayout.setBackground(drawable);
        Button btn_Start = (Button) findViewById(R.id.btn_Start);
        btn_Start.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_Start:
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

        }
    }
}
