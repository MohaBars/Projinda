//Declare varables
const container = document.querySelector('.container');
const searchBox = document.querySelector('.search-box button');
const notFound = document.querySelector('.not-found');
const weatherBox = document.querySelector('.weather-box');
const inputField = document.querySelector('.search-box input');
const cloudsContainer = document.querySelector('.clouds-container');
const sunContainer = document.querySelector('.sun-container');
const sun = document.querySelector('.sun-container .sun')
const rain1 = cloudsContainer.querySelector('.rain1');
const rain2 = cloudsContainer.querySelector('.rain2');
const snow1 = cloudsContainer.querySelector('.snow1');
const snow2 = cloudsContainer.querySelector('.snow2');
const playlistButton = document.querySelector('.container .playlist');
const playlistCover = document.querySelector('.container .playlist img');
let link;
let coverLink;

//If search button is clicked, call the function searchWeather
searchBox.addEventListener('click', searchWeather);
//If Enter key is pressed, call the function searchWeather
inputField.addEventListener('keypress', function(event) {
    // Check if the pressed key is Enter
    if (event.key === 'Enter' || event.keyCode === 13) {
        searchWeather();
    }
});

function searchWeather() {
    //Get the city from the input
    const city = inputField.value.toLowerCase();
    const url = 'http://localhost:8080/weather?query=' + city; 

    //Create a variable to story the city with the first letter capitalized
    let cityCap = city.charAt(0).toUpperCase() + city.slice(1);
    //Create a variable to store the message that will appear to the user
    let message = "Want to get into " + cityCap + "'s mode? Enjoy listening to this playlist on Spotify!"
    //if the input is empty, return
    if(city === ''){
        return;
    }

    // Set all containers display to none
    // By setting them to none we make sure that the page is cleared every time the button is clicked
    weatherBox.style.display = 'none';
    cloudsContainer.style.display = 'none';
    sunContainer.style.display = 'none';
    rain1.style.display = 'none';
    rain2.style.display = 'none';
    snow1.style.display = 'none';
    snow2.style.display = 'none';
    playlistButton.style.display = 'none';


    //Using fetch() method to send and recieve strings
    fetch (url)
        .then(response => response.text())
        .then(data => {
            //convert the data into an array
            var results = parse(data);
            //get the time and convert it into an integer
            var time = parseInt(results[1]);

            //Handles the case where the city is not found
            if(results[0].includes('400'||'404')){
                //Set the container height to 700px display the notFound element with the fadeIn animation
                container.style.height = '700px';
                notFound.style.display = 'block';
                notFound.classList.add('fadeIn');
                return;
            }
            //Otherwise the error image will not be displayed
            notFound.style.display = 'none';
            notFound.classList.remove('fadeIn');

            //Get the description element and store in the variable "description"
            const description = document.querySelector('.weather-box .description');

            //switch case to check the response
            switch (true){
                case results[0].includes('rain' || 'drizzle'):
                    cloudsContainer.style.display = 'block';
                    rain1.style.display = 'flex';
                    rain2.style.display = 'flex';
                    //If it's during nighttime, the background color will be changed to grey-ish dark blue
                    if(time >= 19 || time <= 6) {
                        cloudsContainer.style.backgroundColor = '#2B2D42';
                    }
                    break;

                case results[0].includes('clouds'):
                    cloudsContainer.style.display = 'block';
                    //If it's during nighttime, the background color will be changed
                    if(time >= 19 || time <= 6) {
                        cloudsContainer.style.backgroundColor = '#2B2D42';
                    }
                    break;

                case results[0].includes('snow'):
                    cloudsContainer.style.display = 'block';
                    snow1.style.display = 'flex';
                    snow2.style.display = 'flex';
                    //If it's during nighttime, the background color will be changed
                    if(time >= 19 || time <= 6) {
                        cloudsContainer.style.backgroundColor = '#2B2D42';
                    }
                    break;

                case results[0].includes('clear'):
                    sunContainer.style.display = 'block';
                    //If it's during nighttime, the background color will be changed and the sun will become a moon by changing its color
                    if(time >= 19 || time <= 6) {
                        sunContainer.style.backgroundColor = '#2B2D42';
                        sun.style.backgroundColor = '#DCDCDC';
                    }
                    break;

                //for haze/mist/fog we will reuse the sun container and change its colors, also, we will keep the sun hidden
                case results[0].includes('haze' || 'mist' || 'fog'):
                    sunContainer.style.display = 'block';
                    sun.style.display = 'none';
                    //Change the background color to a gradient of blue and grey
                    sunContainer.style.background = 'linear-gradient(to bottom, #87CEEB, #D8DBE2)';
                    //If it's during nighttime, the background color will be changed
                    if(time >= 19 || time <= 6) {
                        sunContainer.style.background = 'linear-gradient(to bottom, ##2B2D42, #D8DBE2)';
                    }
                    break;
                
                default:
                    
            }

            //change visibility of the playlist button
            playlistButton.style.display = 'inline-block';
            //change the variable link to contain the actual playlist link
            link = results[2];
            //change the variable coverLink to contain the actual link
            coverLink = results[3];
            //change cover image of the button
            playlistCover.src = coverLink;

            //This will capitalize the first letter
            var condition = results[0].charAt(0).toUpperCase() + results[0].slice(1);

            // display the weather condition
            // <br> is used for line break
            // we will also make the font of the condition bigger than the message for aesthetics
            description.innerHTML = "<span style='font-size:56px'>" + condition + "</span>" + "<br><br><br><br><br><br><br>" + message;
            container.style.height = '700px';

            // display the weather box again
            weatherBox.style.display = '';
            weatherBox.classList.add('fadeIn');
        });
}

/**
 * Function for the hyperlink
 */
function goToLink() {
    window.open(link);
}

/**
 * Function to parse recieved data
 */
function parse(data) {
    //split the string by commas
    return data.split(",");
}