package io.elice.shoppingmall.product.Service;

import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.repository.MemberRepository;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.product.DTO.Item.ItemDetailDTO;
import io.elice.shoppingmall.product.DTO.review.ReviewDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemDetail;
import io.elice.shoppingmall.product.Entity.Review.Review;
import io.elice.shoppingmall.product.Entity.Review.ReviewImage;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import io.elice.shoppingmall.product.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ItemRepository itemRepository;


    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO, String username, Long itemId) {
//    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        review.setRate(reviewDTO.getRate());
        review.setUsername(username);

        Member member = memberService.findByUsername(username);
        review.setMember(member);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        review.setItem(item);

        Review savedReview = reviewRepository.save(review);
        ReviewDTO savedReivewDTO = convertToDTO(review);
        savedReivewDTO.setUsername(username);
        return savedReivewDTO;
    }

    @Transactional
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO, String username) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        review.setRate(reviewDTO.getRate());
        review.setUsername(username);

        Item item = itemRepository.findById(reviewDTO.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));
        review.setItem(item);

        Member member = memberService.findByUsername(username);
        review.setMember(member);

        Review updatedReview = reviewRepository.save(review);
        return convertToDTO(updatedReview);
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
        reviewDTO.setTitle(review.getTitle());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setRate(review.getRate());
        reviewDTO.setItemId(review.getItem().getId());
        reviewDTO.setUsername(review.getMember().getUsername());
        return reviewDTO;
    }
}
