package com.msd.erp.servicesTests;

import com.msd.erp.application.services.RentService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.application.views.RentDTO;
import com.msd.erp.domain.Relation;
import com.msd.erp.domain.Rent;
import com.msd.erp.infrastructure.repositories.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        rent = new Rent();
        rent.setRentId(1L);
        rent.setCustomer(mockCustomer);
        rent.setStartDate(LocalDate.of(2024, 11, 1));
        rent.setEndDate(LocalDate.of(2024, 11, 10));
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
        RentDTO rentDTO = new RentDTO();
        rentDTO.setStartDate(LocalDate.of(2024, 11, 5));
        rentDTO.setEndDate(LocalDate.of(2024, 11, 15));
        rentDTO.setCustomer(mockCustomer);

        when(rentRepository.findById(1L)).thenReturn(Optional.of(rent));
        when(rentRepository.save(rent)).thenReturn(rent);

        Optional<Rent> updatedRent = rentService.updateRent(1L, rentDTO);

        assertTrue(updatedRent.isPresent());
        assertEquals(LocalDate.of(2024, 11, 5), updatedRent.get().getStartDate());
        assertEquals(LocalDate.of(2024, 11, 15), updatedRent.get().getEndDate());
        assertEquals(mockCustomer, updatedRent.get().getCustomer());
        verify(rentRepository, times(1)).save(rent);
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
