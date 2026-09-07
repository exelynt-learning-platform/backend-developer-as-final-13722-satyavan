package com.resourcebookingsystem.repository;

import com.resourcebookingsystem.model.Reservation;
import com.resourcebookingsystem.model.ReservationStatus;
import com.resourcebookingsystem.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {
    static Specification<Reservation> filterBy(User user, ReservationStatus status, BigDecimal minPrice,BigDecimal maxPrice){
        return (root,query,cb)->{
            List<Predicate> predicates=new ArrayList<>();
            if(user !=null){
                predicates.add(cb.equal(root.get("user"),user));
            }
            if(status !=null){
                predicates.add(cb.equal(root.get("status"),status));
            }
            if(minPrice !=null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"),minPrice));
            }
            if(maxPrice !=null){
                predicates.add(cb.lessThanOrEqualTo(root.get("price"),maxPrice));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
