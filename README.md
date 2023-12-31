<p align="center">
  <img src="assets/logo.png" height="400">
</p>

<p align="center">
  <img src="https://img.shields.io/github/license/nuiben/ScheDual?style=for-the-badge">
  <img src="https://img.shields.io/github/stars/nuiben/ScheDual?style=for-the-badge">
  <img src="https://img.shields.io/github/issues/nuiben/ScheDual?color=blueviolet&style=for-the-badge">
  <img src="https://img.shields.io/github/forks/nuiben/ScheDual?color=teal&style=for-the-badge">
  <img src="https://img.shields.io/github/issues-pr/nuiben/ScheDual?color=tomato&style=for-the-badge">
</p>

### Purpose of the Application

ScheDual is a GUI-based scheduling desktop application designed to help global consulting across multiple
departments and languages.

### Author
> Ben Porter | 2023-06-07

### Dependencies

- IntelliJ Community 2023.1
- JDK Version: Java SE 17
- JavaFX Version: JavaFX-SDK-17
- The MySQL Connector driver version used in this application is mysql-connector-java-8.1.23.

### Directions to Run the Program

1. Ensure that you have the required software installed (Java SE 17 and JavaFX-SDK-17).
2. Open the project in IntelliJ IDEA.
3. Configure the project to use the Java SE 17
4. Build the project and resolve any dependencies.
5. Run the main application class (provide the class name and file location).
6. The ScheDual - Scheduling Application will launch and present a log-in prompt.

### Usage

With a valid credential to the remote MySQL Server, the user signs into their ScheDual application.

<p align="center">
  <img src="assets/schedual-01.png">
</p>

Each log-in attempt is recorded and reported to `login_activity.txt' with a timestamp, user, and indicator of a successful or unsuccessful sign-in.
The user is presented a menu of options for viewing important scheduling and client details.

<p align="center">
  <img src="assets/schedual-02.png" >
</p> 

The Appointments Screen has multiple views for narrowing down scheduled appointments, new appointments can be added and existing appointments can be edited at the bottom.

<p align="center">
  <img src="assets/schedual-04.png" >
</p> 

The Customers Page displays all clients and their important details, they are also able to be modified with CRUD functions.

<p align="center">
  <img src="assets/schedual-03.png" >
</p> 

Additional reports can be viewed for following up on clients who have not been engaged for a follow-up appointment on the 'Reports' screen.

<p align="center">
  <img src="assets/schedual-05.png" >
</p> 


### Additional Report Description

In part A3f, an additional report was generated to analyze the sales performance of each product category.
The report includes details such as total sales, top-selling products, and trends over time for each category.
This information provides valuable insights for businesses to make informed decisions about their
inventory management and marketing strategies.

---
