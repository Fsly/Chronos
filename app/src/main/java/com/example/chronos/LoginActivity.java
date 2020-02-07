package com.example.chronos;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.chronos.ui.ClearableEditTextWithIcon;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnKeyListener {

    private TextView rightTopBtn;  // ActionBar完成按钮

    private TextView switchModeBtn;  // 注册/登录切换按钮

    private ClearableEditTextWithIcon loginAccountEdit;

    private ClearableEditTextWithIcon loginPasswordEdit;

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
    }

    /**
     * ActionBar 右上角按钮
     */
    private void initRightTopBtn() {
        rightTopBtn = addRegisterRightTopBtn(this, "登录");
        rightTopBtn.setOnClickListener(v -> {
            if (registerMode) {
                register();
            } else {
                //fakeLoginTest(); // 假登录代码示例
                login();
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
        loginAccountEdit.setText("UserAccount");
    }

    /**
     * 注册面板
     */
    private void setupRegisterPanel() {
        loginLayout = findViewById(R.id.login_layout);
        registerLayout = findViewById(R.id.register_layout);
        switchModeBtn = findViewById(R.id.register_login_tip);
        switchModeBtn.setVisibility(View.VISIBLE);
        switchModeBtn.setOnClickListener(v -> switchMode());
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
     * ***************************************** 注册 **************************************
     */
    private void register() {
        if (!registerMode || !registerPanelInited) {
            return;
        }
        if (!checkRegisterContentValid()) {
            return;
        }
        if (!NetworkUtil.isNetAvailable(LoginActivity.this)) {
            ToastHelper.showToast(LoginActivity.this, R.string.network_is_not_available);
            return;
        }
        DialogMaker.showProgressDialog(this, getString(R.string.registering), false);
        // 注册流程
        final String account = registerAccountEdit.getText().toString();
        final String nickName = registerNickNameEdit.getText().toString();
        final String password = registerPasswordEdit.getText().toString();
        ContactHttpClient.getInstance().register(account, nickName, password,
                new ContactHttpClient.ContactHttpCallback<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        ToastHelper.showToast(LoginActivity.this,
                                R.string.register_success);
                        switchMode();  // 切换回登录
                        loginAccountEdit.setText(account);
                        loginPasswordEdit.setText(password);
                        registerAccountEdit.setText("");
                        registerNickNameEdit.setText("");
                        registerPasswordEdit.setText("");
                        DialogMaker.dismissProgressDialog();
                    }

                    @Override
                    public void onFailed(int code,
                                         String errorMsg) {
                        ToastHelper.showToast(LoginActivity.this,
                                getString(
                                        R.string.register_failed,
                                        String.valueOf(
                                                code),
                                        errorMsg));
                        DialogMaker.dismissProgressDialog();
                    }
                });
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
