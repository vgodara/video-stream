package com.stream.video.users.application.di

import com.stream.video.users.application.auth.JwtBuilder
import com.stream.video.users.application.auth.JwtImplementation
import com.typesafe.config.ConfigFactory
import com.video.stream.backend.users.controller.FindUserByIdController
import com.video.stream.backend.users.controller.LoginUserController
import com.video.stream.backend.users.controller.RefreshTokenController
import com.video.stream.backend.users.controller.RegisterUserController
import com.video.stream.backend.users.controller.auth.TokenGenerator
import com.video.stream.backend.users.controller.auth.TokenPayload
import com.video.stream.backend.users.controller.auth.TokenVerifier
import com.video.stream.backend.users.database.TokenDataSourceImpl
import com.video.stream.backend.users.database.UserDataSourceImpl
import com.video.stream.backend.users.database.connection.DbConnection
import com.video.stream.backend.users.datasource.TokenRepositoryImpl
import com.video.stream.backend.users.datasource.UserRepositoryImpl
import com.video.stream.backend.users.datasource.source.TokenDataSource
import com.video.stream.backend.users.datasource.source.UserDataSource
import com.video.stream.backend.users.usecase.*
import com.video.stream.backend.users.usecase.repository.TokenRepository
import com.video.stream.backend.users.usecase.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

object KoinModule {
    const val IO_THREAD: String = "IO"
    const val DEFAULT_THREAD: String = "Default"
    const val DATABASE_URL = "Database_Url"
    const val DATABASE_DRIVER = "Database_Driver"
    const val DATABASE_USER = "Database_User"
    const val DATABASE_PASSWORD = "Database_Password"
    const val JWT_ISSUER = "Issuer"
    const val JWT_AUDIENCE = "Audience"
    const val JWT_REALM = "REALM"
    const val JWT_TOKEN_SECRET = "Token_Secret"
    const val JWT_REFRESH_TOKEN_SECRET = "Refresh_Token_Secret"

    private val configModule = module {
        val resource by lazy {
            ConfigFactory.load("application.conf")
        }
        single(named(DATABASE_URL)) { resource.getString("database.url") }
        single(named(DATABASE_DRIVER)) { resource.getString("database.driver") }
        single(named(DATABASE_USER)) { resource.getString("database.user") }
        single(named(DATABASE_PASSWORD)) { resource.getString("database.password") }
        single(named(JWT_ISSUER)) { resource.getString("jwt.domain") }
        single(named(JWT_AUDIENCE)) { resource.getString("jwt.audience") }
        single(named(JWT_REALM)) { resource.getString("jwt.realm") }
        single(named(JWT_TOKEN_SECRET)) { resource.getString("jwt.tokenSecret") }
        single(named(JWT_REFRESH_TOKEN_SECRET)) { resource.getString("jwt.refreshTokenSecret") }
    }
    private val coroutineConnectModule = configModule + module {
        single<CoroutineContext>(named(IO_THREAD)) { Dispatchers.IO }
        single<CoroutineContext>(named(DEFAULT_THREAD)) { Dispatchers.Default }
        single { Dispatchers.Default }
    }
    private val dataBaseModule = coroutineConnectModule + module {
        single {
            DbConnection(
                get(named(DATABASE_URL)),
                get(named(DATABASE_DRIVER)),
                get(named(DATABASE_USER)),
                get(named(DATABASE_PASSWORD))
            )
        }
        single<UserDataSource> { UserDataSourceImpl(get(named(IO_THREAD))) }
        single<TokenDataSource> { TokenDataSourceImpl(get(named(IO_THREAD))) }

    }
    private val dataSourceModule = dataBaseModule + module {
        single<UserRepository> { UserRepositoryImpl(get(), get(named(DEFAULT_THREAD))) }
        single<TokenRepository> { TokenRepositoryImpl(get(), get(named(DEFAULT_THREAD))) }
    }
    private val useCaseModule = dataSourceModule + module {
        single { FindUserByNameUseCase(get(), get(named(DEFAULT_THREAD))) }
        single { FindUserByIdUseCase(get(), get(named(DEFAULT_THREAD))) }
        single { CreateUserUseCase(get(), get(named(DEFAULT_THREAD))) }
        single { LoginUserUseCase(get(), get(named(DEFAULT_THREAD))) }
        single { RetrieveRefreshTokenUseCase(get(), get(), get(named(DEFAULT_THREAD))) }
        single { SaveRefreshTokenUseCase(get(), get(), get(named(DEFAULT_THREAD))) }
    }
    private val controllerModule = useCaseModule + module {
        factory { RegisterUserController(get(), get(), get(), get(named(DEFAULT_THREAD))) }
        factory { FindUserByIdController(get(), get(named(DEFAULT_THREAD))) }
        factory { LoginUserController(get(), get(), get(), get(named(DEFAULT_THREAD))) }
        factory { RefreshTokenController(get(), get(), get(), get(), get(), get(named(DEFAULT_THREAD))) }
    }

    val applicationModule = controllerModule + module {
        single {
            JwtImplementation(
                get(named(JWT_ISSUER)),
                get(named(JWT_AUDIENCE)),
                get(named(JWT_TOKEN_SECRET)),
                get(named(JWT_REFRESH_TOKEN_SECRET))
            )
        }.binds(arrayOf(TokenVerifier::class, TokenGenerator::class, TokenPayload::class, JwtBuilder::class))

    }

}