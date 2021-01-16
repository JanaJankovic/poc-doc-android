package feri.pora.pocket_doctor.adapters;

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
import feri.pora.datalib.Doctor;
import feri.pora.datalib.User;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.events.OnListChanged;
import feri.pora.pocket_doctor.events.OnOpenChat;

public class DoctorAdapter extends RecyclerView.Adapter <DoctorAdapter
        .DoctorItem> {
    private ArrayList<Doctor> doctors;

    public DoctorAdapter(Context context, ArrayList<Doctor> doctors) {
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public DoctorAdapter.DoctorItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor,
                parent, false);
        DoctorAdapter.DoctorItem viewHolder = new DoctorAdapter.DoctorItem(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.DoctorItem holder, int position) {
        holder.bindItemsToDoctors(doctors.get(position));
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class DoctorItem extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView subtitle;
        private Button buttonCall;
        private Button buttonChat;
        private Button buttonAdd;
        private Button buttonRemove;

        public DoctorItem(@NonNull final View itemView) {
            super(itemView);
            bindGUI(itemView);
        }

        private void bindGUI(View v) {
            title = (TextView) v.findViewById(R.id.textViewTitleDoctor);
            subtitle = (TextView) v.findViewById(R.id.textViewSubtitleDoctor);
            buttonCall = (Button) v.findViewById(R.id.buttonCall);
            buttonChat = (Button) v.findViewById(R.id.buttonOpenChat);
            buttonAdd = (Button) v.findViewById(R.id.buttonMore);
            buttonRemove = (Button) v.findViewById(R.id.buttonRemoveDoctor);
        }

        public void bindItemsToDoctors(Doctor doctor) {
            String[] fullName = doctor.getFullName().split(" ");
            String titleText = "DR. " + fullName[fullName.length - 1];
            title.setText(titleText);
            subtitle.setText(doctor.getPhone());
            User user = ApplicationState.loadLoggedUser();
            if(user.hasDoctor(doctor)) {
                buttonAdd.setVisibility(View.INVISIBLE);
                buttonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.removeDoctorFromList(doctor);
                        ApplicationState.saveLoggedUser(user);
                        EventBus.getDefault().post(new OnListChanged());
                    }
                });
            } else {
                buttonRemove.setVisibility(View.INVISIBLE);
                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.addDoctorToList(doctor);
                        ApplicationState.saveLoggedUser(user);
                        EventBus.getDefault().post(new OnListChanged());
                    }
                });
            }
            buttonChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnOpenChat(doctor));
                }
            });

            //button call ne bom implementiral, ker so telefonske stevilke dejanske stevilke zdravnikov
        }
    }

}
