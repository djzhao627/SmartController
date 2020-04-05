package cn.djzhao.smartcontroller;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.djzhao.smartcontroller.dialog.BottomPanel;
import cn.djzhao.smartcontroller.utils.SharedPreferenceUtils;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_power_iv)
    ImageView tvPowerIv;
    @BindView(R.id.router_power_iv)
    ImageView routerPowerIv;
    @BindView(R.id.light_power_iv)
    ImageView lightPowerIv;
    @BindView(R.id.air_condition_status_tv)
    TextView airConditionStatusTv;
    @BindView(R.id.icebox_status_tv)
    TextView iceboxStatusTv;
    @BindView(R.id.curtain_status_tv)
    TextView curtainStatusTv;

    private Context mContext;

    // TV, router, light
    private boolean[] powers = {false, false, false};
    // airCondition, icebox, curtain
    private int[] status = {28, 70, 80};

    private BottomPanel dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        echo();
    }

    /**
     * 数据回显
     */
    private void echo() {
        powers[0] = SharedPreferenceUtils.getBoolean(mContext, "tv", false);
        powers[1] = SharedPreferenceUtils.getBoolean(mContext, "router", false);
        powers[2] = SharedPreferenceUtils.getBoolean(mContext, "light", false);

        if (powers[0]) {
            tvPowerIv.setImageResource(R.drawable.ic_power_on);
        } else {
            tvPowerIv.setImageResource(R.drawable.ic_power_off);
        }
        if (powers[1]) {
            routerPowerIv.setImageResource(R.drawable.ic_power_on);
        } else {
            routerPowerIv.setImageResource(R.drawable.ic_power_off);
        }
        if (powers[2]) {
            lightPowerIv.setImageResource(R.drawable.ic_power_on);
        } else {
            lightPowerIv.setImageResource(R.drawable.ic_power_off);
        }

        status[0] = SharedPreferenceUtils.getInt(mContext, "airCondition", 20);
        status[1] = SharedPreferenceUtils.getInt(mContext, "icebox", 60);
        status[2] = SharedPreferenceUtils.getInt(mContext, "curtain", 70);

        airConditionStatusTv.setText(String.format("%d℃", status[0]));
        iceboxStatusTv.setText(String.format("%d%%", status[1]));
        curtainStatusTv.setText(String.format("%d%%", status[2]));
    }

    private void initView() {
        dialog = new BottomPanel(mContext);
        dialog.setOnDismissListener(dialog -> echo());
    }

    @OnClick({R.id.tv_power_ll, R.id.router_power_ll, R.id.light_power_ll, R.id.curtain_power_ll, R.id.air_condition_power_ll, R.id.icebox_power_ll, R.id.air_condition_show_ll, R.id.icebox_show_ll, R.id.curtain_show_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_power_ll:
                powers[0] = !powers[0];
                SharedPreferenceUtils.putBoolean(mContext, "tv", powers[0]);
                echo();
                break;
            case R.id.router_power_ll:
                powers[1] = !powers[1];
                SharedPreferenceUtils.putBoolean(mContext, "router", powers[1]);
                echo();
                break;
            case R.id.light_power_ll:
                powers[2] = !powers[2];
                SharedPreferenceUtils.putBoolean(mContext, "light", powers[2]);
                echo();
                break;
            case R.id.curtain_power_ll:
            case R.id.curtain_show_ll:
                dialog.show(0);
                break;
            case R.id.air_condition_power_ll:
            case R.id.air_condition_show_ll:
                dialog.show(1);
                break;
            case R.id.icebox_power_ll:
            case R.id.icebox_show_ll:
                dialog.show(2);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
