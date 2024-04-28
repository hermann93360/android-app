package com.example.dacontrolagent.domain.usecase;

public interface DeliverPackageUseCase {
    void deliverPackage(String packageNumber, byte[] photoOfPackage);
}
