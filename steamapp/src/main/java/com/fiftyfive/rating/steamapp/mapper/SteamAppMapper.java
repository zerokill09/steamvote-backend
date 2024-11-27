package com.fiftyfive.rating.steamapp.mapper;

import com.fiftyfive.rating.steamapp.domain.SteamAppEntity;
import com.fiftyfive.rating.steamapp.dto.SteamAppDto;
import com.fiftyfive.rating.steamapp.dto.ListDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SteamAppMapper {
    SteamAppDto toDto(SteamAppEntity entity);
    SteamAppEntity toEntity(SteamAppDto dto);

    List<SteamAppDto> toDtoList(List<SteamAppEntity> entityList);
    List<SteamAppEntity> toEntityList(List<SteamAppDto> dtoList);
    ListDto<SteamAppDto> toDtoPage(Page<SteamAppEntity> entityPage);
}
