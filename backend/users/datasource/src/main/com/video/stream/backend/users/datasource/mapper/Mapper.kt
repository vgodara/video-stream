package com.video.stream.backend.users.datasource.mapper

interface Mapper<Entity, T> {
    fun Entity.fromEntity(): T
    fun T.toEntity(): Entity
}