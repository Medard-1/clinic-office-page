package kigali.clinic.rw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.service.SpecializationService;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationQueryController {

    @Autowired
    private SpecializationService specService;

    @GetMapping("/unused")
    public ResponseEntity<?> unused() {
        return ResponseEntity.ok(specService.getUnusedSpecializations());
    }
}
