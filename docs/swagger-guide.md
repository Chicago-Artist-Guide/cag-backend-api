# Swagger Guide

To make it even easier to interact with our endpoint I added Swagger-UI. Now when you run the application and view it in your browser you'll be taken to our Swagger-UI page where you can send requests to any of our endpoints just like you would in Postman. The URL I use to get to this page is: **http://localhost:9000/**.

![alt tag](./docs/imgs/swagger-ui.JPG)

Once on this screen, you can click any of our requests, then click the "Try It Out" button on the request. You'll then be prompted to enter in all the information for the request. In this case, it's the authKey and the user request body:

![alt tag](./docs/imgs/swagger-register-user-request.JPG)

Now that you've entered this information you can click the blue "Execute" button to send the request:

![alt tag](./docs/imgs/swagger-register-user-response.JPG)

If you scroll down, you'll see the response from the request. In this case, I got 201 and the created user details returned back to me.

### NOTE

If you'd like to know more about how I setup our app using swagger UI - it's all in the **build.gradle** file where I have comments you can read. 
