# KDT_BE8_Mini-Project: 숙박 예약 API 서비스

### 프로젝트 기간 : 06/17(월) ~ 07/05 (금)

## 설정 방법

<details>
  <summary>Setting</summary>
  <div markdown="1">
        src/main/resources에 있는 example 파일을 참고하여 yaml 파일 3개 생성
  </div>
</details>

## Develop Tool

- Spring Boot 3.3.0
- Spring Boot Security
- Spring Data JPA
- JWT
- MySQL
- Redis
- Nginx
- Docker
- GitHub Actions

## ERD

![](https://github.com/qudwns017/qudwns017/assets/82150958/1c187890-98f4-4778-b950-cd0aa42c9182)

## Directory Structure

<details>

  <summary>Directory Structure</summary>
  <div markdown="1">

```
┌─ main
│  ├─java
│  │  └─com
│  │      └─group6
│  │          └─accommodation
│  │              │  AccommodationApplication.java
│  │              │
│  │              ├─domain
│  │              │  ├─accommodation
│  │              │  │  ├─annotation
│  │              │  │  │  │  StartBeforeEndAndNullable.java
│  │              │  │  │  │  ValidArea.java
│  │              │  │  │  │  ValidCategory.java
│  │              │  │  │  │
│  │              │  │  │  └─validator
│  │              │  │  │          AreaValidator.java
│  │              │  │  │          CategoryValidator.java
│  │              │  │  │          StartBeforeEndAndNullableValidator.java
│  │              │  │  │
│  │              │  │  ├─config
│  │              │  │  │      OpenapiConfig.java
│  │              │  │  │      RestTemplateConfig.java
│  │              │  │  │
│  │              │  │  ├─controller
│  │              │  │  │      AccommodationController.java
│  │              │  │  │
│  │              │  │  ├─converter
│  │              │  │  │      AccommodationConverter.java
│  │              │  │  │
│  │              │  │  ├─model
│  │              │  │  │  ├─dto
│  │              │  │  │  │      AccommodationConditionRequestDto.java
│  │              │  │  │  │      AccommodationDetailResponseDto.java
│  │              │  │  │  │      AccommodationResponseDto.java
│  │              │  │  │  │      ApiResponseDto.java
│  │              │  │  │  │
│  │              │  │  │  ├─entity
│  │              │  │  │  │      AccommodationEntity.java
│  │              │  │  │  │
│  │              │  │  │  └─enums
│  │              │  │  │          Area.java
│  │              │  │  │          Category.java
│  │              │  │  │
│  │              │  │  ├─repository
│  │              │  │  │      AccommodationRepository.java
│  │              │  │  │
│  │              │  │  ├─scheduler
│  │              │  │  │      ScheduledTasks.java
│  │              │  │  │
│  │              │  │  ├─service
│  │              │  │  │      AccommodationApiService.java
│  │              │  │  │      AccommodationService.java
│  │              │  │  │      ApiProcessService.java
│  │              │  │  │      DatabaseInitializationService.java
│  │              │  │  │
│  │              │  │  └─specification
│  │              │  │          AccommodationSpecification.java
│  │              │  │
│  │              │  ├─auth
│  │              │  │  ├─controller
│  │              │  │  │      UserController.java
│  │              │  │  │
│  │              │  │  ├─model
│  │              │  │  │  ├─dto
│  │              │  │  │  │      UserRequestDto.java
│  │              │  │  │  │      UserResponseDto.java
│  │              │  │  │  │
│  │              │  │  │  └─entity
│  │              │  │  │          UserEntity.java
│  │              │  │  │
│  │              │  │  ├─repository
│  │              │  │  │      UserRepository.java
│  │              │  │  │
│  │              │  │  └─service
│  │              │  │          UserService.java
│  │              │  │
│  │              │  ├─likes
│  │              │  │  ├─controller
│  │              │  │  │      UserLikeController.java
│  │              │  │  │
│  │              │  │  ├─model
│  │              │  │  │  ├─dto
│  │              │  │  │  │      UserLikeResponseDto.java
│  │              │  │  │  │
│  │              │  │  │  └─entity
│  │              │  │  │          UserLikeEntity.java
│  │              │  │  │          UserLikeId.java
│  │              │  │  │
│  │              │  │  ├─repository
│  │              │  │  │      UserLikeRepository.java
│  │              │  │  │
│  │              │  │  └─service
│  │              │  │          UserLikeService.java
│  │              │  │
│  │              │  ├─reservation
│  │              │  │  ├─annotation
│  │              │  │  │  │  StartBeforeEnd.java
│  │              │  │  │  │
│  │              │  │  │  └─validator
│  │              │  │  │          StartBeforeEndValidator.java
│  │              │  │  │
│  │              │  │  ├─controller
│  │              │  │  │      ReserveController.java
│  │              │  │  │
│  │              │  │  ├─converter
│  │              │  │  │      ReservationConverter.java
│  │              │  │  │
│  │              │  │  ├─model
│  │              │  │  │  ├─dto
│  │              │  │  │  │      PostReserveRequestDto.java
│  │              │  │  │  │      ReserveListItemDto.java
│  │              │  │  │  │      ReserveResponseDto.java
│  │              │  │  │  │
│  │              │  │  │  └─entity
│  │              │  │  │          ReservationEntity.java
│  │              │  │  │
│  │              │  │  ├─repository
│  │              │  │  │      ReservationRepository.java
│  │              │  │  │
│  │              │  │  └─service
│  │              │  │          ReserveService.java
│  │              │  │
│  │              │  └─room
│  │              │      ├─annotation
│  │              │      │      CheckOutAfterOrEqualCheckIn.java
│  │              │      │
│  │              │      ├─controller
│  │              │      │      RoomController.java
│  │              │      │
│  │              │      ├─converter
│  │              │      │      RoomConverter.java
│  │              │      │
│  │              │      ├─model
│  │              │      │  ├─dto
│  │              │      │  │      AvailableRoomsReq.java
│  │              │      │  │      AvailableRoomsRes.java
│  │              │      │  │      RoomDto.java
│  │              │      │  │
│  │              │      │  └─entity
│  │              │      │          RoomEntity.java
│  │              │      │
│  │              │      ├─repository
│  │              │      │      RoomRepository.java
│  │              │      │
│  │              │      ├─service
│  │              │      │      ApiProcessRoomService.java
│  │              │      │      RoomApiService.java
│  │              │      │      RoomService.java
│  │              │      │
│  │              │      └─validator
│  │              │              RoomPeriodValidator.java
│  │              │
│  │              └─global
│  │                  ├─exception
│  │                  │  │  ExceptionAdvice.java
│  │                  │  │  ExceptionResponse.java
│  │                  │  │
│  │                  │  ├─error
│  │                  │  │      AccommodationErrorCode.java
│  │                  │  │      AuthErrorCode.java
│  │                  │  │      ErrorCode.java
│  │                  │  │      ExampleErrorCode.java
│  │                  │  │      ReservationErrorCode.java
│  │                  │  │      RoomErrorCode.java
│  │                  │  │      UserLikeErrorCode.java
│  │                  │  │
│  │                  │  └─type
│  │                  │          AccommodationException.java
│  │                  │          AuthException.java
│  │                  │          CustomException.java
│  │                  │          ReservationException.java
│  │                  │          RoomException.java
│  │                  │          UserLikeException.java
│  │                  │          ValidException.java
│  │                  │
│  │                  ├─filter
│  │                  │      LoggerFilter.java
│  │                  │
│  │                  ├─model
│  │                  │  └─dto
│  │                  │          PagedDto.java
│  │                  │
│  │                  ├─redis
│  │                  │  ├─config
│  │                  │  │      RedisConfig.java
│  │                  │  │
│  │                  │  ├─model
│  │                  │  │      RefreshToken.java
│  │                  │  │
│  │                  │  └─repository
│  │                  │          RefreshTokenRepository.java
│  │                  │
│  │                  ├─security
│  │                  │  ├─config
│  │                  │  │      CorsConfig.java
│  │                  │  │      SecurityConfig.java
│  │                  │  │      WebSecurityConfig.java
│  │                  │  │      
│  │                  │  ├─exception
│  │                  │  │  │  CustomAuthenticationEntryPoint.java
│  │                  │  │  │  NotFoundErrorController.java
│  │                  │  │  │
│  │                  │  │  └─dto
│  │                  │  │          SecurityExceptionDto.java
│  │                  │  │
│  │                  │  ├─filter
│  │                  │  │      JwtFilter.java
│  │                  │  │      LoginAuthenticationFilter.java
│  │                  │  │
│  │                  │  ├─service
│  │                  │  │      CustomUserDetails.java
│  │                  │  │      CustomUserDetailsService.java
│  │                  │  │
│  │                  │  └─token
│  │                  │      ├─model
│  │                  │      │  └─dto
│  │                  │      │          LoginTokenRequestDto.java
│  │                  │      │          LoginTokenResponseDto.java
│  │                  │      │
│  │                  │      ├─provider
│  │                  │      │      TokenProvider.java
│  │                  │      │
│  │                  │      └─service
│  │                  ├─swagger
│  │                  │      SwaggerConfig.java
│  │                  │
│  │                  └─util
│  │                          Response.java
│  │                          ResponseApi.java
│  │
│  └─resources
│          application-dev.yaml
│          application-dev.yaml.example
│          application-prod.yaml
│          application-prod.yaml.example
│          application.yaml
│          application.yaml.example
│
└─test
    └─java
        └─com
            └─group6
                └─accommodation
                    │  AccommodationApplicationTests.java
                    │
                    └─domain
                        ├─accommodation
                        │  ├─controller
                        │  │      AccommodationControllerTest.java
                        │  │
                        │  ├─model
                        │  │  ├─dto
                        │  │  │      AccommodationDetailResponseDtoTest.java
                        │  │  │      AccommodationResponseDtoTest.java
                        │  │  │      
                        │  │  ├─entity
                        │  │  │      AccommodationEntityTest.java
                        │  │  │
                        │  │  └─enums
                        │  │          AreaTest.java
                        │  │          CategoryTest.java
                        │  │
                        │  ├─repository
                        │  │      AccommodationRepositoryTest.java
                        │  │
                        │  ├─service
                        │  │      AccommodationServiceTest.java
                        │  │
                        │  └─specification
                        │          AccommodationSpecificationTest.java
                        │
                        ├─auth
                        │  ├─controller
                        │  │      UserControllerTest.java
                        │  │
                        │  ├─mock
                        │  │      WithMockCustomUser.java
                        │  │      WithMockCustomUserSecurityContextFactory.java
                        │  │
                        │  ├─repository
                        │  │      UserRepositoryTest.java
                        │  │
                        │  └─service
                        │          UserServiceTest.java
                        │
                        ├─likes
                        │  ├─controller
                        │  │      UserLikeControllerTest.java
                        │  │
                        │  ├─model
                        │  │  ├─dto
                        │  │  │      UserLikeResponseDtoTest.java
                        │  │  │
                        │  │  └─entity
                        │  │          UserLikeEntityTest.java
                        │  │
                        │  ├─repository
                        │  │      UserLikeRepositoryTest.java
                        │  │
                        │  └─service
                        │          UserLikeServiceTest.java
                        │
                        ├─reservation
                        │  ├─config
                        │  │      TestDatabaseConfig.java
                        │  │
                        │  ├─controller
                        │  │      ReserveControllerTest.java
                        │  │
                        │  ├─repository
                        │  │      ReservationRepositoryTest.java
                        │  │
                        │  └─service
                        │          ReserveServiceLockTest.java
                        │          ReserveServiceTest.java
                        │
                        └─room
                            ├─controller
                            │      RoomControllerTest.java
                            │
                            ├─repository
                            │      RoomRepositoryTest.java
                            │
                            └─service
                                    RoomServiceTest.java
```
</div>
</details>



