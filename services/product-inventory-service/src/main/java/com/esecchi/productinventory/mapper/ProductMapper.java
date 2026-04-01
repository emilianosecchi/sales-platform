package com.esecchi.productinventory.mapper;

import com.esecchi.common.event.productinventory.ProductCreatedEvent;
import com.esecchi.productinventory.model.Product;
import com.esecchi.productinventory.request.CreateProductRequest;
import com.esecchi.productinventory.response.ProductResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDate.now())")
    Product toEntity(CreateProductRequest request);

    ProductResponseDTO toResponse(Product product);

    @Mapping(source = "id", target = "productId")
    ProductCreatedEvent toEvent(Product product);
}
