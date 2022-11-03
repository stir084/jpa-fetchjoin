package com.example.jpafetchjoin;

import com.example.jpafetchjoin.domain.Order;
import lombok.Data;

import java.util.List;

@Data
public class MemberDto {
    private Long id;
    private String name;
    private List<Order> orders;

    public MemberDto(Long id, String name, List<Order> orders){
        this.id = id;
        this.name = name;
        this.orders = orders;
    }
}
