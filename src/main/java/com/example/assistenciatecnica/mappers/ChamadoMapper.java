package com.example.assistenciatecnica.mappers;

import com.example.assistenciatecnica.dto.ChamadoDTO;
import com.example.assistenciatecnica.dto.IdNomeDTO;
import com.example.assistenciatecnica.entity.Chamado;
import com.example.assistenciatecnica.entity.Cliente;
import com.example.assistenciatecnica.entity.Tecnico;
import com.example.assistenciatecnica.enums.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ChamadoMapper {
    public ChamadoDTO toDto(Chamado entity) {
        if (entity == null) {
            return null;
        }

        ChamadoDTO dto = new ChamadoDTO();
        dto.setId(entity.getId());
        dto.setDataAbertura(entity.getDataAbertura());
        dto.setDataFechamento(entity.getDataFechamento());
        dto.setPrioridade(entity.getPrioridade());
        dto.setStatus(entity.getStatus());
        dto.setTitulo(entity.getTitulo());
        dto.setObservacoes(entity.getObservacoes());

        if (entity.getCliente() != null) {
            dto.setCliente(new IdNomeDTO(
                    entity.getCliente().getId(),
                    entity.getCliente().getNome()
            ));
        }

        if (entity.getTecnico() != null) {
            dto.setTecnico(new IdNomeDTO(
                    entity.getTecnico().getId(),
                    entity.getTecnico().getNome()
            ));
        }

        return dto;
    }

    public Chamado toEntity(ChamadoDTO dto, Cliente cliente, Tecnico tecnico) {
        if (dto == null) {
            return null;
        }

        Chamado entity = new Chamado();
        updateEntityFromDto(dto, entity, cliente, tecnico);

        return entity;
    }

    public void updateEntityFromDto(ChamadoDTO dto, Chamado entity, Cliente cliente, Tecnico tecnico) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setPrioridade(dto.getPrioridade());
        entity.setStatus(dto.getStatus());
        entity.setTitulo(dto.getTitulo());
        entity.setObservacoes(dto.getObservacoes());

        if (dto.getDataFechamento() != null) {
            entity.setDataFechamento(dto.getDataFechamento());
        } else if (dto.getStatus() == Status.ENCERRADO && entity.getDataFechamento() == null) {
            entity.setDataFechamento(LocalDateTime.now());
        }

        if (cliente != null) {
            entity.setCliente(cliente);
        }
        if (tecnico != null) {
            entity.setTecnico(tecnico);
        }
    }
}