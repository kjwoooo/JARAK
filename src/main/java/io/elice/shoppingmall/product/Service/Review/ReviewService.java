package io.elice.shoppingmall.product.Service.Review;


import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.product.DTO.Review.ReviewDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Review.Review;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import io.elice.shoppingmall.product.Repository.Review.ReviewRepository;
import io.elice.shoppingmall.product.S3.S3Uploader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private S3Uploader s3Uploader;


    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO, String username, Long itemId, MultipartFile imageFile) throws IOException {
        if (imageFile != null) {
            validateImageFile(imageFile);
            List<String> imageFileInfo = s3Uploader.uploadFiles(imageFile, "itemImageDir");
            reviewDTO.setFilePath(imageFileInfo.get(1));
        }

        Review review = new Review();
        review.setContent(reviewDTO.getContent());
        review.setRate(reviewDTO.getRate());
        review.setUsername(username);

        Member member = memberService.findByUsername(username);
        review.setMember(member);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
        review.setItem(item);
        review.setFilePath(reviewDTO.getFilePath());
        Review savedReview = reviewRepository.save(review);
        ReviewDTO savedReivewDTO = convertToDTO(review);
        savedReivewDTO.setUsername(username);
        return savedReivewDTO;
    }

    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO, String username, MultipartFile imageFile) throws IOException {
        // 1. 기존 리뷰 정보 가져오기
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        // 2. 이미지 파일 업로드 및 filePath 설정
        if (imageFile != null) {
            validateImageFile(imageFile);
            List<String> imageFileInfo = s3Uploader.uploadFiles(imageFile, "itemImageDir");
            reviewDTO.setFilePath(imageFileInfo.get(1));
        }

        // 3. 리뷰 정보 업데이트
        existingReview.setContent(reviewDTO.getContent());
        existingReview.setRate(reviewDTO.getRate());

        // 4. 리뷰의 사용자 정보 업데이트 (옵셔널하게 사용)
        // Member member = memberService.findByUsername(username);
        // existingReview.setMember(member);

        // 5. 리뷰의 파일 경로 업데이트
        existingReview.setFilePath(reviewDTO.getFilePath());

        // 6. 리뷰 저장 및 DTO로 변환하여 반환
        Review savedReview = reviewRepository.save(existingReview);
        return convertToDTO(savedReview);
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public List<ReviewDTO> getAllReviews(Long itemId) {
        List<Review> reviews = reviewRepository.findAllByItemIdOrderByCreatedAtDesc(itemId);
        List<ReviewDTO> reviewDTOs = new ArrayList<>();

        for(Review review : reviews){
            ReviewDTO reviewDTO = convertToDTO(review);
            reviewDTOs.add(reviewDTO);
        }

        return reviewDTOs;
    }

    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        return convertToDTO(review);
    }

    public List<ReviewDTO> getMyReviews(String username){
        Member member = memberService.findByUsername(username);
        List<Review> reviews = reviewRepository.findAllByUsername(member.getUsername());
        List<ReviewDTO> reviewDTOs = new ArrayList<>();

        for(Review review : reviews){
            ReviewDTO reviewDTO = convertToDTO(review);
            reviewDTOs.add(reviewDTO);
        }
        return reviewDTOs;
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setRate(review.getRate());
        reviewDTO.setItemId(review.getItem().getId());
        reviewDTO.setUsername(review.getMember().getUsername());
        reviewDTO.setFilePath(review.getFilePath());
        reviewDTO.setCreatedAt(review.getCreatedAt());
        return reviewDTO;
    }

    private void validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new CustomException(ErrorCode.INVALID_IMAGE_FILE);
        }
    }

    public Double getAverageRateByItemId(Long itemId) {
        return reviewRepository.findAverageRateByItemId(itemId);
    }
}
