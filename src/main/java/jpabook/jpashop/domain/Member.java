package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    /* 엔티티 설계시 주의점
    * 1. 엔티티는 가급적 @Setter 사용 금지. 
    * 2. 모든 연관관계는 지연로딩(LAZY)로 설정. EAGER 로 인한 N+1 문제를 방지하기 위함임
    * 3. 컬렉션은 필드에서 바로 초기화한다. 
    *   : 아래 orders 필드가 예시.
    *       하이버네이트는 엔티티를 persist()할 때, 컬렉션을 감싸서 하이버네이트 제공 내장 컬렉션으로 변경한다.
    *       컬렉션을 가져올 때 잘못 생성하면, 하이버네이트 내부 매커니즘에 문제가 발생할 수 있기 때문이다.
    * 4. 테이블, 컬럼명 생성전략을 따로 두는게 낫다.
    * */

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    

}
