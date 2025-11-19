package com.example.assistenciatecnica.mappers;

import com.example.assistenciatecnica.dto.ClienteDTO;
import com.example.assistenciatecnica.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {
    public ClienteDTO toDto(Cliente entity) {
        if (entity == null) {
            return null;
        }

        ClienteDTO dto = new ClienteDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCpf(entity.getCpf());
        dto.setEmail(entity.getEmail());
        dto.setSenha(entity.getSenha());
        dto.setPerfis(entity.getPerfis());
        dto.setDataCriacao(entity.getDataCriacao());

        return dto;
    }

    public Cliente toEntity(ClienteDTO dto) {
        if (dto == null) {
            return null;
        }

        Cliente entity = new Cliente();
        updateEntityFromDto(dto, entity);

        return entity;
    }

    public void updateEntityFromDto(ClienteDTO dto, Cliente entity) {
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