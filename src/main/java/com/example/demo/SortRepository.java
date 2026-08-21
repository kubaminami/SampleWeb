package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SortRepository extends JpaRepository<Sort, Long> {

    // noが1のSortをdisplay順に取得する
    List<Sort> findAllByNoOrderByDisplayAsc(Integer no);
    

}