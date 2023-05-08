package com.example.reproduce.controller;

import com.example.reproduce.dto.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple controller with two endpoints, same paths but different HTTP methods.
 * PUT method is accessible only for user with ADMIN role, GET method is not secured, accessible for all.
 * See {@link com.example.reproduce.config.WebSecurityConfig}
 */
@RestController
@RequestMapping(value = "/api/v1/")
public class ItemController {

    @GetMapping(path = "item/{id}")
    public ResponseEntity<Item> getById(@PathVariable Long id) {
        return new ResponseEntity<>(new Item("Item Name " + id), HttpStatus.OK);
    }

    @PutMapping(path = "item/{id}")
    public ResponseEntity<Item> update(@PathVariable Long id, @RequestBody Item updateItemRequest) {
        return new ResponseEntity<>(updateItemRequest, HttpStatus.OK);
    }

}
