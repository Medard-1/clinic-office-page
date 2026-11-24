package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    public Patient savePatient(Patient patient) {
        return patientRepo.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public Optional<Patient> getPatientById(UUID id) {
        return patientRepo.findById(id);
    }

    public String updatePatient(UUID id, Patient updated) {
        Optional<Patient> existing = patientRepo.findById(id);
        if (existing.isEmpty()) return "Patient not found";
        Patient patient = existing.get();
        patient.setFirstName(updated.getFirstName());
        patient.setLastName(updated.getLastName());
        patient.setDateOfBirth(updated.getDateOfBirth());
        patientRepo.save(patient);
        return "Patient updated successfully";
    }

    public String deletePatient(UUID id) {
        if (!patientRepo.existsById(id)) return "Patient not found";
        patientRepo.deleteById(id);
        return "Patient deleted successfully";
    }

    public List<Patient> getByLastName(String lastName) {
        return patientRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public Object getPatientsByDoctorId(UUID doctorId) {
        if (!doctorRepo.existsById(doctorId)) return "The doctor with that id does not exist";
        return patientRepo.findPatientsByDoctorId(doctorId);
    }
}
