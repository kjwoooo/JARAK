package io.elice.shoppingmall.order.service;

import io.elice.shoppingmall.address.entity.Address;
import io.elice.shoppingmall.address.entity.AddressDTO;
import io.elice.shoppingmall.address.service.AddressService;
import io.elice.shoppingmall.cart.domain.cart.Entity.Cart;
import io.elice.shoppingmall.cart.domain.cartItems.Entity.CartItems;
import io.elice.shoppingmall.cart.service.CartService;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.entity.OrderDetail;
import io.elice.shoppingmall.order.mapper.OrderDetailMapper;
import io.elice.shoppingmall.order.mapper.OrderMapper;
import io.elice.shoppingmall.order.repository.OrderRepository;
import io.elice.shoppingmall.product.Entity.Item.Item;
import io.elice.shoppingmall.product.Entity.Item.ItemImages;
import io.elice.shoppingmall.product.Repository.Item.ItemRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final AddressService addressService;
    private final MemberService memberService;
    private final CartService cartService;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;
    private final OrderDetailMapper orderDetailMapper = OrderDetailMapper.INSTANCE;

    @Autowired
    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository,
                        AddressService addressService, MemberService memberService,
                        CartService cartService) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.addressService = addressService;
        this.memberService = memberService;
        this.cartService = cartService;
    }

    // 주문 조회 (페이징 적용)
    public Page<OrderDTO> getOrdersByMemberId(Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findByMemberIdOrderByIdDesc(memberId, pageable);
        return orders.map(orderMapper::orderToOrderDTO);
    }

    // 주문 상세 조회
    public List<OrderDetailDTO> getOrderDetailsByOrderId(Long orderId, Long memberId) {
        Optional<Order> orderOptional = orderRepository.findByIdAndMemberId(orderId, memberId);
        if(orderOptional.isPresent()) {
            Order order = orderOptional.get();
            List<OrderDetail> orderDetails = order.getOrderDetails();
            return orderDetailMapper.orderDetailsToOrderDetailDTOs(orderDetails);
        } else {
            throw new RuntimeException("주문을 찾을 수 없습니다.");
        }
    }

    // 주문 조회 (단일)
    public Optional<OrderDTO> getOrderById(Long orderId, Long memberId) {
        Optional<Order> orderOptional = orderRepository.findByIdAndMemberId(orderId, memberId);
        return orderOptional.map(orderMapper::orderToOrderDTO);
    }

    // 주문 생성
    public OrderDTO createOrder(String jwtToken, OrderDTO orderDTO) {
        Member member = memberService.findByJwtToken(jwtToken);

        // 배송지 정보를 결정하고 주소를 가져옴
        Address address = resolveAddress(jwtToken, orderDTO);

        // DTO에서 엔티티로 변환
        Order order = orderDTO.toEntity();
        order.setMember(member);
        setOrderAddress(order, address);

        // 장바구니에서 상품 정보 가져오기
        Cart cart = cartService.getCartByMemberId(member.getId());
        List<CartItems> cartItems = cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new RuntimeException("장바구니에 상품이 없습니다.");
        }

        // OrderDetail 설정
        List<OrderDetail> orderDetails = createOrderDetailsFromCartItems(cartItems, order);
        order.setOrderDetails(orderDetails);

        // 가격, 총 개수, 대표 상품 이름, 대표 상품 이미지 설정
        setOrderSummary(order, orderDetails);

        // 주문 저장 및 반환
        Order savedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderDTO(savedOrder);
    }

    // 주문 수정 페이지 호출
    public OrderDTO getUpdateOrderPage(Long orderId, Long memberId) {
        Order order = orderRepository.findByIdAndMemberId(orderId, memberId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
        return orderMapper.orderToOrderDTO(order);
    }

    // 주문 수정
    public OrderDTO updateOrder(String jwtToken, Long orderId, OrderDTO orderDTO) {
        Member member = memberService.findByJwtToken(jwtToken);
        Order order = orderRepository.findByIdAndMemberId(orderId, member.getId())
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        // 배송지 정보를 결정하고 주소를 가져옴
        Address address = resolveAddress(jwtToken, orderDTO);

        // 기존 주문의 주소 정보 업데이트
        setOrderAddress(order, address);

        // OrderDetail 설정
        List<OrderDetail> orderDetails = createOrderDetailsFromOrderDTO(orderDTO, order);
        order.setOrderDetails(orderDetails);

        // 가격, 총 개수, 대표 상품 이름, 대표 상품 이미지 설정
        setOrderSummary(order, orderDetails);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderDTO(savedOrder);
    }

    // 주문 삭제
    public void deleteOrder(String jwtToken, Long orderId) {
        Member member = memberService.findByJwtToken(jwtToken);
        Order order = orderRepository.findByIdAndMemberId(orderId, member.getId())
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
        orderRepository.delete(order);
    }

    // 새로운 배송지 저장 및 변환
    // 기존 배송지 검색 및 변환
    private Address resolveAddress(String jwtToken, OrderDTO orderDTO) {
        if (orderDTO.isUseNewAddress()) {
            // 새로운 배송지 정보 입력받기
            AddressDTO newAddressDTO = new AddressDTO(
                    orderDTO.getRecipientName(),
                    orderDTO.getZipcode(),
                    orderDTO.getAddr(),
                    orderDTO.getAddrDetail(),
                    orderDTO.getRecipientTel(),
                    orderDTO.getDeliveryReq(),
                    "Y" // 기본 배송지 설정 여부
            );
            return addressService.save(jwtToken, newAddressDTO);
        } else {
            // 기존 배송지 정보 가져오기
            List<Address> addresses = addressService.findAllByJwtToken(jwtToken);
            return addresses.stream()
                    .filter(addr -> "Y".equals(addr.getDefDestination()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("기본 배송지 정보가 없습니다. 새로운 배송지를 입력해주세요."));
        }
    }

    // CartItems로부터 OrderDetail 생성
    private List<OrderDetail> createOrderDetailsFromCartItems(List<CartItems> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem -> {
                    // CartItems 필드에 Item이 없음
                    // Cart 담당자와 상의 필요
                    Item item = cartItem.getItem();
                    int price = item.getPrice(); // Item의 가격을 직접 가져옴

                    return OrderDetail.builder()
                            .order(order)
                            .item(item)
                            .price(price)
                            .quantity(cartItem.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // OrderDTO로부터 OrderDetail 생성
    private List<OrderDetail> createOrderDetailsFromOrderDTO(OrderDTO orderDTO, Order order) {
        return orderDTO.getOrderDetails().stream()
                .map(orderDetailDTO -> {
                    Item item = itemRepository.findById(orderDetailDTO.getItemId())
                            .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
                    return orderDetailDTO.toEntity(order, item);
                })
                .collect(Collectors.toList());
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

        // 필수 필드에 값 설정
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
    }
}
