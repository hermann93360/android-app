package com.example.dacontrolagent.domain.service;

import com.example.dacontrolagent.domain.model.Delivery;
import com.example.dacontrolagent.domain.repository.DeliveryRepository;
import com.example.dacontrolagent.domain.usecase.DeliverPackageUseCase;
import com.example.dacontrolagent.domain.usecase.GetDeliveryRouteUseCase;

import java.time.LocalDate;
import java.util.List;

public class DeliveryService implements GetDeliveryRouteUseCase,
        DeliverPackageUseCase {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public List<Delivery> getDeliveryRoute(String email, LocalDate dateOfDeliveryTour) {
        return deliveryRepository.findAllDeliveryByDeliverEmailAndCurrentDate(email, dateOfDeliveryTour);
    }

    @Override
    public void deliverPackage(String packageNumber, byte[] photoOfPackage) {
        deliveryRepository.updatePhotoOfDelivery(packageNumber, photoOfPackage);
        deliveryRepository.updateDeliverStatus(packageNumber);
    }
}
