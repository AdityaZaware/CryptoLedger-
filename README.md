
# CryptoLedger Dashboard

CryptoLedger is a cryptocurrency trading and management platform built with Spring Boot and MySQL. It allows users to buy and sell cryptocurrencies, track prices in real-time, manage their watchlist, place orders, and perform various other actions like withdrawals and password recovery. The platform integrates with CoinGecko API for real-time crypto prices and Razorpay for payment processing.

## Features

- **Coin Service**: Provides real-time cryptocurrency prices from the CoinGecko API.
- **Watchlist Service**: Allows users to track and manage a personalized watchlist of cryptocurrencies.
- **Order Service**: Lets users place buy/sell orders and track their transaction history.
- **Forgot Password**: Implements a secure password recovery system with email verification.
- **Withdrawal Service**: Enables users to withdraw funds to external wallets and manage withdrawal requests.
- **Two-Step Verification**: Enhances account security during registration and login.
- **User to User payment**: Enables users to transfer funds between their wallets.
- **Payment Service**: Integrates with Razorpay for payment processing.


## Tech Stack

- **Backend**: Spring Boot
- **Database**: MySQL
- **Real-time Prices**: CoinGecko API
- **Payment Gateway**: Razorpay
- **Security**: JWT Authentication, Two-Step Verification
- **Notifications**: Email

## Installation

### Prerequisites

- Java 8 or above
- MySQL Database
- Maven
- Razorpay API Key (for payment integration)

### Steps to Run Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/CryptoLedger.git
   cd CryptoLedger
   ```

2. Set up MySQL database:
    - Create a new database in MySQL.
    - Import the provided SQL schema or run the application to auto-create tables.

3. Update application properties:
    - In `src/main/resources/application.properties`, update the MySQL database connection details.

4. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. The application will be running on `http://localhost:8085`.

### API Endpoints for Users Login/Signup

| Endpoint               | Method | Description                               |
|------------------------|--------|-------------------------------------------|
| `/auth/register`       | POST   | Register a new user                       |
| `/auth/signin`         | POST   | Login to the system                       |

## API Endpoints for User Controller

| Endpoint                                              | Method | Description           |
|-------------------------------------------------------|--------|-----------------------|
| `/api/user/profile`                                   | GET    | Getting User profile  |
| `/api/user/verfication/{verficationType}/send-otp`    | POST   | Sending two-fact code |
| `/api/user/enableTwoFactorAuth/verify-otp/{otp}`      | PATCH  | enableTwoFactorAuth   |
| `/auth/user/resetPassword/send-otp/{verficationType}` | POST   | sendForgotPasswordOtp |
| `/auth/user/resetPassword/{email}/verify-otp`         | PATCH  | resetPassword         |

## API Endpoints for Coin Controller

| Endpoint                   | Method | Description        |
|----------------------------|--------|--------------------|
| `/coins`                   | GET    | Getting Coins List |
| `/coins/{coinId}/chart `   | GET    | Get Market Chart   |
| `/coins/ serach`           | GET    | Search Coin        |
| `/coins/top50 `            | GET    | Top50 Coins        |
| `/coins/treading`          | GET    | getTreadingCoins   |
| `/coins/details/{coinId} ` | GET    | getTreadingCoins   |

## API Endpoints for WatchListController

| Endpoint                         | Method | Description         |
|----------------------------------|--------|---------------------|
| `/api/watchlist/user`            | GET    | User WatchList      |
| `/api/watchlist/create `         | POST   | Create WatchList    |
| `/api/watchlist/{watchlistId}`   | GET    | getWatchlistById    |
| `/api/watchlist/add/{coinId} `   | PATCH  | addItemToWatchList  |

## API Endpoints for Wallet

| Endpoint                           | Method | Description             |
|------------------------------------|--------|-------------------------|
| `/api/wallet`                      | GET    | User Wallet             |
| `/api/wallet/transfer/{walletId} ` | PUT    | Sending money to Friend |
| `/api/wallet/order/{orderId}/pay`  | PUT    | Pay Order payment       |
| `/api/wallet/deposit  `            | PUT    | Add Money To Order      |

## API Endpoints for Payment

| Endpoint                                               | Method | Description          |
|--------------------------------------------------------|--------|----------------------|
| `/api/payment/{paymentMethod}/amount/{amount}`         | Post   | Getting Payment Link |


## API Endpoints for Orders

| Endpoint                | Method | Description     |
|-------------------------|--------|-----------------|
| `/api/order/payment`    | POST   | PayOrderPayment |
| `/api/order/{orderId} ` | POST   | Get Order       |
| `/api/order/all`        | GET    | Get all orders  |

## API Endpoints for Withdrawal

| Endpoint                                      | Method | Description                    |
|-----------------------------------------------|--------|--------------------------------|
| `/api/withdrawal/withdrawalRequest/{amount} ` | POST   | Withdrawal Request             |
| `/api/withdrawal/{id}/proceed/{accept} `      | PATCH  | proceed withdrawal request     |
| `/api/withdrawal/history`                     | GET    | Get withdrawal history         |
| `/api/withdrawal/admin/history`               | GET    | Get withdrawal request history |



## Security Considerations

- Passwords are stored securely using bcrypt hashing.
- Forgot Password feature implements a secure password recovery system with email verification.
- JWT (JSON Web Token) is used for session management and authentication.
- Two-step verification adds an additional layer of security during user registration and login.

## Future Enhancements

- Integration with more payment gateways for fiat withdrawals.
- Support for multiple cryptocurrencies and exchanges.
- Advanced features like stop-loss orders and margin trading.
- Enhanced user experience and UI design.

## Contribution

We welcome contributions from the community!.

## Doubts

If you have any questions or need further assistance, please feel free to reach out.

- Email: zawareaditya04@gmail.com
- LinkedIn: https://www.linkedin.com/in/aditya-zaware-310904259/


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
