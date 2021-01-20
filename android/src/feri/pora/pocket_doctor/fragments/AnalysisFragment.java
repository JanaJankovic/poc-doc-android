package feri.pora.pocket_doctor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;


public class AnalysisFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private DemoCollectionAdapter demoCollectionAdapter;

    private String[] titles = new String[]{"Pending", "List"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_list_analysis, null);

        ((UserNavigationActivity) requireActivity()).getSupportActionBar().show();
        ((UserNavigationActivity) requireActivity()).getSupportActionBar()
                .setTitle("Analysis");
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((UserNavigationActivity) requireActivity()).navigationView
                .setCheckedItem(R.id.nav_list_analysis);


        return  rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        demoCollectionAdapter = new DemoCollectionAdapter(this);
        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(demoCollectionAdapter);
        tabLayout = view.findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if(position == 0) {
                tab.setText("Pending");
            } else {
                tab.setText("List");
            }
        }).attach();
    }


    public class DemoCollectionAdapter extends FragmentStateAdapter {
        public DemoCollectionAdapter(Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if(position == 0)
                return new PendingAnalysisFragment();
            else
                return new ListAnalysis();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}