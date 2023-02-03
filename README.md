
# Spider Project

Introduction:

This project intends to allow a user to identify a webpage, and "web crawl" across it, navigating through a start point, and following the branching links and HTML elements to other pages that are then logged, and saved to the user's file system. 


## Business Goals

A user should be able to target a webpage, and save that webpage to a document. The user should then recieve a list of all the links available on that particular webpage, and then recieve a branching list of the links on the webpages those links lead to. The user should be left with a clear, unique set of website links and saved HTML documents that they can peruse, or manage as they see fit. 
## Technical Requirements

In this project, two sites will be used as proof of concept. https://smt-stage.qa.siliconmtn.com/, and the RezDox Admin Performance Websocket page.
There are key requirements for every step of the project:
1. HTML files must be parsed from every page, starting with the staging homepage of siliconmtn. 
2. Saved files must be unique, in this saved links must also be unique. No repeats, if a particular web page is linked to on multiple pages, only save one instance of the HTML and link.
3. The links will be parsed with JSOUP.
4. The pages will be loaded and saved to the file system.
5. Using a Socket library to manage HTTP vs HTTPS pages will be necessary. 
6. For the admin tool, passing in login information, and obfuscating the information from code will be necessary. 
7. To stay logged in, the cookies for the session ID will need to be tracked and stored with code, and passed through when needed.
8. The RezDox Admin part of this assignment should be done in a single session, rather than multiple logins.
9. Memory utilization with streaming files to the file system will need to be managed. 
10. Make sure the code is reusable, and stable. KEEP IT DRY. 
## Screenshots

![App Outline](https://github.com/nlj77/Spider/blob/main/lib/src/main/resources/Spider%20Project%20Outline%20Draft(3).png)


## 🔗 Links
[![portfolio](https://img.shields.io/badge/my_portfolio-000?style=for-the-badge&logo=ko-fi&logoColor=white)](https://github.com/nlj77)
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/nickolas-jones-523b66b7/)


