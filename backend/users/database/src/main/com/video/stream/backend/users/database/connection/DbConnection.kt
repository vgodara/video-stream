package com.video.stream.backend.users.database.connection

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager

class DbConnection(url: String, driver: String, user: String, password: String) {
    init {
        TransactionManager.defaultDatabase = Database.connect(
            url = url,
            driver = driver,
            user = user,
            password = password
        )
    }
}
