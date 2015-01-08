/**
 *
 */
package cn.edu.zju.isst1.ui.main;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.api.LogoutApi;
import cn.edu.zju.isst1.db.DataManager;
import cn.edu.zju.isst1.net.CSTResponse;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.settings.CSTSettings;
import cn.edu.zju.isst1.ui.city.CastellanFragment;
import cn.edu.zju.isst1.ui.job.EmploymentListFragment;
import cn.edu.zju.isst1.ui.job.InternshipListFragment;
import cn.edu.zju.isst1.ui.job.RecommedListFragment;
import cn.edu.zju.isst1.ui.life.WikGridFragment;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.archive.gui.ExperienceFragment;
import cn.edu.zju.isst1.v2.archive.gui.NewsFragment;
import cn.edu.zju.isst1.v2.archive.gui.StudyFragment;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAddressListDataDelegate;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAlumniDataDelegate;
import cn.edu.zju.isst1.v2.contact.contact.gui.BaseContactListFragment;
import cn.edu.zju.isst1.v2.event.campus.data.CSTCampusEventDataDelegate;
import cn.edu.zju.isst1.v2.event.campus.gui.CSTCampusEventListFragment;
import cn.edu.zju.isst1.v2.event.city.data.CSTCityEventDataDelegate;
import cn.edu.zju.isst1.v2.event.city.gui.CSTCityEventListFragment;
import cn.edu.zju.isst1.v2.login.gui.LoginActivity;
import cn.edu.zju.isst1.v2.restaurant.gui.NewRestaurantListFragment;
import cn.edu.zju.isst1.v2.user.data.CSTUserDataDelegate;
import cn.edu.zju.isst1.v2.usercenter.UserCenterFragment;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.gui.PushMessagesActivity;
import cn.edu.zju.isst1.widget.slidemenu.ResideMenu;
import cn.edu.zju.isst1.widget.slidemenu.ResideMenuItem;


/**
 * @author theasir
 */
public class NewMainActivity extends BaseActivity implements View.OnClickListener {

    private final String mDrawerTitle = "导航";

    private String mTitle;

    private boolean IS_EXIT = false;

    private Fragment mCurrentFragment;

    private ResideMenu resideMenu;
    private TextView titleTxv;
    private ImageButton rightBtn;
    private ImageButton homeBtn;

    private ResideMenuItem itemInternship;
    private ResideMenuItem itemConvenient;
    private ResideMenuItem itemExperience;
    private ResideMenuItem itemUserCenter;
    private ResideMenuItem itemRecommend;
    private ResideMenuItem itemEmploy;
    private ResideMenuItem itemCampusEvent;
    private ResideMenuItem itemWiki;
    private ResideMenuItem itemNews;
    private ResideMenuItem itemStudy;
    private ResideMenuItem itemContact;
    private ResideMenuItem itemContactCity;
    private ResideMenuItem itemCityMaster;
    private ResideMenuItem itemCityEvent;

