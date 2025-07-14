# Questions for me:

## Biggest strength: Active Caring about others. Filters into every aspect programming, whether its P.O., user, developer, QA...etc
## Biggest weakness: Perfectionist. Sounds like a strength, but no code is perfect and can predict everything.
## Thinking of every developer that comes after me, and expect as much as possible, for the app to never stop growing.

## Most proud: 20 point story with extensive frontend/backend changes with completely new responsive/ui page. Last project. 0 Bugs
## Most NOT proud: New project I was put on and woefully unprepared
## Most NOT proud: Used to have trouble with deadlines, trying to make things perfect.

## Most annoying thing about programming is 
### Configuration. Do it rarely, and easy to mess up, hard to debug
### Complexity of systems.
### Up and down nature of programming


## Important to me:
### Planning/Architecture: Scaling, maintainability, distribution of knowledge, team code knowledge
### Workload: Consistent overtime indicates a 
### Communication: Many working parts requires communication
### Culture: Cohesion > competitive ambition.

# Common Interview Concepts and Questions

# *************************Shared Concepts*************************

### HTTP
![img_1.png](img_1.png)
#### TCP/IP
Application - User interaction w/Application
Transport - Data Transmission
Internet - Connection
Network Access(3 comb Hardware, 2, 3)
TCP/UDP
Websocket: IP+Port i.e. 127.0.0.1:8080

### Threading vs Async
Async: Execution of a single thread doesn't wait for http calls, but moves on and "returns" once the promise is resolved.
Threading: Multiple threads of execution. 
Sleeping threads: Used to make sure threads don't step on each other. Avoid bad thread concurrency.

Shallow Copy: Reference. Stores the data structure and a reference to the original Object
Deep Copy: Value. Stores copy of the object. AVOIDS ACCIDENTAL DATA UPDATES.


### Stack and Heap memory
Stack: Function references and local variable in LIFO
Heap: Objects and data structures. Dynamic memory allocation

### Design Patterns
#### SOLID
#### MY MOST IMPORTANT
Single Responsibility
DRY
Abstraction
Dependency injection

### Abstraction
#### Provides re-usability. 
#### Services: Different modules can their own implementation of services/functions
#### Data Structures: Allows for treating a data structure as multiple data structures

### Composition and Inheritance.
"Has a": A car has a wheel, a truck has a wheel, a stroller has a wheel. More versatile
"Is a": A car is a vehicle. - Inherits properties from parent.

## MVC
Model-View-Controller
DB-UI-Service(API)

## Data structures: Format for arranging/constructing/Storing data

### Linear
#### IMPLEMENTATION USE CASES
* Hash: Speed. and order doesn't matter
* Tree: Sorted order, slower
* Linked: Insertion order preserved, slower

Arrays: *Fixed size and type*
Lists: *Dynamic size and need collection methods*   ordered, allows duplicates **CAN BE "DYNAMICALLY TYPED, BUT SACRIFICES TYPE SAFETY COMPILE TIME**
Sets: *Uniqueness needed* Single Element, Unique, unordered, no indexes
Stacks: DEPRECATED
Queues: FIFO
Map: *Key-Value*

### Non-Linear aka Abstract Data Structures
#### Trees: Consists of nodes. Each node can have 1 parent but multiple children
Binary Trees: When modified, tree is rearranged
Full: Each node has two children

#### Graphs

#### Static: Fixed size
#### Dynamic: Changeable size

<img src="./src/main/resources/DataStructureHierarchy.png" alt="isolated" width="800"/>

## **********************************PARADIGMS**********************************
### Object Orientated Programming

### Functional Programming
Recursion: Breaking problems down into smaller instances of the same
A function calls itself, adding function calls to the stack. Once the
final condition is met, the stack functions execute(LIFO) from the newest through
original and return the result.

PROS
*Less code*
*Tree Traversal usage*
CONS
*Stack Overflow Risk*
*High memory*
*Slower*

## **********************************JAVA**********************************

### JVM Compiles the Java Code into ByteCode which allows for the application to be platform independent

<img src="img/JVM Overview.jpeg" alt="JVMOverview" width="800"/>

### DESIGN PATTERNS
<img src="./src/main/resources/Java_Design_Patterns.png" alt="isolated" width="800"/>

#### Creational
Factory Method: Creates Factory Class
Abstract Factory: Creates Factory Objects
Singleton: Creates/ensures single instance
Builder: Complex data structures with optional fields.
Prototype: Copying objects from a prototyping.

