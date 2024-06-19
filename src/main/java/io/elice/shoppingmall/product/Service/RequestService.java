package io.elice.shoppingmall.product.Service;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.product.DTO.Item.ItemDetailDTO;
import io.elice.shoppingmall.product.DTO.review.ReplyDTO;
import io.elice.shoppingmall.product.DTO.review.RequestDTO;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemDetail;
import io.elice.shoppingmall.product.Entity.Review.Reply;
import io.elice.shoppingmall.product.Entity.Review.Request;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import io.elice.shoppingmall.product.Repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final MemberService memberService;
    private ReplyDTO convertToDTO(Reply reply) {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setId(reply.getId());
        replyDTO.setContent(reply.getContent());
        replyDTO.setRequestId(reply.getRequest().getId());
        return replyDTO;
    }

    private Reply convertToEntity(ReplyDTO replyDTO) {
        Reply reply = new Reply();
        reply.setId(replyDTO.getId());
        reply.setContent(replyDTO.getContent());
        Request request = requestRepository.findById(replyDTO.getRequestId())
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
        reply.setRequest(request);
        return reply;
    }

    private Request convertToEntity(RequestDTO requestDTO) {
        Request request = new Request();
        request.setId(requestDTO.getId());
        request.setContent(requestDTO.getContent());
        request.setUsername(requestDTO.getUsername());

        Item item = itemRepository.findById(requestDTO.getItemId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        request.setItem(item);

        Member member = memberService.findByUsername(request.getUsername());
        request.setMember(member);

        List<Reply> replies = requestDTO.getReplies().stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        request.setReplies(replies);
        return request;
    }

    private RequestDTO convertToDTO(Request request) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setId(request.getId());
        requestDTO.setContent(request.getContent());
        requestDTO.setUsername(request.getUsername());
        requestDTO.setItemId(request.getItem().getId());

        List<ReplyDTO> replyDTOs = request.getReplies().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        requestDTO.setReplies(replyDTOs);
        return requestDTO;
    }

    //특정 문의 조희
    public RequestDTO getRequestById(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
        return convertToDTO(request);
    }

    //특정 상품 문의 조희
    public List<RequestDTO> getRequestByItemId(Long itemId){
        List<Request> requests = requestRepository.findAllByItemId(itemId);
        List<RequestDTO> requestDTOs = new ArrayList<>();

        for (Request request : requests){
            RequestDTO requestDTO = convertToDTO(request);
            requestDTOs.add(requestDTO);
        }

        return requestDTOs;
    }

    public RequestDTO createRequest(Long itemId, RequestDTO requestDTO, String username) {
        Request request = convertToEntity(requestDTO);

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Request not found"));;
        request.setItem(item);

        request = requestRepository.save(request);
        return convertToDTO(request);
    }

    public RequestDTO updateRequest(Long id, RequestDTO requestDTO) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Inquiry not found"));
        request.setContent(requestDTO.getContent());
        request = requestRepository.save(request);
        return convertToDTO(request);
    }

    public void deleteInquiry(Long id) {
        requestRepository.deleteById(id);
    }
}
