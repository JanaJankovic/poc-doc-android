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

import feri.pora.datalib.Prediction;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.events.OnPendingCancel;
import feri.pora.pocket_doctor.events.OnPendingSend;
import feri.pora.pocket_doctor.events.OnPendingShow;

public class PendingAnalysisAdapter extends RecyclerView.Adapter <PendingAnalysisAdapter
        .PendingItem> {

   private ArrayList<Prediction> predictions;

    public PendingAnalysisAdapter(ArrayList<Prediction> predictions) {
        this.predictions = predictions;
    }

    @NonNull
    @Override
    public PendingAnalysisAdapter.PendingItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending,
                parent, false);
        PendingAnalysisAdapter.PendingItem viewHolder = new PendingAnalysisAdapter.PendingItem(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  PendingAnalysisAdapter.PendingItem holder, int position) {
        holder.bindItemsToData(predictions.get(position));
    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }

    public class PendingItem extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView subtitle;
        private Button buttonShowAnalysis;
        private Button buttonCancel;

        public PendingItem(@NonNull final View itemView) {
            super(itemView);
            bindGUI(itemView);
        }

        private void bindGUI(View v) {
            title = (TextView) v.findViewById(R.id.textViewCategory);
            subtitle = (TextView) v.findViewById(R.id.textViewPendingDate);
            buttonShowAnalysis = (Button) v.findViewById(R.id.buttonShowAnalysis);
            buttonCancel = (Button) v.findViewById(R.id.buttonCancelAnalysis);
        }

        public void bindItemsToData(Prediction prediction) {
            title.setText(prediction.getPrediction());
            subtitle.setText(prediction.getDate());
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnPendingCancel(prediction));
                }
            });
            buttonShowAnalysis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnPendingShow(prediction));
                }
            });
        }
    }
}
