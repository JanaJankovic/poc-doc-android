package feri.pora.pocket_doctor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import feri.pora.datalib.Diagnosis;
import feri.pora.datalib.Therapy;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;

public class TherapyAdapter extends RecyclerView.Adapter<TherapyAdapter.TherapyItem> {
    private ArrayList<Therapy> therapies;

    public TherapyAdapter(ArrayList<Therapy> therapies) {
        this.therapies = therapies;
    }

    @NonNull
    @Override
    public TherapyAdapter.TherapyItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_therapy,
                parent, false);
        TherapyAdapter.TherapyItem viewHolder = new TherapyAdapter.TherapyItem(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TherapyAdapter.TherapyItem holder, int position) {
        holder.bindItems(therapies.get(position));
    }

    @Override
    public int getItemCount() {
        return therapies.size();
    }

    public class TherapyItem extends RecyclerView.ViewHolder {
        private TextView diagnosisName;
        private TextView therapyName;
        private TextView therapyTime;
        private TextView therapyDescription;

        public TherapyItem(@NonNull final View itemView) {
            super(itemView);
            bindGUI(itemView);

        }

        public void bindGUI(View v) {
            therapyName = v.findViewById(R.id.textViewTherapyDiagnosis);
            diagnosisName = v.findViewById(R.id.textViewTherapyName);
            therapyTime = v.findViewById(R.id.textViewTimeTherapy);
            therapyDescription = v.findViewById(R.id.textViewTherapyDescription);
        }

        public void bindItems(Therapy therapy) {
            therapyName.setText(therapy.getName());
            diagnosisName.setText(therapy.getDiagnosis(ApplicationState.loadLoggedUser()));
            String time = therapy.getStart() + " - " + therapy.getEnd();
            therapyTime.setText(time);
            therapyDescription.setText(therapy.getDescription());
        }
    }


}
