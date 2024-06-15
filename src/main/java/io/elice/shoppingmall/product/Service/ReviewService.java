package io.elice.shoppingmall.product.Service;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.repository.MemberRepository;
import io.elice.shoppingmall.member.service.MemberService;
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
//    Long memeberId = 1L;

    //댓글 생성
//    public Long save(ReviewDTO reviewDTO){
//        Optional<Item> optionalItem = itemRepository.findById(reviewDTO.getId());
//        if(optionalItem.isPresent()){
//            Item item = optionalItem.get();
//            Review review = Review.toSaveEntity(reviewDTO, item);
//            return reviewRepository.save(review).getId();
//        }
//        else{
//            return null;
//        }
//    }
//
//    //전체 댓글 조회
//    public List<ReviewDTO> findAll(Long itemId){
//        Item item = itemRepository.findById(itemId).get();
//        List<Review> reviewList = reviewRepository.findAllByItemOrderByIdAsc(item);
//        List<ReviewDTO> reviewDTOList = new ArrayList<>();
//        for(Review review : reviewList){
//            ReviewDTO reviewDTO = ReviewDTO.toReviewDTO(review, memeberId, itemId);
//            reviewDTOList.add(reviewDTO);
//        }
//        return reviewDTOList;
//    }

    // 리뷰 생성
    @Transactional
    public ReviewDTO createReview(Long itemId, ReviewDTO reviewDTO) {
        String username = reviewDTO.getUsername(); // 세션 스토리지에서 가져온 사용자의 username
        // api 호출시 클라이언트가 세션트로리지에 사용자의 username을 저장한다고 함(?)- 확인
        // ReviewDTO에 저장된 username을 사용하여 회원을 조회

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다.")); // 상품 조회

        Member member = memberService.findByUsername(username); // 회원의 닉네임을 사용하여 회원 조회

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
    public Page<ReviewDTO> getReviews(Long itemId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviewPage = reviewRepository.findAllByItem_IdOrderByCreatedAtDesc(itemId, pageable);

        return reviewPage.map(ReviewDTO::fromEntity);
    }

    // 리뷰 수정
    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 리뷰입니다."));

        review.update(reviewDTO.getTitle(), reviewDTO.getContent(), reviewDTO.getImg(), reviewDTO.getRate());

        return ReviewDTO.fromEntity(review);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 리뷰입니다."));

        reviewRepository.deleteById(reviewId);
    }

}
