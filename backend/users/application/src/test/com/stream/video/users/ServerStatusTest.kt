package com.stream.video.users

import com.stream.video.users.application.module
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*

class ServerStatusTest : BehaviorSpec({
    given("Server is running") {
        `when`("I call home page") {
            then("Server should return status code ${HttpStatusCode.OK}") {
                withTestApplication(Application::module) {
                    with(handleRequest(HttpMethod.Get, "/")) {
                        response.status() shouldBe HttpStatusCode.OK
                    }
                }
            }
        }
    }
})