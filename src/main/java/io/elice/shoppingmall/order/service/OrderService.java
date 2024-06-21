package io.elice.shoppingmall.order.service;

import io.elice.shoppingmall.cart.service.CartItemService;
import io.elice.shoppingmall.cart.service.CartService;
import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.dto.OrderRequestDTO;
import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.entity.OrderDetail;
import io.elice.shoppingmall.order.entity.OrderState;
import io.elice.shoppingmall.order.mapper.OrderDetailMapper;
import io.elice.shoppingmall.order.mapper.OrderMapper;
import io.elice.shoppingmall.order.repository.OrderRepository;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemImage;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import io.elice.shoppingmall.product.Service.Item.ItemService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private static final OrderMapper orderMapper = OrderMapper.INSTANCE;
    private static final OrderDetailMapper orderDetailMapper = OrderDetailMapper.INSTANCE;

    @Autowired
    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository, ItemService itemService,
                        CartService cartService, CartItemService cartItemService) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.itemService = itemService;
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    // 주문 조회 (페이징 적용)
    public Page<OrderDTO> getOrdersByMemberId(Long memberId, int pageNumber, int pageSize) {
        validatePagingParameters(pageNumber, pageSize);

        // 페이지 요청 시 id를 기준으로 내림차순 정렬하여 최신 주문이 먼저 나오도록 설정
        Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());

        Page<Order> pagedOrders = orderRepository.findByMemberIdOrderByIdDesc(memberId, pageableRequest);

        validatePagedOrders(pagedOrders);

        return pagedOrders.map(orderMapper::orderToOrderDTO);
    }

    // 주문 상세 조회
    public OrderDTO getOrderDetailsByOrderId(Long orderId, Long memberId) {
        Order order = orderRepository.findByIdAndMemberId(orderId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        List<OrderDetail> orderDetails = order.getOrderDetails();
        if (orderDetails.isEmpty()) {
            throw new CustomException(ErrorCode.NO_ORDER_DETAILS_FOUND);
        }

        OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
        List<OrderDetailDTO> orderDetailDTOs = orderDetailMapper.orderDetailsToOrderDetailDTOs(orderDetails);
        orderDTO.setOrderDetails(orderDetailDTOs);
        return orderDTO;
    }

    // 주문 조회 (단일)
    public Optional<OrderDTO> getOrderById(Long orderId, Long memberId) {
        Order order = orderRepository.findByIdAndMemberId(orderId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        return Optional.of(orderMapper.orderToOrderDTO(order));
    }

    // 주문 생성
    @Transactional
    public OrderDTO createOrder(Member member, @Valid OrderRequestDTO orderRequestDTO) {
        OrderDTO orderDTO = orderRequestDTO.getOrderDTO();
        List<OrderDetailDTO> orderDetailDTOs = orderRequestDTO.getOrderDetailDTOs();

        Order order = orderDTO.toEntity();
        order.setMember(member);
        order.setOrderState(OrderState.PENDING);

        List<OrderDetail> orderDetails = convertToOrderDetails(order, orderDetailDTOs);
        return saveAndReturnOrder(order, orderDetails);
    }

    // 주문 수정 페이지 호출
    public OrderDTO getUpdateOrderPage(Long orderId, @Valid Long memberId) {
        Order order = orderRepository.findByIdAndMemberId(orderId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));
        return orderMapper.orderToOrderDTO(order);
    }

    // 주문 수정(배송 정보 수정)
    @Transactional
    public OrderDTO updateOrder(Member member, Long orderId, OrderDTO orderDTO) {
        Order order = orderRepository.findByIdAndMemberId(orderId, member.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));
        
        // 주문 부분 취소 기능 구현시 수정 및 사용할 조건문
        // 주문 상태가 CANCELLED인 경우 수정 불가

        if (order.getOrderState() == OrderState.CANCELLED) {
            throw new CustomException(ErrorCode.CANNOT_MODIFY_CANCELLED_ORDER);
        }

        // 주문 상태가 PENDING 또는 CONFIRMED일 경우에만 수정 가능
        if (order.getOrderState() != OrderState.PENDING && order.getOrderState() != OrderState.CONFIRMED) {
            throw new CustomException(ErrorCode.CANNOT_MODIFY_ORDER_STATE);
        }

        // 주문 정보 업데이트 (배송 정보만 수정)
        order.setShippingCost(orderDTO.getShippingCost());
        order.setRecipientName(orderDTO.getRecipientName());
        order.setZipcode(orderDTO.getZipcode());
        order.setAddr(orderDTO.getAddr());
        order.setAddrDetail(orderDTO.getAddrDetail());
        order.setRecipientTel(orderDTO.getRecipientTel());
        order.setAddrName(orderDTO.getAddrName());
        order.setDeliveryReq(orderDTO.getDeliveryReq());

        // 주문 저장 및 DTO 반환
        return saveAndReturnOrder(order, order.getOrderDetails());
    }

    // 주문 취소(환불)
    @Transactional
    public void cancelOrder(Member member, Long orderId, String refundReason) {
        Order order = orderRepository.findByIdAndMemberId(orderId, member.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        order.setOrderState(OrderState.CANCELLED);

        if (refundReason != null) {
            order.setRefundReason(refundReason);
        } else {
            order.setRefundReason("취소 사유가 존재하지 않습니다.");  // 기본 환불 사유 설정
        }

        // 모든 OrderDetail의 상태를 CANCELLED로 설정
        for (OrderDetail detail : order.getOrderDetails()) {
            detail.setOrderState(OrderState.CANCELLED);
        }

        orderRepository.save(order);
    }

    // 관리자 전체 주문 수 조회
    public long getTotalOrderCount() {
        return orderRepository.count();
    }

    // 관리자 모든 주문 조회 (페이징 적용 및 검색)
    public Page<OrderDTO> getAllOrders(int pageNumber, int pageSize, String username) {
        validatePagingParameters(pageNumber, pageSize);

        // 페이지 요청 시 id를 기준으로 내림차순 정렬하여 최신 주문이 먼저 나오도록 설정
        Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());

        Page<Order> pagedOrders;
        if (username != null && !username.isEmpty()) {
            pagedOrders = orderRepository.findByMemberUsernameContaining(username, pageableRequest);
        } else {
            pagedOrders = orderRepository.findAll(pageableRequest);
        }

        validatePagedOrders(pagedOrders);
        return pagedOrders.map(orderMapper::orderToOrderDTO);
    }

    // 관리자 주문 상태 수정
    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, OrderState orderState) {
        if (orderState == null) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATE);
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        order.setOrderState(orderState);

        // 모든 OrderDetail의 상태를 Order의 orderState의 값으로 설정
        for (OrderDetail detail : order.getOrderDetails()) {
            detail.setOrderState(orderState);
        }
        
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderDTO(updatedOrder);
    }

    // 관리자 주문 삭제
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));
        orderRepository.delete(order);
    }

    // OrderDetailDTO -> OrderDetail 변환
    private List<OrderDetail> convertToOrderDetails(Order order, List<OrderDetailDTO> orderDetailDTOs) {
        return orderDetailDTOs.stream()
                .map(detailDTO -> {
                    Item item = itemRepository.findById(detailDTO.getItemId())
                            .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

                    OrderDetail orderDetail = detailDTO.toEntity(order, item);
                    orderDetail.setOrderState(order.getOrderState()); // Order의 orderState 값을 설정

                    itemService.reduceQuantity(item.getId(), detailDTO.getQuantity()); // 상품의 수량 감소
                    return orderDetail;
                })
                .toList();
    }

    // 주문 저장 및 DTO 반환
    private OrderDTO saveAndReturnOrder(Order order, List<OrderDetail> orderDetails) {
        order.setOrderDetails(orderDetails);
        setOrderSummary(order, orderDetails);
        order.setPrice(order.getPrice() + order.getShippingCost());

        Order savedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderDTO(savedOrder);
    }

    // 주문 요약 필드 설정 (가격, 총 개수, 대표 상품 이름, 대표 상품 이미지)
    private void setOrderSummary(Order order, List<OrderDetail> orderDetails) {
        int itemTotalPrice = orderDetails.stream().mapToInt(detail -> detail.getPrice() * detail.getQuantity()).sum();
        int totalQuantity = orderDetails.stream().mapToInt(OrderDetail::getQuantity).sum();
        String repItemName = orderDetails.isEmpty() ? "No Item" : orderDetails.get(0).getItem().getItemName();

        String repItemImage = orderDetails.isEmpty() ? "No Image" : orderDetails.get(0).getItem().getItemImages().stream()
                .findFirst()
                .map(ItemImage::getFilePath)
                .orElse("No Image");

        order.setTotalQuantity(totalQuantity);
        order.setRepItemName(repItemName);
        order.setRepItemImage(repItemImage);
        order.setPrice(itemTotalPrice);
    }

    // 예외 처리 헬퍼 메서드
    private void validatePagingParameters(int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new CustomException(ErrorCode.INVALID_PAGING_PARAMETERS);
        }
    }

    private void validatePagedOrders(Page<Order> pagedOrders) {
        if (pagedOrders.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_ORDER);
        }
    }
}
