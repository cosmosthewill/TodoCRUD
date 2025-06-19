//Request
User Registration & Profile
BR-01: Users can register for an account by providing a valid email and password.
BR-02: Users can log in and update their profile (name, email, password).
Task Management
BR-03: A user can create a new task with a title, description, deadline, and optional category.
BR-04: A user can view a list of their tasks, filtered by status (Pending, In Progress, Completed, Overdue) and/or category.
BR-05: A user can update any of their task’s attributes (title, description, deadline, category).
BR-06: A user can mark a task as “Completed.”
BR-07: Tasks whose deadline passes without completion automatically transition to “Overdue.”


Task Sharing & Collaboration
BR-08: A task owner can share a task with one or more other registered users by email.
BR-09: Shared users can view the task and update its status, but only the owner can delete it or change its sharing list.
Categories
BR-10: Users can create, rename, and delete custom categories for organizing tasks.
BR-11: Category names must be unique per user.
Reminders & Notifications
BR-12: Users can request an email reminder for a task at a specified time before its deadline.
BR-13: The system sends reminder emails reliably; if sending fails, it retries according to a back-off policy.


Audit & Traceability
BR-14: Every create/update/delete operation on tasks and categories is recorded in an audit log with timestamp, user, and change details.
BR-15: Users can view a history of changes on each task (who did what, and when).
Security & Access Control
BR-16: Only authenticated users can access the application’s features.
BR-17: Users can only see and act on tasks they own or that have been shared with them.
Reporting & Insights
BR-18: Users can view simple metrics (e.g., number of pending vs. completed tasks) on a dashboard.
External Integrations
BR-19: The application integrates with an email service to send registration confirmations and task reminders.
BR-20: (Optional future) Integrate with calendar APIs to push task deadlines as calendar events.

//

//Security workflow
 Full Breakdown: Step-by-Step
✅ 1. User Sends a Request
Example with HTTP Basic:
GET /tasks
Authorization: Basic dGVzdDp0ZXN0cGFzc3dvcmQ=
Spring decodes the header to:
username = "test"
password = "testpassword"

✅ 2. Spring Security Filter Creates Token
In HTTP Basic or form login, Spring automatically creates:

Authentication authRequest = new UsernamePasswordAuthenticationToken(username, password);
This token holds:

Principal (username)

Credentials (password)

No authorities yet (not authenticated)

✅ 3. AuthenticationManager Calls authenticate()
Spring's default AuthenticationManager checks which AuthenticationProvider supports the token (supports(...)), and finds DaoAuthenticationProvider.

Then it calls:
Authentication result = daoAuthenticationProvider.authenticate(authRequest);
✅ 4. Inside DaoAuthenticationProvider.authenticate(...)
This method does:

public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName(); // extract username
    String password = authentication.getCredentials().toString(); // extract password
    UserDetails user = userDetailsService.loadUserByUsername(username); // your method

    if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new BadCredentialsException("Invalid password");
    }

    return new UsernamePasswordAuthenticationToken(
        user, null, user.getAuthorities()
    );
}
🧠 Breakdown:
Step	                                                What Happens
authentication.getName()	                            Gets the username
authentication.getCredentials()	                        Gets the raw password
userDetailsService.loadUserByUsername(...)	            Fetches user from your DB
passwordEncoder.matches(...)	                        Verifies raw vs. encoded password
Returns authenticated token	                            User is now authenticated

🧪 Example Debug Values
Assume the client logs in with:

username = test

password = test123

Spring creates:

new UsernamePasswordAuthenticationToken("test", "test123");
Then DaoAuthenticationProvider.authenticate(...):

Loads the UserDetails for "test"

Compares "test123" (raw input) with the encoded password from the DB

If valid → user is authenticated.

📌 Summary
Part	                                                        Action
UsernamePasswordAuthenticationToken	                            Holds username + password
Spring Security Filter	                                        Creates token from login form or basic auth
AuthenticationManager	                                        Passes token to DaoAuthenticationProvider
DaoAuthenticationProvider.authenticate()	                    Extracts credentials, loads user, validates password
If valid	                                                    Returns authenticated token with user info

Fillter in user/security/securityconf