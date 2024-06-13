package io.elice.shoppingmall.product.Controller.Item;

import io.elice.shoppingmall.product.DTO.Item.ItemDTO;
import io.elice.shoppingmall.product.DTO.Item.ItemDetailDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemDetail;
import io.elice.shoppingmall.product.Service.Item.ItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    //XXX: 수정 필요. Entity를 컨테이너에서 찾으려 해서 에러
//    private final ItemDetail itemDetail;
    //----------------------------------------

//    private final ReviewService reviewService;
//    private final RequestService requestService;

    //상품 등록
    @PostMapping("/upload")
    public ResponseEntity<ItemDTO> save(@ModelAttribute ItemDTO itemDTO) throws IOException {
        itemService.save(itemDTO);
        return new ResponseEntity(itemDTO, HttpStatus.OK);
    }

    //상품 사이즈, 색상, 수량 등록
    @PostMapping("/{itemId}/details")
    public ResponseEntity<ItemDetailDTO> saveItemDetail(@PathVariable Long itemId, @RequestBody ItemDetailDTO itemDetailDTO){
        //XXX: 수정 필요.
//        itemDetailDTO.setItemId(itemId);
//        itemService.saveItemDetail(itemDetail);
        return new ResponseEntity(itemDetailDTO, HttpStatus.OK);
    }

    //상품 상세 페이지 조회
    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id, Model model){
        ItemDTO itemdto = itemService.findById(id);
//        List<ReviewDTO> reviewDTOList = reviewService.findAll(id);
//        List<RequestDTO> requestDTOList = requestService.findAll(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    //상품 수정
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody ItemDTO itemDTO){
        itemService.update(itemDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    //상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        itemService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