    private ResideMenuItem itemLine1;
    private ResideMenuItem itemLine2;
    private ResideMenuItem itemLine3;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_reside_menu_activity);

        mCurrentFragment = null;
        setUpMenu();
        setUpActionbar();
        initHandler();
        if (savedInstanceState == null) {
            mCurrentFragment = NewsFragment.getInstance();
            titleTxv.setText(R.string.menu_news);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, mCurrentFragment, "fragment")
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

        }


        requestGlobalData();
        updateLogin();
    }

    /**
     * 初始化侧滑栏
     */
    private void setUpMenu() {

//        titleTxv = (TextView) findViewById(R.id.title_bar_title_menu);
//        rightBtn = (Button) findViewById(R.id.title_bar_right_menu);
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background3);
        resideMenu.setUserName(CSTUserDataDelegate.getCurrentUser(this).name);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftenu width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemCampusEvent = new ResideMenuItem(this, R.drawable.menux_zaixiaohuodong, R.string.menu_campus_event);
        itemEmploy = new ResideMenuItem(this, R.drawable.menux_jiuye, R.string.menu_employment);
        itemNews = new ResideMenuItem(this, R.drawable.menux_ruanyuankuaixun, R.string.menu_news);
        itemStudy = new ResideMenuItem(this, R.drawable.menux_xuexiyuandi, R.string.menu_study);
        itemConvenient = new ResideMenuItem(this, R.drawable.menux_bianjiefuwu, R.string.menu_convenient_service);
        itemWiki = new ResideMenuItem(this, R.drawable.menux_ruanyuanbaike, R.string.menu_wiki);
        itemContact = new ResideMenuItem(this, R.drawable.menux_tongxunlu, R.string.menu_contact);
        itemContactCity = new ResideMenuItem(this, R.drawable.menux_tongchengxiaoyou, R.string.menu_contact_city);
        itemCityMaster = new ResideMenuItem(this, R.drawable.menux_chengzhu, R.string.menu_city_master);
        itemExperience = new ResideMenuItem(this, R.drawable.menux_jingyanjiaoliu, R.string.menu_experience);
        itemInternship = new ResideMenuItem(this, R.drawable.menux_shixi, R.string.menu_internship);
        itemUserCenter = new ResideMenuItem(this, R.drawable.menux_gerenzhongxin, R.string.menu_user_center);
        itemRecommend = new ResideMenuItem(this, R.drawable.menux_neitui, R.string.menu_recommend);
        itemCityEvent = new ResideMenuItem(this, R.drawable.menux_tongchenghuodong, R.string.menu_city_event);

        itemLine1 = new ResideMenuItem(this, R.color.darkgray, "", 0);
        itemLine2 = new ResideMenuItem(this, R.color.darkgray, "", 0);
        itemLine3 = new ResideMenuItem(this, R.color.darkgray, "", 0);


        itemCampusEvent.setOnClickListener(this);
        itemNews.setOnClickListener(this);
        itemEmploy.setOnClickListener(this);
        itemStudy.setOnClickListener(this);
        itemWiki.setOnClickListener(this);
        itemConvenient.setOnClickListener(this);
        itemContact.setOnClickListener(this);
        itemContactCity.setOnClickListener(this);
        itemCityMaster.setOnClickListener(this);
        itemExperience.setOnClickListener(this);
        itemInternship.setOnClickListener(this);
        itemUserCenter.setOnClickListener(this);
        itemRecommend.setOnClickListener(this);
        itemCityEvent.setOnClickListener(this);

//        itemCityMaster.setOnClickListener(this);
//        itemExperience.setOnClickListener(this);
//        itemInternship.setOnClickListener(this);
//        itemCalendar.setOnClickListener(this);
//        itemSettings.setOnClickListener(this);
//        resideMenu.addMenuItem(itemIcon, ResideMenu.DIRECTION_LEFT);

        resideMenu.addMenuItem(itemNews, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemWiki, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCampusEvent, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemConvenient, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemStudy, ResideMenu.DIRECTION_LEFT);

        resideMenu.addMenuItem(itemLine1, ResideMenu.DIRECTION_LEFT);

        resideMenu.addMenuItem(itemInternship, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemEmploy, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemRecommend, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemExperience, ResideMenu.DIRECTION_LEFT);

        resideMenu.addMenuItem(itemLine2, ResideMenu.DIRECTION_LEFT);

        resideMenu.addMenuItem(itemContact, ResideMenu.DIRECTION_LEFT);

        resideMenu.addMenuItem(itemLine3, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCityMaster, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCityEvent, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemContactCity, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemUserCenter, ResideMenu.DIRECTION_LEFT);


//        resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_RIGHT);
//        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

