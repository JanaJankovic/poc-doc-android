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

import feri.pora.datalib.Analysis;
import feri.pora.datalib.Diagnosis;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.events.OnAnalysisShow;

public class AnalysisAdapter extends RecyclerView.Adapter <AnalysisAdapter.AnalysisItem> {

    private ArrayList<Analysis> analyses;

    public AnalysisAdapter(ArrayList<Analysis> analyses) {
        this.analyses = analyses;
    }

    @NonNull
    @Override
    public AnalysisAdapter.AnalysisItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_analysis_list,
                parent, false);
        AnalysisAdapter.AnalysisItem viewHolder = new AnalysisAdapter.AnalysisItem(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  AnalysisAdapter.AnalysisItem holder, int position) {
        holder.bindItemsToData(analyses.get(position));
    }

    @Override
    public int getItemCount() {
        return analyses.size();
    }

    public class AnalysisItem extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView subtitle;
        private Button buttonShowAnalysis;

        public AnalysisItem(@NonNull final View itemView) {
            super(itemView);
            bindGUI(itemView);
        }

        private void bindGUI(View v) {
            title = (TextView) v.findViewById(R.id.textViewListAnalysis);
            subtitle = (TextView) v.findViewById(R.id.textViewDoctorConfirm);
            buttonShowAnalysis = (Button) v.findViewById(R.id.buttonShowConfirmedAnalysis);
        }

        public void bindItemsToData(Analysis analysis) {
            title.setText(analysis.getTitle());
            subtitle.setText(analysis.getDescription());
            buttonShowAnalysis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnAnalysisShow(analysis));
                }
            });
        }
    }

}
