package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.repository.OfficeRepository;

@Service 
public class OfficeService {

    @Autowired 
    private OfficeRepository offRepo;

    public String saveOffice(Office office) {
        if (offRepo.findByOfficeNumber(office.getOfficeNumber()).isPresent())
            return "Office with this number already exists";
        offRepo.save(office);
        return "Office is saved successfully";
    }

    public List<Office> getAllOffices() {
        return offRepo.findAll();
    }

    public Optional<Office> getOfficeById(UUID id) {
        return offRepo.findById(id);
    }

    public String updateOffice(UUID id, Office updated) {
        Optional<Office> existing = offRepo.findById(id);
        if (existing.isEmpty()) return "Office not found";
        Office office = existing.get();
        office.setName(updated.getName());
        office.setOfficeNumber(updated.getOfficeNumber());
        offRepo.save(office);
        return "Office updated successfully";
    }

    public String deleteOffice(UUID id) {
        if (!offRepo.existsById(id)) return "Office not found";
        offRepo.deleteById(id);
        return "Office deleted successfully";
    
    }

    public List<Office> getFreeOffices() {
        return offRepo.findFreeOffices();
    }
    public List<Office> searchOffices(String keyword) {
        return offRepo.searchOffices(keyword);
    }
    

    public String deleteOfficeByOfficeNumber(int officeNumber) {
        Optional<Office> office = offRepo.findByOfficeNumber(officeNumber);

        if (office.isEmpty()) {
            return "Office not found";
        }
        offRepo.delete(office.get());
        return "Office deleted successfully";
    }
}