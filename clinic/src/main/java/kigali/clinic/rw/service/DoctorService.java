package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private SpecializationRepository specializationRepo;

    public String saveDoctor(Doctor doctor, UUID officeId) {
        if (doctorRepo.findByFirstNameAndLastNameAndDateOfBirth(
                doctor.getFirstName(), doctor.getLastName(), doctor.getDateOfBirth()).isPresent())
            return "Doctor with the same name and date of birth already exists";

        if (officeId != null) {
            Optional<Office> office = officeRepository.findById(officeId);
            if (office.isEmpty()) return "Office not found";
            if (doctorRepo.findByOffice(office.get()).isPresent())
                return "This office is already occupied by another doctor";
            doctor.setOffice(office.get());
        }

        doctorRepo.save(doctor);
        return "Doctor saved successfully";
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

    public Optional<Doctor> getDoctorById(UUID id) {
        return doctorRepo.findById(id);
    }

    public String updateDoctor(UUID id, Doctor updated) {
        Optional<Doctor> existing = doctorRepo.findById(id);
        if (existing.isEmpty()) return "Doctor not found";
        Doctor doctor = existing.get();
        doctor.setFirstName(updated.getFirstName());
        doctor.setLastName(updated.getLastName());
        doctor.setDateOfBirth(updated.getDateOfBirth());
        doctorRepo.save(doctor);
        return "Doctor updated successfully";
    }

    public String deleteDoctor(UUID id) {
        if (!doctorRepo.existsById(id)) return "Doctor not found";
        doctorRepo.deleteById(id);
        return "Doctor deleted successfully";
    }

    public String addSpecialization(UUID doctorId, UUID specializationId) {
        Optional<Doctor> doctor = doctorRepo.findById(doctorId);
        if (doctor.isEmpty()) return "Doctor not found";
        Optional<Specialization> spec = specializationRepo.findById(specializationId);
        if (spec.isEmpty()) return "Specialization not found";
        if (doctor.get().getSpecializations().contains(spec.get()))
            return "The doctor already has that specialization";
        doctor.get().getSpecializations().add(spec.get());
        doctorRepo.save(doctor.get());
        return "Specialization added to the doctor";
    }

    public String removeSpecialization(UUID doctorId, UUID specializationId) {
        Optional<Doctor> doctor = doctorRepo.findById(doctorId);
        if (doctor.isEmpty()) return "Doctor not found";
        Optional<Specialization> spec = specializationRepo.findById(specializationId);
        if (spec.isEmpty()) return "Specialization not found";
        if (!doctor.get().getSpecializations().contains(spec.get()))
            return "The doctor does not have that specialization";
        doctor.get().getSpecializations().remove(spec.get());
        doctorRepo.save(doctor.get());
        return "Specialization removed from the doctor";
    }

    public String moveToOffice(UUID doctorId, int officeNumber) {
        Optional<Doctor> doctor = doctorRepo.findById(doctorId);
        if (doctor.isEmpty()) return "Doctor not found";
        Optional<Office> office = officeRepository.findByOfficeNumber(officeNumber);
        if (office.isEmpty()) return "Office not found";
        if (doctorRepo.existsByOffice(office.get())) return "That office is taken by other doctor";
        doctor.get().setOffice(office.get());
        doctorRepo.save(doctor.get());
        return "Office changed successfully";
    }

    public List<Doctor> getBySpecializationName(String name) {
        return doctorRepo.findBySpecializationName(name);
    }

    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorRepo.findDoctorsWithoutOffice();
    }
}
