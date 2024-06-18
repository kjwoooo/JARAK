package io.elice.shoppingmall.product.Service;

import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.repository.MemberRepository;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.product.DTO.Item.ItemDTO;
import io.elice.shoppingmall.product.DTO.ReviewDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Review.Review;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import io.elice.shoppingmall.product.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    // 리뷰 생성
    @Transactional
    public ReviewDTO createReview(Long itemId, ReviewDTO reviewDTO) {
        // 세션 스토리지에서 가져온 사용자의 username
        // api 호출시 클라이언트가 세션트로리지에 사용자의 username을 저장한다고 함(?)- 확인
        // ReviewDTO에 저장된 username을 사용하여 회원을 조회

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM)); // 상품 조회

        Member member = memberService.findByUsername(reviewDTO.getUsername()); // 회원의 닉네임을 사용하여 회원 조회

        Review review = new Review();
        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        review.setImg(reviewDTO.getImg()); // (이미지 기능 미완성)
        review.setRate(reviewDTO.getRate()); // (평점 기능 미완성)
        review.setMember(member);
        review.setItem(item);

        Review savedReview = reviewRepository.save(review);

        return ReviewDTO.fromEntity(savedReview);
    }

    // 리뷰 조회(페이지네이션)
    //FIX
//    public Page<ReviewDTO> getReviews(Long itemId, int page, int size) {
//    Pageable pageable = PageRequest.of(page, size);
//    Page<Review> reviewPage = reviewRepository.findAllByItem_IdOrderByCreatedAtDesc(itemId, pageable);
//    return reviewList.map(ReviewDTO::fromEntity);
    public List<ReviewDTO> getReviews(Long itemId) {
        List<Review> reviews = reviewRepository.findAllByItem_IdOrderByCreatedAtDesc(itemId);
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        for(Review review : reviews){
            ReviewDTO reviewDTO = ReviewDTO.fromEntity(review);
            reviewDTOs.add(reviewDTO);
        }
        return reviewDTOs;
    }

    // 리뷰 수정
    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        review.update(reviewDTO.getTitle(), reviewDTO.getContent(), reviewDTO.getImg(), reviewDTO.getRate());

        return ReviewDTO.fromEntity(review);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        reviewRepository.deleteById(reviewId);
    }

}
