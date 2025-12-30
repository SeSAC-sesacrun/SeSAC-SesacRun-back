package com.example.sesacrunback.domain.cart.controller;

import com.example.sesacrunback.domain.cart.dto.request.CartCreateRequest;
import com.example.sesacrunback.domain.cart.dto.response.CartResponse;
import com.example.sesacrunback.domain.cart.service.CartService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<CartResponse>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CartCreateRequest request
    ) {
        CartResponse response = cartService.create(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CartResponse>>> findAll(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<CartResponse> carts = cartService.findAll(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(carts));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(
            @PathVariable("itemId") Long itemId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        cartService.delete(itemId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }

}
