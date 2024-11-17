package com.msd.erp.application.services;

import com.msd.erp.application.computations.RentComputation;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.application.views.RentLineDTO;
import com.msd.erp.domain.Article;
import com.msd.erp.domain.Rent;
import com.msd.erp.domain.RentLine;
import com.msd.erp.domain.VATRate;
import com.msd.erp.infrastructure.repositories.RentLineRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RentLineService {

    @Autowired
    private RentService rentService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private VATRateService vatService;

    @Autowired
    private RentLineRepository rentLineRepository;

    @Autowired
    private DomainValidationService validationService;

    public RentLine createRentLine(RentLine rentLine) {
        Rent rent = rentService.findById(rentLine.getRent().getRentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rent not found"));

        Article article = articleService.getArticleById(rentLine.getArticle().getArticleid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        VATRate vatRate = vatService.getVATRateById(rentLine.getVat().getVatid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "VATRate not found"));

        rentLine.setRent(rent);
        rentLine.setArticle(article);
        rentLine.setVat(vatRate);
        RentComputation.computeLineAmounts(rentLine);

        RentLine createdRentLine = rentLineRepository.save(rentLine);

        rentService.updateRentHeaderTotals(rent, rentLine.getLineAmount(), rentLine.getLineAmountWithVAT(),
                rentLine.getLineAmountWithPenalties());

        rentService.save(rent);

        return createdRentLine;
    }

    public Optional<RentLine> getRentLineById(Long rentLineId) {
        return rentLineRepository.findById(rentLineId);
    }

    public List<RentLine> getRentLinesByRentId(Long rentId) {
        return rentLineRepository.findByRentId(rentId);
    }

    public List<RentLine> getAllRentLines() {
        return rentLineRepository.findAll();
    }

    // update rent line
    public RentLine updateRentLine(RentLine rentLine) {
        Rent rent = rentService.findById(rentLine.getRent().getRentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rent not found"));

        Double oldLineAmount = rentLine.getLineAmount();
        Double oldLineAmountWithPenalties = rentLine.getLineAmountWithPenalties();
        Double oldLineAmountWithVAT = rentLine.getLineAmountWithVAT();

        RentComputation.computeLineAmounts(rentLine);

        rentService.updateRentHeaderTotals(rent,
                oldLineAmount, rentLine.getLineAmount(),
                oldLineAmountWithVAT, rentLine.getLineAmountWithVAT(),
                oldLineAmountWithPenalties, rentLine.getLineAmountWithPenalties());

        rentService.save(rent);

        return rentLineRepository.save(rentLine);
    }

    // method for DTO-based updates
    public RentLine updateRentLine(Long rentLineId, RentLineDTO rentLineDTO) {

        RentLine rentLine = rentLineRepository.findById(rentLineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RentLine not found"));

        if (rentLineDTO.getPricePerDay() != null) {
            rentLine.setPricePerDay(rentLineDTO.getPricePerDay());
        }

        if (rentLineDTO.getQuantity() != null) {
            rentLine.setQuantity(rentLineDTO.getQuantity());
        }

        if (rentLineDTO.getVat() != null) {
            VATRate vatRate = vatService.getVATRateById(rentLineDTO.getVat().getVatid())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "VAT rate not found"));
            rentLine.setVat(vatRate);
        }

        return updateRentLine(rentLine);
    }

    public RentLine save(RentLine rentLine) {
        validationService.validateEntity(rentLine);
        return rentLineRepository.save(rentLine);
    }

    @Transactional
    public boolean deleteRentLineAndUpdateRent(Long rentLineId) {
        Optional<RentLine> optionalRentLine = rentLineRepository.findById(rentLineId);
        if (optionalRentLine.isPresent()) {
            RentLine rentLine = optionalRentLine.get();
            Rent rent = rentLine.getRent();

            rentService.updateRentHeaderTotals(
                    rent,
                    rentLine.getLineAmount() * -1,
                    rentLine.getLineAmountWithVAT() * -1,
                    rentLine.getLineAmountWithPenalties() * -1);

            rentService.save(rent);

            rentLineRepository.delete(rentLine);

            return true;
        }
        return false;
    }

}
