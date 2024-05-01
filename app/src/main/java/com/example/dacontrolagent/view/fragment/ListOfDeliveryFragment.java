package com.example.dacontrolagent.view.fragment;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.FOCUS_RIGHT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

import static com.example.dacontrolagent.view.activity.ItineraryActivity.addChildInLayout;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dacontrolagent.R;
import com.example.dacontrolagent.domain.model.Delivery;
import com.example.dacontrolagent.view.activity.ItineraryActivity;
import com.example.dacontrolagent.viewmodel.DeliveryViewModel;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.time.LocalDate;
import java.util.List;

public class ListOfDeliveryFragment extends Fragment {

    private List<Delivery> deliveries;
    private DeliveryViewModel deliveryViewModel;
    private LinearLayout listOfDelivery;
    private TextView remainingPackagesText;
    private TextView deliveredPackagesText;
    private long numberOfPackagesRemaining;
    private long numberOfPackagesDelivered;
    private TextView appBarText;


    public ListOfDeliveryFragment(List<Delivery> deliveries, TextView appBarText) {
        this.deliveries = deliveries;
        this.appBarText = appBarText;
    }

    public ListOfDeliveryFragment(TextView appBarText) {
        this.appBarText = appBarText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_of_delivery, container, false);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        deliveryViewModel = new ViewModelProvider(requireActivity()).get(DeliveryViewModel.class);

        deliveryViewModel.getLiveDeliveries().observe(getActivity(), liveDeliveries -> {
            deliveries = liveDeliveries;
        });

        PieChart pieChart = view.findViewById(R.id.pieChart);

        listOfDelivery = view.findViewById(R.id.listOfDelivery);

        remainingPackagesText = view.findViewById(R.id.remainingPackages);
        deliveredPackagesText = view.findViewById(R.id.deliveredPackages);

        numberOfPackagesRemaining = deliveries.stream()
                .filter(delivery -> !delivery.isDelivered())
                .count();

        numberOfPackagesDelivered = deliveries.stream()
                .filter(Delivery::isDelivered)
                .count();

        String remainingText = numberOfPackagesRemaining + " " + getString(R.string.packages_remaining);
        String deliveryText = numberOfPackagesDelivered + " " + getString(R.string.packages_delivered);

        remainingPackagesText.setText(remainingText);
        deliveredPackagesText.setText(deliveryText);

        pieChart.addPieSlice(new PieModel("Remaining packages", numberOfPackagesRemaining, Color.parseColor("#242424")));
        pieChart.addPieSlice(new PieModel("Delivered packages", numberOfPackagesDelivered, Color.parseColor("#66a828")));
        pieChart.startAnimation();

        for (Delivery delivery : deliveries) {
            RelativeLayout deliveryContainer = new RelativeLayout(getActivity());
            CustomizationView.of(deliveryContainer)
                    .width(MATCH_PARENT)
                    .height(WRAP_CONTENT)
                    .margins(0, 0, 0, 25)
                    .background(R.drawable.border_style_delivery);

            LinearLayout deliveryData = new LinearLayout(getActivity());
            CustomizationView.of(deliveryData)
                    .width(WRAP_CONTENT)
                    .height(WRAP_CONTENT)
                    .orientation(VERTICAL);

            TextView nameTextView = new TextView(getActivity());
            TextView packageNumberTextView = new TextView(getActivity());
            TextView addressTextView = new TextView(getActivity());

            setStyleOfTextViewDelivery(nameTextView, packageNumberTextView, addressTextView);

            nameTextView.setText(delivery.getName());
            addressTextView.setText(delivery.getAddress());
            packageNumberTextView.setText(delivery.getPackageNumber());

            //refactor this
            ImageView deliveryStatus = new ImageView(getActivity());
            RelativeLayout.LayoutParams deliveryStatusLayoutParams = new RelativeLayout.LayoutParams(100, 90);
            deliveryStatusLayoutParams.addRule(ALIGN_PARENT_RIGHT);
            deliveryStatus.setLayoutParams(deliveryStatusLayoutParams);
            deliveryStatus.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.deliveryContainer));

            if(delivery.isDelivered()) {
                deliveryStatus.setImageResource(R.drawable.delivered);
            } else {
                deliveryStatus.setImageResource(R.drawable.remaining);
            }

            addChildInLayout(deliveryData, nameTextView, addressTextView, packageNumberTextView);
            addChildInLayout(deliveryContainer, deliveryData, deliveryStatus);

            listOfDelivery.addView(deliveryContainer);


            deliveryContainer.setOnClickListener(v -> {
                if(!delivery.isDelivered()) {
                    DeliveryDetailFragment fragment = new DeliveryDetailFragment(delivery, appBarText);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                } else {
                    FinalDeliveryCheckFragment fragment = new FinalDeliveryCheckFragment(delivery, appBarText);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }

            });

        }


    }


    private void setStyleOfTextViewDelivery(TextView ...textViews) {
        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.poppins_extrabold);
        for (TextView textView : textViews) {
            textView.setTypeface(typeface);
        }
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }
}