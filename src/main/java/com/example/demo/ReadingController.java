package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readings")
@CrossOrigin
public class ReadingController {

    @Autowired
    private ReadingRepository repository;

    @GetMapping
    public List<Reading> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{sensorId}")
    public List<Reading> getBySensorId(@PathVariable String sensorId) {
        return repository.findBySensorId(sensorId);
    }

    @PostMapping
    public Reading create(@RequestBody Reading reading) {
        return repository.save(reading);
    }
}
