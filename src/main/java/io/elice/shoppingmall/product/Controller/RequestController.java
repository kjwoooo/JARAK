package io.elice.shoppingmall.product.Controller;

import io.elice.shoppingmall.product.DTO.RequestDTO;
import io.elice.shoppingmall.product.DTO.ReviewDTO;
import io.elice.shoppingmall.product.Service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/request/save")
    public ResponseEntity save(@RequestBody RequestDTO requestDTO){
        Integer saveResult = requestService.save(requestDTO);
        if(saveResult != null){
            List<RequestDTO> requestDTOList = requestService.findAll(requestDTO.getItemId());
            return new ResponseEntity<>(requestDTOList, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }
}
