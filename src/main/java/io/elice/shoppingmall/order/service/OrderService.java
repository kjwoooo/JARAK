package io.elice.shoppingmall.order.service;

import io.elice.shoppingmall.address.entity.Address;
import io.elice.shoppingmall.address.entity.AddressDTO;
import io.elice.shoppingmall.address.entity.AddressResponseDTO;
import io.elice.shoppingmall.address.service.AddressService;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.repository.MemberRepository;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.order.dto.OrderDTO;
import io.elice.shoppingmall.order.dto.OrderDetailDTO;
import io.elice.shoppingmall.order.entity.Order;
import io.elice.shoppingmall.order.entity.OrderDetail;
import io.elice.shoppingmall.order.mapper.OrderDetailMapper;
import io.elice.shoppingmall.order.mapper.OrderMapper;
import io.elice.shoppingmall.order.repository.OrderRepository;
import io.elice.shoppingmall.product.Entity.Item.Item;
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
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;
    private final OrderDetailMapper orderDetailMapper = OrderDetailMapper.INSTANCE;

    @Autowired
    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository, AddressService addressService, MemberService memberService) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.addressService = addressService;
        this.memberService = memberService;
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

        Address address;

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
            addressService.save(jwtToken, newAddressDTO);
            address = newAddressDTO.toEntity();
        } else {
            // 기존 배송지 정보 가져오기
            List<AddressResponseDTO> addresses = addressService.findAllByJwtToken(jwtToken);
            AddressResponseDTO addressResponse = addresses.stream()
                    .filter(addr -> "Y".equals(addr.getDefDestination()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("기본 배송지 정보가 없습니다. 새로운 배송지를 입력해주세요."));
            address = addressResponse.toEntity();
        }

        // 주소 정보를 OrderDTO에 설정
        orderDTO.setRecipientName(address.getRecipientName());
        orderDTO.setZipcode(address.getZipcode());
        orderDTO.setAddr(address.getAddr());
        orderDTO.setAddrDetail(address.getAddrDetail());
        orderDTO.setRecipientTel(address.getRecipientTel());
        orderDTO.setDeliveryReq(address.getDeliveryReq());

        // DTO에서 엔티티로 변환
        Order order = orderDTO.toEntity();
        order.setMember(member);  // member 설정

        // OrderDetail 설정
        List<OrderDetail> orderDetails = orderDTO.getOrderDetails().stream()
                .map(orderDetailDTO -> {
                    Item item = itemRepository.findById(orderDetailDTO.getItemId())
                            .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
                    return orderDetailDTO.toEntity(order, item);
                })
                .collect(Collectors.toList());
        order.setOrderDetails(orderDetails);

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
            AddressResponseDTO savedAddressResponse = addressService.save(jwtToken, newAddressDTO);
            Address address = savedAddressResponse.toEntity();
            order.setRecipientName(address.getRecipientName());
            order.setZipcode(address.getZipcode());
            order.setAddr(address.getAddr());
            order.setAddrDetail(address.getAddrDetail());
            order.setRecipientTel(address.getRecipientTel());
            order.setDeliveryReq(address.getDeliveryReq());
        }

        // OrderDetail 설정
        List<OrderDetail> orderDetails = orderDTO.getOrderDetails().stream()
                .map(orderDetailDTO -> {
                    Item item = itemRepository.findById(orderDetailDTO.getItemId())
                            .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
                    return orderDetailDTO.toEntity(order, item);
                })
                .collect(Collectors.toList());
        order.setOrderDetails(orderDetails);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderDTO(savedOrder);
    }
}
