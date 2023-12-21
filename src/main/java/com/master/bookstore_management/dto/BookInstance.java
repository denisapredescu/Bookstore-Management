package com.master.bookstore_management.dto;

public record BookInstance(
        int copies,
        String name,
        int price,
        String series_name,
        int volume,
        int year,
        int author_id,
        Boolean is_deleted

    ) {
}
