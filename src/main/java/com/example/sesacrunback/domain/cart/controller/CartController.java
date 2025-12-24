package com.example.sesacrunback.domain.cart.controller;

import com.example.sesacrunback.domain.cart.dto.request.CartCreateRequest;
import com.example.sesacrunback.domain.cart.dto.response.CartResponse;
import com.example.sesacrunback.domain.cart.service.CartService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
//import com.example.sesacrunback.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor

public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<CartResponse>> create(
            @RequestHeader("X-USER-ID") Long userId, // @AuthenticationPrincipal 대신 @RequestHeader 사용   // TODO : user 생성 후 변경
            //@AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CartCreateRequest request
    )
    {
//        if (userDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        CartResponse response = cartService.create(userDetails.getId(), request);
        CartResponse response = cartService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CartResponse>>> findAll(
            @RequestHeader("X-USER-ID") Long userId // @AuthenticationPrincipal 대신 @RequestHeader 사용   // TODO : user 생성 후 변경
            //@AuthenticationPrincipal CustomUserDetails userDetails
    ) {
//        if (userDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        List<CartResponse> carts = cartService.findAll(userDetails.getId());
        List<CartResponse> carts = cartService.findAll(userId);
        return ResponseEntity.ok(ApiResponse.success(carts));
    }

    @DeleteMapping("/{itemId}") // 요청 경로 변경
    public ResponseEntity<Void> delete(
            @PathVariable("itemId") Long itemId,
            @RequestHeader("X-USER-ID") Long userId // @AuthenticationPrincipal 대신 @RequestHeader 사용   // TODO : user 생성 후 변경
            //@AuthenticationPrincipal CustomUserDetails userDetails

    ) {
//        if (userDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        cartService.delete(itemId, userDetails.getId());
        cartService.delete(itemId, userId);
        return ResponseEntity.noContent().build();
    }

}
