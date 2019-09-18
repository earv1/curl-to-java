# curl-to-java
[![CircleCI](https://circleci.com/gh/earv1/curl-to-java.svg?style=svg)](https://circleci.com/gh/earv1/curl-to-java)&nbsp;<a href="https://codeclimate.com/github/just1689/curl-to-java/maintainability"><img src="https://api.codeclimate.com/v1/badges/0189ac942dad13f3d7e8/maintainability" /></a>&nbsp;<a href="https://codebeat.co/projects/github-com-just1689-curl-to-java-master"><img alt="codebeat badge" src="https://codebeat.co/badges/b2f364cf-38f1-4fcd-bef0-6c403efc07dc" /></a>
<br />

## Description
Generate java requests and tests from curls.

## How to Use
Latest release: <img src="https://img.shields.io/github/v/release/earv1/curl-to-java.svg">

- Download the latest <a href="https://github.com/earv1/curl-to-java/releases">release</a>.
- Add your curls in resources/curls.txt.
- Run `gradle run`.

## Requirements
Java 9+

## How does it work
Curl to java works by generating taking a curl request, and generating java code with tests out of it.

Specifically, it performs the following steps.
1. Transform curl request into generated restTemplate request.
2. Run generated java code and get json response.
3. Generate more code, specifically tests from json response.
4. Run all the code together and see if the tests pass.

## Features
1. Transforms a curl into a java http request.
2. Generates tests to make sure the response is the same each time.
3. Can customise generated code with templates.
