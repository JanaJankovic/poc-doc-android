package feri.pora.pocket_doctor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import feri.pora.pocket_doctor.R;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeItem> {
    String titles[] = new String[] {"Diagnosis", "Therapies", "Analysis",
            "Request analysis", "Pulse", "Doctors"};
    int images[] = new int[] {R.drawable.ic_medical_handbook_blue, R.drawable.ic_therapy_blue,
            R.drawable.ic_bar_chart_blue, R.drawable.ic_analytics_blue, R.drawable.ic_cardiology_blue,
            R.drawable.ic_stethoscope_doctor};

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private HomeAdapter.OnItemClickListener listener;

    public void setOnClickListener(HomeAdapter.OnItemClickListener listener) {
        this.listener =  listener;
    }

    public HomeAdapter() {
    }

    @NonNull
    @Override
    public HomeAdapter.HomeItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,
                parent, false);
        HomeAdapter.HomeItem viewHolder = new HomeAdapter.HomeItem(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.HomeItem holder, int position) {
        holder.bindItems(titles[position], images[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class HomeItem extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView imageView;

        public HomeItem(@NonNull final View itemView) {
            super(itemView);
            bindGUI(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }

        public void bindGUI(View v) {
            title = v.findViewById(R.id.textViewItemName);
            imageView = v.findViewById(R.id.imageViewItem);
        }

        public void bindItems(String title, int imageId) {
            this.title.setText(title);
            imageView.setImageResource(imageId);
        }
    }

}
