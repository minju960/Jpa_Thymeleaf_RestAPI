package jpastudy.jpashop.domain.item;

import jpastudy.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    // 상품 이름
    private String name;

    // 상품 가격
    private int price;

    // 재고 수량
    private int stockQuantity;


    //==비즈니스 로직==//
    // 주문 취소되서 재고수량 증가할 때
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
    // 주문 체결되어 재고수량 감소할 때
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }


}
