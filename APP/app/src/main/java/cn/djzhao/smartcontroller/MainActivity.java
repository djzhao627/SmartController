package cn.djzhao.smartcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.receiver.listener.BluetoothBondListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.djzhao.smartcontroller.dialog.BottomPanel;
import cn.djzhao.smartcontroller.entity.Device;
import cn.djzhao.smartcontroller.utils.SharedPreferenceUtils;
import cn.djzhao.smartcontroller.view.NumberAnimTextView;
import okhttp3.Call;
import okhttp3.Response;

import static cn.djzhao.smartcontroller.utils.Constants.BASE_URL;
import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

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
    @BindView(R.id.devices_rv)
    RecyclerView devicesLv;
    @BindView(R.id.env_sound)
    TextView envSound;
    @BindView(R.id.env_light)
    TextView envLight;
    @BindView(R.id.env_temperature)
    NumberAnimTextView envTemperature;
    @BindView(R.id.env_water)
    TextView envWater;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private Context mContext;

    // TV, router, light
    private boolean[] powers = {false, false, false};
    // airCondition, icebox, curtain
    private int[] status = {28, 70, 80};

    private BottomPanel dialog;

    private BluetoothClient mClient;

    private final BluetoothBondListener mBluetoothBondListener = new BluetoothBondListener() {
        @Override
        public void onBondStateChanged(String mac, int bondState) {
            // bondState = Constants.BOND_NONE, BOND_BONDING, BOND_BONDED
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        echo();
        initEvent();
        getDataFromServer();
        asynchronousUpdate();
    }

    /**
     * 异步更新温度信息
     */
    private void asynchronousUpdate() {
        Timer timer = new Timer();
        Task task = new Task();
        timer.schedule(task, 2000, 12000);
    }

    /**
     * 事件初始化
     */
    private void initEvent() {
        initBluetooth();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDataFromServer();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        /*refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
            }
        });*/
    }

    private void initBluetooth() {
        List<BluetoothDevice> connectedBluetoothLeDevices = BluetoothUtils.getConnectedBluetoothLeDevices();
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();
        mClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                Beacon beacon = new Beacon(device.scanRecord);
                BluetoothLog.v(String.format("beacon for %s\n%s", device.getAddress(), beacon.toString()));
            }

            @Override
            public void onSearchStopped() {

            }

            @Override
            public void onSearchCanceled() {

            }
        });
        mClient.registerBluetoothBondListener(mBluetoothBondListener);
    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        OkGo
                .get(BASE_URL + "getDevices")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Type collectionType = new TypeToken<List<Device>>() {
                        }.getType();
                        ArrayList<Device> devices = new Gson().fromJson(s, collectionType);
                        devicesLv.setAdapter(new DeviceAdapter(mContext, devices));
                    }
                });
        OkGo
                .post(BASE_URL + "getEnv")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        String[] split = s.split("\\|");
                        envSound.setText(split[0]);
                        envLight.setText(split[1]);
                        if (SharedPreferenceUtils.getBoolean(mContext, "air", false)) {
                            envTemperature.setNumberString(envTemperature.getText().toString(), split[2]);
                        }
                        envWater.setText(split[3]);
                    }
                });
    }

    /**
     * 数据回显
     */
    private void echo() {
        powers[0] = SharedPreferenceUtils.getBoolean(mContext, "tv", false);
        powers[1] = SharedPreferenceUtils.getBoolean(mContext, "air", false);
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

        String currentTemp = envTemperature.getText().toString();
        if (!currentTemp.equals(String.valueOf(status[0]))) {
            sendDataToServer(status[0]);
            sendDataToSinglechip(status[0] > Integer.parseInt(currentTemp) ? "1" : "0");
        }
        if (SharedPreferenceUtils.getBoolean(mContext, "air", false)) {
            envTemperature.setNumberString(currentTemp, String.valueOf(status[0]));
        }

        airConditionStatusTv.setText(String.format("%d℃", status[0]));
        iceboxStatusTv.setText(String.format("%d%%", status[1]));
        curtainStatusTv.setText(String.format("%d%%", status[2]));
    }

    /**
     * 将数据使用蓝牙发送给单片机
     *
     * @param status 1 温度升高 2 温度降低
     */
    private void sendDataToSinglechip(String status) {
        if (!mClient.isBluetoothOpened()) {
            Toast.makeText(mContext, "蓝牙未开启，无法发送数据", Toast.LENGTH_SHORT).show();
            mClient.openBluetooth();
            return;
        }
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice bluetoothDevice : devices)
        {
            ParcelUuid[] uuids = bluetoothDevice.getUuids();
            mClient.write(bluetoothDevice.getAddress(), uuids[0].getUuid(), uuids[1].getUuid(), status.getBytes(), new BleWriteResponse() {
                @Override
                public void onResponse(int code) {
                    if (code == REQUEST_SUCCESS) {

                    } else {
                        Log.d("response", String.valueOf(code));
                    }
                }
            });

        }
    }

    /**
     * 温度数据传输至后台
     */
    private void sendDataToServer(int temp) {
        OkGo
                .get(BASE_URL + "updateTemperature?temp=" + temp)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                    }
                });
    }

    private void initView() {
        mClient = new BluetoothClient(mContext);
        dialog = new BottomPanel(mContext);
        dialog.setOnDismissListener(dialog -> echo());
        devicesLv.setLayoutManager(new LinearLayoutManager(this));
        envTemperature.setDuration(8000);
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
                SharedPreferenceUtils.putBoolean(mContext, "air", powers[1]);
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

    /**
     * 任务
     */
    class Task extends TimerTask {
        @Override
        public void run() {
            if (SharedPreferenceUtils.getBoolean(mContext, "air", false)) {
                OkGo
                        .get(BASE_URL + "getTemperature")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                if (!TextUtils.isEmpty(s)) {
                                    envTemperature.setNumberString(envTemperature.getText().toString(), s);
                                }
                            }
                        });
            }
        }
    }
}
