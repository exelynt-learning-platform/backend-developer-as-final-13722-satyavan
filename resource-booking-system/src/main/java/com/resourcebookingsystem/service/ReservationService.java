package com.resourcebookingsystem.service;

import com.resourcebookingsystem.dto.CreateReservationRequest;
import com.resourcebookingsystem.dto.ReservationResponse;
import com.resourcebookingsystem.model.*;
import com.resourcebookingsystem.repository.ReservationRepository;
import com.resourcebookingsystem.repository.ResourceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ResourceRepository resourceRepository;

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request, User user){
        if(request.getEndTime().isBefore(request.getStartTime())){
            throw new IllegalArgumentException("End time must be after start time");
        }

        Resource resource=resourceRepository.findById(request.getResourceId())
                .orElseThrow(()->new EntityNotFoundException("Resource not found"));
        Reservation reservation=Reservation.builder()
                .user(user)
                .resource(resource)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(ReservationStatus.PENDING)
                .price(resource.getBasePrice())
                .build();
        return mapToResponse(reservationRepository.save(reservation));
    }

    public Page<ReservationResponse> getReservations(User user, ReservationStatus status,
                                                     BigDecimal minPrice, BigDecimal maxPrice,
                                                     Pageable pageable){
        // Enforce USER can only view own; ADMIN can view all
        User targetUser=(user.getRole() == Role.ROLE_ADMIN) ? null: user;
        return reservationRepository.findAll(
                ReservationRepository.filterBy(targetUser,status,minPrice,maxPrice),
                pageable
        ).map(this::mapToResponse);
    }

    @Transactional
    public ReservationResponse updateStatus(Long reservationId, ReservationStatus newStatus, User currentUser){
        Reservation reservation=reservationRepository.findById(reservationId)
                .orElseThrow(()->new EntityNotFoundException("Reservation not found"));
        // User can only cancel their own reservation; Admin can transition to any status
        if(currentUser.getRole() != Role.ROLE_ADMIN){
            if(!reservation.getUser().getId().equals(currentUser.getId())){
                throw new AccessDeniedException("You are not authorized to modify this reservation");
            }
            if (newStatus != ReservationStatus.CANCELLED) {
                throw new IllegalArgumentException("Users can only cancel their reservations");
            }
        }
        reservation.setStatus(newStatus);
        return mapToResponse(reservationRepository.save(reservation));
    }

    private ReservationResponse mapToResponse(Reservation r){
        return ReservationResponse.builder()
                .id(r.getId())
                .userId(r.getUser().getId())
                .userEmail(r.getUser().getEmail())
                .resourceId(r.getResource().getId())
                .resourceName(r.getResource().getName())
                .startTime(r.getStartTime())
                .endTime(r.getEndTime())
                .status(r.getStatus())
                .price(r.getPrice())
                .build();
    }
}