#### Structural
Decorator: Decorator design pattern is used to modify the functionality of an object at runtime.
<img src="./src/main/resources/Decorator.jpg" alt="isolated" width="800"/>

Facade: Encapsulates complex system behind interface.

DAO: Data Access Object.

#### Behavioral
Observer:
- Watchers are notified upon a Watched state change.
- One-to-many
- State change causes triggers

Strategy:
- Uses different STRATEGIES decided by client at runtime.
- Lambdas replacing concrete implementation with the use of anonymous functions.
- Passing in Java Functional interfaces
- Consumer
- Context(strategy1, strategy2...etc)
-
#### SOLID
Single Responsibility
Open Closed: Extension = yes, Modification = no (Inheritance)
Liskov: Sub classes should be appropriate for base types (Inheritance. Test for inheritance correctness)
Interface Segregation: Clients should not be forced to depend on methods they do not use.
Dependency Inversion: High and Low modules depend on abstractions, not each other. (Constructors used to inject/create objects)


Dependency injection: Inversion of Control


#### MY MOST IMPORTANT
Abstraction because it:
- Teaches important concepts such as separation of concerns and how it promotes flexibility, scalability, and maintainability through reusable code.
- Without it, code becomes rigid and limits developers.
- Services: Different modules can have their own implementation of services/functions
- Data Structures: Allows for treating a data structure as multiple data structures
#### SOLID
#### Singleton
#### DAO
#### Factory
#### Observer

### Interfaces
Provides a level of abstraction which provides versatility for unique and individual implementations.

### Abstract Classes
Can contain implementation as well as declarations that must be implemented.

### Exceptions: Unexpected runtime occurrences which must be handled by the code.

#### Two Types:
Checked: Compile Time

Unchecked: Run Time

### Unit Testing: Writing code to test individual parts of code.
### Purpose:
#### Accurate Code Functionality
#### Early warning when new code breaks existing functionality
#### Demonstrates developer intent

#### When to use
Whenever possible.

### Use cases:
Happy path: When input is valid and what is expected
Negative path: Invalid input
Make sure exceptions are handled
All null test case to ensure that all null pointers are handled

## *************************************Javascript*************************************

### Angular

### React
Component Based. Builds components through a react wrapper which consists of HTML.
Components are reusable.

You will describe the UI you want to see for the different visual states of your component 
(“initial state”, “typing state”, “success state”), and then trigger the state changes in response to user input.

Example: A web page may have a page component which has children consisting of a Header, Footer, Side panel, Content components

#### React Recommended Frameworks Apr 2025
-Main
-Next.js
-Routing
-ReactRouter
-Data Fetching
-ReactQuery

#### Isomorphic rendering
Javascript runs both on the client and the server.
- Whereas Angular and Ember render on DOM load. But Javascript renders faster than the DOM.
- Why React uses the Virtual DOM.
**** HOOKS:
* Local State 
useState: declares a state variable that you can update directly. *EVENT HANDLERS*
useReducer: declares a state variable with the update logic inside a reducer function. 
  very similar to useState, but it lets you move the state update logic from event handlers 
  into a single function outside of your component.
* Parent/s state
useContext: reads and subscribes to a context.
* 3rd Party
useEffect connect to and synchronize with external systems. 
useRef: DOM/Browser Manipulation

* 
* STATE GOES DOWN.
* Lifting State to parent
* Class components: Do have state BUT can't use Hooks.
* Functional components: Don't have state BUT can use Hooks to make up for that.
* Hooks: allow using state and others without writing a class
  Virtual DOM: React creates an in-memory data structure cache, computes the resulting differences, and efficiently updates only the changed parts in the browser DOM. This approach significantly improves performance compared to direct DOM manipulation.

* JSX (JavaScript XML): A syntax extension that allows writing HTML-like code in JavaScript. JSX makes the code more readable and expressive while providing the full power of JavaScript.
* Unidirectional Data Flow: React follows a one-way data binding model where data flows from parent to child components. This makes the code more predictable and easier to debug.
* Declarative UI: React allows you to describe what your UI should look like for a given state, and it handles the DOM updates when the underlying data changes.
*** Advanced Features:
* React Hooks: Introduced in React 16.8, hooks allow using state and other React features in functional components without writing classes.
* Context API: Provides a way to share values between components without explicitly passing props through every level of the component tree.
* Error Boundaries: Components that catch JavaScript errors anywhere in their child component tree and display fallback UI instead of crashing.
* Server-Side Rendering (SSR): Enables rendering React components on the server before sending HTML to the client, improving performance and SEO.
* Concurrent Mode: A set of new features (in development) that help React apps stay responsive and gracefully adjust to the user's device capabilities and network speed.
* React Server Components: A new feature that allows components to be rendered entirely on the server, reducing bundle size and improving performance.
* Suspense: A feature that lets your components "wait" for something before rendering, supporting code-splitting and data fetching with cleaner code.

