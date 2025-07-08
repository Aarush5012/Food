
# Zaika 🍽️
Zaika is a full-stack restaurant menu and ordering system with user roles (Admin, User, Guest). It supports interactive cart-based ordering, price editing for admins, and secure login via Spring Security. Built with **Spring Boot**, **Thymeleaf**, and **MySQL**, it delivers a clean, modern UI with real-time menu item updates and user-specific navigation.

![Zaika Homepage](./screenshots/zaika_homeP.png)

---

## Features

- **Dynamic Menu System**
    - **Admin Menu**: View, edit, and update prices with live buttons.
    - **User Menu**: Add items to cart using `+/-` buttons and checkout.
    - **Guest Menu**: Explore items, but redirected on checkout.

- **User Roles & Authentication**
    - Spring Security with custom `UserDetailsService`
    - Login and registration system
    - Role-based redirection (Admin → dashboard, User → menu)

- **Cart Functionality**
    - Quantity management per item
    - Subtotal/total updates in real-time
    - Cart persistence across views (menu, cart)

- **Order Placement**
    - On successful order, quantities reset and confirmation is shown.

- **Modular Architecture**
    - Clean controller-service-entity-repository structure
    - Uses Thymeleaf fragments (`navbar`, etc.)

- **Assets**
    - Organized image directory
    - Global CSS for consistent styling

---

## Technology Stack

- **Backend**: Spring Boot, Spring MVC, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript
- **Database**: MySQL
- **Security**: BCrypt password hashing, role-based access
- **IDE**: IntelliJ IDEA

---

## Setup and Installation

### 1. Clone the Repository

```bash
git clone https://github.com/aayien-IITI/zaika.git
cd zaika
```

### 2. Configure MySQL

- Create a database `zaika` in MySQL.
- Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/zaika
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

### 4. Access the App

Visit: `http://localhost:8080`

---

## Screenshots



- ![Login Page](./screenshots/zaika_login.png)
- ![User Menu](./screenshots/zaika_menu.png)
- ![Admin Menu](./screenshots/zaika_adminMenu.png)
- ![Cart](./screenshots/zaika_cart.png)
- ![Order Success](./screenshots/zaika_orderPlaced.png)

---

## Project Structure

```bash
src/
├── main/
│   ├── java/com.adityaPandit.zaika/
│   │   ├── config/                  # Security and exception handlers
│   │   ├── controllers/            # All MVC controllers
│   │   ├── entities/               # JPA entity classes
│   │   ├── repositories/           # Spring Data repositories
│   │   ├── security/               # Custom security configuration
│   │   └── services/               # Business logic
│   ├── resources/
│   │   ├── static/
│   │   │   ├── css/
│   │   │   └── images/
│   │   └── templates/              # Thymeleaf HTML templates
│   └── application.properties
└── test/
```

---

## License

MIT License
