package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    /* 값타입 : 변경불가능하게 설계해야 함.
    * 따라서 @Setter 는 제거하고, 모든 값을 초기화하는 생성자를 만들어준다.
    *
    * @Embeddable : JPA 스펙상에서 이 타입은 기본생성자가 필요함(리플랙션 할 때 필요)
    *       다만 public 이면 아무데서나 가져 쓸 수 있으므로 protected 로 설정
    * */
    protected Address() { }
    
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    
}
