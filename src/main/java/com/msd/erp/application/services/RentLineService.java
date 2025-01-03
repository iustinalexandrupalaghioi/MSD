package com.msd.erp.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.msd.erp.application.computations.RentComputation;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.application.views.RentLineDTO;
import com.msd.erp.domain.Article;
import com.msd.erp.domain.Rent;
import com.msd.erp.domain.RentLine;
import com.msd.erp.domain.RentState;
import com.msd.erp.infrastructure.repositories.RentLineRepository;

import jakarta.transaction.Transactional;

@Service
public class RentLineService {

    @Autowired
    private RentService rentService;

    @Autowired
    private ArticleService articleService;

    

    @Autowired
    private RentLineRepository rentLineRepository;

    @Autowired
    private DomainValidationService validationService;

    public RentLine createRentLine(RentLine rentLine) {
        Rent rent = rentService.findById(rentLine.getRent().getRentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rent not found"));

        if (rent.getState() == RentState.CONFIRMED || 
            rent.getState() == RentState.CANCELLED || 
            rent.getState() == RentState.SENT || 
            rent.getState() == RentState.RETURNED) {
        throw new IllegalStateException("Cannot add rent lines to a rent in the " + rent.getState() + " state.");
    }

        Article article = articleService.getArticleById(rentLine.getArticle().getArticleid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        

        rentLine.setRent(rent);
        rentLine.setArticle(article);
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
        
        if (rent.getState() == RentState.CONFIRMED || 
            rent.getState() == RentState.CANCELLED || 
            rent.getState() == RentState.RETURNED) {
        throw new IllegalStateException("Cannot update rent lines to a rent in the " + rent.getState() + " state.");
    }

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

         if (rentLineDTO.getArticle() != null) {
            Optional<Article> article = articleService.getArticleById(rentLineDTO.getArticle().getArticleid());
            rentLine.setArticle(article.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found")));
        }

        if (rentLineDTO.getQuantity() != null) {
            rentLine.setQuantity(rentLineDTO.getQuantity());
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

            if (rent.getState() == RentState.CONFIRMED || 
                rent.getState() == RentState.SENT){
                throw new IllegalStateException("Cannot delete rent lines from a rent in the " + rent.getState() + " state.");
            }

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
