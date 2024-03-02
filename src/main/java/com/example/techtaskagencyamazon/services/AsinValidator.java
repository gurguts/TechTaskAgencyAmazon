package com.example.techtaskagencyamazon.services;

import com.example.techtaskagencyamazon.exception.ReportException;
import com.example.techtaskagencyamazon.interfaces.AsinValidatorInterface;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ASIN validation service
 */
@Service
public class AsinValidator implements AsinValidatorInterface {
    /**
     * Length ASIN.
     */
    private final byte ASIN_LENGTH = 10;

    /**
     * Method for ASIN validation.
     *
     * @param asin The ASIN to validate.
     * @throws ReportException if ASIN is invalid.
     */
    public void validate(String asin) {
        if (asin.length() != ASIN_LENGTH) {
            throw new ReportException("Incorrect asin: " + asin);
        }
    }

    /**
     * Method for validating the ASIN list.
     *
     * @param asinList List of ASINs to validate.
     * @throws ReportException if any ASIN in the list is invalid.
     */
    public void validate(List<String> asinList) {
        for (String asin : asinList) {
            if (asin.length() != ASIN_LENGTH) {
                throw new ReportException("Incorrect asin: " + asin);
            }
        }
    }
}
