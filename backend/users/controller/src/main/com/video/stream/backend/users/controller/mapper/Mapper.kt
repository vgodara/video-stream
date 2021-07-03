package com.video.stream.backend.users.controller.mapper

interface Mapper<Model, T> {
    fun toModel(data: T): Model
    fun fromModel(model: Model): T
}