package io.elice.shoppingmall.product.Controller;

import io.elice.shoppingmall.product.DTO.review.RequestDTO;
import io.elice.shoppingmall.product.Entity.Review.Request;
import io.elice.shoppingmall.product.Service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {
    @Autowired
    private RequestService requestService;

    @GetMapping("/{itemId}")
    public List<RequestDTO> getRequestsByItemId(Long itemId) {
        return requestService.getRequestByItemId(itemId);
    }

    @GetMapping("/{itemId}/{id}")
    public RequestDTO getRequestById(@PathVariable Long id) {
        return requestService.getRequestById(id);
    }

    @PostMapping("/{itemId}")
    public RequestDTO createRequest(@PathVariable Long itemId, @RequestBody RequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        return requestService.createRequest(itemId, requestDTO, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    public RequestDTO updateRequest(@PathVariable Long id, @RequestBody RequestDTO requestDTO) {
        return requestService.updateRequest(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long id) {
        requestService.deleteInquiry(id);
        return ResponseEntity.noContent().build();
    }
}