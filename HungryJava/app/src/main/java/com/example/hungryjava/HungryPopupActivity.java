package com.example.hungryjava;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.ViewTreeObserver;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.ViewAnimationUtils;
import com.google.android.material.button.MaterialButton;

public class HungryPopupActivity extends AppCompatActivity {

    private FloatingActionButton fabHungry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hungry_popup);

        // Apply fade-in transition for the window when entering
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());

        // Ensure circular reveal animation happens once the transition is ready
        supportPostponeEnterTransition(); // Postpone the enter transition until we're ready

        // Root view of the activity (e.g., a RelativeLayout, ConstraintLayout, etc.)
        final View rootView = findViewById(R.id.popupContainer); // Replace with the actual root layout ID

        fabHungry = findViewById(R.id.fabHungry);

        // Set up the FAB transition name for shared element transition
        fabHungry.setTransitionName("fab_transition");

        // Set up the circular reveal effect for the full activity window
        rootView.post(new Runnable() {
            @Override
            public void run() {
                // Get the center of the right-bottom corner of the screen
                int cx = rootView.getRight();
                int cy = rootView.getBottom();

                // The final radius is the diagonal distance from the center of the right-bottom corner to the opposite corner
                int finalRadius = (int) Math.hypot(rootView.getWidth(), rootView.getHeight());

                // Circular reveal animation for the whole activity window
                Animator anim = ViewAnimationUtils.createCircularReveal(rootView, cx, cy, 0f, finalRadius);
                anim.setDuration(600);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.start();

                rootView.setVisibility(View.VISIBLE); // Make sure the root view is visible
                supportStartPostponedEnterTransition(); // Start the postponed enter transition
            }
        });

        // Button to close the activity and trigger the exit transition
        fabHungry.setOnClickListener(v -> startExitAnimation());

        // Exit transition setup
        rootView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                rootView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    private void startExitAnimation() {
        final View rootView = findViewById(R.id.popupContainer);

        // Fade out the popup activity
        rootView.animate()
                .alpha(0f)
                .setDuration(100)
                .withEndAction(() -> {
                    setResult(RESULT_OK); // Notify the previous activity
                    finishAfterTransition(); // Finish after fade out
                })
                .start();
    }

    @Override
    public void onBackPressed() {
        startExitAnimation(); // Play exit animation before closing
    }
}
