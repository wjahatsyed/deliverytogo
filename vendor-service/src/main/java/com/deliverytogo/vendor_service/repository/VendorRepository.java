package com.deliverytogo.vendor_service.repository;

import com.deliverytogo.vendor_service.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    List<Vendor> findByCityAndActive(String city, boolean active);

    List<Vendor> findByCountryAndActive(String country, boolean active);
}
