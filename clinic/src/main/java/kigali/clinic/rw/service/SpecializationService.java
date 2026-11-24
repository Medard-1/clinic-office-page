package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    public String saveSpecialization(Specialization specialization) {
        if (specRepo.findByName(specialization.getName()).isPresent())
            return "Specialization already exists";
        specRepo.save(specialization);
        return "Specialization saved successfully";
    }

    public List<Specialization> getAllSpecializations() {
        return specRepo.findAll();
    }

    public Optional<Specialization> getSpecializationById(UUID id) {
        return specRepo.findById(id);
    }

    public String updateSpecialization(UUID id, Specialization updated) {
        Optional<Specialization> existing = specRepo.findById(id);
        if (existing.isEmpty()) return "Specialization not found";
        Specialization spec = existing.get();
        spec.setName(updated.getName());
        specRepo.save(spec);
        return "Specialization updated successfully";
    }

    public String deleteSpecialization(UUID id) {
        if (!specRepo.existsById(id)) return "Specialization not found";
        specRepo.deleteById(id);
        return "Specialization deleted successfully";
    }

    public String assignDoctorToSpecialization(UUID specializationId, UUID doctorId) {
        Optional<Specialization> spec = specRepo.findById(specializationId);
        Optional<Doctor> doctor = doctorRepo.findById(doctorId);

        if (spec.isEmpty()) return "Specialization not found";
        if (doctor.isEmpty()) return "Doctor not found";

        Doctor d = doctor.get();
        for (Specialization s : d.getSpecializations()) {
            if (s.getId().equals(specializationId)) {
                return "Doctor already has this specialization";
            }
        }
        d.getSpecializations().add(spec.get());
        doctorRepo.save(d);
        return "Doctor assigned to specialization successfully";
    }

    public String removeDoctorFromSpecialization(UUID specializationId, UUID doctorId) {
        Optional<Specialization> spec = specRepo.findById(specializationId);
        Optional<Doctor> doctor = doctorRepo.findById(doctorId);

        if (spec.isEmpty()) return "Specialization not found";
        if (doctor.isEmpty()) return "Doctor not found";

        Doctor d = doctor.get();
        Specialization toRemove = null;
        for (Specialization s : d.getSpecializations()) {
            if (s.getId().equals(specializationId)) {
                toRemove = s;
            }
        }
        if (toRemove != null) {
            d.getSpecializations().remove(toRemove);
        }
        doctorRepo.save(d);
        return "Doctor removed from specialization successfully";
    }

    public List<Specialization> getUnusedSpecializations() {
        return specRepo.findUnusedSpecializations();
    }
}
