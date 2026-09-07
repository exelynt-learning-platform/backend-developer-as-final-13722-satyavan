package com.resourcebookingsystem.controller;

import com.resourcebookingsystem.dto.CreateReservationRequest;
import com.resourcebookingsystem.dto.ReservationResponse;
import com.resourcebookingsystem.model.ReservationStatus;
import com.resourcebookingsystem.model.User;
import com.resourcebookingsystem.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody CreateReservationRequest request,
                                        @AuthenticationPrincipal User currentUser){
        return new ResponseEntity<>(reservationService.createReservation(request,currentUser), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ReservationResponse>> getAll(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false)ReservationStatus status,
            @RequestParam(required = false)BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(page = 0, size = 10,sort = "startTime",direction = Sort.Direction.DESC)Pageable pageable
            ){
        return ResponseEntity.ok(reservationService.getReservations(currentUser,status,minPrice,maxPrice,pageable));

    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ReservationResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam ReservationStatus status,
            @AuthenticationPrincipal User currentUser
    ){
        return ResponseEntity.ok(reservationService.updateStatus(id,status,currentUser));
    }

}
