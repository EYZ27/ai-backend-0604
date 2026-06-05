package com.sesac.aibackend0604.controller;

import com.sesac.aibackend0604.domain.User;
import com.sesac.aibackend0604.dto.UserRequest;
import com.sesac.aibackend0604.dto.UserResponse;
import com.sesac.aibackend0604.error.NotFoundException;
import com.sesac.aibackend0604.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> list() {
        return userService.findAll().stream().map(UserResponse::from).toList();
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        User user = userService.findById(id).orElseThrow(() -> NotFoundException.of("user", id));
        return UserResponse.from(user);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest req) {
        if (userService.existsByUsername(req.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exists:" + req.username());
        }
        User saved = userService.save(req.toEntity());
        URI location = URI.create("/users/" + saved.getId());
        return ResponseEntity.created(location).body(UserResponse.from(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!userService.existsById(id)) {
            throw NotFoundException.of("user", id);
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
