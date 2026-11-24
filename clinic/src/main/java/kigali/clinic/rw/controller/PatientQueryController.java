package kigali.clinic.rw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.service.AppointmentService;
import kigali.clinic.rw.service.PatientService;

@RestController
@RequestMapping("/api/patients")
public class PatientQueryController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/by-last-name")
    public ResponseEntity<?> byLastName(@RequestParam String lastName) {
        return ResponseEntity.ok(patientService.getByLastName(lastName));
    }

    @GetMapping("/of-doctor/{doctorId}")
    public ResponseEntity<?> ofDoctor(@PathVariable UUID doctorId) {
        Object result = patientService.getPatientsByDoctorId(doctorId);
        if (result instanceof String) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/frequent")
    public ResponseEntity<?> frequent(@RequestParam long min) {
        List<Object[]> rows = appointmentService.getFrequentPatients(min);
        List<Patient> patients = new ArrayList<>();
        for (Object[] row : rows) {
            patients.add((Patient) row[0]);
        }
        return ResponseEntity.ok(patients);
    }
}
