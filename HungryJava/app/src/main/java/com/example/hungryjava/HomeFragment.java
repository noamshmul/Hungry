package com.example.hungryjava;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton btnHungry = view.findViewById(R.id.fabHungry);
        btnHungry.setOnClickListener(v -> {
            // Start the second activity with the shared element transition
            Intent intent = new Intent(getActivity(), HungryPopupActivity.class);
            // Add a shared element transition for the FAB
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    getActivity(),
                    btnHungry, // View in the first activity
                    "fab_transition" // Transition name (used in the second activity)
            );
            startActivity(intent, options.toBundle());
        });

        return view;
    }
}