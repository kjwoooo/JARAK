package io.elice.shoppingmall.product.Controller;

import io.elice.shoppingmall.product.DTO.ItemDTO;
import io.elice.shoppingmall.product.DTO.RequestDTO;
import io.elice.shoppingmall.product.DTO.ReviewDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Review.Review;
import io.elice.shoppingmall.product.Service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
//    private final ReviewService reviewService;
//    private final RequestService requestService;

//    //상품 등록 화면
//    @GetMapping("/upload")
//    public String saveForm(){return "save"}

    //상품 등록
    @PostMapping("/upload")
    public ResponseEntity<ItemDTO> save(@ModelAttribute ItemDTO itemDTO){
        itemService.save(itemDTO);
        return new ResponseEntity(itemDTO, HttpStatus.OK);
    }

    //상품 상세 페이지 조회
    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Integer id, Model model){
        ItemDTO itemdto = itemService.findById(id);
//        List<ReviewDTO> reviewDTOList = reviewService.findAll(id);
//        List<RequestDTO> requestDTOList = requestService.findAll(id);
        return new ResponseEntity(HttpStatus.OK);
    }

//    //상품 수정 페이지
//    @GetMapping("/{id}/modify")
//    public String updateForm(@PathVariable Integer id, Model model){
//        ItemDTO itemDTO = itemService.findById(id);
//        model.addAttribute("itemModify", itemDTO);
//        return "modify";
//    }

    //상품 수정
    @PutMapping("/{id}/modify")
    public ResponseEntity update(@RequestBody ItemDTO itemDTO){
        itemService.update(itemDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    //상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        itemService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
