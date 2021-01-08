package feri.pora.pocket_doctor.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import feri.pora.datalib.Device;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.events.OpenMeasureEvent;

public class RecycleViewBluetoothAdapter extends RecyclerView.Adapter <RecycleViewBluetoothAdapter
        .RecycleViewDevice> {
    private ArrayList<Device> devices;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private OnItemClickListener listener;

    public void setOnLongClickListener(OnItemClickListener listener) {
        this.listener =  listener;
    }

    public RecycleViewBluetoothAdapter(Context context, ArrayList<Device> devices) {
        this.context = context;
        this.devices = devices;
    }

    @NonNull
    @Override
    public RecycleViewDevice onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connectable_device,
                parent, false);
        RecycleViewDevice viewHolder = new RecycleViewDevice(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewDevice holder, int position) {
        holder.bindItemsToDevices(devices.get(position));
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class RecycleViewDevice extends RecyclerView.ViewHolder {
        private TextView titles;
        private TextView subtitles;
        private Button measureButton;

        public RecycleViewDevice(@NonNull final View itemView) {
            super(itemView);
            bindGUI(itemView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                    return true;
                }
            });
        }

        public void bindGUI(View v) {
            titles = v.findViewById(R.id.textView);
            subtitles = v.findViewById(R.id.textViewSubtitle);
            measureButton = v.findViewById(R.id.buttonMeasure);
        }

        public void bindItemsToDevices(Device device) {
            titles.setText(device.getName());
            subtitles.setText(device.getStatus());
            measureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OpenMeasureEvent());
                }
            });
        }
    }
}
