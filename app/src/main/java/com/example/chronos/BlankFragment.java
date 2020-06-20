package com.example.chronos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BlankFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container,
                false);

        final Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.scale_alpha_anim);
        final Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.scale_alpha_anim);
        final Animation animation3 = AnimationUtils.loadAnimation(getContext(), R.anim.scale_alpha_anim);
        final Animation animation4 = AnimationUtils.loadAnimation(getContext(), R.anim.scale_alpha_anim);

        final FrameLayout scanner = view.findViewById(R.id.layout_scanner);
        final ImageView circle1=scanner.findViewById(R.id.circle1);
        final ImageView circle2=scanner.findViewById(R.id.circle2);
        final ImageView circle3=scanner.findViewById(R.id.circle3);
        final ImageView circle4=scanner.findViewById(R.id.circle4);

        scanner.findViewById(R.id.start_can).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle1.startAnimation(animation1);

                animation2.setStartOffset(600);
                circle2.startAnimation(animation2);

                animation3.setStartOffset(1200);
                circle3.startAnimation(animation3);

                animation4.setStartOffset(1800);
                circle4.startAnimation(animation4);
            }
        });

        return view;
    }
}
