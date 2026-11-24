package kigali.clinic.rw.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.service.OfficeService;

@RestController
@RequestMapping("/api/office")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    @PostMapping("/save")
    @CrossOrigin("*")
    public ResponseEntity<?> saveOffice(@RequestBody Office office) {
        String msg = officeService.saveOffice(office);
        if (msg.equals("Office is saved successfully")) {
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/all")
    @CrossOrigin("*")
    public ResponseEntity<?> getAllOffices() {
        return ResponseEntity.ok(officeService.getAllOffices());
    }
        @GetMapping("/search")
        @CrossOrigin("*")
    public ResponseEntity<?> searchOffices(@RequestParam String keyword) {
        return ResponseEntity.ok(officeService.searchOffices(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOfficeById(@PathVariable UUID id) {
        Optional<Office> office = officeService.getOfficeById(id);
        if (office.isEmpty()) {
            return new ResponseEntity<>("Office not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(office.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOffice(@PathVariable UUID id, @RequestBody Office office) {
        String msg = officeService.updateOffice(id, office);
        if (msg.equals("Office updated successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOffice(@PathVariable UUID id) {
        String msg = officeService.deleteOffice(id);
        if (msg.equals("Office deleted successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }
    
}
 