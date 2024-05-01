package com.example.dacontrolagent.domain.repository.impl;

import com.example.dacontrolagent.domain.model.Delivery;
import com.example.dacontrolagent.domain.model.User;
import com.example.dacontrolagent.domain.repository.DeliveryRepository;
import com.example.dacontrolagent.domain.repository.UserRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemory implements UserRepository, DeliveryRepository {

    private static final List<Delivery> deliveries = Arrays.asList(
            new Delivery(
                    "test@test.com",
                    "PKG123456789",
                    "John Doe",
                    "Mr.",
                    "32 Rue du Colonel Moll, 93330 Neuilly-sur-Marne",
                    2.5347545164121548F,
                    48.859834124187934F,
                    0,
                    "+33760405709"),
            new Delivery(
                    "test@test.com",
                    "PKG124545",
                    "Hermann Doe",
                    "Mr.",
                    "6 Av Danielle Casanova",
                    123.456F,
                    45.67F,
                    0,
                    "+33760405709")
    );
    @Override
    public User findUserByEmail(String email) {
        return new User("test@test.com", "abc");
    }

    @Override
    public List<Delivery> findAllDeliveryByDeliverEmailAndCurrentDate(String email, LocalDate currentDate) {
        return deliveries.stream().filter(delivery -> delivery.getEmailOfDeliver().equalsIgnoreCase(email)).collect(Collectors.toList());
    }

    @Override
    public void updatePhotoOfDelivery(String packageNumber, byte[] photoOfDeliver) {
        Optional<Delivery> currentDelivery = deliveries.stream().filter(delivery -> delivery.getPackageNumber().equalsIgnoreCase(packageNumber)).findAny();
        currentDelivery.ifPresent(delivery -> delivery.setPhotoOfPackage(photoOfDeliver));
    }

    @Override
    public void updateDeliverStatus(String packageNumber) {
        Optional<Delivery> currentDelivery = deliveries.stream().filter(delivery -> delivery.getPackageNumber().equalsIgnoreCase(packageNumber)).findAny();
        currentDelivery.ifPresent(delivery -> delivery.setDelivered(true));
    }

}
