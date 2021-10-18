# DakaDining
A native Android app written in Kotlin to help facilitate students at WPI to trade meal swipes. 
Assignment done through **pairs programming**.

## Task ID | Demonstration of Completion
(1) See Canvas submission for Checkpoint PDF

(2) For video see comment attached to Canvas submission, link is also included here: https://drive.google.com/file/d/1HjkZ1gyIbq-e6umscSQBBWHNf8M6ZcR3/view?usp=sharing

More information about the correctness and evaluation of our application is detailed below

(4) Find source code enclosed in Canvas zip submission

(7) Design achievements detailed below

(8) Technical achievements detailed below

(5) Find Debug APK enclosed in Canvas zip submission

(6) This is the README


## Evaluation
Due to the complicated nature of our application backend we were unable to demonstrate correctness through the JUnit testing framework and instead tested our application through user experimentation. In order to have written JUnit tests we would have had to figure out how to implement Observers/LiveData that can connect with our remote MongoDB database using our API. We read through a lot of documentation and tried to execute this using Mockito/Roboelectirc with our project, however, despite immense effort, due to the complexity of the issue and the time constraint, we were unable to write unit tests to evaluate the critical features of our apps. If we were able to properly configure JUnit testing we would have had test cases that checked the login credentials of both valid and invalid users. Our JUnit tests would also check for updates in the database after decrementing a seller’s swipes, creating, editing, and deleting an offering and a request, accepting an offer, fulfilling a request, and taking a profile picture.


### Performance Profiling

We read through each of the sites linked on the documentation for best practices and tried to implement them throughout our application.
<img width="1651" alt="Resting State" src="https://user-images.githubusercontent.com/64321589/137019461-f9f99255-cae6-468e-ad35-e89f88b18bd8.png">
After working with the Android Profiler to analyze consumption of resources such as CPU, memory, network, and energy, we concluded that as shown by the image above, our app once it reaches a ‘resting state’ after the initial bootup uses minimal energy and network resources, with sporadic CPU usage and consistent memory usage around 238 MB.
<img width="1650" alt="sharp increase" src="https://user-images.githubusercontent.com/64321589/137019526-cf0a101f-77d7-409b-9433-9541966f289a.png">
As shown in this image, which corresponds to when a user registers their account in the application, there is a sharp increase in network resources from Sharp increase in network resources, sending 1.1 KB/s and receiving 1.8 KB/s. The CPU usage with this action also spikes up to close to aroud 30%. This initial spike of network resources, which is shown more isolated in the image below, corresponds to the usage of a remote MongoDB database and the increase in resources when registering is due to the inital API connection being established and sending the user data over to the database.


<img width="129" alt="initial spike" src="https://user-images.githubusercontent.com/64321589/137019974-3c5a2002-38e2-49e5-bef7-db7216bc8907.png">

The last image shows the Android Profiler as a user attempts to login. As you can see the receiving network resources spiked very close to 256 KB/s as the application retrieved and verified the login credentials from the database using the API.


<img width="171" alt="getting user" src="https://user-images.githubusercontent.com/64321589/137019986-4c655fe3-c233-4e6e-af10-57f65a804ace.png">

## Design Achievements
- Our application implements localization by localizing all of the user facing text so that our app can be read in English, Spanish, Hindi, or Mandarin. By providing the appropriate resources for our app, i.e. Spanish, Hindi, and Mandarin versions of our res/values/strings.xml files, the Daka Dining App will localize the text based on the user’s device’s language setting.
Note: Translations were taken from Google Translate so we apologize for any discrepancies/incorrect wording.
- The fragment where user accounts registered as buyers can submit a new request implements a date and time picker. This improves the overall user experience by simplifying the selection of date/time as well as removing the need for error checking on the backend to make sure a valid point in time is entered.

## Technical Achievements
- Our application includes a registration and login system and with this, we also implement means of validating user credentials on the backend. To do this, we had to ensure that two users could not register with the same email, and that users were notified if they tried to login with an incorrect password or alternatively an account that didn’t exist.
- In order to maintain all of the app data and have it persistent between user sessions as well as have it linked to specific user credentials, we used a remote MongoDB database to host our information. 
- For our network component: to insert documents into our MongoDB database as well as access information in it, we created a REST API to interface with it using HTTP requests. This was challenging to integrate because our schemas changed a decent amount as different aspects of our application were realized.

While there are no potential problems with our submitted implementation there are certainly features we would have liked to implement if we had had the time. A minor addition we would have made is to display the name and email of the offerer or requestor of each respective offer and request, so that students would know who to connect with and when for their swipes. Another feature we think would be nice to implement is a notification system, so users are notified when their offers are accepted or their requests are fulfilled. Thirdly, we might have considered enabling access to Outlook so students can import their meal swipe dates directly into their Calendar. Similarly with this, while it likely would have been complicated to do, we would have liked to implement authentication with students’ WPI accounts.

