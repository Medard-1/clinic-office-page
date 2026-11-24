package kigali.clinic.rw.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/save")
    public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor,
                                        @RequestParam(required = false) UUID officeId) {
        String msg = doctorService.saveDoctor(doctor, officeId);
        if (msg.equals("Doctor saved successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable UUID id) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        if (doctor.isEmpty()) {
            return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(doctor.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor) {
        String msg = doctorService.updateDoctor(id, doctor);
        if (msg.equals("Doctor updated successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id) {
        String msg = doctorService.deleteDoctor(id);
        if (msg.equals("Doctor deleted successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<?> addSpecialization(@PathVariable UUID doctorId,
                                               @PathVariable UUID specializationId) {
        String msg = doctorService.addSpecialization(doctorId, specializationId);
        if (msg.equals("Specialization added to the doctor")) {
            return ResponseEntity.ok(msg);
        } else if (msg.equals("The doctor already has that specialization")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<?> removeSpecialization(@PathVariable UUID doctorId,
                                                  @PathVariable UUID specializationId) {
        String msg = doctorService.removeSpecialization(doctorId, specializationId);
        if (msg.equals("Specialization removed from the doctor")) {
            return ResponseEntity.ok(msg);
        } else if (msg.equals("The doctor does not have that specialization")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{doctorId}/office")
    public ResponseEntity<?> moveToOffice(@PathVariable UUID doctorId,
                                          @RequestParam int officeNumber) {
        String msg = doctorService.moveToOffice(doctorId, officeNumber);
        if (msg.equals("Office changed successfully")) {
            return ResponseEntity.ok(msg);
        } else if (msg.equals("That office is taken by other doctor")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }
}
