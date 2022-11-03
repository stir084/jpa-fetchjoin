package com.example.jpafetchjoin;

import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String name;
    private String item;

    public MemberDto(Long id, String name, String item){
        this.id = id;
        this.name = name;
        this.item = item;
    }
}
