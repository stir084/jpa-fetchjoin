package com.example.jpafetchjoin.repository;

import com.example.jpafetchjoin.domain.Member;
import com.example.jpafetchjoin.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public List<Member> findAllFetchJoin() {
        return em.createQuery("select m from Member m join fetch m.orders o where o.item = 'apple'", Member.class) //join fetch Member가 아니다.
                .getResultList();
    }
    public List<Member> findAllFetchJoin2(Long id) {
        return em.createQuery("select m from Member m join fetch m.orders", Member.class)
               //.setParameter("id", id)//join fetch Member가 아니다.
                .getResultList();
    }
    public List<Member> findAllFetchJoin3() {
        return em.createQuery("select m from Member m join fetch m.orders where m.id = 1", Member.class)
                //.setParameter("id", id)//join fetch Member가 아니다.
                .getResultList();
    }
    public List<Member> testtest() {
        return em.createQuery("select m from Member m join m.orders o where o.item ='apple'", Member.class)
                //.setParameter("id", id)//join fetch Member가 아니다.
                .getResultList();
    }

    public List<Member> findAllFetchJoi() {
        return em.createQuery("select m from Member m join fetch m.orders o where o.item = 'apple' and m.id = 1", Member.class) //join fetch Member가 아니다.
                .getResultList();
    }
    public List<Member> findAllFetchJoi2(Long id) {
        return em.createQuery("select distinct m from Member m join fetch m.orders where m.id=2", Member.class)
                //.setParameter("id", id)//join fetch Member가 아니다.
                .getResultList();
    }

}