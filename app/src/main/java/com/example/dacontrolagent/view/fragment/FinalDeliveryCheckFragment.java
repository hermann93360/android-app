package com.example.dacontrolagent.view.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dacontrolagent.R;
import com.example.dacontrolagent.domain.model.Delivery;
import com.example.dacontrolagent.viewmodel.DeliveryViewModel;

public class FinalDeliveryCheckFragment extends Fragment {

    private Delivery delivery;
    private TextView appBarText;
    private ImageView photoOfPackage;
    private LinearLayout container;
    private Button submit;

    private DeliveryViewModel deliveryViewModel;

    public FinalDeliveryCheckFragment(Delivery delivery, TextView appBarText) {
        this.delivery = delivery;
        this.appBarText = appBarText;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_final_delivery_check, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        if(delivery.isDelivered()) {
            TextView packageDelivered = new TextView(getActivity());
            CustomizationView.of(packageDelivered)
                    .text(getString(R.string.delivered_package))
                    .font(getActivity(), R.font.poppins_light);
            submit.setText(R.string.update);

            CustomizationView.of(container)
                    .withChild(packageDelivered);
        } else {
            submit.setText(R.string.done);
            submit.setOnClickListener(v -> {
                deliveryViewModel.clickOnDeliverPackage(delivery);
                ListOfDeliveryFragment fragment = new ListOfDeliveryFragment(appBarText);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.itineraryActivity, fragment)
                        .commit();
            });
        }

        photoOfPackage = view.findViewById(R.id.photoOfPackage);
        photoOfPackage.setImageBitmap(BitmapFactory.decodeByteArray(delivery.getPhotoOfPackage(), 0, delivery.getPhotoOfPackage().length));
    }

    private void initViews(@NonNull View view) {
        deliveryViewModel = new ViewModelProvider(requireActivity()).get(DeliveryViewModel.class);
        container = view.findViewById(R.id.container);
        submit = view.findViewById(R.id.bottomBtn);
    }
}