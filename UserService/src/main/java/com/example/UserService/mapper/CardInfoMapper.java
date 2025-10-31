package com.example.UserService.mapper;

import com.example.UserService.model.dto.CardInfoRequest;
import com.example.UserService.model.dto.CardInfoResponse;
import com.example.UserService.model.entity.CardInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardInfoMapper {

    @Mapping(source = "user.id", target = "userId")
    CardInfoResponse toDto(CardInfo entity);

    @Mapping(target = "user", ignore = true)
    CardInfo toEntity(CardInfoRequest dto);

    List<CardInfoResponse> toDtos(List<CardInfo> entities);
}