package com.example.assistenciatecnica.mappers;

import com.example.assistenciatecnica.dto.TecnicoDTO;
import com.example.assistenciatecnica.entity.Tecnico;
import org.springframework.stereotype.Component;

@Component
public class TecnicoMapper {
    public TecnicoDTO toDto(Tecnico entity) {
        if (entity == null) {
            return null;
        }

        TecnicoDTO dto = new TecnicoDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCpf(entity.getCpf());
        dto.setEmail(entity.getEmail());
        dto.setSenha(entity.getSenha());
        dto.setPerfis(entity.getPerfis());
        dto.setDataCriacao(entity.getDataCriacao());

        return dto;
    }

    public Tecnico toEntity(TecnicoDTO dto) {
        if (dto == null) {
            return null;
        }

        Tecnico entity = new Tecnico();
        updateEntityFromDto(dto, entity);

        return entity;
    }

    public void updateEntityFromDto(TecnicoDTO dto, Tecnico entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());
        entity.setEmail(dto.getEmail());
        entity.setSenha(dto.getSenha());

        if (dto.getPerfis() != null) {
            entity.getPerfis().clear();
            entity.getPerfis().addAll(dto.getPerfis());
        }
    }
}