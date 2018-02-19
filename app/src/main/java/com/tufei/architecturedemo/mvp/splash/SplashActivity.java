package com.tufei.architecturedemo.mvp.splash;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.tufei.architecturedemo.R;
import com.tufei.architecturedemo.base.mvp.BaseActivity;
import com.tufei.architecturedemo.mvp.single.SingleActivity;
import com.tufei.architecturedemo.mvp.face.FaceActivity;
import com.tufei.architecturedemo.mvp.main.MainActivity;
import com.tufei.architecturedemo.utils.LogUtil;

import javax.inject.Inject;

import butterknife.OnClick;

/**
 * @author tufei
 * @date 2017/9/12
 */
public class SplashActivity extends BaseActivity implements SplashContract.View {

    @Inject
    SplashContract.Presenter mPresenter;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void attachPresenter() {
        attach(mPresenter);
    }

    @Override
    protected void initData() {
        LogUtil.d(TAG, "splashPresenter = " + mPresenter);
    }

    @OnClick({R.id.btn_send_msg, R.id.btn_face, R.id.btn_check_update, R.id.btn_single})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send_msg:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.TASK_ID, "你好！");
                startActivity(intent);
                break;
            case R.id.btn_face:
                startActivity(FaceActivity.class);
                break;
            case R.id.btn_check_update:
                //接口是假的。只是演示一下，使用rxjava2+retrofit2时候的示例代码。
                //mPresenter.checkUpdate();
                break;
            case R.id.btn_single:
                startActivity(SingleActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void showMainActivity() {
        startActivity(MainActivity.class);
    }

    @Override
    public void showDownProgress(int progress) {
        //做相关操作
    }

    @Override
    public void showUpdateTipDialog(String path) {
        AlertDialog updateTipDialog = new AlertDialog.Builder(this).setPositiveButton("更新", (dialog, which) -> {
            mPresenter.update(path);
        }).setNegativeButton("取消", (dialog, which) -> {

        }).show();
        addDialog(updateTipDialog);
    }

    @Override
    public void showToast(String tip) {
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
    }
}
