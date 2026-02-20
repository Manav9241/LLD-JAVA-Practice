### FACADE DESIGN PATTERN

- Facade pattern provides a simplified, unified interface to a set of complex subsystem
- It hides the complexity of the system and exposes only what is necessary

### Facade V/S Adapter

Over the standard UML it is noticed that both only create a mediator class that interracts with the client and the system.

The main difference is in the intent of that object.

- Facade: Hides complexity
- Adapter: To establish an interraction between completely different interfaces.

### Principle of Least Knowledge

Take any object, now from any method in that object, principle tells you to invoke only those methods that belong to:

- The object itself
- The object passed in as a parameter
- Any object that method creates
- Any object with (HAS-A) relationshipt

