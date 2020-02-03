package com.example.chronos;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronos.adapter.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Method;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    static String[] FUNCTION = {"聊天", "排行", "周历", "闹钟", "计划"};

    //工具条
    private Toolbar toolbar;
    private TextView titleText;

    //装载碎片的viewPager
    private ViewPager viewPager;
    private MenuItem menuItem;//所在碎片指针

    //底部导航栏
    private BottomNavigationView bottomNavigationView;

    //抽屉导航栏
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        titleText = findViewById(R.id.title_text);
        viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);

        //增加三明治图标
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);//把抽屉开关增加到抽屉布局
        toggle.syncState();//使工具条(Toolbar)上的图标与抽屉的状态同步，单机图标打开抽屉时图标会改变

        //把活动注册为抽屉导航的监听器（单击选项时活动会得到通知）
        navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_chat:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_rank:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.item_schedule:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.item_clock:
                                viewPager.setCurrentItem(3);
                                break;
                            case R.id.item_plan:
                                viewPager.setCurrentItem(4);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                titleText.setText(FUNCTION[position]);
                Log.d("MainActivity", "position:" + position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setupViewPager(viewPager);

        //改左上角导航图标
//        Resources resources = MainActivity.this.getResources();
//        Drawable drawable = resources.getDrawable(R.mipmap.add_3);
//        int size = 44;
//        CircleDrawable circleDrawable = new CircleDrawable(drawable, MainActivity.this, size);
//        toolbar.setNavigationIcon(circleDrawable);

        //toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.mipmap.add_2_s));

        //跳至第三页
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
        viewPager.setCurrentItem(2);
    }

    //工具栏溢出区
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    //按下后退按钮
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //溢出区菜单项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_friend:
                Toast.makeText(this, "我是第一个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_group:
                Toast.makeText(this, "我是第二个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_clock:
                Toast.makeText(this, "我是第三个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_plan:
                Toast.makeText(this, "我是第四个", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    //向ViewPager添加片段
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new BlankFragment());
        adapter.addFragment(new BlankFragment());
        adapter.addFragment(new BlankFragment());
        adapter.addFragment(new BlankFragment());
        adapter.addFragment(new BlankFragment());
        viewPager.setAdapter(adapter);
    }

    //抽屉导航菜单项
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_drafts:
                Toast.makeText(this, "我是第一个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_sent:
                Toast.makeText(this, "我是第二个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_trash:
                Toast.makeText(this, "我是第三个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_help:
                Toast.makeText(this, "我是第四个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_feedback:
                Toast.makeText(this, "我是第五个", Toast.LENGTH_SHORT).show();
                break;
            default:
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
