**CPEN 221 / Fall 2020 / MiniProject 2**

The Graph ADT and Virtual Worlds
===

This mini-project has two parts. Part 2 can benefit from your implementation of Part 1.

## Part 1: Unweighted Directed Graphs

In this part of the mini-project, you will implement a `Graph` interface using two different graph representations. You will then develop several algorithms that use the `Graph` interface that might be used in a social network.

Your goals for this machine problem are to:
+ Understand and apply the concept of encapsulation;
+ Understand interfaces;
+ Understand what graphs are and how they can be represented;
+ Implement some basic graph algorithms.

**Preliminary reference**: Wikipedia entry on Graphs https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)

### Task 1: Basic Graph Implementations
First, write two classes that implement the `ca.ubc.ece.cpen221.graphs.core.Graph` interface, which represents a _directed_ graph.
+ **Adjacency List**: Inside the package `ca.ubc.ece.cpen221.graphs.one`, implement the `AdjacencyListGraph` class. Your implementation must internally represent the graph as an _adjacency list_. If you are not familiar with the adjacency list representation of graphs, see the [Wikipedia page on the adjacency list representation](https://en.wikipedia.org/wiki/Adjacency_list) as a reference.
+ **Adjacency Matrix**: Next, implement the `AdjacencyMatrixGraph` class in the `ca.ubc.ece.cpen221.graphs.one` package. Your implementation must internally represent the graph as an adjacency matrix. If you are not familiar with the adjacency matrix representation of graphs, see the [Wikipedia page on the adjacency matrix representation](https://en.wikipedia.org/wiki/Adjacency_matrix) as a reference.

This task uses Java Generics. Each `Vertex` may hold content of a specified type. For example, a graph that where each vertex containts a `String` as content would be declared using the type `Graph<String>` and a vertex in such a graph would be of type `Vertex<String>`. (This is similar to how `List`s are implemented in the standard Java library.)

### Task 2: Graph Algorithms 1
For this part of the assignment, you will implement algorithms that might be used for social network analysis using your graph implementations.

Your algorithms must use only the methods provided in the interface, and can not use any features specific to the implementation of `Graph` being used. Your algorithms must work correctly on any correct implementation of a `Graph`, including your `AdjacencyMatrixGraph` and `AdjacencyListGraph` implementations.

_All your algorithms must work on directed graphs (digraphs)._

+ **Breadth first search (BFS)**: Implement the [breadth first search](https://en.wikipedia.org/wiki/Breadth-first_search) algorithm to traverse a graph.
+ **Depth first search (DFS)**: Implement the [depth first search](https://en.wikipedia.org/wiki/Depth-first_search) algorithm to traverse a graph.

For both BFS and DFS above, you should use traverse the entire graph. You should return a set of lists (`Set<List<Vertex>>`). Each list in the set is a connected component of the graph. The vertices in each list are in the order they were visited by the traversal routines. More specifically, start a BFS or DFS traversal at every possible vertex and each such traversal will produce a list. The set of all these lists is what you want to return. Furthermore, when deciding which vertex to visit next, you should select the vertex with the lexicographically smaller label. This approach ensures that there is exactly one list corresponding to a BFS or DFS starting at a given vertex.

### Task 3: Graph Algorithms 2

+ **Common upstream vertices**: Given a graph _G_ and two vertices _a_ and _b_ in _G_, your implementation should return a list of all vertices _u_ such that there is an edge from _u_ to _a_ and an edge from _u_ to _b_. If there are no such vertices then your implementation should return an empty list.
+ **Common downstream vertices**: Given a graph _G_ and two vertices _a_ and _b_ in _G_, your implementation should return a list of all vertices _v_ such that there is an edge from _a_ to _v_ and an edge from _b_ to _v_.  
+ **Distance**: Implement a method to find the distance between two vertices in an unweighted graph. In an unweighted graph _G_, given two vertices _s_ and _t_, the distance between the two vertices is the minimum number of edges that would have to be traversed to get to _t_ from _s_. In other words, the distance between two vertices is the length of the shortest path between the two vertices. The distance between a vertex and itself is 0. If no path exists from _s_ to _t_ then your method should take appropriate action.
+ **Graph diameter**: The diameter of a graph is the maximum distance among the distances between all pairs of vertices in the graph. If it is not possible to get to vertex _t_ from vertex _s_ then the distance between those two vertices, and consequently the graph diameter, is _infinity_. We will, however, adopt a more relaxed definition where we will specify the diameter as being the maximum finite distance among pairs of vertices except in the case when all distances are infinite (when we will treat the diameter as infinite). Implement a method to determine the diameter of a graph.

### Task 4: Analyzing Social Networks

> As long as your code runs in a _reasonable_ amount of time, and returns the correct values, you do not need to worry about the [time complexity](https://en.wikipedia.org/wiki/Time_complexity) of your algorithms. _However_, there may be some extra credit for particularly efficient implementations.

An anonymized dataset from Twitter is provided in the file `datasets/twitter.txt`. The file contains many rows, with each row containing an entry of the form `a -> b` to indicate that `b` follows `a`.

We would like to answer the following questions from this dataset:

+ **Common influencers**: Given users `a` and `b`, who are the common users that both `a` and `b` follow?
+ **Count retweets**: Suppose user `a` tweets a message, what is the minimum number of retweets needed before `a`'s tweet appears in `b`'s feed? (The assumption here is that a tweet will appear in `b`'s feed if the tweet originated from one of the users that `b` follows.)

You should implement suitable methods for such analysis, including a `public static void main(String[ ] args)` method in a class named `TwitterAnalysis`. The `main` method in `TwitterAnalysis` should handle command-line arguments.

## Part 2: Virtual Worlds

In this part of the mini-project, you will complete and extend a virtual world. This world contains many different items -- animate and inanimate -- which can interact with each other in complex ways. For example, `Fox`es hunt for `Rabbit`s, which in turn try to outwit their predators. It's up to you to determine just how complex the interaction will be.

Your goals for this machine problem are to:

+ Practice encapsulation and code reuse with subtypes and delegation;
+ Program against existing interfaces;
+ Think about code from a design point of view.

> The basic code structure given to you for this machine problem uses the **delegation pattern** in the implementation of the AI. You may want to read more about the delegation pattern starting with the [Wikipedia article](https://en.wikipedia.org/wiki/Delegation_pattern).

We have developed a virtual-world environment that can simulate the interaction of many items and actors. This world is flat and consists of many fields that can each have one `Item`. In the beginning the world will contain `Grass`, `Rabbit`s, and `Fox`es, and you will add additional `Item`s as part of this machine problem.

Time in the virtual-world simulation progresses in discrete steps; in every step an `Actor` may act, for example, by moving, eating, or breeding. We have provided some simple AIs for rabbits and foxes; you will implement more intelligent AIs for them and other items you create.

This machine problem has two parts: 

+ filling the world with additional items, 
+ and creating intelligent AIs for `Rabbit`s and `Fox`es.

### Task 5: Give Life To The Virtual World

This is a creative task. Add at least **four** new subtypes of `Item` to the virtual world:

+ `Animal`s: In the `animals` package create at least one additional animal type, such as lions, flies, and elephants.
+ `Vehicle`s: In the `vehicles` package create at least one vehicle, which can run over (destroy) everything with a lower strength but will crash (be destroyed) when running into something with a greater strength. Like real vehicles, your vehicles should build momentum when moving, so it takes time for them to accelerate or brake or turn; they can change directions only at low speed. Note that the speed of a `Vehicle` is controlled by the cool-down period.
+ **Your own category**: In a separate package implement at least one subtype of `Item`. Examples might include tornadoes and earthquakes, mountains and cliffs, even [Scarlet Witch](https://en.wikipedia.org/wiki/Scarlet_Witch), [Master Yoda](https://en.wikipedia.org/wiki/Yoda) or [Voldemort](https://en.wikipedia.org/wiki/Lord_Voldemort). _Use your imagination!_

You have considerable freedom in this machine problem for which items you add and how your items behave. Your items might range from a simple stone to sophisticated characters and weapon systems, from real-world animals to science fiction creatures, or include technical objects.

When designing your items think about subtyping and interfaces. You may want to introduce additional interfaces or abstract classes where suitable. You may get extra credit for creativity.

### Task 6: Create AIs for Rabbits and Foxes

Provide a more intelligent behavior for Rabbits and Foxes by providing an implementation of their AI classes. The best AIs are those that generate the largest average animal population over the entire time, measured separately for rabbits and foxes.

Your AI implementations may benefit from using your graph implementations from earlier.

### Background for Part 2

This section describes the design of the virtual world and its rules.

#### Objects In The World

The world contains the following object types, with each type having different properties and specifications described here.

+ `Item`: An `Item` represents a physical object in the virtual world that occupies a field in a specific location, where it is represented with a picture. For example, `Fox`es, `Rabbit`s, and `Grass` are Items.
+ `Actor`: An Actor can actively affect the state of the world. Many `Item`s are `Actor`s; they can decide to move, eat, breed, or perform other actions. The world regularly determines each `Actor`'s next action by calling `getNextAction( )`; the Actor returns a `Command` that represents the next action. `Actor`s can act at different speeds, acting on every step in the world or only every n<sup>th</sup> step -- the speed of an actor is determined by its cool-down period.
+ `ArenaAnimal`: Rabbits and foxes are special, and we have already provided an implementation of them. Rabbits and foxes can only see the immediate world around them (determined by a view range, measured in [Manhattan distance](https://en.wikipedia.org/wiki/Taxicab_geometry)). They have energy, starting with a default value and increasing when they
   eat something, but also slowly decreasing each time they act. Your additional items may behave like arena animals, but are not required to do so.

Additionally, you will find interfaces representing `MoveableItem`s (that can be moved to an adjacent location at each step), `LivingItem`s (that are actors which can move, eat, breed, and have energy), `Food` (representing edible items providing plant or meat-based calories), and so forth.

There are also implementations of `Grass`, `Gardener`s, `Rabbit`s (eating Grass), `Fox`es (eating `Rabbit`s), and `Gnat`s (generally behaving just stupidly) provided in the world already. You may change these implementations to foster reuse, but the behavior of these existing items should not be changed. 

For rabbits and foxes you should provide corresponding AIs that survive the best in the arena by implementing the AI interfaces.

#### `Command`s and Behaviours

All actors are periodically asked for their next action, which they provide by returning a `Command`. How often they are asked depends on their speed; `getCoolDownPeriod` returns the number of steps to be skipped before the next action. AIs of rabbits and foxes may only return instances of the predefined commands `BreedCommand`, `EatCommand`, `MoveCommand`, and `WaitCommand`. 

The following are the predefined rules for breeding, eating, and moving that apply to rabbits and foxes:

+ `BreedCommand`: When an `ArenaAnimal` breeds, it makes a copy of itself on an adjacent tile (one of the 8 tiles around it). The `LivingItem` can only breed when it has enough energy (`getBreedLimit` returns the minimum required energy to breed) and has a valid empty adjacent location. Breeding occurs alone; there is no mating between rabbits or between foxes. When an `ArenaAnimal` breeds, its energy is reduced to 50 percent of its former energy (rounded down), and the newly-bred animal also starts at 50 percent of its parent's energy. Finally, a newly bred `ArenaAnimal` must be placed in an empty location that is adjacent to the parent.
+ `EatCommand`: An `ArenaAnimal` is only able to eat something that is adjacent to it. By eating a `Food`, the `ArenaAnimal` increases its energy by the food's calories (plant calories for rabbits and meat for foxes) up to its maximum energy level. The food (vegetable/non-vegetable) must be edible by the eater (herbivorous/carnivorous) and the eater must possess greater strength than the food, i.e., foxes should not attempt to eat grass or other foxes.
+ `MoveCommand`: An `ArenaAnimal` can only move once at a time, and moving distance is restricted by its moving range. Also, it must move only to valid, empty locations.
+ `WaitCommand`: Simply doing nothing is the  final option. Note that all living items lose energy each time `getNextAction` is called, even if they choose to do nothing, so they may eventually die of hunger.

The above rules needed to be obeyed strictly by the AI for rabbits and foxes. Your own items may handle them more flexibly (e.g., they may jump around further on the world) and may add additional `Command`s.

#### Implementing `Item`s With Reuse

When implementing your own items you should maximize code reuse. You may modify the implementation (but not the behavior) of existing items to improve reuse. Your submission should not contain any duplicated code.

#### Implementing the AI for the Arena

> This is also where you can get creative. You only need to develop a process for deciding on the actions for the creatures in the virtual world. Don't get overwhelmed by the use of the term AI: you are simply developing some rules for decision making.

You must implement the AI for rabbits and foxes by implementing the AI interface. For technical reasons, your AI classes must have a zero-argument constructor (you cannot participate in our virtual world tournament if your AIs require constructor arguments).

The AI for rabbits and foxes is restricted in flexibility compared what other actors can do. They can only see nearby parts of the world through the `ArenaWorld` interface and may only return the predefined commands obeying the rules above.

Note that the AI should only rely on the interface contracts of arena animals, but not on specific implementations. For instance, we may chose to modify the size of the world, the energy limits, or the view ranges of animals. Returning invalid commands or attempting to cheat by casting the `ArenaWorld` to `World`, or casting other objects to specific implementations, will negate your submission for the the `Fox` and `Rabbit` AI implementations.

#### The `World`

The `World` class is the core engine of the game. It tracks all items (and removes dead ones) and actors. The world regularly gets the next action for each actor and performs the actions. The world organizes items in a 2-dimensional grid (`n x n`, for arbitrary ` n`) of `Item`s, with the top left corner being (0; 0). Locations are represented by the `Location` class, which contains several potentially useful methods. We also provide a utility class with potentially useful functionality.

We provide a GUI to visualize the world with its items. The GUI has a simple interface containing two buttons: a <kbd>Step</kbd> to execute a single step; and a <kbd>Start/Stop</kbd> toggle button to run indefinitely until the toggle button is pressed again. For completing the machine problem, it is not necessary to understand the implementation of the GUI. To initialize the world with your items, modify the `Main` class. In the arena competition, we will initialize a large virtual world with grass and all competing rabbits and foxes.

## Guidelines

+ State the representation invariant and the abstraction function for the two implementations of the Graph ADT as comments in the appropriate Java files. You should, of course, write RIs, AFs and specs wherever appropriate.
+ Properly encapsulate your implementation and avoid representation exposure.
+ Avoid using `instanceof` and downcasts. Avoid casting an interface to a specific implementation. 
+ Do not use the `java.lang.Class` class or the `java.lang.reflect` package. You do not need -- and should not use -- those techniques for this mini-project.
+ Do not edit any files in the `ca.ubc.ece.cpen221.graphs.core` package or any of the method declarations we’ve initially provided for you. You should also not edit any code in the package `ca.ubc.ece.cpen221.graphs.two.core`. In most situations, you should only be adding/changing code where indicated by `TODO` comments.
+ Make sure your code is readable and follow the style guide. Follow good code hygiene by adopting practices discussed in the course notes.
+ Write clear and concise specifications for the methods that you have been asked to implement (and other methods too).
+ Write tests that provide at least 95% lines of code coverage and branch coverage in the package `ca.ubc.ece.cpen221.graphs.one`.
+ For this mini-project, we understand that testing code in this virtual environment is very difficult, and we rather you devote your time to practice code reuse. You should try to run your implementations in the GUI and experiment with different behaviors You may write test code, but we do not have any testing-related requirements for that part of the mini-project.
+ Each task is worth 1 point. For Task 6, your evaluation will be based on how effective your submission is relative to other submissions.

## Additional Hints

+ You may create helper classes and helper methods to help you with the assignment, as long as your code is compatible with the provided interfaces.
+ The tasks may be underspecified. Use your judgment. Write specifications. You can ask reasonable questions on Piazza.
+ The implementation of `Vertex`, and consequentially of `Graph`, involves the use of generics. You can read more about Java Generics starting with [Oracle Java Tutorial](https://docs.oracle.com/javase/tutorial/java/generics/index.html).
+ To understand how to handle command line arguments (as required by `TwitterAnalysis`), you can start with [this tutorial](https://www.baeldung.com/java-command-line-arguments).
+ Do not be intimidated by the length of this `README`. Some of the tasks are surprisingly straightforward. 
## Previously Asked Questions

1. Can an edge have the same vertex as the start and end point?
	* Such edges are sometimes called self-loops. For the purpose of this assignment, we will disallow self-loops.
2. What is the distance from a vertex to itself?
	* The distance from a vertex to itself is 0. (We will use this as a definition for this assignment.)
3. Who is an upstream neighbour?
   * In a directed graph, if you have `a -> b`, or an edge from _a_ to _b_, then _a_ is an upstream neighbour of _b_, and equivalently _b_ is a downstream neighbour of _a_.
4. In an undirected graph, an edge between _a_ and _b_ implies that we can go from _a_ to _b_ and then _b_ to _a_. Is this a loop?
	* No; this is not considered a loop. A loop requires two or more edges except in the case of self-loops, which we are disallowing in any case. We will also not consider the situation when there are multiple edges between the same pair of vertices.
