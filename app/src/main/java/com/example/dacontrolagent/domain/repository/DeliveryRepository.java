package com.example.dacontrolagent.domain.repository;

import com.example.dacontrolagent.domain.model.Delivery;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryRepository {

    List<Delivery> findAllDeliveryByDeliverEmailAndCurrentDate(String email, LocalDate currentDate);

    void updatePhotoOfDelivery(String packageNumber, byte[] photoOfDeliver);

    void updateDeliverStatus(String packageNumber);
}
