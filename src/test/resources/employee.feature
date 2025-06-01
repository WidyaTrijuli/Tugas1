Feature: Employee API

  Background:
    Given The base url in this feature is "https://whitesmokehouse.com/webhook"

  Scenario:
    When Send a http "POST" request to "/employee/add" with body:
      """
      {
        "email": "usertestdummy78@test.com",
        "password": "test",
        "full_name": "usertestdummy78",
        "department": "IT",
        "title": "QA"
      }
      """
    Then The response status must be 200
    And The response schema should be match with schema "add_employee_schema.json"

  Scenario:
    When Send a http "POST" request to "/employee/login" with body:
      """
      {
        "email": "usertestdummy78@test.com",
        "password": "test"
      }
      """
    Then The response status must be 200
    And Save the token from the response to local storage

  Scenario:
    Given Make sure token in local storage is not empty
    When Send a http "PUT" request to "/employee/update" with body:
      """
      {
        "email": "usertestdummy77@test.com",
        "full_name": "usertestdummy77",
        "title": "Lead + Senior QA",
        "password": "test",
        "department": "IT 4"
      }
      """
    Then The response status must be 200
    And Full name in the response must be "usertestdummy77"
    And Department in the response must be "IT 4"
    And Title in the response must be "Lead + Senior QA"
  
  Scenario:
    Given Make sure token in local storage is not empty
    When Send a http "DELETE" request to "/employee/delete" with body:
      """
      {}
      """
    Then The response status must be 200
