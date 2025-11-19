package com.example.assistenciatecnica.services;

import com.example.assistenciatecnica.dto.TecnicoDTO;
import com.example.assistenciatecnica.entity.Tecnico;
import com.example.assistenciatecnica.mappers.TecnicoMapper;
import com.example.assistenciatecnica.repositories.TecnicoRepository;
import com.example.assistenciatecnica.repositories.ClienteRepository;
import com.example.assistenciatecnica.exceptions.DatabaseException;
import com.example.assistenciatecnica.exceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TecnicoMapper mapper;

    @Transactional(readOnly = true)
    public List<TecnicoDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TecnicoDTO findById(Long id) {
        Tecnico entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Técnico não encontrado"));
        return mapper.toDto(entity);
    }

    @Transactional
    public TecnicoDTO insert(TecnicoDTO dto) {
        validarCpfEmail(dto);

        Tecnico entity = mapper.toEntity(dto);
        entity = repository.save(entity);

        return mapper.toDto(entity);
    }

    @Transactional
    public TecnicoDTO update(Long id, TecnicoDTO dto) {
        Tecnico entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Técnico não encontrado"));

        mapper.updateEntityFromDto(dto, entity);
        entity = repository.save(entity);

        return mapper.toDto(entity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Técnico não encontrado");
        }

        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não é possível excluir o técnico. Ele possui chamados associados.");
        }
    }

    private void validarCpfEmail(TecnicoDTO dto) {
        if (repository.existsByCpf(dto.getCpf()) || clienteRepository.existsByCpf(dto.getCpf())) {
            throw new DatabaseException("CPF já cadastrado");
        }
        if (repository.existsByEmail(dto.getEmail()) || clienteRepository.existsByEmail(dto.getEmail())) {
            throw new DatabaseException("Email já cadastrado");
        }
    }
}