package com.example.hungryjava;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class HungryPopupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hungry_popup);

        MaterialButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> finish());

        animatePopup();
    }

    private void animatePopup() {
        // Find the popup container view
        final View view = findViewById(R.id.popupContainer);

        // Ensure the view is attached to the window
        view.post(new Runnable() {
            @Override
            public void run() {
                // Get the center coordinates for the circular reveal (bottom-right corner)
                int cx = view.getRight();  // X-coordinate of the bottom-right corner
                int cy = view.getBottom(); // Y-coordinate of the bottom-right corner

                // Calculate the final radius, which is the distance from the center to the farthest corner of the screen
                int finalRadius = (int) Math.hypot(view.getWidth(), view.getHeight());

                // Create the circular reveal animation
                Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius);
                anim.setDuration(600);  // Set the duration of the animation
                anim.setInterpolator(new DecelerateInterpolator());  // Set the interpolator for smooth animation
                anim.start();  // Start the animation

                // Optionally, set the view visible after the animation starts if it's initially hidden
                view.setVisibility(View.VISIBLE);
            }
        });
    }
}