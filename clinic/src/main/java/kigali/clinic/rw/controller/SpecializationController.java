package kigali.clinic.rw.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.service.SpecializationService;

@RestController
@RequestMapping("/api/specialization")
public class SpecializationController {

    @Autowired
    private SpecializationService specService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Specialization specialization) {
        String msg = specService.saveSpecialization(specialization);
        if (msg.equals("Specialization saved successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(specService.getAllSpecializations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Specialization> spec = specService.getSpecializationById(id);
        if (spec.isEmpty()) {
            return new ResponseEntity<>("Specialization not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spec.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Specialization specialization) {
        String msg = specService.updateSpecialization(id, specialization);
        if (msg.equals("Specialization updated successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = specService.deleteSpecialization(id);
        if (msg.equals("Specialization deleted successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{specializationId}/assign/{doctorId}")
    public ResponseEntity<?> assignDoctor(@PathVariable UUID specializationId,
                                          @PathVariable UUID doctorId) {
        String msg = specService.assignDoctorToSpecialization(specializationId, doctorId);
        if (msg.equals("Doctor assigned to specialization successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{specializationId}/remove/{doctorId}")
    public ResponseEntity<?> removeDoctor(@PathVariable UUID specializationId,
                                          @PathVariable UUID doctorId) {
        String msg = specService.removeDoctorFromSpecialization(specializationId, doctorId);
        if (msg.equals("Doctor removed from specialization successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }
}
