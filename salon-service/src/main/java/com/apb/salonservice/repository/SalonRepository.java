package com.apb.salonservice.repository;

import com.apb.salonservice.model.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalonRepository extends JpaRepository<Salon, Long> {
    Salon findByOwnerId(Long ownerId);

    @Query(
            "SELECT s FROM Salon s WHERE"
                    + "(LOWER(s.city) like LOWER(CONCAT('%', :keyword, '%')) OR "
                    + "LOWER(s.name) like LOWER(CONCAT('%', :keyword, '%')) OR "
                    + "LOWER(s.address) like LOWER(CONCAT('%', :keyword, '%')))"
    )
    List<Salon> searchSalons(@Param("keyword") String keyword);
}
