# ContactApp

1 - Choose Librares
  a - SQLite - Is easy and maybe is the most use library for internal database
  b - RecycleAdapter view - Recycle view is light and do not get much memory from the device
  c - Picasso - To display the images

2 - Description to how to generate it.
  The app is simple, as soon as you launch it will download the the data and display it in a List, if you click on the item the app will open the second screen with details and the option to edit and delete (you can see those buttons on the top-right side) also there is a floating button on the first screen to add a new contact 

3 - General explanation about the code and app.
  When the app starts it checks the internet connection, if it is OK, the app download the Json pack and save on the internal database using SQlite. Once all data is saved, it query and retrieve it on the main screen using RecycleView
  When we press add (floating button), it goes to the second screen and it allows you input the details and saves on internal database, after save it will take you to the first screen and updating the list. it also occurs if you delete or update the register.  

  If there is no connection, it will look on the data base for any remaining data and display it.

  P.S. The app deletes all the data when it launch for the first time, of course it is not a good practice, I just made it to be able to test the Json more than one time. 



4 - what should you change on your implementation if you have more time
  I would try to use Retrofit, I read about it and it seems to be a good library to use to retrieve Json data
  I would also put the feature of take pictures in case of a new Contact
