package org.example.kafka;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 복잡한 구조 혹은 다양한 지시와 파라미터를 담은 형태로 메세지 전송하는 그릇
 */
@Data
@ToString
public class OrderDto {
    private String orderId;
    private String msg;
    @Builder
    public OrderDto(String orderId, String msg) {
        this.orderId = orderId;
        this.msg = msg;
    }
}