//        homeBtn = (ImageButton) findViewById(R.id.title_bar_left_menu);
//        homeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
//            }
//        });
//
//        rightBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(NewMainActivity.this, PushMessagesActivity.class);
//                NewMainActivity.this.startActivity(intent);
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        resideMenu.closeMenu();
        if (view == itemNews) {
            changeFragment(NewsFragment.getInstance());
            titleTxv.setText(R.string.menu_news);
        } else if (view == itemWiki) {
            changeFragment(WikGridFragment.getInstance());
            titleTxv.setText(R.string.menu_wiki);
        } else if (view == itemCampusEvent) {
            changeFragment(CSTCampusEventListFragment.getInstance());
            titleTxv.setText(R.string.menu_campus_event);
        } else if (view == itemConvenient) {
            changeFragment(NewRestaurantListFragment.getInstance());
            titleTxv.setText(R.string.menu_convenient_service);
        } else if (view == itemStudy) {
            changeFragment(StudyFragment.getInstance());
            titleTxv.setText(R.string.menu_study);
        } else if (view == itemInternship) {
            changeFragment(InternshipListFragment.getInstance());
            titleTxv.setText(R.string.menu_internship);
        } else if (view == itemEmploy) {
            changeFragment(EmploymentListFragment.getInstance());
            titleTxv.setText(R.string.menu_employment);
        } else if (view == itemRecommend) {
            changeFragment(RecommedListFragment.getInstance());
            titleTxv.setText(R.string.menu_recommend);
        } else if (view == itemExperience) {
            changeFragment(ExperienceFragment.getInstance());
            titleTxv.setText(R.string.menu_experience);
        } else if (view == itemContact) {
            changeFragment((BaseContactListFragment
                    .getInstance(BaseContactListFragment.FilterType.MY_CLASS)));
            titleTxv.setText(R.string.menu_contact);
        } else if (view == itemCityMaster) {
            changeFragment(CastellanFragment.GetInstance());
            titleTxv.setText(R.string.menu_city_master);
        } else if (view == itemCityEvent) {
            changeFragment(CSTCityEventListFragment.getInstance());
            titleTxv.setText(R.string.menu_city_event);
        } else if (view == itemContactCity) {
            changeFragment((BaseContactListFragment
                    .getInstance(BaseContactListFragment.FilterType.MY_CITY)));
            titleTxv.setText(R.string.menu_contact_city);
        } else if (view == itemUserCenter) {
            changeFragment(UserCenterFragment.getInstance());
            titleTxv.setText(R.string.menu_user_center);
        }
//        resideMenu.closeMenu();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Lgr.d(this.getClass().getName(), "back button pressed");
            if (!IS_EXIT) {
                exit();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
//            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
            homeBtn.setVisibility(View.INVISIBLE);
        }

        @Override
        public void closeMenu() {
//            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
            homeBtn.setVisibility(View.VISIBLE);
        }
    };

    /**
     * 切換fragment
     *
     * @param targetFragment 目標fragement
     */
    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
//        getFragmentManager()
//                .beginTransaction()
//                .replace(R.id.main_fragment, targetFragment, "fragment")
//                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                .commit();

        FragmentTransaction transaction = getFragmentManager().beginTransaction().
                setTransitionStyle(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        // 先判断是否被add过
        if (!targetFragment.isAdded()) {

            // 隐藏当前的fragment，add下一个到Activity中
            transaction.hide(mCurrentFragment).add(R.id.main_fragment, targetFragment).commit();
        } else {

            // 隐藏当前的fragment，显示下一个
            transaction.hide(mCurrentFragment).show(targetFragment).commit();
        }
        mCurrentFragment = targetFragment;
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                IS_EXIT = false;
            }
        };
    }

    private void exit() {
        if (!IS_EXIT) {
            IS_EXIT = true;
            Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {

            Lgr.e("NewMainAcitivity ", "exit application");

            this.finish();
        }
    }

    public void logout() {
        LogoutApi.logout(new RequestListener() {

            @Override
            public void onComplete(Object result) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onHttpError(CSTResponse response) {
                Lgr.i("logout onHttpError: " + response.getStatus());

            }

            @Override
            public void onException(Exception e) {
                // TODO Auto-generated method stub

            }
        });
        DataManager.deleteCurrentUser();

        CSTCampusEventDataDelegate.deleteAllCampusEvent(this);
        CSTCityEventDataDelegate.deleteAllCityEvent(this);
        CSTAlumniDataDelegate.deleteAllAlumni(this);
        CSTAddressListDataDelegate.deleteAllAlumni(this);

        CSTSettings.setAutoLogin(false, NewMainActivity.this);
        NewMainActivity.this.startActivity(new Intent(NewMainActivity.this,
                LoginActivity.class));
        NewMainActivity.this.finish();
    }

    private void setUpActionbar() {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        View viewTitleBar = getLayoutInflater().inflate(R.layout.main_activity_menu, null);
        getActionBar().setCustomView(viewTitleBar, lp);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setDisplayShowCustomEnabled(true);
        homeBtn = (ImageButton) getActionBar().getCustomView().findViewById(R.id.title_bar_left_menu);
        rightBtn = (ImageButton) getActionBar().getCustomView().findViewById(R.id.title_bar_right_menu);
        titleTxv = (TextView) getActionBar().getCustomView().findViewById(R.id.title_bar_title_menu);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMainActivity.this, PushMessagesActivity.class);
                NewMainActivity.this.startActivity(intent);
            }
        });
    }


}
