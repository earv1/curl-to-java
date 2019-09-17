# curl-to-java
<a href="https://codeclimate.com/github/just1689/curl-to-java/maintainability"><img src="https://api.codeclimate.com/v1/badges/0189ac942dad13f3d7e8/maintainability" /></a><br />


I really wanted something that could take a curl and turn into a spring Spring rest template. I then realized that I could also generate checks for telemetry, in order to make sure that systems are behaving as expected. This is the result!


## How to Use
Add your curls in resources/curls.txt and `gradle run`

## Requirements
Java 9+

## How does it work
Curl to java works by generating taking a curl request, and generating java code with tests out of it.

Specifically, it performs the following steps
1. Transform curl request into generated java http request
2. Run generated java code and get json response.
3. Generate more code, specifically tests from json response.
4. Run all the code together and see if the tests pass

## Features
Besides for transforming curls into java with tests to make sure you get the right response,
templates are also supported! So you can customize the output.