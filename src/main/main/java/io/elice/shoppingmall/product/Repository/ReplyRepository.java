package io.elice.shoppingmall.product.Repository;

import io.elice.shoppingmall.product.Entity.Review.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByRequestId(Long requestId);
}
