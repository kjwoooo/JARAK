package io.elice.shoppingmall.option.controller;

import io.elice.shoppingmall.exception.ErrorResponse;
import io.elice.shoppingmall.option.service.BrandService;
import io.elice.shoppingmall.option.dto.BrandDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@Tag(name = "브랜드 관리", description = "브랜드 관련 API")
public class BrandController {

    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @Operation(summary = "모든 브랜드 조회", description = "모든 브랜드를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BrandDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        List<BrandDto> brands = brandService.getAllBrands();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @Operation(summary = "하나의 브랜드 조회", description = "특정 브랜드를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BrandDto.class))),
            @ApiResponse(responseCode = "404", description = "브랜드를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> getBrand(
            @Parameter(name = "id", description = "조회하려는 브랜드의 id", in = ParameterIn.PATH) @PathVariable Long id) {
        BrandDto brand = brandService.getBrand(id);
        return new ResponseEntity<>(brand, HttpStatus.OK);
    }

    @Operation(summary = "브랜드 생성", description = "새로운 브랜드를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = BrandDto.class))),
            @ApiResponse(responseCode = "400", description = "브랜드 생성 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BrandDto> createBrand(@RequestBody BrandDto brandDto) {
        BrandDto createdBrand = brandService.createBrand(brandDto);
        return new ResponseEntity<>(createdBrand, HttpStatus.CREATED);
    }

    @Operation(summary = "브랜드 수정", description = "기존의 브랜드를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BrandDto.class))),
            @ApiResponse(responseCode = "404", description = "브랜드를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<BrandDto> updateBrand(
            @Parameter(name = "id", description = "수정하려는 브랜드의 id", in = ParameterIn.PATH) @PathVariable Long id,
            @RequestBody BrandDto brandDto) {
        BrandDto updatedBrand = brandService.updateBrand(id, brandDto);
        return new ResponseEntity<>(updatedBrand, HttpStatus.OK);
    }

    @Operation(summary = "브랜드 삭제", description = "기존의 브랜드를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(
            @Parameter(name = "id", description = "삭제하려는 브랜드의 id", in = ParameterIn.PATH) @PathVariable Long id) {
        brandService.deleteBrand(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
