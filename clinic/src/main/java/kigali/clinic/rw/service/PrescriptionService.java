package kigali.clinic.rw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    public String savePrescription(Prescription prescription, UUID appointmentId) {
        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if (appointment.isEmpty()) return "The appointment with that id does not exist";

        if (appointment.get().getStatus() != AppointmentStatus.COMPLETED)
            return "Prescriptions can only be written for COMPLETED appointments";

        prescription.setAppointment(appointment.get());
        prescriptionRepo.save(prescription);
        return "Prescription saved successfully";
    }

    public List<Prescription> getByAppointment(UUID appointmentId) {
        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if (appointment.isEmpty()) return new ArrayList<>();
        return prescriptionRepo.findByAppointment(appointment.get());
    }

    public Optional<Prescription> getPrescriptionById(UUID id) {
        return prescriptionRepo.findById(id);
    }

    public String deletePrescription(UUID id) {
        if (!prescriptionRepo.existsById(id)) return "Prescription not found";
        prescriptionRepo.deleteById(id);
        return "Prescription deleted successfully";
    }
}
