package kigali.clinic.rw.controller;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.service.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/save/{doctorId}")
    public ResponseEntity<?> saveAppointment(@RequestBody Appointment appointment,
                                             @PathVariable UUID doctorId) {
        String msg = appointmentService.saveAppointment(appointment, doctorId);
        if (msg.equals("Appointment saved successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable UUID id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        if (appointment.isEmpty()) {
            return new ResponseEntity<>("Appointment not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(appointment.get());
    }

    @GetMapping("/filter/pending-today")
    public ResponseEntity<?> getPendingToday() {
        return ResponseEntity.ok(appointmentService.getPendingToday());
    }

    @GetMapping("/filter/status/{status}")
    public ResponseEntity<?> getByStatus(@PathVariable AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable UUID id,
                                               @RequestBody Appointment appointment) {
        String msg = appointmentService.updateAppointment(id, appointment);
        if (msg.equals("Appointment updated successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable UUID id) {
        String msg = appointmentService.deleteAppointment(id);
        if (msg.equals("Appointment deleted successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable UUID id,
                                          @RequestParam AppointmentStatus status) {
        String msg = appointmentService.updateStatus(id, status);
        if (msg.equals("Appointment status updated successfully")) {
            return ResponseEntity.ok(msg);
        } else if (msg.equals("The appointment with that id does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<?> reschedule(@PathVariable UUID id,
                                        @RequestParam String newDate) {
        String msg = appointmentService.reschedule(id, Date.valueOf(newDate));
        if (msg.equals("Appointment rescheduled successfully")) {
            return ResponseEntity.ok(msg);
        } else if (msg.equals("The appointment with that id does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        } else if (msg.equals("Doctor is already booked on that date")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }
}
