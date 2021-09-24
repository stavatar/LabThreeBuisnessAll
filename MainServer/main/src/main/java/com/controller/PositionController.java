package com.controller;


import com.entity.Position;
import com.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "PositionController", description = "Содержит методы для работы с ролями")
@RestController
@Log
public class PositionController
{
    @Autowired
    private PositionService positionService;

    @GetMapping(value = "/user/role/all/")
    @Operation(summary = "Вывод списка ролей")
    public ResponseEntity<List<Position>> readAllRoles()
    {
        final List<Position> posts = positionService.getAll();

        return posts != null &&  !posts.isEmpty()
                ? new ResponseEntity<>(posts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
