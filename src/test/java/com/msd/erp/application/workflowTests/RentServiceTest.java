package com.msd.erp.application.workflowTests;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.msd.erp.application.services.RentService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.application.views.RentDTO;
import com.msd.erp.domain.Relation;
import com.msd.erp.domain.Rent;
import com.msd.erp.infrastructure.repositories.RentRepository;

class RentServiceTest {

    @Mock
    private RentRepository rentRepository;

    @Mock
    private DomainValidationService validationService;

    @InjectMocks
    private RentService rentService;

    private Rent rent;
    private Relation mockCustomer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockCustomer = new Relation();
        mockCustomer.setRelationid(1L);

        // Create rent object
        rent = new Rent();
        rent.setRentId(1L);
        rent.setCustomer(mockCustomer);

        // Convert LocalDate to Date and set start date and end date
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 10);

        // Convert LocalDate to Date by setting time to midnight (start of the day)
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        rent.setStartDate(start);
        rent.setEndDate(end);

        // Set other properties
        rent.setTotalPrice(100.0);
        rent.setTotalPriceWithVAT(119.0);
        rent.setTotalPriceWithPenalties(219.0);
    }

    @Test
    void findById_ShouldReturnRentIfExists() {
        when(rentRepository.findById(1L)).thenReturn(Optional.of(rent));

        Optional<Rent> foundRent = rentService.findById(1L);

        assertTrue(foundRent.isPresent());
        assertEquals(rent, foundRent.get());
        verify(rentRepository, times(1)).findById(1L);
    }

    @Test
    void findByCustomerId_ShouldReturnRentsForCustomer() {
        when(rentRepository.findByCustomerId(1L)).thenReturn(Arrays.asList(rent));

        List<Rent> rents = rentService.findByCustomerId(1L);

        assertEquals(1, rents.size());
        assertEquals(rent, rents.get(0));
        verify(rentRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void findAll_ShouldReturnAllRents() {
        when(rentRepository.findAll()).thenReturn(Arrays.asList(rent));

        List<Rent> rents = rentService.findAll();

        assertEquals(1, rents.size());
        assertEquals(rent, rents.get(0));
        verify(rentRepository, times(1)).findAll();
    }

    @Test
    void save_ShouldValidateAndSaveRent() {
        when(rentRepository.save(rent)).thenReturn(rent);

        Rent savedRent = rentService.save(rent);

        verify(validationService, times(1)).validateEntity(rent);
        verify(rentRepository, times(1)).save(rent);

        assertEquals(rent, savedRent);
    }

   
    @Test
void updateRent_ShouldUpdateRentDetails() {
    // Create a RentDTO with LocalDate values
    RentDTO rentDTO = new RentDTO();
    
    // Convert LocalDate to Date before setting it on the RentDTO
    Date startDate = Date.from(LocalDate.of(2024, 11, 5).atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date endDate = Date.from(LocalDate.of(2024, 11, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
    
    // Set the Date values on RentDTO
    rentDTO.setStartDate(startDate); // Pass Date, not LocalDate
    rentDTO.setEndDate(endDate);     // Pass Date, not LocalDate
    rentDTO.setCustomer(mockCustomer);

    // Prepare the Rent entity (possibly the one you expect to update)
    Rent rent = new Rent();
    rent.setRentId(1L);
    rent.setStartDate(startDate); // Set Date value
    rent.setEndDate(endDate);     // Set Date value
    rent.setCustomer(mockCustomer);

    // Mock repository calls
    when(rentRepository.findById(1L)).thenReturn(Optional.of(rent));
    when(rentRepository.save(rent)).thenReturn(rent);

    // Call the updateRent method
    Optional<Rent> updatedRent = rentService.updateRent(1L, rentDTO);

    // Assertions to check if the Rent entity was updated correctly
    assertTrue(updatedRent.isPresent());
    assertEquals(startDate, updatedRent.get().getStartDate()); // Check start date
    assertEquals(endDate, updatedRent.get().getEndDate());     // Check end date
    assertEquals(mockCustomer, updatedRent.get().getCustomer()); // Check customer
    verify(rentRepository, times(1)).save(rent); // Verify save was called
}
    @Test
    void updateRent_ShouldReturnEmptyIfRentNotFound() {
        when(rentRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Rent> updatedRent = rentService.updateRent(1L, new RentDTO());

        assertFalse(updatedRent.isPresent());
        verify(rentRepository, never()).save(any());
    }

    @Test
    void deleteById_ShouldDeleteRent() {
        rentService.deleteById(1L);

        verify(rentRepository, times(1)).deleteById(1L);
    }

}
