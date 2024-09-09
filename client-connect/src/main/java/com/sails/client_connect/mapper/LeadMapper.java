package com.sails.client_connect.mapper;

import com.sails.client_connect.dto.LeadDTO;
import com.sails.client_connect.entity.Lead;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LeadMapper {

    //mapping entity to dto
    @Mapping(source = "user.user_id", target = "userId")
    LeadDTO toDto(Lead lead);

    //mapping dto to entity
    @Mapping(source = "userId", target = "user.user_id")
    Lead toEntity(LeadDTO leadDTO);
}
