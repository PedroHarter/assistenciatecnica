package com.example.assistenciatecnica.services;

import com.example.assistenciatecnica.dto.ChamadoDTO;
import com.example.assistenciatecnica.entity.Chamado;
import com.example.assistenciatecnica.entity.Cliente;
import com.example.assistenciatecnica.entity.Tecnico;
import com.example.assistenciatecnica.enums.Status;
import com.example.assistenciatecnica.exceptions.DatabaseException;
import com.example.assistenciatecnica.mappers.ChamadoMapper;
import com.example.assistenciatecnica.repositories.ChamadoRepository;
import com.example.assistenciatecnica.repositories.ClienteRepository;
import com.example.assistenciatecnica.repositories.TecnicoRepository;
import com.example.assistenciatecnica.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private ChamadoMapper mapper;

    @Transactional(readOnly = true)
    public List<ChamadoDTO> findAll() {
        return chamadoRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChamadoDTO findById(Long id) {
        Chamado entity = chamadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado"));
        return mapper.toDto(entity);
    }

    @Transactional
    public ChamadoDTO insert(ChamadoDTO dto) {
        Cliente cliente = buscarCliente(dto);
        Tecnico tecnico = buscarTecnico(dto);

        Chamado entity = mapper.toEntity(dto, cliente, tecnico);
        entity = chamadoRepository.save(entity);

        return mapper.toDto(entity);
    }

    @Transactional
    public ChamadoDTO update(Long id, ChamadoDTO dto) {
        Chamado entity = chamadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado"));

        Cliente cliente = buscarCliente(dto);
        Tecnico tecnico = buscarTecnico(dto);

        mapper.updateEntityFromDto(dto, entity, cliente, tecnico);
        entity = chamadoRepository.save(entity);

        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        Chamado entity = chamadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado"));

        if (entity.getStatus() != Status.ENCERRADO) {
            throw new DatabaseException("Não é possível excluir um chamado que não está encerrado. Status atual: " + entity.getStatus());
        }

        chamadoRepository.delete(entity);
    }

    private Cliente buscarCliente(ChamadoDTO dto) {
        if (dto.getCliente() == null || dto.getCliente().getId() == null) {
            throw new ResourceNotFoundException("Cliente é obrigatório");
        }
        Long clienteId = dto.getCliente().getId();
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + clienteId));
    }

    private Tecnico buscarTecnico(ChamadoDTO dto) {
        if (dto.getTecnico() == null || dto.getTecnico().getId() == null) {
            throw new ResourceNotFoundException("Técnico é obrigatório");
        }
        Long tecnicoId = dto.getTecnico().getId();
        return tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Técnico não encontrado: " + tecnicoId));
    }
}