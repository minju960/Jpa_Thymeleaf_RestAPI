package jpastudy.jpashop.domain;


import jpastudy.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class EntityTest {

    @Autowired
    EntityManager em;

    @Test @Rollback(value = false)
    public void entity() throws Exception {
        // Member 생성
        Member member = new Member();
        member.setName("몽타");

        Address address = new Address("서울","동작","12345");

        // Member에 Address 저장
        member.setAddress(address);

        // 영속성 컨텍스트에 저장
        em.persist(member);

        // Order 생성
        Order order = new Order();
        // Order & Member 연결
        order.setMember(member);


        // Delivery 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // Order & Delivery 연결
        order.setDelivery(delivery);

        // Item - Book 생성
        Book book = new Book();
        book.setName("책이름");
        book.setPrice(10000);
        book.setStockQuantity(10);
        book.setAuthor("저자");
        book.setIsbn("1234-abcd");
        // 영속성 컨텍스트에 저장
        em.persist(book);

        // OrderItem 생성
        OrderItem orderItem = new OrderItem();
        orderItem.setCount(2);
        orderItem.setOrderPrice(20000);
        orderItem.setItem(book);

        // Order & OrderItem 연결
        order.addOrderItem(orderItem);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);

        //영속성 컨텍스트에 저장
        em.persist(order);
    }

}