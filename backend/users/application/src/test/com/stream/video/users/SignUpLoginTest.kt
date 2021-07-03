package com.stream.video.users

import com.stream.video.users.application.module
import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalSuccess
import com.video.stream.common.NetworkResponse
import com.video.stream.common.toLocalResponse
import com.video.stream.common.users.v1.request.LoginRequest
import com.video.stream.common.users.v1.request.RefreshTokenRequest
import com.video.stream.common.users.v1.request.SignUpRequest
import com.video.stream.common.users.v1.response.LoginResponse
import com.video.stream.common.users.v1.response.RefreshTokenResponse
import com.video.stream.utilities.EmptyString
import com.video.stream.utilities.generateSalt
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldNotBeInstanceOf
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.auth.AuthScheme.Bearer
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SignUpLoginTest : BehaviorSpec({
    val userName = generateSalt()
    val password = generateSalt()
    var token: String = EmptyString
    var refreshToken: String? = null
    given("Trying to create a new user") {
        When("Calling server at /user/signup") {
            then("should create a user with given name and password") {
                withTestApplication(Application::module) {
                    with(handleRequest(HttpMethod.Post, "/user/signup") {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.withoutParameters().toString())
                        setBody(Json.encodeToString(SignUpRequest(userName, password)))
                    }) {
                        response.status()?.isSuccess() shouldBe true
                        response.content shouldNotBe null
                        val networkResponse: NetworkResponse<LoginResponse> = Json.decodeFromString(response.content!!)
                        when (val localResponse = networkResponse.toLocalResponse()) {
                            is LocalFailure -> {
                                shouldNotBeInstanceOf<LocalFailure<LoginResponse>>()
                            }
                            is LocalSuccess -> {
                                localResponse.data.userName shouldBe userName
                            }
                        }
                    }
                }
            }
        }
        and("Try to login using newly created user") {
            When("Calling server at user/login") {
                then("should be able to login successfully") {
                    withTestApplication(Application::module) {
                        with(handleRequest(HttpMethod.Post, "/user/login") {
                            addHeader(
                                HttpHeaders.ContentType,
                                ContentType.Application.Json.withoutParameters().toString()
                            )
                            setBody(Json.encodeToString(LoginRequest(userName, password)))
                        }) {
                            response.status()?.isSuccess() shouldBe true
                            response.content shouldNotBe null
                            val networkResponse: NetworkResponse<LoginResponse> =
                                Json.decodeFromString(response.content!!)
                            when (val localResponse = networkResponse.toLocalResponse()) {
                                is LocalFailure -> {
                                    shouldNotBeInstanceOf<LocalFailure<LoginResponse>>()
                                }
                                is LocalSuccess -> {
                                    localResponse.data.userName shouldBe userName
                                    token = localResponse.data.token
                                    refreshToken = localResponse.data.refreshToken
                                }
                            }
                        }
                    }
                }
            }

        }
        and("Try to access protected url using token") {
            When("Calling /user/me") {
                then("Should return status code ${HttpStatusCode.OK}") {
                    withTestApplication(Application::module) {
                        with(
                            handleRequest(HttpMethod.Get, "/user/me") {
                                addHeader(
                                    HttpHeaders.Authorization,
                                    "$Bearer $token"
                                )
                            },
                        ) {
                            response.status()?.isSuccess() shouldBe true
                        }
                    }
                }
            }
        }
        And("Try to retrieve new token using refresh token") {
            refreshToken shouldNotBe null
            When("calling /user/refreshToken") {
                Then("should return a refresh token") {
                    withTestApplication(Application::module) {
                        with(
                            handleRequest(HttpMethod.Post, "/user/refreshToken") {
                                addHeader(
                                    HttpHeaders.Authorization,
                                    "$Bearer $token"
                                )
                                addHeader(
                                    HttpHeaders.ContentType,
                                    ContentType.Application.Json.withoutParameters().toString()
                                )
                                setBody(Json.encodeToString(RefreshTokenRequest(null, refreshToken!!)))
                            },
                        ) {
                            response.status()?.isSuccess() shouldBe true
                            val networkResponse: NetworkResponse<RefreshTokenResponse> =
                                Json.decodeFromString(response.content!!)

                            when (val localResponse = networkResponse.toLocalResponse()) {
                                is LocalFailure -> {
                                    shouldNotBeInstanceOf<LocalFailure<LoginResponse>>()
                                }
                                is LocalSuccess -> {
                                    localResponse.data.token shouldNotBe null
                                    token = localResponse.data.token
                                }
                            }
                        }
                    }
                }
            }
        }
        And("Try to access protected url using new token") {
            When("Calling /user/me") {
                then("Should return status code ${HttpStatusCode.OK}") {
                    withTestApplication(Application::module) {
                        with(
                            handleRequest(HttpMethod.Get, "/user/me") {
                                addHeader(
                                    HttpHeaders.Authorization,
                                    "$Bearer $token"
                                )
                            },
                        ) {
                            response.status()?.isSuccess() shouldBe true
                        }
                    }
                }
            }
        }
    }
})
