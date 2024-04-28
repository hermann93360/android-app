package com.example.dacontrolagent.domain.usecase;

import com.example.dacontrolagent.domain.model.Delivery;

import java.time.LocalDate;
import java.util.List;

public interface GetDeliveryRouteUseCase {
    List<Delivery> getDeliveryRoute(String email, LocalDate dateOfDeliveryTour);
}
