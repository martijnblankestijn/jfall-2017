# J-Fall 2017
Presentation material for CQRS: processing events to query-databases.

## Abstract
Command Query Responsibility Segregation (CQRS) and Event Sourcing (ES) have been around since the mid 2000’s, so nothing new there. 
CQRS even draws on principles formulated in the 90’s to separate the reads (queries) from the writes (commands) of the application. 
Application of these patterns means a different way of thinking about modelling and consistency.

Most of the articles and presentations involve the Command-side of CQRS and how the events are stored. 
Personally I have always wondered about the Query-side of CQRS. 
How to process the events that are generated as a consequence of the received commands? 
How to translate these events to populate the query-databases to achieve the scalability? 
How about consistency and scalability of this process itself? 
What if something goes wrong in the processing? 
For a recent project I started to investigate how the processing of the events can be implemented in a clustered environment with Akka Persistence.

In this session we will look at CQRS and ES and an example application with Akka Persistence. 
Next we will dive into modelling the processing of the events to create query-databases and how to implement this.

Attendees will leave with insights how the Query-side of CQRS can be realised and some of the design decisions involved.
