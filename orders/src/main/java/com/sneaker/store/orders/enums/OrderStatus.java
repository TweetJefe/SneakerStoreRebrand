package com.sneaker.store.orders.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    NEW(1),// Создан, но ещё не оплачен
    PENDING_PAYMENT(2),// Ожидает оплаты
    PAID(3), // Оплачен, ожидает подтверждения магазина
    PROCESSING(4), // В обработке (сборка на складе)
    SHIPPED(5), // Отправлен покупателю
    DELIVERED(6), // Доставлен
    COMPLETED(7), // Завершён (подтвержден клиентом)
    CANCELLED(8), // Отменён пользователем или магазином
    RETURN_REQUESTED(9), // Клиент запросил возврат
    RETURNED(10), // Возврат подтверждён и обработан
    REFUNDED(11);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }
}
