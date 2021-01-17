package feri.pora.pocket_doctor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import feri.pora.datalib.Doctor;
import feri.pora.datalib.MeasureData;
import feri.pora.datalib.User;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.events.OnListChanged;
import feri.pora.pocket_doctor.events.OnMeasurementSend;
import feri.pora.pocket_doctor.events.OnOpenChat;
import feri.pora.pocket_doctor.events.OnSendMeasurementCancel;

public class SendDoctorAdapter extends RecyclerView.Adapter <SendDoctorAdapter
        .DoctorItem> {
    private ArrayList<Doctor> doctors;
    private MeasureData measureData;

    public SendDoctorAdapter(ArrayList<Doctor> doctors, MeasureData measureData) {
        this.doctors = doctors;
        this.measureData = measureData;
    }

    @NonNull
    @Override
    public SendDoctorAdapter.DoctorItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send_doctor,
                parent, false);
        SendDoctorAdapter.DoctorItem viewHolder = new SendDoctorAdapter.DoctorItem(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SendDoctorAdapter.DoctorItem holder, int position) {
        holder.bindItemsToDoctors(doctors.get(position));
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class DoctorItem extends RecyclerView.ViewHolder {
        private TextView title;
        private Button buttonSend;
        private Button buttonCancel;

        public DoctorItem(@NonNull final View itemView) {
            super(itemView);
            bindGUI(itemView);
        }

        private void bindGUI(View v) {
            title = (TextView) v.findViewById(R.id.textViewDoctorName);
            buttonSend = (Button) v.findViewById(R.id.buttonSendMeasure);
            buttonCancel = (Button) v.findViewById(R.id.buttonCancelMeasure);
        }

        public void bindItemsToDoctors(Doctor doctor) {
            String[] fullName = doctor.getFullName().split(" ");
            String titleText = "DR. " + fullName[fullName.length - 1];
            title.setText(titleText);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnSendMeasurementCancel());
                }
            });
            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnMeasurementSend(measureData.getBeatsPerMinute(),
                            measureData.getSpo2(), ApplicationState.loadLoggedUser().getPrivateKey(),
                            doctor.getPublicKey()));
                }
            });
        }
    }
}
