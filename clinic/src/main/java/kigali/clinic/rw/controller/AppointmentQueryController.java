package kigali.clinic.rw.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentQueryController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/by-status")
    public ResponseEntity<?> byStatus(@RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.getByStatusOrdered(status));
    }

    @GetMapping("/between")
    public ResponseEntity<?> between(@RequestParam String start, @RequestParam String end) {
        return ResponseEntity.ok(
            appointmentService.getBetweenDatesInclusive(Date.valueOf(start), Date.valueOf(end))
        );
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Appointment appointment,
                                  @RequestParam UUID doctorId) {
        String msg = appointmentService.saveAppointment(appointment, doctorId);
        if (msg.equals("Appointment saved successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        } else if (msg.equals("Doctor is already booked on that date")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<?> statsByStatus() {
        return ResponseEntity.ok(appointmentService.getCountByStatus());
    }

    @GetMapping("/frequent-patients")
    public ResponseEntity<?> frequentPatients(@RequestParam long min) {
        List<Object[]> rows = appointmentService.getFrequentPatients(min);
        List<Patient> patients = new ArrayList<>();
        for (Object[] row : rows) {
            patients.add((Patient) row[0]);
        }
        return ResponseEntity.ok(patients);
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<?> cancelDay(@RequestParam UUID doctorId, @RequestParam String date) {
        return ResponseEntity.ok(appointmentService.cancelDay(doctorId, Date.valueOf(date)));
    }

    @GetMapping("/page")
    public ResponseEntity<?> page(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(appointmentService.getPagedAppointments(
                PageRequest.of(page, size, Sort.by("appointmentDate").ascending())
        ));
    }

    @DeleteMapping("/cancelled-before")
    public ResponseEntity<?> deleteCancelledBefore(@RequestParam String date) {
        return ResponseEntity.ok(appointmentService.deleteCancelledBefore(Date.valueOf(date)));
    }
}
