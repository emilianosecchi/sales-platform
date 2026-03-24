package com.esecchi.productinventory.service;

import com.esecchi.productinventory.model.Warehouse;
import com.esecchi.productinventory.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Boolean existsById(Long id) {
        return warehouseRepository.existsById(id);
    }

    public Warehouse getWarehouseReferenceById(Long id) {
        return warehouseRepository.getReferenceById(id);
    }

}