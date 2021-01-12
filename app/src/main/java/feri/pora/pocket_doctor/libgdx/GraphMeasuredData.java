package feri.pora.pocket_doctor.libgdx;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import feri.pora.pocket_doctor.fragments.MeasureDataFragment;

public class GraphMeasuredData extends MeasureDataFragment implements AndroidFragmentApplication.Callbacks {

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void exit() {

    }
}
