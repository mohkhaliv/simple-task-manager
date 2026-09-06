<img width="1859" height="996" alt="image" src="https://github.com/user-attachments/assets/b5622ca0-b1d5-4b71-9486-c85879b4bd1a" />

# Task Management System

Full-stack task management application built with Angular, Spring Boot, and MySQL.

## Live Demo
https://task-management-system-beryl-ten.vercel.app/

## Tech Stack
- Angular
- Spring Boot
- MySQL
- Docker
- JUnit, Mockito, MockMvc
- GitHub Actions
- Railway & Vercel

## Features
- Create, edit, delete, and toggle tasks
- Search and filter tasks
- REST API integration
- Input validation and exception handling

## Engineering Practices
- Controller–Service–Repository architecture
- Automated backend tests
- CI with GitHub Actions
- Dockerized backend + database
- Full-stack cloud deployment

## Architecture
```text
Angular Frontend (Vercel)
          | REST API
          v
Spring Boot Backend (Railway)
          |
          v
MySQL Database (Railway)
```

## Run Locally

Backend:
```bash
cd backend
mvn spring-boot:run
```
Frontend:
```bash
cd frontend
npm install
npm start
```
Docker:
```bash
docker compose up --build
```
## Testing
```bash
cd backend
mvn test
```
