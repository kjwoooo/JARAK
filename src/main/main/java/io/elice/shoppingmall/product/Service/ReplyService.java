package io.elice.shoppingmall.product.Service;

import io.elice.shoppingmall.product.DTO.review.ReplyDTO;
import io.elice.shoppingmall.product.Entity.Review.Reply;
import io.elice.shoppingmall.product.Entity.Review.Request;
import io.elice.shoppingmall.product.Repository.ReplyRepository;
import io.elice.shoppingmall.product.Repository.RequestRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Service
@RequiredArgsConstructor
public class ReplyService {

    private final RequestRepository requestRepository;
    private final ReplyRepository replyRepository;

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

    //특정 문의에 모든 답글 출력
    public List<ReplyDTO> getAllRepliesByRequestId(Long requestId) {
        List<Reply> replies = replyRepository.findAllByRequestId(requestId);
        List<ReplyDTO> replyDTOs = new ArrayList<>();

        for(Reply reply : replies){
            ReplyDTO replyDTO = convertToDTO(reply);
            replyDTOs.add(replyDTO);
        }

        return replyDTOs;
    }

    //특정 답글 출력
    public ReplyDTO getReplyById(Long id) {
        Reply reply = replyRepository.findById(id).orElseThrow(() -> new RuntimeException("Reply not found"));
        return convertToDTO(reply);
    }

    public ReplyDTO createReply(ReplyDTO replyDTO) {
        Reply reply = convertToEntity(replyDTO);
        reply = replyRepository.save(reply);
        return convertToDTO(reply);
    }

    public ReplyDTO updateReply(Long id, ReplyDTO replyDTO) {
        Reply reply = replyRepository.findById(id).orElseThrow(() -> new RuntimeException("Reply not found"));
        reply.setContent(replyDTO.getContent());
        reply = replyRepository.save(reply);
        return convertToDTO(reply);
    }

    public void deleteReply(Long id) {
        replyRepository.deleteById(id);
    }
}
