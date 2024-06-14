package io.elice.shoppingmall.order.service;

import io.elice.shoppingmall.address.entity.Address;
import io.elice.shoppingmall.address.entity.AddressDTO;
import io.elice.shoppingmall.address.service.AddressService;
import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import io.elice.shoppingmall.cart.domain.cartItems.Entity.CartItems;
import io.elice.shoppingmall.cart.service.CartItemService;
import io.elice.shoppingmall.cart.service.CartService;
import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.entity.OrderDetail;
import io.elice.shoppingmall.order.entity.OrderState;
import io.elice.shoppingmall.order.mapper.OrderDetailMapper;
import io.elice.shoppingmall.order.mapper.OrderMapper;
import io.elice.shoppingmall.order.repository.OrderRepository;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemImages;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final AddressService addressService;
    private final MemberService memberService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private static final OrderMapper orderMapper = OrderMapper.INSTANCE;
    private static final OrderDetailMapper orderDetailMapper = OrderDetailMapper.INSTANCE;

    @Autowired
    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository,
                        AddressService addressService, MemberService memberService,
                        CartService cartService, CartItemService cartItemService) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.addressService = addressService;
        this.memberService = memberService;
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    // 주문 조회 (페이징 적용)
    public Page<OrderDTO> getOrdersByMemberId(Long memberId, int pageNumber, int pageSize) {
        Pageable pageableRequest = PageRequest.of(pageNumber, pageSize);
        Page<Order> pagedOrders = orderRepository.findByMemberIdOrderByIdDesc(memberId, pageableRequest);
        return pagedOrders.map(orderMapper::orderToOrderDTO);
    }

    // 주문 상세 조회
    public List<OrderDetailDTO> getOrderDetailsByOrderId(Long orderId, Long memberId) {
        Order order = orderRepository.findByIdAndMemberId(orderId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));
        return orderDetailMapper.orderDetailsToOrderDetailDTOs(order.getOrderDetails());
    }

    // 주문 조회 (단일)
    public Optional<OrderDTO> getOrderById(Long orderId, Long memberId) {
        Order order = orderRepository.findByIdAndMemberId(orderId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));
        return Optional.of(orderMapper.orderToOrderDTO(order));
    }

    // 전체 주문 수 조회
    public long getTotalOrderCount() {
        return orderRepository.count();
    }

    // 주문 생성
    public OrderDTO createOrder(String jwtToken, @Valid OrderDTO orderDTO) {
        Member member = memberService.findByJwtToken(jwtToken);
        Address address = resolveAddress(jwtToken, orderDTO);

        Order order = orderMapper.orderDTOToOrder(orderDTO);
        order.setMember(member);
        order.setOrderState(OrderState.PENDING); // 기본 주문 상태를 PENDING로 설정
        setOrderAddress(order, address);

        List<CartItems> cartItems = getCartItems(member);
        if (cartItems.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_CART);
        }

        List<OrderDetail> orderDetails = createOrderDetailsFromCartItems(cartItems, order);
        return saveAndReturnOrder(order, orderDetails);
    }

    // 주문 수정 페이지 호출
    public OrderDTO getUpdateOrderPage(Long orderId, @Valid Long memberId) {
        Order order = orderRepository.findByIdAndMemberId(orderId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));
        return orderMapper.orderToOrderDTO(order);
    }

    // 주문 수정
    public OrderDTO updateOrder(String jwtToken, Long orderId, OrderDTO orderDTO) {
        Member member = memberService.findByJwtToken(jwtToken);
        Order order = orderRepository.findByIdAndMemberId(orderId, member.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        Address address = resolveAddress(jwtToken, orderDTO);
        setOrderAddress(order, address);

        List<OrderDetail> orderDetails = createOrderDetailsFromOrderDTO(orderDTO, order);
        return saveAndReturnOrder(order, orderDetails);
    }

    // 주문 삭제
    public void deleteOrder(String jwtToken, Long orderId) {
        Member member = memberService.findByJwtToken(jwtToken);
        Order order = orderRepository.findByIdAndMemberId(orderId, member.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));
        orderRepository.delete(order);
    }

    // 주문 저장 및 DTO 반환
    private OrderDTO saveAndReturnOrder(Order order, List<OrderDetail> orderDetails) {
        order.setOrderDetails(orderDetails);
        setOrderSummary(order, orderDetails);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderDTO(savedOrder);
    }

    // 선택된 주소 확인
    private Address resolveAddress(String jwtToken, OrderDTO orderDTO) {
        Long selectedAddressId = orderDTO.getSelectedAddressId();

        if (selectedAddressId != null) {
            // 선택된 주소가 있으면 해당 주소를 반환
            return addressService.findById(selectedAddressId);
        } else {
            // 선택된 주소가 없으면 주소 리스트를 조회하여 첫 번째 주소를 반환
            return getOrSaveAddress(jwtToken, orderDTO);
        }
    }

    // 주소 리스트 존재 확인
    private Address getOrSaveAddress(String jwtToken, OrderDTO orderDTO) {
        List<Address> addresses = addressService.findAllByJwtToken(jwtToken);

        if (addresses.isEmpty()) {
            // 주소 리스트가 없다면 새로운 주소 저장
            return saveNewAddress(jwtToken, orderDTO);
        } else {
            // 주소 리스트가 존재하면 첫 번째 주소를 반환
            return addresses.get(0);
        }
    }

    // 새로운 주소 저장
    private Address saveNewAddress(String jwtToken, OrderDTO orderDTO) {
        AddressDTO newAddressDTO = new AddressDTO(
                orderDTO.getRecipientName(),
                orderDTO.getZipcode(),
                orderDTO.getAddr(),
                orderDTO.getAddrDetail(),
                orderDTO.getRecipientTel(),
                orderDTO.getDeliveryReq(),
                orderDTO.getAddrName()
        );
        return addressService.save(jwtToken, newAddressDTO);
    }

    // CartItems 가져오기
    private List<CartItems> getCartItems(Member member) {
        Cart cart = cartService.findCartByMemberId(member);
        return cartItemService.findAllItemsByCartId(cart.getId()).stream()
                .map(cartItemDto -> {
                    Item item = itemRepository.findById(cartItemDto.getItem_id())
                            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
                    return cartItemDto.toEntity(cart, item);
                })
                .toList(); // Stream.toList()로 변경하여 불변 리스트를 반환
    }

    // CartItems로부터 OrderDetail 생성
    private List<OrderDetail> createOrderDetailsFromCartItems(List<CartItems> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem -> OrderDetail.builder()
                        .order(order)
                        .item(cartItem.getItem_id())
                        .price(cartItem.getItem_id().getPrice())
                        .quantity(cartItem.getQuantity())
                        .orderState(OrderState.PENDING) // 기본 주문 상태 설정
                        .build())
                .toList(); // Stream.toList()로 변경하여 불변 리스트를 반환
    }

    // OrderDTO로부터 OrderDetail 생성
    private List<OrderDetail> createOrderDetailsFromOrderDTO(OrderDTO orderDTO, Order order) {
        return orderDTO.getOrderDetails().stream()
                .map(orderDetailDTO -> {
                    Item item = itemRepository.findById(orderDetailDTO.getItemId())
                            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
                    return orderDetailDTO.toEntity(order, item);
                })
                .toList();  // Stream.toList()로 변경하여 불변 리스트를 반환
    }

    // 주문 요약 필드 설정 (가격, 총 개수, 대표 상품 이름, 대표 상품 이미지)
    private void setOrderSummary(Order order, List<OrderDetail> orderDetails) {
        int totalPrice = orderDetails.stream().mapToInt(detail -> detail.getPrice() * detail.getQuantity()).sum();
        int totalQuantity = orderDetails.stream().mapToInt(OrderDetail::getQuantity).sum();
        String repItemName = orderDetails.isEmpty() ? "No Item" : orderDetails.get(0).getItem().getItemName();
        String repItemImage = orderDetails.isEmpty() ? "No Image" : orderDetails.get(0).getItem().getItemImagesList().stream()
                .findFirst()
                .map(ItemImages::getStoredFileName)
                .orElse("No Image");

        order.setTotalQuantity(totalQuantity);
        order.setRepItemName(repItemName);
        order.setRepItemImage(repItemImage);
        order.setPrice(totalPrice);
    }

    // 주문 객체에 주소 정보 설정
    private void setOrderAddress(Order order, Address address) {
        order.setRecipientName(address.getRecipientName());
        order.setZipcode(address.getZipcode());
        order.setAddr(address.getAddr());
        order.setAddrDetail(address.getAddrDetail());
        order.setRecipientTel(address.getRecipientTel());
        order.setAddrName(address.getAddrName());
        order.setDeliveryReq(address.getDeliveryReq());
    }
}
