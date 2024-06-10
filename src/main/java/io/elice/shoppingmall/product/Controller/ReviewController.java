package io.elice.shoppingmall.product.Controller;

import com.sun.net.httpserver.HttpsServer;
import io.elice.shoppingmall.product.DTO.ReviewDTO;
import io.elice.shoppingmall.product.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    //댓글 등록
    @PostMapping("/comment/save")
    public ResponseEntity save(@RequestBody ReviewDTO reviewDTO){
        Long saveResult = reviewService.save(reviewDTO);
        if(saveResult != null){
            List<ReviewDTO> reviewDTOList = reviewService.findAll(reviewDTO.getItemId());
            return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }
}
