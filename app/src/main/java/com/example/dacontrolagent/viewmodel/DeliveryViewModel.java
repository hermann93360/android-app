package com.example.dacontrolagent.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dacontrolagent.domain.model.Delivery;
import com.example.dacontrolagent.domain.repository.impl.RestDeliveryRepository;
import com.example.dacontrolagent.domain.service.DeliveryService;
import com.example.dacontrolagent.domain.usecase.DeliverPackageUseCase;
import com.example.dacontrolagent.domain.usecase.GetDeliveryRouteUseCase;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DeliveryViewModel extends ViewModel {

    private final GetDeliveryRouteUseCase getDeliveryRouteUseCase;

    private final DeliverPackageUseCase deliverPackageUseCase;

    private final MutableLiveData<List<Delivery>> liveDeliveries = new MutableLiveData<>();

    public DeliveryViewModel() {
        this.getDeliveryRouteUseCase = new DeliveryService(new RestDeliveryRepository());
        this.deliverPackageUseCase = new DeliveryService(new RestDeliveryRepository());
    }

    public void getDeliveryRoute(String email, LocalDate dateOfDelivery) {
        new Thread(() -> {
            liveDeliveries.postValue(getDeliveryRouteUseCase.getDeliveryRoute(email, dateOfDelivery));
        }).start();
    }

    public MutableLiveData<List<Delivery>> getLiveDeliveries() {
        return liveDeliveries;
    }

    public void clickOnDeliverPackage(Delivery delivery) {
        new Thread(() -> {
            deliverPackageUseCase.deliverPackage(delivery.getPackageNumber(), delivery.getPhotoOfPackage());
        }).start();

        Optional<Delivery> currentDelivery = liveDeliveries.getValue().stream()
                .filter(dlv -> dlv.getPackageNumber().equalsIgnoreCase(delivery.getPackageNumber())).findAny();

        if(currentDelivery.isPresent()) {
            currentDelivery.get().setDelivered(true);
            currentDelivery.get().setPhotoOfPackage(delivery.getPhotoOfPackage());
        }

        liveDeliveries.postValue(liveDeliveries.getValue());
    }
}
