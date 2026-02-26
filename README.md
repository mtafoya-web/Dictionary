# Dictionary Application (Java Swing)

![Java](https://img.shields.io/badge/Java-17+-blue)
![Swing](https://img.shields.io/badge/GUI-Swing-orange)
![Architecture](https://img.shields.io/badge/Architecture-MVC-green)
![Testing](https://img.shields.io/badge/Tested_with-JUnit5-red)
![Status](https://img.shields.io/badge/Status-Active-success)

A desktop **Dictionary Application** built with **Java and Swing**,
designed using a clean MVC architecture.\
This project demonstrates strong fundamentals in data structures,
separation of concerns, event-driven programming, validation logic, and
scalable software design.

------------------------------------------------------------------------

## Features

-   Add and update words with frequency metadata\
-   Case-insensitive word search\
-   Remove dictionary entries\
-   Prefix-based filtering\
-   Sorted word display\
-   Import dictionary data from file\
-   Export dictionary data to file\
-   Input validation and controlled UI editing state

------------------------------------------------------------------------

###  Search Word: 
-    Finds the word placed in the search box to the bottom left.
-    If a prefix is placed the word or words with that prefix will appear.
![Search Word](images/searchWordV2.png)

---

###  Add Word
-    Will clear the textField to the right.
-    Requires user to input information about the word.
-    Requires the Save button to be pressed to update the dictionary.
![Add Word](images/addWordV2.png)

---

###  Delete Word
-    Hover over the word you want to delete.
-    press the delete button.
![Delete Word](images/deleteWordV2.png)

---

###  Import Dictionary
-    Finds the file by opening up the user Filepath.
-    Each line must contain exactly 5 fields seperated by |
-    example: word|pronunciation|definition|example|syn1, syn2, syn3
-    
![Import](images/importV2.png)

---

###  Top 5 Most Searched
-    Displays the 5 most searched words in the dictionary
-    A search counts as a top level search or click on the textField
![History](images/historyV2.png)

---

###  Exit Confirmation
-    Displays and exit warning and confirmation.
![Exit](images/exitV2.png)

---

### 🕰 Version 1 (Original Prototype)
-    The first version of this project
-    File structured unorganized and not following object orientation
![Dictionary V1](images/DictionaryV1.png)
```

------------------------------------------------------------------------

## Architecture

This application follows an MVC-inspired layered design to ensure
maintainability and testability.

### View -- `DictionaryPanel`

-   Contains all Swing UI components\
-   Responsible only for displaying data\
-   Exposes listener hooks for user interaction\
-   No business logic

### Controller -- `DictionaryController`

-   Handles user events\
-   Performs validation\
-   Connects the View to the Service\
-   Manages application state transitions

### Service -- `DictionaryService`

-   Implements core dictionary logic\
-   Uses `HashMap<String, DictionaryEntry>` for storage\
-   Handles add, update, delete, search, sort, and prefix filtering\
-   Fully independent from UI

### Data Flow

    User Action
         ↓
    Controller
         ↓
    DictionaryService
         ↓
    Controller
         ↓
    View Update

------------------------------------------------------------------------

## 📚 Data Structure Design

The dictionary is implemented using:

``` java
Map<String, DictionaryEntry>
```

### Why `HashMap`?

-   O(1) average lookup time\
-   Efficient for frequent searches\
-   Ideal for key-value dictionary modeling\
-   Keys normalized to lowercase for case-insensitive behavior

------------------------------------------------------------------------

## Version Evolution

### Version 1 -- Functional Prototype

-   Basic dictionary operations\
-   Import/export support\
-   UI and logic tightly coupled

### Version 2 -- Refactored Architecture

-   Proper MVC separation\
-   Listener-based event handling\
-   `Optional` for safe null handling\
-   Service-layer unit testability\
-   Cleaner validation and state management

The refactor significantly improved scalability, readability, and
maintainability.

------------------------------------------------------------------------

## Testing

### Unit Testing (JUnit 5)

`DictionaryService` is independently testable:

-   Case-insensitive lookup\
-   Add/update behavior\
-   Deletion logic\
-   Prefix filtering\
-   Sorting correctness

------------------------------------------------------------------------

## How to Run

1.  Clone the repository
2.  Open the project in IntelliJ IDEA (or preferred IDE)
3.  Ensure JDK 17+ is configured
4.  Run the main application class

------------------------------------------------------------------------

## Technologies Used

-   Java\
-   Swing\
-   HashMap\
-   Optional\
-   JUnit 5\
-   IntelliJ IDEA

------------------------------------------------------------------------

## Engineering Highlights

-   Designed using MVC separation of concerns\
-   Optimized search performance with `HashMap`\
-   Defensive programming using `Optional`\
-   Structured validation before persistence\
-   Refactored from prototype to scalable architecture

------------------------------------------------------------------------

## What This Project Demonstrates

-   Strong understanding of core data structures\
-   Clean architectural design\
-   Event-driven desktop application development\
-   Refactoring for maintainability\
-   Designing for testability
