package feri.pora.pocket_doctor.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import feri.pora.datalib.Doctor;
import feri.pora.datalib.Prediction;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.adapters.DoctorAdapter;
import feri.pora.pocket_doctor.adapters.PendingAnalysisAdapter;
import feri.pora.pocket_doctor.events.OnOpenChat;
import feri.pora.pocket_doctor.events.OnPendingCancel;
import feri.pora.pocket_doctor.events.OnPendingSend;
import feri.pora.pocket_doctor.events.OnPendingShow;
import rx.subscriptions.CompositeSubscription;

public class PendingAnalysisFragment extends Fragment {
    private CompositeSubscription subscription;

    private RecyclerView recyclerView;
    private PendingAnalysisAdapter adapter;

    private ArrayList<Prediction> predictions;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_pending_analysis, container, false);

        predictions = new ArrayList<>();
        subscription = new CompositeSubscription();
        bindGUI(rootView);
        //getPredictions -> from mongo

        return rootView;
    }

    public void bindGUI(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleViewPendingAnalysis);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnPendingCancel event) {
        //delete from db
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnPendingShow event) {
        //open fragment that shows
    }
}