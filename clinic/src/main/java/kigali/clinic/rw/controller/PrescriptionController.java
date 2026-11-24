package kigali.clinic.rw.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.service.PrescriptionService;

@RestController
@RequestMapping("/api/prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/save/{appointmentId}")
    public ResponseEntity<?> savePrescription(@RequestBody Prescription prescription,
                                              @PathVariable UUID appointmentId) {
        String msg = prescriptionService.savePrescription(prescription, appointmentId);
        if (msg.equals("Prescription saved successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        } else if (msg.equals("The appointment with that id does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getByAppointment(@PathVariable UUID appointmentId) {
        return ResponseEntity.ok(prescriptionService.getByAppointment(appointmentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPrescriptionById(@PathVariable UUID id) {
        Optional<Prescription> prescription = prescriptionService.getPrescriptionById(id);
        if (prescription.isEmpty()) {
            return new ResponseEntity<>("Prescription not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(prescription.get());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePrescription(@PathVariable UUID id) {
        String msg = prescriptionService.deletePrescription(id);
        if (msg.equals("Prescription deleted successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }
}
