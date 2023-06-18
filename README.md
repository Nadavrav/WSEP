System initialization instructions:
1. run project
2. in any browser, paste the url: loadhost:8080

Structure of initialization file:
jeson formatted, with the following structure:
  1. list of users, each containing a username, password and a list of stores. each users in that list is registered. if the user has an admin field and it is set to true, the user is set to be a system admin
  2. if the stores list exists and is not empry, user logs in and opens the stores. each store contains: name and a list of products.
  3. if the list of products exsits and is not empty, user adds each of them to the store. each products contains: name, category, description, price, quantity.
  4. 
