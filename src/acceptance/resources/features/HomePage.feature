Feature: Home Page
    As a consumer,
    I want to access the service home page

    Scenario: Home Page
        When user visits "/" page
        Then page contains elements by "[class='hello']"
