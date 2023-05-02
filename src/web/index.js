//Declare varables
const container = document.querySelector('.container');
const searchBox = document.querySelector('.search-box button');
const notFound = document.querySelector('.not-found');
const weatherBox = document.querySelector('.weather-box');
const inputField = document.querySelector('.search-box input');
const cloudsContainer = document.querySelector('.clouds-container');
const sunContainer = document.querySelector('.sun-container');
const rain1 = cloudsContainer.querySelector('.rain1');
const rain2 = cloudsContainer.querySelector('.rain2');
const snow1 = cloudsContainer.querySelector('.snow1');
const snow2 = cloudsContainer.querySelector('.snow2');
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
    const url = 'http://localhost:8080/weather?query=' + city; 

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


    //Using fetch() method to send and recieve strings
    fetch (url)
        .then(response => response.text())
        .then(data => {

            //convert the data into an array
            var arr = parse(data);

            //Handles the case where the city is not found
            if(arr[0].includes('400'||'404')){
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
                    cloudsContainer.style.display = 'block';
                    rain1.style.display = 'flex';
                    rain2.style.display = 'flex';
                    break;

                case arr[0].includes('clouds'):
                    cloudsContainer.style.display = 'block';
                    break;

                case arr[0].includes('snow'):
                    cloudsContainer.style.display = 'block';
                    snow1.style.display = 'flex';
                    snow2.style.display = 'flex';
                    break;

                case arr[0].includes('clear'):
                    sunContainer.style.display = 'block';
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