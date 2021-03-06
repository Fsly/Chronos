package com.example.chronos;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronos.common.ToastHelper;
import com.example.chronos.connect.Connect;
import com.example.chronos.database.LoginUser;
import com.example.chronos.ui.ClearableEditTextWithIcon;

import org.litepal.LitePal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity implements View.OnKeyListener {

    private TextView rightTopBtn;  // ActionBar完成按钮

    private TextView switchModeBtn;  // 注册/登录切换按钮

    //登录输入框
    private ClearableEditTextWithIcon loginAccountEdit;
    private ClearableEditTextWithIcon loginPasswordEdit;

    //注册输入框
    private ClearableEditTextWithIcon registerAccountEdit;
    private ClearableEditTextWithIcon registerNickNameEdit;
    private ClearableEditTextWithIcon registerPasswordEdit;

    private View loginLayout;

    private View registerLayout;

    private boolean registerMode = false; // 注册模式

    private boolean registerPanelInited = false; // 注册面板是否初始化

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(R.string.login);

        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        initRightTopBtn();
        setupLoginPanel();
        setupRegisterPanel();
    }


    /**
     * ActionBar 右上角按钮
     */
    private void initRightTopBtn() {
        rightTopBtn = addRegisterRightTopBtn(this, "登录");
        rightTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerMode) {
                    LoginActivity.this.register();
                } else {
                    LoginActivity.this.login();
                }
            }
        });
    }

    /**
     * 登录面板
     */
    private void setupLoginPanel() {
        loginAccountEdit = findViewById(R.id.edit_login_account);
        loginPasswordEdit = findViewById(R.id.edit_login_password);
        loginAccountEdit.setIconResource(R.drawable.user_account_icon);
        loginPasswordEdit.setIconResource(R.drawable.user_pwd_lock_icon);
        loginAccountEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        loginPasswordEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        loginAccountEdit.addTextChangedListener(textWatcher);
        loginPasswordEdit.addTextChangedListener(textWatcher);
        loginPasswordEdit.setOnKeyListener(this);
        //loginAccountEdit.setText("UserAccount");
    }

    /**
     * 注册面板
     */
    private void setupRegisterPanel() {
        loginLayout = findViewById(R.id.login_layout);
        registerLayout = findViewById(R.id.register_layout);
        switchModeBtn = findViewById(R.id.register_login_tip);
        switchModeBtn.setVisibility(View.VISIBLE);
        switchModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.switchMode();
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (registerMode) {
                return;
            }
            // 登录模式  ，更新右上角按钮状态
            boolean isEnable = loginAccountEdit.getText().length() > 0 &&
                    loginPasswordEdit.getText().length() > 0;
            updateRightTopBtn(rightTopBtn, isEnable);
        }
    };

    private void updateRightTopBtn(TextView rightTopBtn, boolean isEnable) {
        rightTopBtn.setText("完成");
        rightTopBtn.setBackgroundResource(R.drawable.g_white_btn_selector);
        rightTopBtn.setEnabled(isEnable);
        rightTopBtn.setTextColor(getResources().getColor(R.color.color_blue_0888ff));
        //rightTopBtn.setPadding(ScreenUtil.dip2px(10), 0, ScreenUtil.dip2px(10), 0);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * ***************************************** 登录 **************************************
     */
    private void login() {
        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        final String account = loginAccountEdit.getEditableText().toString().toLowerCase();
        final String password = loginPasswordEdit.getEditableText().toString();
        final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "提示", "登录中\u2026", false);

        //注册成功，切换回登录，修改UI
        final Runnable loginSuccess = new Runnable() {
            @Override
            public void run() {
                try {
                    // 登录
                    Log.i("LoginActivity", "login success");

                    // 初始化消息提醒配置
                    initNotificationConfig();

                    // 进入主界面
                    finish();

                    ToastHelper.showToast(LoginActivity.this, "登录成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        final Runnable loginFail = new Runnable() {
            @Override
            public void run() {
                ToastHelper.showToast(LoginActivity.this, "用户名或密码错误");
            }
        };

        //保存用户名
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connect connect = new Connect();
                boolean isReg = connect.login(account, password);
                Log.d("LoginActivity", "登录结果：" + isReg);
                if (dialog.isShowing()) dialog.dismiss();

                if (isReg) {
                    String nickName = connect.getUserData(account, password);
                    localDataGet(account, nickName);
                    new Handler(Looper.getMainLooper()).post(loginSuccess);
                } else {
                    new Handler(Looper.getMainLooper()).post(loginFail);
                }
            }
        }).start();
    }

    //登录数据记录
    private void localDataGet(String account,String nickName) {
        LitePal.getDatabase();
        LoginUser user=new LoginUser();
        LitePal.deleteAll(LoginUser.class);
        user.setAccount(account);
        user.setNickName(nickName);
        user.setStatus(true);
        user.save();
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        // 加载状态栏配置
        // 更新配置
    }

    /**
     * ***************************************** 注册 **************************************
     */
    private void register() {
        if (!registerMode || !registerPanelInited) {
            return;
        }
        if (!checkRegisterContentValid()) {
            return;
        }
        // 注册流程
        final String account = registerAccountEdit.getText().toString();
        final String nickName = registerNickNameEdit.getText().toString();
        final String password = registerPasswordEdit.getText().toString();
        final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "提示", "注册中\u2026", false);

        //注册成功，切换回登录，修改UI
        final Runnable switchBack = new Runnable() {
            @Override
            public void run() {
                try {
                    switchMode();  // 切换回登录
                    loginAccountEdit.setText(account);
                    loginPasswordEdit.setText(password);
                    registerAccountEdit.setText("");
                    registerNickNameEdit.setText("");
                    registerPasswordEdit.setText("");

                    ToastHelper.showToast(LoginActivity.this, "注册成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        final Runnable same = new Runnable() {
            @Override
            public void run() {
                ToastHelper.showToast(LoginActivity.this, "账号已存在");
            }
        };

        final Runnable failRegister = new Runnable() {
            @Override
            public void run() {
                ToastHelper.showToast(LoginActivity.this, "注册失败");
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Connect connect = new Connect();
                int isReg = connect.register(account, nickName, password);
                Log.d("LoginActivity", "注册结果：" + isReg);
                if (dialog.isShowing()) dialog.dismiss();

                switch (isReg) {
                    case 1:
                        //如果注册成功
                        new Handler(Looper.getMainLooper()).post(switchBack);
                        break;
                    case 2:
                        new Handler(Looper.getMainLooper()).post(same);
                        break;
                    case 0:
                        new Handler(Looper.getMainLooper()).post(failRegister);
                        break;
                    default:
                        break;
                }
            }
        }).start();
    }

    private boolean checkRegisterContentValid() {
        if (!registerMode || !registerPanelInited) {
            Log.d("LogActivity", "什么鬼");
            return false;
        }
        // 帐号检查
        String account = registerAccountEdit.getText().toString().trim();
        if (account.length() <= 0 || account.length() > 20) {
            Toast.makeText(this, "帐号限20位字母或者数字", Toast.LENGTH_SHORT).show();
            return false;
        }
        // 昵称检查
        String nick = registerNickNameEdit.getText().toString().trim();
        if (nick.length() <= 0 || nick.length() > 10) {
            Toast.makeText(this, "昵称限10位汉字、字母或者数字", Toast.LENGTH_SHORT).show();
            return false;
        }
        // 密码检查
        String password = registerPasswordEdit.getText().toString().trim();
        if (password.length() < 6 || password.length() > 20) {
            Toast.makeText(this, "密码必须为6~20位字母或者数字", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * ***************************************** 注册/登录切换 **************************************
     */
    private void switchMode() {
        registerMode = !registerMode;
        if (registerMode && !registerPanelInited) {
            registerAccountEdit = findViewById(R.id.edit_register_account);
            registerNickNameEdit = findViewById(R.id.edit_register_nickname);
            registerPasswordEdit = findViewById(R.id.edit_register_password);
            registerAccountEdit.setIconResource(R.drawable.user_account_icon);
            registerNickNameEdit.setIconResource(R.drawable.nick_name_icon);
            registerPasswordEdit.setIconResource(R.drawable.user_pwd_lock_icon);
            registerAccountEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            registerNickNameEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            registerPasswordEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            registerAccountEdit.addTextChangedListener(textWatcher);
            registerNickNameEdit.addTextChangedListener(textWatcher);
            registerPasswordEdit.addTextChangedListener(textWatcher);
            registerPanelInited = true;
        }
        setTitle(registerMode ? R.string.register : R.string.login);
        rightTopBtn.setText(registerMode ? R.string.register : R.string.login);
        loginLayout.setVisibility(registerMode ? View.GONE : View.VISIBLE);
        registerLayout.setVisibility(registerMode ? View.VISIBLE : View.GONE);
        switchModeBtn.setText(registerMode ? R.string.login_has_account : R.string.register);
        if (registerMode) {
            rightTopBtn.setEnabled(true);
        } else {
            boolean isEnable = loginAccountEdit.getText().length() > 0 &&
                    loginPasswordEdit.getText().length() > 0;
            rightTopBtn.setEnabled(isEnable);
        }
    }

    public TextView addRegisterRightTopBtn(AppCompatActivity activity, String strResId) {
        TextView textView = findViewById(R.id.action_bar_right_clickable_textview);
        textView.setText(strResId);
        textView.setBackgroundResource(R.drawable.register_right_top_btn_selector);
        //textView.setPadding(ScreenUtil.dip2px(10), 0, ScreenUtil.dip2px(10), 0);
        return textView;
    }
}
