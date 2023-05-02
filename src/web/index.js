//Declare varables
const container = document.querySelector('.container');
const searchBox = document.querySelector('.search-box button');
const notFound = document.querySelector('.not-found');
const weatherBox = document.querySelector('.weather-box');
const inputField = document.querySelector('.search-box input');
const playlistButton = document.querySelector('.container .playlist');
const playlistCover = document.querySelector('.container .playlist img');
let link = "http://google.com";
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

    const city = inputField.value.toLowerCase();
    var results;

    if(city === ''){
        return;
    }

    // hide the weather box
    weatherBox.style.display = 'none';

    //use fetch() method to send and recieve strings
    fetch ('http://localhost:8080/weather?query=' + city)
        .then(response => response.text())
        .then(data => {

            //convert the data into an array
            var arr = parse(data);

            //Handles the case where the city is not found
            if(arr[0].includes('Error')){
                container.style.height = '700px';
                notFound.style.display = 'block';
                notFound.classList.add('fadeIn');
                return;
            }

            //Otherwise the error image will not be displayed
            notFound.style.display = 'none';
            notFound.classList.remove('fadeIn');

            const description = document.querySelector('.weather-box .description');

            //switch case to check the response
            switch (true){
                case arr[0].includes('rain' || 'drizzle'):
                    var cloudsContainer = document.querySelector('.clouds-container');
                    var rain1 = cloudsContainer.querySelector('.rain1');
                    var rain2 = cloudsContainer.querySelector('.rain2');
                    cloudsContainer.style.display = 'block';
                    rain1.style.display = 'flex';
                    rain2.style.display = 'flex';
                    break;

                case arr[0].includes('clouds'):
                    var cloudsContainer = document.querySelector('.clouds-container');
                    cloudsContainer.style.display = 'block';
                    break;

                case arr[0].includes('snow'):
                    var cloudsContainer = document.querySelector('.clouds-container');
                    var snow1 = cloudsContainer.querySelector('.snow1');
                    var snow2 = cloudsContainer.querySelector('.snow2');
                    cloudsContainer.style.display = 'block';
                    snow1.style.display = 'flex';
                    snow2.style.display = 'flex';
                    break;

                case arr[0].includes('clear'):
                    var sunContainer = document.querySelector('.sun-container');
                    sunContainer.style.display = 'block';
                    break;

                //for haze/mist/fog we will reuse the sun container and change its colors
                case arr[0].includes('haze' || 'mist' || 'fog'):
                    sunContainer.style.display = 'none';
                    sunContainer.style.background = 'linear-gradient(to bottom, #87CEEB, #DCDCDC)';
                    break;
                
                default:
                    
            }

            //change visibility of the playlist button
            // playlistButton.style.display = 'inline-block';
            //change the variable coverLink to contain the actual link
            // coverLink = arr[2];
            //change cover image of the button
            // playlistCover.src = coverLink;

            //This will capitalize the first letter
            var condition = arr[0].charAt(0).toUpperCase() + arr[0].slice(1);

            description.innerHTML = condition;
            container.style.height = '700px';

            // display the weather box again
            weatherBox.style.display = '';
            weatherBox.classList.add('fadeIn');

            // Find the GIF element and set its source to the corresponding URL
            var gifElem = document.querySelector('.weather-box .gif');
            gifElem.src = `http://localhost:8080/gif?query=${condition}`;
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