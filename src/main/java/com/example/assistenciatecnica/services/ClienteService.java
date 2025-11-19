package com.example.assistenciatecnica.services;

import com.example.assistenciatecnica.dto.ClienteDTO;
import com.example.assistenciatecnica.entity.Cliente;
import com.example.assistenciatecnica.mappers.ClienteMapper;
import com.example.assistenciatecnica.repositories.ClienteRepository;
import com.example.assistenciatecnica.repositories.TecnicoRepository;
import com.example.assistenciatecnica.exceptions.DatabaseException;
import com.example.assistenciatecnica.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private ClienteMapper mapper;

    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO findById(Long id) {
        Cliente entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return mapper.toDto(entity);
    }

    @Transactional
    public ClienteDTO insert(ClienteDTO dto) {
        validarCpfEmail(dto);

        Cliente entity = mapper.toEntity(dto);
        entity = repository.save(entity);

        return mapper.toDto(entity);
    }

    @Transactional
    public ClienteDTO update(Long id, ClienteDTO dto) {
        Cliente entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        mapper.updateEntityFromDto(dto, entity);
        entity = repository.save(entity);

        return mapper.toDto(entity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não é possível excluir o cliente (dados relacionados).");
        }
    }

    private void validarCpfEmail(ClienteDTO dto) {
        if (repository.existsByCpf(dto.getCpf()) || tecnicoRepository.existsByCpf(dto.getCpf())) {
            throw new DatabaseException("CPF já cadastrado");
        }
        if (repository.existsByEmail(dto.getEmail()) || tecnicoRepository.existsByEmail(dto.getEmail())) {
            throw new DatabaseException("Email já cadastrado");
        }
    }
}