package com.sails.client_connect.mapper;

import com.sails.client_connect.dto.CustomerDTO;
import com.sails.client_connect.dto.CustomerUpdateDTO;
import com.sails.client_connect.dto.CustomersFinancingDTO;
import com.sails.client_connect.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    //mapping dto to entity
    @Mapping(source = "userId", target = "user.user_id")
    Customer toEntity(CustomerDTO customerDTO);

    //mapping entity to dto
    @Mapping(source = "user.user_id", target = "userId")
    CustomerDTO toDto(Customer customer);

    @Mapping(source = "user.user_id", target = "userId")
    CustomerUpdateDTO toUpdateDto(Customer customer);

    CustomersFinancingDTO toFinancingDto(Customer customer);
}
