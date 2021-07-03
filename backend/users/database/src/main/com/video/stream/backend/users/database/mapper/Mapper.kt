package com.video.stream.backend.users.database.mapper


interface Mapper<Entry, Entity> {
    fun Entry.toEntity(): Entity
}