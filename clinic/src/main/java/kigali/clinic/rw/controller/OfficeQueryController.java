package kigali.clinic.rw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.service.AppointmentService;
import kigali.clinic.rw.service.OfficeService;

@RestController
@RequestMapping("/api/offices")
public class OfficeQueryController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private OfficeService officeService;

    @GetMapping("/busiest")
    public ResponseEntity<?> busiest() {
        Object[] row = appointmentService.getBusiestOffice();
        if (row == null) {
            return ResponseEntity.ok("No appointments yet");
        }
        return ResponseEntity.ok(row);
    }

    @GetMapping("/free")
    public ResponseEntity<?> freeOffices() {
        return ResponseEntity.ok(officeService.getFreeOffices());
    }
}
