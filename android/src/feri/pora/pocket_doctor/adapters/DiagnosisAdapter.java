package feri.pora.pocket_doctor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import feri.pora.datalib.Diagnosis;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;

public class DiagnosisAdapter extends RecyclerView.Adapter<DiagnosisAdapter.DiagnosisItem> {
    private ArrayList<Diagnosis> diagnoses;

    public DiagnosisAdapter(ArrayList<Diagnosis> diagnoses) {
        this.diagnoses = diagnoses;
    }

    @NonNull
    @Override
    public DiagnosisAdapter.DiagnosisItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diagnosis,
                parent, false);
        DiagnosisAdapter.DiagnosisItem viewHolder = new DiagnosisAdapter.DiagnosisItem(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiagnosisAdapter.DiagnosisItem holder, int position) {
        holder.bindItems(diagnoses.get(position));
    }

    @Override
    public int getItemCount() {
        return diagnoses.size();
    }

    public class DiagnosisItem extends RecyclerView.ViewHolder {
        private TextView doctorFullName;
        private TextView diagnosisName;
        private TextView diagnosisDescription;

        public DiagnosisItem(@NonNull final View itemView) {
            super(itemView);
            bindGUI(itemView);

        }

        public void bindGUI(View v) {
            doctorFullName = v.findViewById(R.id.textViewDoctorDiagnosis);
            diagnosisName = v.findViewById(R.id.textViewDiagnosis);
            diagnosisDescription = v.findViewById(R.id.textViewDescription);
        }

        public void bindItems(Diagnosis diagnosis) {
            doctorFullName.setText(diagnosis.getDoctorFullname(ApplicationState.loadLoggedUser()));
            String name = diagnosis.getName() + " " + diagnosis.getTimestamp();
            diagnosisName.setText(name);
            diagnosisDescription.setText(diagnosis.getDescription());
        }
    }

}
