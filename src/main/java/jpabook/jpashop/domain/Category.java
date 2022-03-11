package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    /* 실무에서는 @ManyToMany 를 지양.
    * DB에서는 중간테이블에 컬럼을 추가할 수 없고, 세밀한 쿼리를 실행하기 어렵다. 
    * N:N 매핑은 1:N, N:1 매핑으로 풀어내서 사용하자.
    * */
    @ManyToMany
    @JoinTable(name = "category_item",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    //셀프로 자기 내부에 양방향 관계를 맺은 것이다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    /* 연관관계 메서드 */
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
