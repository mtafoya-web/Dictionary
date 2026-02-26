Dictionary Application (Java Swing)

A desktop Dictionary Application built in Java using Swing.
This project demonstrates data structure selection, MVC architecture, event-driven programming, validation logic, and scalable application design.
 
Project Overview

This application allows users to:

Add and update words with metadata

Search for words (case-insensitive)

Track and display word frequency

Remove dictionary entries

Import and export dictionary data

Filter words by prefix

Manage dictionary state using a structured MVC architecture

The project evolved from a functional prototype (Version 1) to a properly layered and testable architecture (Version 2).

Architecture (Version 2)

The application follows a clean MVC-inspired structure:

1️⃣ View – DictionaryPanel

Contains all Swing UI components

Displays dictionary data

Exposes listener hooks

Contains no business logic

2️⃣ Controller – DictionaryController

Handles user interactions

Performs input validation

Connects UI to service layer

Manages application state transitions

3️⃣ Service – DictionaryService

Core dictionary logic

Stores data in a HashMap

Implements add, delete, search, sort, and prefix filtering

Independent of UI


User Interaction
        ↓
Controller
        ↓
DictionaryService (business logic)
        ↓
Controller
        ↓
View Update

This separation improves:

Maintainability

Testability

Scalability

Code clarity

Data Structure Design

The dictionary is implemented as:

Map<String, DictionaryEntry>
Why HashMap?

O(1) average lookup time

Efficient for frequent searches

Ideal for key-value dictionary model

Keys normalized to lowercase for case-insensitive search

Version 1 Features

Version 1 focused on core functionality:

Add word with frequency

Find word

Remove word

Display frequent words

Import dictionary from file

Export dictionary to file

Basic validation

Limitations:

UI and logic tightly coupled

Harder to test independently

Limited scalability

Version 2 Improvements

Version 2 refactored the system to introduce:

MVC separation

Listener hook architecture

Optional for safe null handling

Controlled UI editing state

Structured validation logic

Service-layer testability

Cleaner event handling

🧪 Testing Strategy
Unit Testing

DictionaryService is independently testable using JUnit:

Case-insensitive lookup

Add/update behavior

Deletion logic

Prefix filtering

Sorting correctness

Manual Testing

Duplicate word handling

Invalid frequency validation

Edge-case user input

File path validation

UI state transitions

🛠 Technologies Used

Java

Swing (GUI framework)

HashMap (Data structure)

Optional (Safe null handling)

JUnit 5 (Unit testing)

IntelliJ IDEA

Key Engineering Decisions

Used HashMap for performance optimization

Implemented case-insensitive keys via normalization

Introduced MVC separation to avoid UI-business logic coupling

Wrapped service lookups in Optional to eliminate null risks

Designed UI with event-driven hooks to decouple controller logic

Enabled structured validation before data persistence

Learning Outcomes

This project demonstrates:

Data structure selection based on performance requirements

Event-driven programming in Swing

Clean separation of concerns (MVC pattern)

Defensive programming with Optional

Application-level validation strategies

Refactoring from prototype to scalable architecture

Designing for testability
