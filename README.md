# Spring Authorization Server

## Настройка OAuth2 сервера авторизации
В файле  `OAuth2AuthServerSecurityConfiguration.java`  находится конфигурация OAuth2 сервера авторизации.
Здесь описан  `RegisteredClientRepository`  для хранения зарегистрированных клиентов, генерируется RSA ключ для подписи токенов.
Также настраивается  `ProviderSettings`  и  `UserDetailsService` .

Сам `` настраивается так: 
4.  `.clientId("gateway")` : Устанавливает идентификатор клиента как "gateway".
5.  `.clientSecret("{noop}secret")` : Устанавливает секрет клиента как "{noop}secret". "{noop}" означает, что секрет не требует шифрования. В таком виде он и будет лежать в БД.
6.  `.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)` : Устанавливает метод аутентификации клиента как `ClientAuthenticationMethod.CLIENT_SECRET_BASIC`, что означает, что клиент будет использовать базовую аутентификацию с использованием идентификатора (`client_id`) и секрета клиента (`secret`).
7.  `.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)` : Устанавливает тип авторизации клиента как `AuthorizationGrantType.AUTHORIZATION_CODE`, что означает, что клиент будет использовать авторизационный код (`authorization_code`) для получения доступа к ресурсам.
8.  `.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)` : Устанавливает тип авторизации клиента как `AuthorizationGrantType.REFRESH_TOKEN`, что означает, что клиент может использовать обновляемый токен для продления доступа без повторной аутентификации.
9.  `.redirectUri("http://127.0.0.1:8080/login/oauth2/code/gateway")` : Этот код устанавливает URL, на который будет перенаправлен пользователь после аутентификации. После перенаправления на этот URL, сервер обрабатывает полученный авторизационный код и обменивает его на токен доступа. 
Мы здесь указываем наш сервер авторизации в качестве домена, так как он и обменяет код на токен для клиента.
10.  `.scope(OidcScopes.OPENID)` : Устанавливает область видимости клиента на "openid". Это протокол OpenID Connect (OIDC), который является протоколом идентификации на основе OAuth 2.0. 

```
.scope(OidcScopes.OPENID)` : Это метод, который устанавливает область видимости для клиента. 
Область видимости определяет, какие данные клиент может запрашивать у ресурсного сервера. 
В данном случае используется  OidcScopes.OPENID , что означает, что клиент будет запрашивать данные, 
связанные с идентификацией пользователя.
```
11.  `.scope("message.read")` : Устанавливает дополнительную область видимости клиента на "message.read". В названии для scope ограничений нет, однако общепринято называть их по принципу `ресурс.право_доступа` или `ресурс:право_доступа`
12.  `new JdbcRegisteredClientRepository(jdbcTemplate)` : Создает новый экземпляр JdbcRegisteredClientRepository, который реализует интерфейс RegisteredClientRepository и использует jdbcTemplate для взаимодействия с базой данных.
13.  `registeredClientRepository.save(registeredClient)` : Сохраняет созданный registeredClient в репозитории.

## Настройка безопасности
В том же файле  `OAuth2AuthServerSecurityConfiguration.java`  настраиваются два  `SecurityFilterChain` .
Первый отвечает за безопасность самого сервера авторизации, второй - за безопасность остального API.

## Регистрация пользователя
В  `UserService.java`  реализована регистрация нового пользователя. При регистрации создается  `CustomUserDetails`  с ролью по умолчанию `ROLE_USER`  и сохраняется в БД с помощью  `JdbcUserDetailsManager` .

## API регистрации
В  `AuthenticationController.java`  есть единственный endpoint  `/register`  для регистрации нового пользователя. Он вызывает метод  `createUser`  из  `UserService` .

## База данных
В файле  `schema.sql`  описана схема базы данных для сервера авторизации.
Содержит таблицы:

-  `oauth2_registered_client`  - для хранения зарегистрированных клиентов
-  `users`  - для хранения пользователей
-  `authorities`  - для хранения ролей пользователей

Схема базы данных используется при запуске приложения для инициализации таблиц.