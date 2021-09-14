package com.repository;


;

import com.Security.RoleType;
import com.entity.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends CrudRepository<Position, Long> {
    Position findByName(RoleType name);
}