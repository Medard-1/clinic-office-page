package kigali.clinic.rw.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Patient;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    Optional<Appointment> findByPatientAndDoctorAndAppointmentDate(Patient patient, Doctor doctor, Date date);
    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, Date date);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByStatusAndAppointmentDate(AppointmentStatus status, Date date);

    List<Appointment> findByDoctorAndStatusOrderByAppointmentDateAsc(Doctor doctor, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.status = kigali.clinic.rw.domain.AppointmentStatus.SCHEDULED AND a.appointmentDate < CURRENT_DATE")
    List<Appointment> findOverduePendingAppointments();

    @Query("SELECT a.doctor, COUNT(a) FROM Appointment a GROUP BY a.doctor ORDER BY COUNT(a) DESC")
    List<Object[]> countAppointmentsPerDoctor();

    @Query("SELECT a FROM Appointment a JOIN a.doctor d JOIN d.specializations s WHERE s.name = :name")
    List<Appointment> findByDoctorSpecializationName(@Param("name") String name);

    Page<Appointment> findByDoctorOrderByAppointmentDateAsc(Doctor doctor, Pageable pageable);

    List<Appointment> findByPatientAndStatus(Patient patient, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :start AND :end ORDER BY a.appointmentDate ASC")
    List<Appointment> findBetweenDates(@Param("start") Date start, @Param("end") Date end);

    long countByDoctorAndStatus(Doctor doctor, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor WHERE a.status = kigali.clinic.rw.domain.AppointmentStatus.CANCELLED")
    List<Appointment> findAllCancelledWithDetails();

    List<Appointment> findByDoctorOrderByAppointmentDateDesc(Doctor doctor);

    @Query("SELECT DISTINCT a.doctor FROM Appointment a WHERE a.status = 'COMPLETED'")
    List<Doctor> findDoctorsWithCompletedAppointments();

    List<Appointment> findByAppointmentDate(Date date);

    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);

    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(Date start, Date end);

    boolean existsByDoctorAndAppointmentDateAndStatusNot(Doctor doctor, Date date, AppointmentStatus status);

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countByStatus();

    @Query("SELECT a.patient, COUNT(a) FROM Appointment a GROUP BY a.patient HAVING COUNT(a) >= :min ORDER BY COUNT(a) DESC")
    List<Object[]> findFrequentPatients(@Param("min") long min);

    @Query("SELECT o.name, o.officeNumber, COUNT(a) FROM Appointment a JOIN a.doctor d JOIN d.office o GROUP BY o.name, o.officeNumber ORDER BY COUNT(a) DESC")
    List<Object[]> findBusiestOffice(Pageable pageable);

    @Modifying
    @Query("UPDATE Appointment a SET a.status = kigali.clinic.rw.domain.AppointmentStatus.CANCELLED WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date AND a.status <> kigali.clinic.rw.domain.AppointmentStatus.COMPLETED")
    int cancelDayAppointments(@Param("doctorId") UUID doctorId, @Param("date") Date date);

    @Modifying
    @Query("DELETE FROM Appointment a WHERE a.status = kigali.clinic.rw.domain.AppointmentStatus.CANCELLED AND a.appointmentDate < :date")
    int deleteCancelledBefore(@Param("date") Date date);
}
