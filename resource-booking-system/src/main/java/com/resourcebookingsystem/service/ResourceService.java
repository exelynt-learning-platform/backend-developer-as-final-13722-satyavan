package com.resourcebookingsystem.service;

import com.resourcebookingsystem.dto.ResourceDto;
import com.resourcebookingsystem.model.Resource;
import com.resourcebookingsystem.repository.ResourceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceRepository repository;

    public List<Resource> getAll(){
        return repository.findAll();
    }

    public Resource getById(Long id){
        return repository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Resource not found with ID: "+id));
    }

    public Resource create(ResourceDto dto){
        Resource r=Resource.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .type(dto.getType())
                .basePrice(dto.getBasePrice())
                .build();
        return repository.save(r);
    }
    public Resource update(Long id, ResourceDto dto){
        Resource r=getById(id);
        r.setName(dto.getName());
        r.setDescription(dto.getDescription());
        r.setType(dto.getType());
        r.setBasePrice(dto.getBasePrice());
        return repository.save(r);
    }
    public void delete(Long id){
        Resource r=getById(id);
        repository.delete(r);
    }

}
