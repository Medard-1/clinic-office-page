package kigali.clinic.rw.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.service.PatientService;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/save")
    public ResponseEntity<?> savePatient(@RequestBody Patient patient) {
        return new ResponseEntity<>(patientService.savePatient(patient), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable UUID id) {
        Optional<Patient> patient = patientService.getPatientById(id);
        if (patient.isEmpty()) {
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(patient.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable UUID id, @RequestBody Patient patient) {
        String msg = patientService.updatePatient(id, patient);
        if (msg.equals("Patient updated successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable UUID id) {
        String msg = patientService.deletePatient(id);
        if (msg.equals("Patient deleted successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }
}