* Uncontrolled components: manage their own state internally via the DOM (<input>, <textarea>, or <select>)
* A higher-order component (HOC) is a function that takes a component and returns a new enhanced component 
    with additional props, behavior, or data

### Node
Backend routing javascript framework.
Express.js

### Typescript
Statically typed wrapper with a JS runtime
Types are determined based off of common properties.
Union Types

## *****************************************FLUTTER*****************************************
<img src="./src/main/resources/DataStructureHierarchy.png" alt="isolated" width="800"/>


### Widget lifecycle
#### Constructs widget       sets state    builds     updates       rebuilds   releases resources 
####                                                                           though widget may 
####                                                                            be added back
#### WidgetConstructor() -> initState() -> build() -> setState() -> build() -> deactivate()   -> dispose()

### Widget lifecycle methods
createState(): This method is required and creates a State object for the widget. It holds all the mutable state for that widget. 
The State object is associated with the BuildContext by setting the mounted property to true.

initState(): This method is automatically called after the widget is inserted into the tree. 
It is executed only once when the state object is created for the first time. Use this method for initializing variables and subscribing to data sources.

didChangeDependencies(): The framework calls this method immediately after initState(). 
It is also called when an object that the widget depends on changes. Use this method to handle changes in dependencies, but it is rarely needed as the build method is always called after this.

build(): This method is required and is called many times during the lifecycle. 
It is called after didChangeDependencies() and whenever the widget needs to be rebuilt. Update the UI of the widget in this method.

didUpdateWidget(): This method is called when the parent widget changes its configuration 
and requires the widget to rebuild. It receives the old widget as an argument, allowing you to compare it with the new widget. 
Use this method to handle changes in the widget's configuration.

setState(): The setState() method notifies the framework that the internal state of the widget has changed and needs to be updated. 
Whenever you modify the state, use this method to trigger a rebuild of the widget's UI.

deactivate(): This method is called when the widget is removed from the widget tree but can be reinserted before the current frame changes are finished. 
Use this method for any cleanup or pausing ongoing operations.

dispose(): This method is called when the State object is permanently removed from the widget tree. Use this method for cleaning up resources, such as data listeners or closing connections.

## ***************************************Machine Learning***************************************
Structured Learning: Labeled
Unstructured Learning: Unlabeled
Shared Learning: AI learns from AI
## ***************************************Databases***************************************

### Relational
ORM
SQL
Complex structure based on relationships between tables/data

### NoSQL
Keys
Simple structures
Fast

### AWS
Create EC2 Instance
Launch Instance

#### SSH into server:
https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-instance-connect-set-up.html

ssh -i my_ec2_private_key.pem ec2-user@ec2-a-b-c-d.us-west-2.compute.amazonaws.com

#### Install Connect client

sudo yum install ec2-instance-connect

### LinkedInR

## ***************************************SECURITY***************************************

### Malware: Malicious Software
Viruses: An unsolicited and unwanted malicious program.
Crypto-malware: A malicious program that encrypts programs and files on the computer in order to extort money from the user.
Ransomware: Denies access to a computer system or data until a ransom is paid. Can be spread through a phishing email or unknowingly infected website.
Worm: A self-contained infection that can spread itself through networks, emails, and messages.
Trojan: A form of malware that pretends to be a harmless application.
Rootkit: A backdoor program that allows full remote access to a system.
Keylogger: A malicious program that saves all of the keystrokes of the infected machine.
Adware: A program that produces ads and pop ups using your browser, may replace the original browser and produce fake ads to remove the adware in order to download more malware.
Spyware: Software that installs itself to spy on the infected machine, sends the stolen information over the internet back to the host machine.
Bots: AI that when inside an infected machine performs specific actions as a part of a larger entity known as a botnet.
RAT (Remote Access Trojan): A remotely operated Trojan.
Logic bomb: A malicious program that lies dormant until a specific date or event occurs.
Backdoor: Allows for full access to a system remotely.
