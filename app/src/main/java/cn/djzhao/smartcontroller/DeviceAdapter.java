package cn.djzhao.smartcontroller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.djzhao.smartcontroller.entity.Device;

/**
 * 设备适配器
 *
 * @author djzhao
 * @date 20/04/12 21:18
 * @email djzhao627@gmail.com
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.NormalTextViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<Device> devices;

    public DeviceAdapter(Context context, List<Device> devices) {
        this.devices = devices;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_device, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        Device device = devices.get(position);
        holder.name.setText(device.getName());
        holder.value.setText(String.format(Locale.CHINA, "%s%s", device.getValue(), device.getUnit()));
    }

    @Override
    public int getItemCount() {
        return devices == null ? 0 : devices.size();
    }

    static class NormalTextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_device_name)
        TextView name;
        @BindView(R.id.item_device_value)
        TextView value;

        NormalTextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
