# Congestion Tax Calculator 

[Linkedin - Swetha Srinivasan](https://www.linkedin.com/in/swetha-srinivasan-291122119/)

[Portfolio- Swetha Srinivasan](https://swethasrinivasan.live/)

## Volvo Cars Congestion Tax Calculator assignment 

### Docker : Steps to run 

Voila! The application is dockerised.

**Steps:**

    Docker build -t congestiontax:1 .

And I have used my Docker Desktop to simply build the container from the image. And run the container !! 

Visit the following link to view the swagger documentation on your browser:

    http://localhost:8000/swagger-ui/index.html

#### Advantages:

Implicity
Fast scaling systems
Flexible appliation delivery
Support for microservices architecture (Highly useful in our case)


## :question: Problem  Scenario

Implement the changes in the congestion calculator and additions required to implement a bug-free solution that solves the problem of seamlessly calculating the taxes for the following vehicle types.

:point_right: Bus :bus:, Car :car:, Diplomat :blue_car:, Emergency :ambulance:, Foreign :minibus:, Military :police_car:, Motorbike :bike:, Motorcycle :bike: :point_left:



### Given conditions to be addressed for Tax calculation 


1. City: Gothenburg { Should also be scalable in the future to add more cities }
2. Max Amount per Day: 60 SEK
3. No tax on weekends
4. No tax on public holidays
5. No tax on days before public holidays
6. No tax on July month
7. Taxation Table for the time of the day

    | Time | Amount (SEK) |
    | ------ | ------ |
    | 06:00 - 06:29 | 8 |
    | 06:30 - 06:59 | 13 |
    | 07:00 - 07:59 | 18 |
    | 08:00 - 08:29 | 13 |
    | 08:30 - 14:59 | 8 |
    | 15:00 - 15:29 | 13 |
    | 15:30 - 16:59 | 18 |
    | 17:00 - 17:59 | 13 |
    | 18:00 - 18:29 | 8 |
    | 18:30 - 05:59 | 0 |
8. Single charge rule: If a vehicle passes many times in 60 mins: It has to be charged the maximum amount of the time limit
9. Tax Exempt vehicles = Emergency vehicles, Busses, Diplomat vehicles, Motorcycles, Military vehicles, Foreign vehicles
10. Year = 2013

11. The application currently doesn't have an entry point, add a way to call the calculation with different inputs, preferrably over HTTP.

 

### Tech Stack Utlized for the implementation

- [ ] Spring Boot Application 3.0.0
- [ ] Maven Project
- [ ] JDK 19
- [ ] Junit 4.13.2
- [ ] Swagger 3
- [ ] Database - MySql
- [ ] Docker 20.10.14

:::info
:bulb: **Experience:** 
It was exciting to utlize latest tech stack and solve it for Volvo!
:::

> But again as I have written in the CV: Code, tools, libraries, packages and syntaxes is just one google search away! I m a firm believer of concepts, architecture design patterns, logics, efficient code handlers and more in that similar line! Even this I found a similar version with more faults in one google search - but the idea is all about solving the problem!! 

### Approach

#### Thought Process 



##### Started off with DROOLS :-1: 

- Created a decision rule engine with scalable approach for business user (Example City Municipality officers in this case) to execute the condition based on the action
- Used DROOLS for this and have configured that for the aforementioned rules
- Data stored in MongoDB as a document, felt misleading data strcuture that I have created! Regretted looking my code for few mins, drank two cups of coffee! And got back to retrospection!

Unfortunately, felt this could be of an inefficient solution! As this can be a bit overkill in terms of the executing rules in hierarchies and establishing rules between params !!

Hence in such kind of system its advisable not to use Drools as it wont be of that benefit with respect to performance and the control.


##### Went to basics, Google!! :+1: 


    
Started with the thought to list down the inputs required to process the congestion tax calculation and how to access them when the API is hit!
    Here is a illustration of the worflow :

![](https://i.imgur.com/WnmIrlb.png)





#### Controller
The entry point to the application is configured in this layer
    
    POST METHOD = "/api/v1/tax-calculate"

#### Service

The congestion tax calculations are performed in the Service Layer by validating the input parameters  = City Name, Vehicle Type and Check_in_time.
Methods Defined include:
    
    1.getTax()
    2.calculateTotalTaxBySingleChargeRule()
    3.getSingleChargeRule()
    4.getTollFeeByTariffAndDate()
    5.ConstructChargesByDate()
    6.isTollFreeVehicle()
    7.IsTollFreeDate()
    8.isValidCity()
    9.isValidVehicle()
    
#### Entity
It consists of the Entities related to 
    
    1. CityEntity()
    2. VehicleEntity()
    3. TariffEntity()
    4. NoTaxDaysEntity()
    5. NoTaxMonthEntity()
    6. TaxDaysEntity()
    7. TaxRuleEntity()

The entitites are defined to form a table including Joints and primary keys, illustrating the hierarchy and relationship weightage to determine the tax amount calculation. 

#### Repository
It is used to indicate that the class provides the mechanism for storage, retrieval, search, update and delete operation on objects.

There are two repositories created for entities City and Vehicle using JpaRepository. 

    cityRepository
    vehicleRepository

    

#### Database

MqSQL DB is chosen for the congestion tax calculation as the data can be effectively retried relational data

### Improvements Done:

As per the inital code repository these were the improvements made and identified

1. **Identified** : The use of deprecated Date methods were removed
**Improved** : Gregorian calendar is used to retireve the days and month.

2. **Identfied** : Static assignments  for vehicles and  validation logic in tax rule table can be modified 
**Improved** : Input parameters dynamically received and accessed from DB for validation.
    
3. **Identfied**: As per the If-else logic to calulate the tax amount --> it did not pass all cases to cover all times of the day 
**Improved** : Repository is utilized to store the required data of the table parameters for the tax calculations and all time zones.

4. **Identified** : Individual classes of Car and Motorbike were removed.
**Modified** : Configured to access the Vehicle type under VehicleEntity 

5. **Identified** : The logic here is calculating tax based on perday basis.
**Decided** : To retain approach for the summation of the tax amount to our users if the scope can be increased to cover a range of dates. Like a Monthly subscription use case!

6. As part of the Holiday Calendar considerations, pre and post day of a public holiday is considered in this implementation and is completely configurable.
    


### Bonus Scenario

Expectation is to be able to scale the tax calculation for different cities. 
    
    This Code is optimised for Multi-City, Multiple Dates, different tax rules - this is optimised to perform the scaling part.
    

The entities related to the tax rules can be added to the database records and can be validated to calculate tax using the same. It is highly configurable for multiple cities and years.
        
### Let's think about this! 

Aspects which can be worked upon as part of the project execution.

1. While scaling the Tax calculator API, we can include Currency Conversions to being able to display the taxes  in localized curreny for different cities.
2. The Business Logic layer can be worked upon using Drools for a Decision Rule Engine Table, thereby helping business users to configure the updates in Tax rules tables. With some more added clarity to the sequence of the validation ,it can be definitlely done!


### Takeaways

**Fun** : Loved the challenge, super eager in trying out different models, architecture patterns in solving this was sheer fun

**Knowledge Gained** : Experimenting the ways of coding helped me gain this way of solving the problem as in using SQL for this problem.

There can be more takeaways, even the code is widely available in the internet but the point is not the way of solving instead choosing what suits the best for the solution. As in we have so many references in the present day STACK OVERFLOW similar sources, but my motivation to apply for this job is to solve the unsolveable cases for the greater good of people lives @ Volvo cars! 








