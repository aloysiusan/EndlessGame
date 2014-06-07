package com.theendlessgame.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import com.theendlessgame.logic.FileManager;
import com.theendlessgame.gameobjects.Player;
import com.theendlessgame.logic.GameController;

public class GameActivity extends ActionBarActivity {

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

        wayBackground = (ImageView)findViewById(R.id.wayBackground);
        intersectionBackground = (ImageView)findViewById(R.id.intersectionBackground);
        wayIndicator = (ImageView)findViewById(R.id.wayIndicator);
        livesView = (RatingBar)findViewById(R.id.livesView);
        adView = (FrameLayout)findViewById(R.id.adView);
        adLogoView = (ImageView)findViewById(R.id.adLogoView);
        lblNodeId = (TextView)findViewById(R.id.lblNodeId);
        lblScore = (TextView)findViewById(R.id.lblScore);
        playerCar = (ImageView) findViewById(R.id.playerView);

        _ImgActualArm =  (ImageView) findViewById(R.id.armView);

        wayBackground.setY(0);
        intersectionBackground.setY(-_ScreenHeight);
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
        RelativeLayout.LayoutParams carLayout = new RelativeLayout.LayoutParams(DEFAULT_POSITION, DEFAULT_POSITION);
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

    public void endGame(){
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Your final score is: " + Player.getInstance().getScore() + "pts")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / DPI);
        return px;
    }

    public void setPlayerLane(int pLaneNumber){
        playerCar.setX(((_ScreenWidth-MARGIN*2)/AMOUNT_LANES) * pLaneNumber + DEFAULT_POSITION_X_OFFSET);
        playerCar.setY(_ScreenHeight - DEFAULT_POSITION_Y_OFFSET);
    }

    public void addEnemy(int pLaneNumber, int pPosY){
        ImageView imgEnemy = new ImageView(this);
        imgEnemy.setImageResource(R.drawable.enemigo);
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.rLayoutGame);
        relativeLayout.addView(imgEnemy);
        imgEnemy.setLayoutParams(new RelativeLayout.LayoutParams(DEFAULT_POSITION,DEFAULT_POSITION));
        setObjectLane(imgEnemy, pLaneNumber, pPosY);
        setContentView(relativeLayout);
        _ImgEnemies.add(imgEnemy);
    }
    public void addShot(int pLaneNumber, int pPosY){
        ImageView imgShot = new ImageView(this);
        imgShot.setImageResource(R.drawable.disparo_jugador);
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.rLayoutGame);
        relativeLayout.addView(imgShot);
        imgShot.setLayoutParams(new RelativeLayout.LayoutParams(DEFAULT_POSITION,DEFAULT_POSITION));
        setObjectLane(imgShot, pLaneNumber, pPosY);
        setContentView(relativeLayout);
        _ImgShots.add(imgShot);

    }
    public void addArm(int pLaneNumber, int pPosY){
        ImageView imgArm = new ImageView(this);
        imgArm.setImageResource(R.drawable.arma);
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.rLayoutGame);
        relativeLayout.addView(imgArm);
        imgArm.setLayoutParams(new RelativeLayout.LayoutParams(DEFAULT_POSITION,DEFAULT_POSITION));
        setObjectLane(imgArm, pLaneNumber, pPosY);
        setContentView(relativeLayout);
        _ImgArm = imgArm;

    }

    public void setObjectLane(ImageView pObject, int pLaneNumber, int pPosY){
        pObject.setX(((_ScreenWidth-MARGIN*2)/AMOUNT_LANES) * pLaneNumber + DEFAULT_POSITION_X_OFFSET);
        pObject.setY(pPosY);
    }

    public ImageView getImgActualArm() {
        return _ImgActualArm;
    }

    public ImageView getArm() {
        return _ImgArm;
    }

    public void setArm(ImageView _Arm) {
        this._ImgArm = _Arm;
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

    private static GameActivity _Instance = null;
    private static final float DPI = 160f;

    private final int MARGIN = 189;
    private final int AMOUNT_LANES = 5;
    private final int DEFAULT_POSITION = 100;
    private final int DEFAULT_POSITION_X_OFFSET = 70;
    private final int DEFAULT_POSITION_Y_OFFSET = 250;

    private float _ScreenWidth;
    private float _ScreenHeight;
    private GestureDetectorCompat mDetector;
    private ArrayList<ImageView> _ImgEnemies = new ArrayList<ImageView>();
    private ArrayList<ImageView> _ImgShots = new ArrayList<ImageView>();
    private ImageView _ImgArm = null;
    private ImageView _ImgActualArm;
    private GameController _GameController;

    protected ImageView wayBackground;
    protected ImageView intersectionBackground;
    protected ImageView wayIndicator;
    protected RatingBar livesView;
    protected FrameLayout adView;
    protected ImageView adLogoView;
    protected TextView lblNodeId;
    protected TextView lblScore;
    protected ImageView playerCar;
}
