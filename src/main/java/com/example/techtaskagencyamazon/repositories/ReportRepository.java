package com.example.techtaskagencyamazon.repositories;

import com.example.techtaskagencyamazon.models.report.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for the report repository. It provides methods for working with reporting data in a MongoDB database.
 */
public interface ReportRepository extends MongoRepository<Report, String> {
    /**
     * Find reports by date of sales and traffic.
     *
     * @param date The date by which to find reports.
     * @return A list of reports corresponding to the specified date.
     */
    @Query(value = "{ 'salesAndTrafficByDate' : { $elemMatch: { 'date' : ?0 } } }",
            fields = "{ 'salesAndTrafficByDate.$' : 1 }")
    List<Report> findBySalesAndTrafficByDate_Date(LocalDate date);

    /**
     * Find reports by ASIN sales and traffic.
     *
     * @param asin ASIN by which to find reports.
     * @return A list of reports matching the specified ASIN.
     */
    @Query(value = "{ 'salesAndTrafficByAsin' : { $elemMatch: { 'parentAsin' : ?0 } } }",
            fields = "{ 'salesAndTrafficByAsin.$' : 1 }")
    List<Report> findBySalesAndTrafficByAsin_Asin(String asin);

    /**
     * Get all reports by date of sales and traffic.
     *
     * @return List all reports by sales and traffic date.
     */
    @Query(value = "{}", fields = "{ 'salesAndTrafficByDate' : 1 }")
    List<Report> findAllBySalesAndTrafficByDate();

    /**
     * Get all reports on ASIN sales and traffic.
     *
     * @return List all sales and traffic ASIN reports.
     */
    @Query(value = "{}", fields = "{ 'salesAndTrafficByAsin' : 1 }")
    List<Report> findAllBySalesAndTrafficByAsin();
}
