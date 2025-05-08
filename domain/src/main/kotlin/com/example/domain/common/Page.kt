package com.example.domain.common

data class Page<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Long,
    val currentPage: Int,
    val pageSize: Int,
    val isLast: Boolean
)