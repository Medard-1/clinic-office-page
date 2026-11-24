package kigali.clinic.rw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorQueryController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/by-specialization")
    public ResponseEntity<?> bySpecialization(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.getBySpecializationName(name));
    }

    @GetMapping("/without-office")
    public ResponseEntity<?> withoutOffice() {
        return ResponseEntity.ok(doctorService.getDoctorsWithoutOffice());
    }
}
