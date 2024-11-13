package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.SizeModel;
import shop.biday.model.entity.SizeEntity;
import shop.biday.service.SizeService;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sizes")
@Tag(name = "sizes", description = "Size Controller")
public class SizeController {
    private final SizeService sizeService;

    @GetMapping
    @Operation(summary = "사이즈 목록", description = "상품 등록하거나, 상세 페이지에서 사이즈 목록 띄울 때 사용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사이즈 목록 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "사이즈 찾을 수 없음")
    })
    public ResponseEntity<List<SizeEntity>> findAll() {
        return ResponseEntity.ok(sizeService.findAll());
    }

    @GetMapping("/findByProduct")
    @Operation(summary = "상품 아이디 기준 사이즈 목록", description = "상품 등록하거나, 상세 페이지에서 사이즈 목록 띄울 때 사용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사이즈 목록 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "사이즈 찾을 수 없음")
    })
    @Parameter(name = "id", description = "상품 id", example = "1")
    public ResponseEntity<List<SizeModel>> findAllByProductId(@RequestParam("productId") Long productId) {
        return ResponseEntity.ok(sizeService.findAllByProductId(productId));
    }

    @PostMapping
    @Operation(summary = "사이즈 등록", description = "사이즈 새로 등록하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사이즈 등록 성공"),
            @ApiResponse(responseCode = "404", description = "사이즈 등록 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleSizeModel", value = """ 
                        { 
                            "product" : "브랜드 이름",
                            "name" : "사이즈 이름(enum : XS~XXL)", 
                            "createdAt" : "등록 시간"
                        } 
                    """)})
    })
    public ResponseEntity<SizeEntity> create(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody SizeModel size) {
        return ResponseEntity.ok(sizeService.save(userInfoHeader, size));
    }

    @PatchMapping
    @Operation(summary = "사이즈 수정", description = "사이즈 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사이즈 수정 성공"),
            @ApiResponse(responseCode = "404", description = "사이즈 수정 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleSizeModel", value = """ 
                        { 
                            "product" : "브랜드 이름",
                            "name" : "사이즈 이름(enum : XS~XXL)", 
                            "createdAt" : "등록 시간"
                        } 
                    """)})
    })
    public ResponseEntity<SizeEntity> update(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody SizeModel size) {
        return ResponseEntity.ok(sizeService.update(userInfoHeader, size));
    }

    @DeleteMapping
    @Operation(summary = "사이즈 삭제", description = "사이즈 삭제하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사이즈 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "사이즈 삭제 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(name = "sizeId", description = "사이즈 id", example = "1")
    })
    public ResponseEntity<String> delete(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam Long id) {
        return ResponseEntity.ok(sizeService.deleteById(userInfoHeader, id));
    }
}