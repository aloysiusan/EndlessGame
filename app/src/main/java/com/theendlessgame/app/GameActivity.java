package com.theendlessgame.app;

import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;

import com.theendlessgame.logic.FileManager;
import com.theendlessgame.gameobjects.Player;
import com.theendlessgame.logic.GameController;

public class GameActivity extends ActionBarActivity {
    private static GameActivity _Instance = null;
    private final int _Margin = 189;
    private final int _AmountLanes = 5;
    private float _ScreenWidth;
    private float _ScreenHeight;
    private GestureDetectorCompat mDetector;
    private ArrayList<ImageView> _ImgEnemies = new ArrayList<ImageView>();
    private ArrayList<ImageView> _ImgShots = new ArrayList<ImageView>();
    private GameController _GameController;


    public static synchronized GameActivity getInstance(){
        return _Instance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        _Instance = this;
        _ScreenWidth = getResources().getDisplayMetrics().widthPixels ;
        _ScreenHeight = getResources().getDisplayMetrics().heightPixels;

        ImageView background = (ImageView)findViewById(R.id.wayBackground);
        ImageView background2 = (ImageView)findViewById(R.id.intersectionBackground);

        background.setY(0);
        background2.setY(-_ScreenHeight);
        this.mDetector = new GestureDetectorCompat(this,new GestureListener());

        _GameController = GameController.getInstance();
        FileManager.setActivity(this);
        FileManager manager = new FileManager();
        try {
            manager.createFile(".data");
        } catch (IOException e) {}
        _GameController.startGame();


        UIThread.getInstance().setGameActivity(this);
        UIThread.getInstance().start();

        ImageView car = (ImageView) findViewById(R.id.playerView);
        RelativeLayout.LayoutParams carLayout = new RelativeLayout.LayoutParams(100,100);
        car.setLayoutParams(carLayout);
        setPlayerLane(Player.getInstance().getLaneNum());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
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
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void setPlayerLane(int pLaneNumber){
        ImageView playerCar = (ImageView) findViewById(R.id.playerView);
        playerCar.setX(((_ScreenWidth-_Margin*2)/_AmountLanes) * pLaneNumber + 70);
        playerCar.setY(_ScreenHeight-250);
    }

    public void addEnemy(int pLaneNumber, int pPosY){
        ImageView imgEnemy = new ImageView(this);
        imgEnemy.setImageResource(R.drawable.enemigo);
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.rLayoutGame);
        relativeLayout.addView(imgEnemy);
        imgEnemy.setLayoutParams(new RelativeLayout.LayoutParams(100,100));
        setObjectLane(imgEnemy, pLaneNumber, pPosY);
        setContentView(relativeLayout);
        _ImgEnemies.add(imgEnemy);
    }
    public void addShot(int pLaneNumber, int pPosY){
        ImageView imgShot = new ImageView(this);
        imgShot.setImageResource(R.drawable.disparo_jugador);
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.rLayoutGame);
        relativeLayout.addView(imgShot);
        imgShot.setLayoutParams(new RelativeLayout.LayoutParams(100,100));
        setObjectLane(imgShot, pLaneNumber, pPosY);
        setContentView(relativeLayout);
        _ImgShots.add(imgShot);

    }

    public void setObjectLane(ImageView pObject, int pLaneNumber, int pPosY){
        pObject.setX(((_ScreenWidth-_Margin*2)/_AmountLanes) * pLaneNumber + 70);
        pObject.setY(pPosY);
    }

    public ArrayList<ImageView> getImgEnemies() {
        return _ImgEnemies;
    }

    public ArrayList<ImageView> getImgShots() {
        return _ImgShots;
    }

    public float getScreenHeight() {
        return _ScreenHeight;
    }
}
