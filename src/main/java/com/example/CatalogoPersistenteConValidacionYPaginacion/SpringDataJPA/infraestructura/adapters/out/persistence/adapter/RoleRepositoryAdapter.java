package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.adapter;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.model.Role;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.port.out.RoleRepositoryPort;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.mapper.RolePersistenceMapper;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.repository.RoleJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final RoleJpaRepository roleJpaRepository;
    private final RolePersistenceMapper roleMapper;

    public RoleRepositoryAdapter(RoleJpaRepository roleJpaRepository, RolePersistenceMapper roleMapper) {
        this.roleJpaRepository = roleJpaRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByName(name).map(roleMapper::toDomain);
    }

    @Override
    public Role save(Role role) {
        var roleJpaEntity = roleMapper.toJpaEntity(role);
        var savedEntity = roleJpaRepository.save(roleJpaEntity);
        return roleMapper.toDomain(savedEntity);
    }
}
