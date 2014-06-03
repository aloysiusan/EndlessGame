package com.theendlessgame.app;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
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

import java.util.ArrayList;

import com.theendlessgame.Model.Intersection;
import com.theendlessgame.Model.Player;
import com.theendlessgame.Logic.GameLogic;

public class GameActivity extends ActionBarActivity {
    private static GameActivity _Instance = null;
    private final int _Margin = 100;
    private final int _AmountLanes = 5;
    private float _ScreenWidth;
    private float _ScreenHeight;
    private GestureDetectorCompat mDetector;
    private ArrayList<ImageView> _ImgEnemies = new ArrayList<ImageView>();
    private ArrayList<ImageView> _ImgShots = new ArrayList<ImageView>();
    private GameLogic _GameController;


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
        this.mDetector = new GestureDetectorCompat(this,new GestureListener());

        _GameController = GameLogic.getInstance();
        _GameController.startGame();

        UIThread.getInstance().set_GameActivity(this);
        UIThread.getInstance().start();

        ImageView car = (ImageView) findViewById(R.id.image_Car);
        RelativeLayout.LayoutParams carLayout = new RelativeLayout.LayoutParams(100,100);
        car.setLayoutParams(carLayout);
        setPlayerLane(Player.getInstance().get_LaneNum());
        //addEnemy(1,800);
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setPlayerLane(int pLaneNumber){
        ImageView playerCar = (ImageView) findViewById(R.id.image_Car);
        playerCar.setX(((_ScreenWidth-_Margin*2)/_AmountLanes) * pLaneNumber -100);
        playerCar.setY(_ScreenHeight-200);
    }
    public void addEnemy(int pLaneNumber, int pPosY){
        ImageView imgEnemy = new ImageView(this);
        imgEnemy.setBackgroundColor(Color.rgb(200, 200, 200));
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.rLayoutGame);
        relativeLayout.addView(imgEnemy);
        imgEnemy.setLayoutParams(new RelativeLayout.LayoutParams(100,100));
        setObjectLane(imgEnemy, pLaneNumber, pPosY);
        setContentView(relativeLayout);
        _ImgEnemies.add(imgEnemy);
        System.out.println("enemy added");

//        Enemy enemy = new Enemy(pLaneNumber,pPosY);
//        Intersection.get_ActualIntersection().addEnemy(enemy);
//        enemy.startThread();
    }
    public void addShot(int pLaneNumber, int pPosY){
        ImageView imgShot = new ImageView(this);
        imgShot.setBackgroundColor(Color.rgb(100, 100, 100));
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.rLayoutGame);
        relativeLayout.addView(imgShot);
        imgShot.setLayoutParams(new RelativeLayout.LayoutParams(100,100));
        setObjectLane(imgShot, pLaneNumber, pPosY);
        setContentView(relativeLayout);
        _ImgShots.add(imgShot);
        System.out.println("shot added");

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setObjectLane(ImageView pObject, int pLaneNumber, int pPosY){
        pObject.setX(((_ScreenWidth-_Margin*2)/_AmountLanes) * pLaneNumber -100);
        pObject.setY(pPosY);

    }

    public ArrayList<ImageView> get_ImgEnemies() {
        return _ImgEnemies;
    }

    public ArrayList<ImageView> get_ImgShots() {
        return _ImgShots;
    }

    public float get_ScreenHeight() {
        return _ScreenHeight;
    }
}
