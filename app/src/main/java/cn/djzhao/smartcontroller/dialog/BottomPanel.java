package cn.djzhao.smartcontroller.dialog;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import cn.djzhao.smartcontroller.R;
import cn.djzhao.smartcontroller.utils.SharedPreferenceUtils;

public class BottomPanel extends BottomSheetDialog implements OnSeekChangeListener, View.OnClickListener {

    private String TAG = "BottomPanel";

    LinearLayout curtainPowerLl;
    LinearLayout airConditionPowerLl;
    LinearLayout iceboxPowerLl;
    IndicatorSeekBar curtainSB, airConditionSB, iceboxSB;
    Button cancel;

    private Context mContext;

    private int type;

    public BottomPanel(@NonNull Context context) {
        super(context);
        View view = getLayoutInflater().inflate(R.layout.layout_bottom_operation, null);
        setContentView(view);
        mContext = context;
        curtainPowerLl = view.findViewById(R.id.curtain_power_ll);
        airConditionPowerLl = view.findViewById(R.id.air_condition_power_ll);
        iceboxPowerLl = view.findViewById(R.id.icebox_power_ll);

        curtainSB = view.findViewById(R.id.curtain_power_sb);
        airConditionSB = view.findViewById(R.id.air_condition_power_sb);
        iceboxSB = view.findViewById(R.id.icebox_power_sb);

        cancel = view.findViewById(R.id.cancel);

        initEvent();
    }

    private void initEvent() {
        curtainSB.setOnSeekChangeListener(this);
        airConditionSB.setOnSeekChangeListener(this);
        iceboxSB.setOnSeekChangeListener(this);
        cancel.setOnClickListener(this);
    }

    /**
     *  显示弹窗
     * @param type 0 窗帘     1 空调    2 冰箱
     */
    public void show(int type) {
        this.type = type;
        switch (type) {
            case 0:
                curtainPowerLl.setVisibility(View.VISIBLE);
                airConditionPowerLl.setVisibility(View.GONE);
                iceboxPowerLl.setVisibility(View.GONE);
                break;
            case 1:
                airConditionPowerLl.setVisibility(View.VISIBLE);
                curtainPowerLl.setVisibility(View.GONE);
                iceboxPowerLl.setVisibility(View.GONE);
                break;
            case 2:
                iceboxPowerLl.setVisibility(View.VISIBLE);
                curtainPowerLl.setVisibility(View.GONE);
                airConditionPowerLl.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        super.show();
    }

    @Override
    public void onSeeking(SeekParams seekParams) {
        if (type == 0) {
            SharedPreferenceUtils.putInt(mContext, "curtain", seekParams.progress);
        } else if (type == 1) {
            SharedPreferenceUtils.putInt(mContext, "airCondition", seekParams.progress);
        } else if (type == 2) {
            SharedPreferenceUtils.putInt(mContext, "icebox", seekParams.progress);
        }
    }

    @Override
    public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
