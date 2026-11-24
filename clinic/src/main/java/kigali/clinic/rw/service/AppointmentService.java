package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    public String saveAppointment(Appointment appointment, UUID doctorId) {

        Optional<Doctor> doctor = doctorRepo.findById(doctorId);
        if (doctor.isEmpty()) return "Doctor not found";

        Patient patient = appointment.getPatient();
        if (patient == null || !patientRepo.existsById(patient.getId()))
            return "Patient not found";

        if (appointment.getAppointmentDate() == null)
            return "Appointment date is required";

        if (appointment.getAppointmentDate().toLocalDate().isBefore(LocalDate.now()))
            return "Appointment date cannot be in the past";

        if (appointmentRepo.findByPatientAndDoctorAndAppointmentDate(
                patient, doctor.get(), appointment.getAppointmentDate()).isPresent())
            return "This patient already has an appointment with this doctor on that date";

        if (appointmentRepo.existsByDoctorAndAppointmentDateAndStatusNot(
                doctor.get(), appointment.getAppointmentDate(), AppointmentStatus.CANCELLED))
            return "Doctor is already booked on that date";

        appointment.setDoctor(doctor.get());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepo.save(appointment);
        return "Appointment saved successfully";
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public Optional<Appointment> getAppointmentById(UUID id) {
        return appointmentRepo.findById(id);
    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepo.findByStatus(status);
    }

    public List<Appointment> getPendingToday() {
        return appointmentRepo.findByStatusAndAppointmentDate(
                AppointmentStatus.SCHEDULED, new java.sql.Date(System.currentTimeMillis()));
    }

    public String updateAppointment(UUID id, Appointment updated) {
        Optional<Appointment> existing = appointmentRepo.findById(id);
        if (existing.isEmpty()) return "Appointment not found";
        Appointment appointment = existing.get();
        appointment.setAppointmentDate(updated.getAppointmentDate());
        appointment.setReason(updated.getReason());
        appointmentRepo.save(appointment);
        return "Appointment updated successfully";
    }

    public String deleteAppointment(UUID id) {
        if (!appointmentRepo.existsById(id)) return "Appointment not found";
        appointmentRepo.deleteById(id);
        return "Appointment deleted successfully";
    }

    public String updateStatus(UUID id, AppointmentStatus newStatus) {
        Optional<Appointment> existing = appointmentRepo.findById(id);
        if (existing.isEmpty()) return "The appointment with that id does not exist";

        Appointment appointment = existing.get();
        AppointmentStatus current = appointment.getStatus();

        if (current == AppointmentStatus.SCHEDULED) {
            if (newStatus != AppointmentStatus.CONFIRMED && newStatus != AppointmentStatus.CANCELLED) {
                return "Cannot change status from " + current + " to " + newStatus;
            }
        } else if (current == AppointmentStatus.CONFIRMED) {
            if (newStatus != AppointmentStatus.COMPLETED && newStatus != AppointmentStatus.CANCELLED) {
                return "Cannot change status from " + current + " to " + newStatus;
            }
        } else {
            return "Cannot change status from " + current + " to " + newStatus;
        }

        appointment.setStatus(newStatus);
        appointmentRepo.save(appointment);
        return "Appointment status updated successfully";
    }

    public String reschedule(UUID id, java.sql.Date newDate) {
        Optional<Appointment> existing = appointmentRepo.findById(id);
        if (existing.isEmpty()) return "The appointment with that id does not exist";

        Appointment appointment = existing.get();

        if (appointment.getStatus() == AppointmentStatus.COMPLETED)
            return "A COMPLETED appointment cannot be rescheduled";

        if (appointment.getStatus() == AppointmentStatus.CANCELLED)
            return "A CANCELLED appointment cannot be rescheduled";

        if (newDate.toLocalDate().isBefore(LocalDate.now()))
            return "Appointment date cannot be in the past";

        if (appointmentRepo.existsByDoctorAndAppointmentDateAndStatusNot(
                appointment.getDoctor(), newDate, AppointmentStatus.CANCELLED))
            return "Doctor is already booked on that date";

        appointment.setAppointmentDate(newDate);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepo.save(appointment);
        return "Appointment rescheduled successfully";
    }

    public List<Appointment> getPendingByDoctorOrderedByDate(Doctor doctor) {
        return appointmentRepo.findByDoctorAndStatusOrderByAppointmentDateAsc(doctor, AppointmentStatus.SCHEDULED);
    }

    public List<Appointment> getOverduePending() {
        return appointmentRepo.findOverduePendingAppointments();
    }

    public List<Object[]> getAppointmentsPerDoctor() {
        return appointmentRepo.countAppointmentsPerDoctor();
    }

    public List<Appointment> getBySpecialization(String name) {
        return appointmentRepo.findByDoctorSpecializationName(name);
    }

    public Page<Appointment> getPagedByDoctor(Doctor doctor, int page) {
        return appointmentRepo.findByDoctorOrderByAppointmentDateAsc(
                doctor, PageRequest.of(page, 10, Sort.by("appointmentDate").ascending()));
    }

    public List<Appointment> getByPatientAndStatus(Patient patient, AppointmentStatus status) {
        return appointmentRepo.findByPatientAndStatus(patient, status);
    }

    public List<Appointment> getBetweenDates(java.sql.Date start, java.sql.Date end) {
        return appointmentRepo.findBetweenDates(start, end);
    }

    public long countByDoctorAndStatus(Doctor doctor, AppointmentStatus status) {
        return appointmentRepo.countByDoctorAndStatus(doctor, status);
    }

    public List<Appointment> getCancelledWithDetails() {
        return appointmentRepo.findAllCancelledWithDetails();
    }

    public List<Appointment> getByDoctorOrderedDesc(Doctor doctor) {
        return appointmentRepo.findByDoctorOrderByAppointmentDateDesc(doctor);
    }

    public List<Doctor> getDoctorsWithCompleted() {
        return appointmentRepo.findDoctorsWithCompletedAppointments();
    }

    public List<Appointment> getByDate(java.sql.Date date) {
        return appointmentRepo.findByAppointmentDate(date);
    }

    public List<Appointment> getByStatusOrdered(AppointmentStatus status) {
        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> getBetweenDatesInclusive(java.sql.Date start, java.sql.Date end) {
        return appointmentRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    public List<Object[]> getCountByStatus() {
        return appointmentRepo.countByStatus();
    }

    public List<Object[]> getFrequentPatients(long min) {
        return appointmentRepo.findFrequentPatients(min);
    }

    public Object[] getBusiestOffice() {
        List<Object[]> results = appointmentRepo.findBusiestOffice(PageRequest.of(0, 1));
        if (results.isEmpty()) return null;
        return results.get(0);
    }

    @Transactional
    public String cancelDay(UUID doctorId, java.sql.Date date) {
        int count = appointmentRepo.cancelDayAppointments(doctorId, date);
        return count + " appointments cancelled";
    }

    public Page<Appointment> getPagedAppointments(Pageable pageable) {
        return appointmentRepo.findAll(pageable);
    }

    @Transactional
    public String deleteCancelledBefore(java.sql.Date date) {
        int count = appointmentRepo.deleteCancelledBefore(date);
        return count + " appointments deleted";
    }
}
