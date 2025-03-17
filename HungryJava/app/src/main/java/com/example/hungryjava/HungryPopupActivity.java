package com.example.hungryjava;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import androidx.annotation.Nullable;
import android.transition.Fade;
import android.transition.Transition;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HungryPopupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hungry_popup);

        // Fade-in animation for the window
        //getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());

        // Set up the transition
        supportPostponeEnterTransition(); // This postpones the transition so we can wait for the view to be fully laid out.

        // Get the FAB element from the layout
        FloatingActionButton fabHungry = findViewById(R.id.fabHungry);

        // Define the shared element transition from the FAB (as previously defined in the first activity)
        fabHungry.setTransitionName("fab_transition");

        // Delay the circular reveal until the transition is ready
        fabHungry.post(new Runnable() {
            @Override
            public void run() {
                // Get the center of the FAB for the reveal effect
                int cx = fabHungry.getRight();
                int cy = fabHungry.getBottom();

                // Calculate the final radius of the reveal animation
                int finalRadius = (int) Math.hypot(fabHungry.getWidth(), fabHungry.getHeight());

                // Create the circular reveal animation
                Animator anim = ViewAnimationUtils.createCircularReveal(fabHungry, cx, cy, 0f, finalRadius);
                anim.setDuration(600); // Set the duration of the animation
                anim.setInterpolator(new DecelerateInterpolator()); // Smooth interpolation
                anim.start(); // Start the animation

                // Optionally, set the view visible after the animation starts if it's initially hidden
                fabHungry.setVisibility(View.VISIBLE);

                // Allow the shared element transition to occur after this
                supportStartPostponedEnterTransition();
            }
        });

        // Button to close the activity
        MaterialButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> finish());
    }
}
