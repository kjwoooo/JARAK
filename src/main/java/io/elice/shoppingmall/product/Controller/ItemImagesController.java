package io.elice.shoppingmall.product.Controller;

import io.elice.shoppingmall.product.DTO.ItemDTO;
import io.elice.shoppingmall.product.DTO.ItemImagesDTO;
import io.elice.shoppingmall.product.Entity.Item.ItemImages;
import io.elice.shoppingmall.product.Service.ItemImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ItemImages")
public class ItemImagesController {

    private final ItemImagesService itemImagesService;


    @PostMapping("/upload")
    public ResponseEntity<ItemImagesDTO> save(@ModelAttribute ItemImagesDTO itemImagesDTO){
        itemImagesService.save(itemImagesDTO);
        return new ResponseEntity(itemImagesDTO, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        itemImagesService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
