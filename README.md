# Projinda23 - Weather Forecast App

This project is an application that displays the weather condition in a selected city of the user’s choice. Based on the weather, a playlist on Spotify will be recommended that will match the mood of the weather. 

## Description

The application utilizes the Open Weather Map API to retrieve real-time weather data and the Spotify API to fetch playlists and generate music recommendations that match the weather conditions. It is developed using Java for the backend logic and parsing of the APIs, while HTML and JavaScript are used for the frontend design.

## Getting Started

### Installing

* Git clone the repository to any location of your choice

### Executing program

* Preperation
To run the program you make your way to "saved-location"/projinda23/ and then run the following code 
```
javac -cp "lib/json-20230227.jar" src/*.java
cd src
java -cp .:../lib/json-20230227.jar Main

```
Then paste http://localhost:8080/index.html into your browser.

* Using the application
To use the application you input a city in the white search box on the center of the screen and then either press the **Enter-Key** or the arrow to the right of the input text. Then you will see the weather of that location and 

You will then get the weather description of that city and a hyperlink to a playlist on Spotify that should more or less match the mood of the citys weather.

## Authors

Mohammed Ba Rashed
Kevin Löv
Athanasios Pittakis

## Acknowledgments

Mathias Grindsäter - Supervisor