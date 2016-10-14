# QuizREST

Week 12 project at Skill Distillery.

The primary goal was to create and consume a RESTful API with Java backend and vanilla JavaScript frontend. 

The JavaScript code can be found in Quiz/WebContent (https://github.com/JosiahDM/QuizREST/tree/master/Quiz/WebContent).

## Challenges

- Learning and understanding what was going on with the Jackson annotations. 
- This was only my second time writing an app with JavaScript, so the code started out kind of ugly.
 But as I went along I got a better hang of it and the code got a little cleaner. I would like to 
 make things more modular and encapsulated in the future, but at this point I think it turned out ok.
 
## Cool(ish) things

- Originally figured out how to make my own ajax method before switching over to Kris's version. My version at line 452 of
app.js runs the callback passing the parsed data if it is successful. I can see how passing the XMLHttpRequest is probably more
flexible, so I ended up using his version.
- Created a login feature that creates cookies with JavaScript to track the current session. There are very minimal cookies,  and they are not secure. I just wanted some minimal user login ability. Would be interested in learning more about secure cookies, however. In future apps I'd like to do more with security for this. 
- The login does go through the backend and validates username
 and password with bcrypt hashed passwords. Invalid logins will show an error message via the Java backend sending back a HTTP 
 status code of 401 Unauthorized. View app.js line 384 for the logic of the cookie creation. 
- Made a login event so if the user finishes a quiz while not logged in, they are prompted to login to save score. On login, 
the event will be triggered and automatically save their score to the database. Logic for this is at app.js line 137-141, 
then line 390 where the actual dispatchEvent function happens.
