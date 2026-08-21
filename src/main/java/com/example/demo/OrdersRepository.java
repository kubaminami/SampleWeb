package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    //ユーザーIDで注文履歴を取得するメソッドを追加
    List<Orders> findByUserId(int userId);
}
